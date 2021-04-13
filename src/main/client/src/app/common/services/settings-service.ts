import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { ApplicationSettingRaw } from "../model/applicationSettings";

@Injectable({
    providedIn: 'root'
  })

export class SettingsService {

    private settingsBaseUrl = '/settings';

    constructor(private http: HttpClient) { }

  getRawSettings(): Observable<ApplicationSettingRaw[]> {
    const url = this.settingsBaseUrl + '/manage';
    return this.http.get<ApplicationSettingRaw[]>(url);
  }

  saveRawSettings(settings: ApplicationSettingRaw[]): Observable<ApplicationSettingRaw[]> {
    const url = this.settingsBaseUrl + '/manage';
    return this.http.put<ApplicationSettingRaw[]>(url, settings);
  }
}
