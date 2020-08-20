import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../model/page';
import { Contract, ContractDto } from '../model/contract';
import { ContractType } from '../model/contractType';
import { Customer } from '../model/customer';

@Injectable({
    providedIn: 'root'
  })

export class ContractService {

    private contractBaseUrl = '/contract';
    private custListUrl = '/customer/list/active';

    constructor(private http: HttpClient) { }

    getContractTypes() {
        const url = this.contractBaseUrl + '/types';
        return this.http.get<ContractType[]>(url);
    }

    getContracts(page = 0, size= 10): Observable<Page<ContractDto>> {
        const url = this.contractBaseUrl + '/list/' + size + '/' + page;
        return this.http.get<Page<ContractDto>>(url);
    }

    getAllContracts(page = 0, size= 10): Observable<Page<ContractDto>> {
        const url = this.contractBaseUrl + '/list/deleted/' + size + '/' + page;
        return this.http.get<Page<ContractDto>>(url);
    }

    getArchivedContracts(page = 0, size= 10): Observable<Page<ContractDto>> {
        const url = this.contractBaseUrl + '/list/archived/' + size + '/' + page;
        return this.http.get<Page<ContractDto>>(url);
    }

    addContract(contract: Contract): Observable<Contract> {
        const url = this.contractBaseUrl + '/add';
        return this.http.post<Contract>(url, contract);
    }

    updateContract(contract: Contract): Observable<Contract> {
        const url = this.contractBaseUrl + '/' + contract.id.toString();
        return this.http.put<Contract>(url, contract);
    }

    deleteContract(contract: ContractDto): Observable<{}> {
        const url = this.contractBaseUrl + '/' + contract.id.toString();
        return this.http.delete<Contract>(url);
    }

    deleteContracts(ids: number[]): Observable<{}> {
        let url = this.contractBaseUrl + '/list/';
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

    undeleteContract(contract: ContractDto) {
        const url = this.contractBaseUrl + '/undelete/' + contract.id.toString();
        return this.http.put<Contract>(url, null);
    }

    archiveContract(contract: ContractDto): Observable<Contract> {
        const url = this.contractBaseUrl + '/archive/' + contract.id.toString();
        return this.http.put<Contract>(url, null);
    }

    unarchiveContract(contract: ContractDto): Observable<Contract> {
        const url = this.contractBaseUrl + '/unarchive/' + contract.id.toString();
        return this.http.put<Contract>(url, null);
    }

    getSelectableCustomers(): Observable<Customer[]> {
        return this.http.get<Customer[]>(this.custListUrl);
    }

    getFilteredSelectableCustomers(filter: string): Observable<Customer[]> {
        const url = this.custListUrl + '/' + filter;
        return this.http.get<Customer[]>(url);
    }

    getContract(id: Number): Observable<Contract> {
        const url = this.contractBaseUrl + '/' + id.toString();
        return this.http.get<Contract>(url);
    }
}
