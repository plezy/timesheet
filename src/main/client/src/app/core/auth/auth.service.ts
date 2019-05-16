import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, timer } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { JwtResponse } from './jwt-response';
import { AuthLoginInfo } from './login-info';

import { TokenStoreService } from './token-store.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private authUrl = '/auth/';
  private loginUrl = this.authUrl + 'logon';
  private logoutUrl = this.authUrl + 'logout';
  private renewtUrl = this.authUrl + 'renew';

  private refreshTimer: any;
  private refreshExec: any;

  constructor(private http: HttpClient, private tokenStorage: TokenStoreService) {
    // console.log('Setting timer');
    this.refreshTimer = timer(0, 10000); // check if renewal is needed each 10 seconds
    this.refreshExec = this.refreshTimer.subscribe(
      tick => {
        // console.log('Tick : ' + tick);
        this.scheduleRenewal();
      }
    );
  }

  attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
    // console.log('Attempt authorization ...');
    return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  }

  logout() {
    // console.log('Logging out ...');
    this.http.get(this.logoutUrl, httpOptions).subscribe((res) => {
        console.log(res);
      });
    this.tokenStorage.clear();
  }

  isAuthenticated(): boolean {
    if (typeof this.tokenStorage.getAuthorities() === undefined) {
      return false;
    }
    if (this.tokenStorage.getAuthorities().length > 0) {
      return true;
    } else {
      return false;
    }
  }

  getAuthorities(): any {
    return this.tokenStorage.getAuthorities();
  }

  getUsername(): string {
    return this.tokenStorage.getUsername();
  }

  hasAuthority(authRequested: string): boolean {
    let result = false;
    const authorities = this.tokenStorage.getAuthorities();
    if (authorities) {
      if (authorities.length > 0) {
        return authorities.includes(authRequested);
      }
    }
    return result;
  }
  
  hasOneOfAuthority(authRequested: string[]): boolean {
    let result = false;
    const authorities = this.tokenStorage.getAuthorities();
    if (authorities) {
      if (authorities.length > 0) {
        if (authRequested.length > 0) {
          for (let i=0; i<authRequested.length; i++) {
            if (authorities.includes(authRequested[i]))
              return true;
          }
        }
      }
    }
    return result;
  }
  
  private renewToken() {
    this.http.post<JwtResponse>(this.renewtUrl, null, httpOptions).subscribe(
      data => {
        this.tokenStorage.saveToken(data.token);
        this.tokenStorage.saveTokenType(data.type);
        this.tokenStorage.saveUsername(data.username);
        this.tokenStorage.saveAuthorities(data.authorities);
        this.tokenStorage.saveAuthRenew(data.ttl);
      },
      error => {
        console.log(error);
        this.logout();
      }
    );
  }

  scheduleRenewal() {
    if (!this.isAuthenticated()) { return; }

    // console.log('Check for renewal ...');
    const expireAt = this.tokenStorage.getAuthRenew().getTime();
    const now = Date.now();
    // console.log('expireAt : ' + expireAt);
    // console.log('now      : ' + now);
    if (now >= expireAt) {
      console.log('Renew Token ....');
      this.renewToken();
    }
  }

}
