import { Component } from '@angular/core';

import { AuthService } from './auth/auth.service';
import { TokenStoreService } from './auth/token-store.service';

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

  constructor(private authService: AuthService, private tokenStorage: TokenStoreService) {
    this.authorities = tokenStorage.getAuthorities();
    this.username = tokenStorage.getUsername();
    console.log('Authorities ', this.authorities);
    this.authority = this.authorities.length > 0;
    console.log('Authority   ', this.authority);
  }

  onLogout() {
    this.authService.logout();
    this.tokenStorage.clear();
    window.location.reload();
  }
}
