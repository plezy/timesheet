import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationSettingRaw } from 'src/app/common/model/applicationSettings';
import { I18nService } from 'src/app/common/services/i18n-service';
import { SettingsService } from 'src/app/common/services/settings-service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  rawSettings: ApplicationSettingRaw[];
  lang: string = 'en';

  constructor(private authService: AuthService, private settingsService: SettingsService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/core/login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_SETTINGS')) {
      this.router.navigate(['/core/home']);
      return;
    }
    this.loadData();
  }

  loadData() {
    this.settingsService.getRawSettings().subscribe(
      result => {
        this.rawSettings = result;
      }
    );
  }


}
