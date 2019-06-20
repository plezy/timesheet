import { Component, OnInit } from '@angular/core';
import { VersionService } from 'src/app/common/services/version.service';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-env',
  templateUrl: './env.component.html',
  styleUrls: ['../version.css']
})
export class EnvComponent implements OnInit {

  private envInfos: any;
  private envInfosKeys: Array<string>;

  displayedColumns: string[] = ['key', 'value'];

  constructor(private service: VersionService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/core/login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_SETTINGS')) {
      this.router.navigate(['/core/home']);
      return;
    }

    this.service.getEnvVariables().subscribe(
      result => {
        this.envInfos = result;
        this.envInfosKeys = new Array<string>();
        Object.keys(this.envInfos).forEach(
          key => {
            this.envInfosKeys.push(key);
          }
        );
    });
  }

}
