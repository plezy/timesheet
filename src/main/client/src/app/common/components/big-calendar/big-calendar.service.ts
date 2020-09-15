import { Injectable } from '@angular/core';
import * as moment from "moment";
import {Subject} from "rxjs";

export class TextLine {
  momentDate: moment.Moment;
  text: string;
}

@Injectable({
  providedIn: 'root'
})
export class BigCalendarService {

  private setLine1TextSource = new Subject<TextLine>();
  private setLine2TextSource = new Subject<TextLine>();

  line1Text$ = this.setLine1TextSource.asObservable();
  line2Text$ = this.setLine2TextSource.asObservable();

  constructor() { }

  setLine1Text(textLine: TextLine)  {
    this.setLine1TextSource.next(textLine);
  }

  setLine2Text(textLine: TextLine)  {
    this.setLine2TextSource.next(textLine);
  }
}
