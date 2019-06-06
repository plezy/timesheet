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

  getAllUsers(page = 0, size= 10): Observable<Page<User>> {
    const url = this.userBaseUrl + '/listAll/' + page + '/' + size;
    return this.http.get<Page<User>>(url);
  }

  getMe(): Observable<User> {
    const url = this.userBaseUrl + '/me';
    return this.http.get<User>(url);
  }

  saveUser(user: User) {
    const url = this.userBaseUrl + '/me';
    return this.http.put(url, user);
  }

  updateUserPassword(userId: number, password: string) {
    const url = this.userBaseUrl + '/setPassword';
    const message = {
      id: userId,
      message: password
    };
    return this.http.put<User>(url, message);
  }

  deleteUser(user: User) {
    const url = this.userBaseUrl + '/' + user.id.toString();
    return this.http.delete(url);
  }

  lockUser(user: User) {
    const url = this.userBaseUrl + '/lock/' + user.id.toString();
    return this.http.put(url, null);
  }

  unlockUser(user: User) {
    const url = this.userBaseUrl + '/unlock/' + user.id.toString();
    return this.http.put(url, null);
  }

  undeleteUser(user: User) {
    const url = this.userBaseUrl + '/undelete/' + user.id.toString();
    return this.http.put(url, null);
  }

}
