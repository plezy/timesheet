import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../model/page';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUrl = '/user';

  constructor(private http: HttpClient) { }

  getUsers(page = 0, size= 10): Observable<Page<User>> {
    const url = this.userBaseUrl + '/list/' + page + '/' + size;
    return this.http.get<Page<User>>(url);
  }
}