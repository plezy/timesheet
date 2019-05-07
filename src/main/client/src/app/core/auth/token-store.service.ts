import { Injectable } from '@angular/core';
import * as _moment from 'moment';

const TOKEN_KEY = 'AuthToken';
const TOKENTYPE_KEY = 'AuthTokenType';
const USERNAME_KEY = 'AuthUsername';
const AUTHORITIES_KEY = 'AuthAuthorities';
const AUTH_TTL = 'AuthTTL';
const AUTH_RENEW = 'AuthRenewal';

@Injectable({
  providedIn: 'root'
})
export class TokenStoreService {

  private roles: Array<string> = [];
  private timeoutId: number;

  constructor() { }

  clear() {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.removeItem(TOKENTYPE_KEY);
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.removeItem(AUTHORITIES_KEY);
    window.sessionStorage.removeItem(AUTH_TTL);
    window.sessionStorage.removeItem(AUTH_RENEW);
  }

  public saveToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveTokenType(type: string) {
    window.sessionStorage.removeItem(TOKENTYPE_KEY);
    window.sessionStorage.setItem(TOKENTYPE_KEY, type);
  }

  public getTokenType(): string {
    return sessionStorage.getItem(TOKENTYPE_KEY);
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public saveAuthorities(authorities: string[]) {
    window.sessionStorage.removeItem(AUTHORITIES_KEY);
    window.sessionStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
  }

  public getAuthorities(): string[] {
    this.roles = [];

    if (sessionStorage.getItem(TOKEN_KEY)) {
      JSON.parse(sessionStorage.getItem(AUTHORITIES_KEY)).forEach(authority => {
        this.roles.push(authority.authority);
      });
    }

    return this.roles;
  }

  /*
  public saveAuthTTL(ttl: number) {
    window.sessionStorage.removeItem(AUTH_TTL);
    window.sessionStorage.setItem(AUTH_TTL, ttl.toString());
  }

  public getAuthTTL(): number {
    // tslint:disable-next-line:radix
    return parseInt(sessionStorage.getItem(AUTH_TTL));
  }

  public saveAuthRenew(ttl?: number) {
    let delay: number;
    if (typeof ttl !== undefined) {
      this.saveAuthTTL(ttl);
      delay = ttl;
    } else {
      delay = this.getAuthTTL();
    }
    delay = delay * 900; // convert delay to 90% of milliseconds
    const now = _moment();
    now.add(delay, 'ms');
    window.sessionStorage.removeItem(AUTH_RENEW);
    window.sessionStorage.setItem(AUTH_RENEW, now.toString());
    const source = timer(1000, 2000);
    const subscribe = source.subscribe(val => console.log(val));
  }
  */

  public saveAuthRenew(ttl: number) {
    const delay = ttl * 900; // convert delay to 90% of milliseconds
    const now = _moment();
    now.add(delay, 'ms');
    window.sessionStorage.removeItem(AUTH_RENEW);
    window.sessionStorage.setItem(AUTH_RENEW, now.toString());
  }

  public getAuthRenew(): Date {
    const authRenew = _moment( sessionStorage.getItem(AUTH_RENEW));
    console.log('authRenew : ' + authRenew);
    return authRenew.toDate();
  }

}
