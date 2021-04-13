import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ApplicationSettingDatedValueRaw, ApplicationSettingRaw} from "../../../common/model/applicationSettings";
import {SettingsService} from "../../../common/services/settings-service";
import {I18nService} from "../../../common/services/i18n-service";
import {now} from "moment";

@Component({
  selector: 'app-setting',
  templateUrl: './one-setting.component.html',
  styleUrls: ['./one-setting.component.scss']
})
export class OneSettingComponent implements OnInit {

  @Input('setting')
  setting: ApplicationSettingRaw;
  i18nSettingText : string;
  booleanValue = false;

  @Input('lang')
  lang: string;

  @Output()
  onSettingChanged: EventEmitter<void> = new EventEmitter<void>();

  constructor(private i18nService: I18nService) { }

  ngOnInit(): void {
    this.getI18nText();
  }

  getI18nText() {
    this.i18nService.getText(this.setting.settingId, this.lang).subscribe(
      result => {
        this.i18nSettingText = result.value;
      }
    );
  }

  onValueSliderChange(event) {
    this.setting.value = event.checked ? "true" : "false";
    this.settingChanged();
  }

  onDatedValueSliderChange(i, event) {
    this.setting.datedValues[i].value = event.checked ? "true" : "false";
    this.settingChanged();
  }

  settingChanged() {
    this.onSettingChanged.emit();
  }

  clickAddDatedSetting() {
    if (!this.setting.dateLinked) {
      console.log("ERROR : attempt to add dated setting to non date linked setting")
    } else {
      let datedSetting = new ApplicationSettingDatedValueRaw();
      let date = new Date();
      date.setHours(0, 0, 0, 0);
      datedSetting.dateEndValid = date;
      if (this.setting.javaType === 'java.lang.Boolean')
        datedSetting.value = 'false';
      this.setting.datedValues.push(datedSetting);
      this.onSettingChanged.emit();
    }
  }

  clickDeleteDatedSetting(idx: number) {
    console.log("index : ", idx);
    this.setting.datedValues.splice(idx, 1);
    this.onSettingChanged.emit();
  }
}
