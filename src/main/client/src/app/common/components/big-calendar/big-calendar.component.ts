import {Component, EventEmitter, Input, OnInit, Output, OnDestroy} from '@angular/core';
import * as moment from "moment";
import { Subscription} from "rxjs";
import { BigCalendarService } from "./big-calendar.service";

const monthNames: string[] = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];

export interface CalendarDate {
  momentDate: moment.Moment;
  selected?: boolean;
  isToday?: boolean;
  isCurrentMonth?: boolean;
}

@Component({
  selector: 'big-calendar',
  templateUrl: './big-calendar.component.html',
  styleUrls: ['./big-calendar.component.scss']
})

export class BigCalendarComponent implements OnInit, OnDestroy {

  dayNames: string[] = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"];

  monthName: string;
  month: number;
  year: number;

  currentDate: moment.Moment;
  today = moment();

  weeks: CalendarDate[][] = [];

  line1Texts = new Map();
  line2Texts = new Map();

  @Input('autoSwitchMonth')
  autoSwitchMonth: boolean = false;
  @Input('selectOnlyInMonth')
  selectOnlyInMonth: boolean = false;
  @Input('yearlyMoveButton')
  yearlyMoveButton: boolean = false;

  @Output()
  onItemClicked: EventEmitter<CalendarDate> = new EventEmitter<CalendarDate>();
  @Output()
  onMonthChanged: EventEmitter<void> = new EventEmitter<void>();

  listenLine1Text: Subscription;
  listenLine2Text: Subscription;

  constructor(private bigCalendarService: BigCalendarService) {
    this.listenLine1Text = bigCalendarService.line1Text$.subscribe(
      textLine => {
        this.setDayTextLine1(textLine.momentDate, textLine.text);
    });
    this.listenLine2Text = bigCalendarService.line2Text$.subscribe(
      textLine => {
        this.setDayTextLine2(textLine.momentDate, textLine.text);
      });
  }

  ngOnInit() {
    this.setMoment(this.today);
  }

  ngOnDestroy() {
    this.listenLine1Text.unsubscribe();
    this.listenLine2Text.unsubscribe();
  }

  setMoment(date: moment.Moment) {
    this.currentDate = date;
    this.updateDate();
  }

  setDate(date: Date) {
    this.currentDate = moment(date);
    this.updateDate();
  }

  private updateDate() {
    this.month = this.currentDate.month();
    this.monthName = monthNames[this.month];
    this.year = this.currentDate.year();
    const dates = this.fillCalendarArray(this.currentDate);
    // removes week beyond the current month
    if (!dates[35].isCurrentMonth) {
      dates.splice(35);
    }
    // rearrange calendar dates in weeks
    this.weeks = [];
    while (dates.length > 0) {
      this.weeks.push(dates.splice(0, 7));
    }
    // cleans textMaps
    this.line1Texts.clear();

    console.log(this.weeks);
    this.onMonthChanged.emit();
  }

  isToday(date: moment.Moment): boolean {
    return this.today.isSame(moment(date), 'day');
  }

  fillCalendarArray(currentMoment: moment.Moment): CalendarDate[] {
    // first day of month (from monday 0, to sunday 6)
    const firstOfMonth = (moment(currentMoment).startOf('month').day() + 6) % 7;
    // console.log('firstOfMonth : ' + firstOfMonth);
    // compute date of first day on the grid
    const firstDayOfGrid = moment(currentMoment).startOf('month').subtract(firstOfMonth, 'd');
    //console.log('firstDayOfGrid : ' + firstDayOfGrid);
    const start = firstDayOfGrid.date();
    //console.log('start of calendar : ' + start);

    return Array.from(Array(42).keys()).map((i): CalendarDate => {
      const day = firstDayOfGrid.clone().add(i, 'd');
      //console.log(day);
      return {
        momentDate: day,
        selected: false,
        isToday: this.isToday(day),
        isCurrentMonth: day.month() == this.month
      }
    });
  }

  incrementMonth() {
    this.currentDate.add(1, 'M');
    this.updateDate();
  }

  decrementMonth() {
    this.currentDate.add(-1, 'M');
    this.updateDate();
  }

  incrementYear() {
    this.currentDate.add(1, 'y');
    this.updateDate();
  }

  decrementYear() {
    this.currentDate.add(-1, 'y');
    this.updateDate();
  }

  setDayTextLine1(theDate: moment.Moment, text: String) {
    //console.log("in setDayTextLine1");
    var weekOfDate = theDate.isoWeek();
    // checks if week on the displayed calendar by checking week of date week of first and last element in row
    if (weekOfDate >= this.weeks[0][0].momentDate.isoWeek() && weekOfDate <= this.weeks[this.weeks.length-1][0].momentDate.isoWeek() ) {
      // given date is in range, and is part of the displayed calendar
      var theMoment = theDate.clone();
      theMoment.set({hour:0,minute:0,second:0,millisecond:0});
      this.line1Texts. set(theMoment.unix(), text);
      //console.log(this.line1Texts);
    }
  }

  setDayTextLine2(theDate: moment.Moment, text: String) {
    var weekOfDate = theDate.isoWeek();
    // checks if week on the displayed calendar by checking week of date week of first and last element in row
    if (weekOfDate >= this.weeks[0][0].momentDate.isoWeek() && weekOfDate <= this.weeks[this.weeks.length-1][0].momentDate.isoWeek() ) {
      // given date is in range, and is part of the displayed calendar
      var theMoment = theDate.clone();
      theMoment.set({hour:0,minute:0,second:0,millisecond:0});
      this.line2Texts. set(theMoment.unix(), text);
      console.log(this.line2Texts);
    }
  }

  itemClicked(item: CalendarDate) {
    console.log("Item clicked");
    if (!this.selectOnlyInMonth || item.isCurrentMonth) {
      this.onItemClicked.emit(item);
      if ((!item.isCurrentMonth) && this.autoSwitchMonth) {
        //console.log("Autoswitching month");
        this.setMoment(item.momentDate);
      }
    }
  }
}
