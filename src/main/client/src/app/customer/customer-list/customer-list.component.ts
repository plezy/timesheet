import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/auth/auth.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/common/services/user.service';
import { User } from 'src/app/common/model/user';
import { CustomerService } from 'src/app/common/services/customer.service';
import { Page } from 'src/app/common/model/page';
import { Customer } from 'src/app/common/model/customer';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {

  page: Page<Customer>;

  me: User;

  pageSizeOptions = [5, 10, 20];
  pageSize = 10;
  pageIndex = 0;
  pageFirstLast = true;

  displayedColumns: string[] = ['name', 'action'];

  constructor(private userService: UserService, private customerService: CustomerService,
              private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/core/login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_CUSTOMERS')) {
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

  /**
   * Data management
   */

  /** retrieve rows */
  loadData() {
    /**
    this.cleanSelection();
    // console.log('loading data ...');
    if (this.showDeleted) { */
      this.customerService.getCustomers(this.pageSize, this.pageIndex).subscribe(
        result =>  {
          this.page = result;
        }
      ); /*
    } else {
      this.userService.getUsers(this.pageSize, this.pageIndex).subscribe(
        result =>  {
          this.page = result;
        }
      );
    }
    */
  }

  getData(event) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

}
