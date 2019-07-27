import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../model/page';
import { Customer } from '../model/customer';

@Injectable({
    providedIn: 'root'
  })

export class CustomerService {

    private customerBaseUrl = '/customer';

    constructor(private http: HttpClient) { }

    getCustomers(page = 0, size= 10): Observable<Page<Customer>> {
        const url = this.customerBaseUrl + '/list/' + size + '/' + page;
        return this.http.get<Page<Customer>>(url);
    }

    getAllCustomers(page = 0, size= 10): Observable<Page<Customer>> {
        const url = this.customerBaseUrl + '/list/deleted/' + size + '/' + page;
        return this.http.get<Page<Customer>>(url);
    }

    getArchivedCustomers(page = 0, size= 10): Observable<Page<Customer>> {
        const url = this.customerBaseUrl + '/list/archived/' + size + '/' + page;
        return this.http.get<Page<Customer>>(url);
    }

    addCustomer(customer: Customer): Observable<Customer> {
        const url = this.customerBaseUrl + '/add';
        return this.http.post<Customer>(url, customer);
    }

    updateCustomer(customer: Customer): Observable<Customer> {
        const url = this.customerBaseUrl + '/' + customer.id.toString();
        return this.http.put<Customer>(url, customer);
    }

    deleteCustomer(customer: Customer): Observable<{}> {
        const url = this.customerBaseUrl + '/' + customer.id.toString();
        return this.http.delete<Customer>(url);
    }

    deleteCustomers(ids: number[]): Observable<{}> {
        let url = this.customerBaseUrl + '/list/';
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
        const url = this.customerBaseUrl + '/undelete/' + customer.id.toString();
        return this.http.put<Customer>(url, null);
    }

    archiveCustomer(customer: Customer): Observable<Customer> {
        const url = this.customerBaseUrl + '/archive/' + customer.id.toString();
        return this.http.put<Customer>(url, null);
    }

    unarchiveCustomer(customer: Customer): Observable<Customer> {
        const url = this.customerBaseUrl + '/unarchive/' + customer.id.toString();
        return this.http.put<Customer>(url, null);
    }
}
