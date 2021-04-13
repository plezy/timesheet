import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationSettingRaw } from 'src/app/common/model/applicationSettings';
import { I18nService } from 'src/app/common/services/i18n-service';
import { SettingsService } from 'src/app/common/services/settings-service';
import { AuthService } from '../auth/auth.service';
import { User } from "../../common/model/user";
import { UserService } from "../../common/services/user.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  rawSettings: ApplicationSettingRaw[];
  lang: string = 'en';
  settingsChanged = false;
  me: User;

  constructor(private authService: AuthService, private userService: UserService, private settingsService: SettingsService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/core/login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_SETTINGS')) {
      this.router.navigate(['/core/home']);
      return;
    }

    this.userService.getMe().subscribe(
      me => {
        this.me = me;
      }
    );

    this.loadData();
  }

  loadData() {
    this.settingsService.getRawSettings().subscribe(
      result => {
        this.rawSettings = result;
      }
    );
  }

  onSettingChanged() {
    this.settingsChanged = true;
  }

  cancel() {
    this.loadData();
    this.settingsChanged = false;
  }

  save() {
    console.log(this.rawSettings);
    this.settingsService.saveRawSettings(this.rawSettings).subscribe(
      result => {
        this.rawSettings = result;
      }
    );
  }
}
