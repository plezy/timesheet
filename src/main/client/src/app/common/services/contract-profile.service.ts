import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ContractProfile } from '../model/contractProfile';
import { HttpClient } from '@angular/common/http';
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class ContractProfileService {

  private contractProfileBaseUrl = '/contract/profiles';

  constructor(private http: HttpClient) { }

  getProfileForContract(contractId: number): Observable<ContractProfile[]> {
      const url = this.contractProfileBaseUrl + "/" + contractId.toString();
      return this.http.get<ContractProfile[]>(url);
  }

  getAssignable(profileId: number): Observable<User[]> {
    const url = this.contractProfileBaseUrl + "/assignable/" + profileId.toString();
    return this.http.get<User[]>(url);
  }

  addAssignees(contractId: number, profileId: number, assigneeIds: number[]): Observable<ContractProfile> {
    const url = this.contractProfileBaseUrl + "/addassignees";
    const body = { "contractId": contractId, "profileId": profileId, "assigneeIds": assigneeIds };
    return this.http.put<ContractProfile>(url, body);
  }

  removeAssignee(contractId: number, profileId: number, assigneeIds: number[]): Observable<ContractProfile> {
    const url = this.contractProfileBaseUrl + "/delassignees/" + contractId.toString() + "/" + profileId.toString() + "/" + assigneeIds.toString();
    return this.http.delete<ContractProfile>(url);
  }
}
