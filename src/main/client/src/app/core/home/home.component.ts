import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) {

  }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      console.log('routing to login');
      this.router.navigate(['/core/login']);
      return;
    } else {
      console.log('authenticated');
    }
  }
}
