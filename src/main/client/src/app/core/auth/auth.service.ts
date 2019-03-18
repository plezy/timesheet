import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  private loginUrl = '/auth/logon';
  private logoutUrl = '/auth/logout';

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
      this.tokenStorage.clear();
  }

  isAuthenticated(): boolean {
    if (this.tokenStorage.getAuthorities().length > 0)
      return true;
    else
      return false;
  }

  getAuthorities(): any {
    return this.tokenStorage.getAuthorities();
  }

  getUsername(): string {
    return this.tokenStorage.getUsername();
  }
}  
