import { Component, OnInit } from '@angular/core';
import { VersionService } from 'src/app/common/services/version.service';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-build',
  templateUrl: './build.component.html',
  styleUrls: ['../version.css']
})
export class BuildComponent implements OnInit {

  timestamp: string;
  version: string;

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

    this.service.getBuildTimestamp().subscribe(
      res => {
        this.timestamp = res.value;
      }
    );

    this.service.getBuildVersion().subscribe(
      res => {
        this.version = res.value;
      }
    );

  }

}
