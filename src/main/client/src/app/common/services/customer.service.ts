import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../model/page';
import { Customer } from '../model/customer';

@Injectable({
    providedIn: 'root'
  })

export class CustomerService {

    private userBaseUrl = '/customer';

    constructor(private http: HttpClient) { }

    getCustomers(page = 0, size= 10): Observable<Page<Customer>> {
        const url = this.userBaseUrl + '/list/' + size + '/' + page;
        return this.http.get<Page<Customer>>(url);
    }
}
