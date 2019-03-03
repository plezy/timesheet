import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { JwtResponse } from './jwt-response';
import { AuthLoginInfo } from './login-info';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = '/auth/logon';
  private logoutUrl = '/auth/logout';

  constructor(private http: HttpClient) {
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
  }
}
