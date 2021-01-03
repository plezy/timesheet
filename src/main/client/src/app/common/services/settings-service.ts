import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
  })

export class SettingsService {

    private settingsBaseUrl = '/settings';

    constructor(private http: HttpClient) { }

}