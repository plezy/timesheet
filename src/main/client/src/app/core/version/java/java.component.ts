import { Component, OnInit } from '@angular/core';
import { VersionService } from 'src/app/common/services/version.service';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-java',
  templateUrl: './java.component.html',
  styleUrls: ['../version.css']
})
export class JavaComponent implements OnInit {

  private javaInfos: any;
  private javaInfosKeys: Array<string>;

  displayedColumns: string[] = ['key', 'value'];

  constructor(private service: VersionService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_SETTINGS')) {
      this.router.navigate(['home']);
      return;
    }

    this.service.getSysProperties().subscribe(
      result => {
        this.javaInfos = result;
        this.javaInfosKeys = new Array<string>();
        Object.keys(this.javaInfos).forEach(
          key => {
            this.javaInfosKeys.push(key);
          }
        );
    });
  }

}
