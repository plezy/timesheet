import { Component, OnInit } from '@angular/core';
import { VersionService } from 'src/app/common/services/version.service';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-git',
  templateUrl: './git.component.html',
  styleUrls: ['./git.component.css']
})
export class GitComponent implements OnInit {

  private gitInfos: any;
  private gitInfosKeys: Array<string>;

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

    this.service.getBuildGitInfos().subscribe(
      result => {
        this.gitInfos = result;
        // console.log('GitInfos : ' + this.gitInfos);
        this.gitInfosKeys = new Array<string>();
        Object.keys(this.gitInfos).forEach(
          key => {
            this.gitInfosKeys.push(key);
            // console.log('Key : ' + key);
          }
        );
    });
  }
}
