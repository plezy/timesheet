import { Component } from '@angular/core';

import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Time\'sApp';

  authorities: string[];
  username: string;
  authority: boolean;

  constructor(private authService: AuthService) {
    this.authorities = this.authService.getAuthorities();
    this.username = this.authService.getUsername();
    console.log('Authorities ', this.authorities);
    this.authority = this.authService.isAuthenticated();
    console.log('Authority   ', this.authority);
  }

  onLogout() {
    this.authService.logout();
    window.location.reload();
  }
}
