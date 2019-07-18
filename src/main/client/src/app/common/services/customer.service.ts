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

    getAllCustomers(page = 0, size= 10): Observable<Page<Customer>> {
        const url = this.userBaseUrl + '/list/deleted/' + size + '/' + page;
        return this.http.get<Page<Customer>>(url);
    }

    addCustomer(customer: Customer): Observable<Customer> {
        const url = this.userBaseUrl + '/add';
        return this.http.post<Customer>(url, customer);
    }

    updateCustomer(customer: Customer): Observable<Customer> {
        const url = this.userBaseUrl + '/' + customer.id.toString();
        return this.http.put<Customer>(url, customer);
    }

    deleteCustomer(customer: Customer): Observable<{}> {
        const url = this.userBaseUrl + '/' + customer.id.toString();
        return this.http.delete<Customer>(url);
    }

    deleteCustomers(ids: number[]): Observable<{}> {
        let url = this.userBaseUrl + '/list/';
        let first = true;
        ids.forEach( value => {
          if (first) {
            url = url + value;
            first = false;
          } else {
            url = url + ',' + value;
          }
        });
        return this.http.delete(url);
    }

    undeleteCustomer(customer: Customer) {
        const url = this.userBaseUrl + '/undelete/' + customer.id.toString();
        return this.http.put(url, null);
    }
}
