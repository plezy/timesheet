import { Component, OnInit } from '@angular/core';
import {CalendarDate} from "../../common/components/big-calendar/big-calendar.component";
import {BigCalendarService} from "../../common/components/big-calendar/big-calendar.service";
import {TaskTimetrack} from "../../common/model/taskTimetrack";
import {User} from "../../common/model/user";
import {TimetrackService} from "../../common/services/timetrack.service";
import {AuthService} from "../../core/auth/auth.service";
import {Router} from "@angular/router";
import {UserService} from "../../common/services/user.service";

@Component({
  selector: 'timesheet-home',
  templateUrl: './timesheet-home.component.html',
  styleUrls: ['./timesheet-home.component.css']
})
export class TimeSheetHomeComponent implements OnInit {
  me: User;
  selectedDate: CalendarDate;
  taskTimetracks: TaskTimetrack[];
  displayedColumns: string[] = ["locked", "contract", "task", "duration", "note"];

  constructor(private authService: AuthService, private router: Router, private userService: UserService,
          private calendarService: BigCalendarService, private timetrackService: TimetrackService) {

  }

  ngOnInit(): void {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
      return;
    }

    if (! this.authService.hasOneOfAuthority(['ENTER_TIME_TRACK', 'ENTER_OTHERS_TIME_TRACK'])) {
      this.router.navigate(['/']);
      return;
    }

    this.userService.getMe().subscribe(
      me => {
        this.me = me;
      }
    );
  }

  calendarDateClicked(event) {
    this.selectedDate = event;
    this.updateTimeTrackTable();
  }

  onMonthChanged() {
    console.log("Month changed");
    delete this.selectedDate;
    this.updateTimeTrackTable();
  }

  updateTimeTrackTable(user? : User) {
    if (this.selectedDate) {
      console.log("Date : " + this.selectedDate.momentDate.toISOString());
      if (user) {
        // TODO implement
        this.taskTimetracks = [];
      } else {
        console.log("Update Time Track table for me");
        this.timetrackService.getTimeTrackForMeOnDate(this.selectedDate.momentDate).subscribe(
          taskTimetracks => {
            this.taskTimetracks = taskTimetracks;
            console.log(this.taskTimetracks)
          });
      }
    } else {
      this.taskTimetracks = [];
    }
  }

  noteLostFocus(task: TaskTimetrack, i: any) {
    console.log("note left : " + task.note + "(id: " + task.taskID +")");
  }


  noteChanged(task: any, i: any) {
    console.log("note changed : " + task.note + "(id: " + task.taskID +")");
  }
}
