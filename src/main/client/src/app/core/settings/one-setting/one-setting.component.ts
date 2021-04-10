import {Component, Input, OnInit} from '@angular/core';
import {ApplicationSettingRaw} from "../../../common/model/applicationSettings";
import {SettingsService} from "../../../common/services/settings-service";
import {I18nService} from "../../../common/services/i18n-service";

@Component({
  selector: 'app-setting',
  templateUrl: './one-setting.component.html',
  styleUrls: ['./one-setting.component.scss']
})
export class OneSettingComponent implements OnInit {

  @Input('setting')
  setting: ApplicationSettingRaw;
  i18nSettingText : string;

  @Input('lang')
  lang: string;

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
}
