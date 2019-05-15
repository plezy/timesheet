import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StringValue } from './string-value';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const versionBaseUrl = '/appinfos';

@Injectable({
  providedIn: 'root'
})
export class VersionService {

  constructor(private http: HttpClient) { }

  getBuildVersion(): Observable<StringValue> {
    return this.http.get<StringValue>(versionBaseUrl + '/version');
  }

  getBuildTimestamp(): Observable<StringValue> {
    return this.http.get<StringValue>(versionBaseUrl + '/timestamp');
  }

  getBuildGitInfos(): Observable<string> {
    return this.http.get<string>(versionBaseUrl + '/git');
  }

  getSysProperties(): Observable<string> {
    return this.http.get<string>(versionBaseUrl + '/properties');
  }

  getEnvVariables(): Observable<string> {
    return this.http.get<string>(versionBaseUrl + '/env');
  }

}
