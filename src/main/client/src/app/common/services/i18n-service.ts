import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { StringValue } from "./string-value";

@Injectable({
    providedIn: 'root'
  })

export class I18nService {

    private i18nBaseUrl = '/i18n';

    constructor(private http: HttpClient) { }

    getText(textId: string, lang: string): Observable<StringValue> {
        const url = this.i18nBaseUrl + "/" + textId + "/" + lang;
        return this.http.get<StringValue>(url);
    }
}