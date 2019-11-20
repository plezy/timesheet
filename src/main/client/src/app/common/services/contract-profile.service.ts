import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ContractProfile } from '../model/contractProfile';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ContractProfileService {

  private contractProfileBaseUrl = '/profile';

  constructor(private http: HttpClient) { }

  getProfileForCOntract(contractId: number): Observable<ContractProfile[]> {
      const url = this.contractProfileBaseUrl + '/for/' + contractId.toString();
      return this.http.get<ContractProfile[]>(url);
  }
}
