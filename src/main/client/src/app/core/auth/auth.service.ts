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

  private refresh: any;
  private _expireAt: Date;

  constructor(private http: HttpClient, private tokenStorage: TokenStoreService) {
  }

  attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
    console.log('Attempt authorization ...');
    return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  }

  logout() {
    console.log('Logging out ...');
    this.http.get(this.logoutUrl, httpOptions).subscribe((res) => {
        console.log(res);
      });
    this.unscheduleRenewal();
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
      }
    );
  }

  scheduleRenewal() {
    if (!this.isAuthenticated()) { return; }
    this.unscheduleRenewal();

    const expiresAt = this.tokenStorage.getAuthRenew().getTime(); // yimr in msec

    const expiresIn$ = of(expiresAt).pipe(
      mergeMap(
        expAt => {
          const now = Date.now(); // now in msec
          // Use timer to track delay until expiration
          // to run the refresh at the proper time
          const delay = expAt - now;
          console.log('setting timer to : ' + delay + ' msec.');
          return timer(Math.max(1, delay));
        }
      )
    );

    // Once the delay time from above is
    // reached, get a new JWT and schedule
    // additional refreshes
    this.refresh = expiresIn$.subscribe(
      () => {
        console.log('Renew token ...');
        this.renewToken();
        this.scheduleRenewal();
      }
    );
  }

  unscheduleRenewal() {
    if (this.refresh) {
      this.refresh.unsubscribe();
    }
  }
}
