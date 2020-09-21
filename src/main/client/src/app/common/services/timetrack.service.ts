import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { TaskTimetrack } from "../model/taskTimetrack";
import { dateFormat } from "../constants";
import { Moment } from "moment";

@Injectable({
  providedIn: 'root'
})
export class TimetrackService {
  private timetrackBaseUrl = '/timesheet';
  constructor(private http: HttpClient) { }

  getTimeTrackForUserOnDate(userID: number, ladate: Moment): Observable<TaskTimetrack[]> {
    const url = this.timetrackBaseUrl + "/timetrack/" + userID.toString + "/" + ladate.format(dateFormat);
    return this.http.get<TaskTimetrack[]>(url);
  }

  getTimeTrackForMeOnDate(ladate: Moment): Observable<TaskTimetrack[]> {
    const url = this.timetrackBaseUrl + "/mytimetrack/" + ladate.format(dateFormat);
    return this.http.get<TaskTimetrack[]>(url);
  }
}
