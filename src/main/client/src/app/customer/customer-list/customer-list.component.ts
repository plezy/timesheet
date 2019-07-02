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

  displayedColumns: string[] = ['name', 'siege', 'action'];

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

/**
 * UI 
 */
  getAddress(customer: Customer): string {
    let str: string;
    if (customer.address.addressLine1.length > 0) {
        str = str + customer.address.addressLine1;
    }

    if (customer.address.addressLine2.length > 0) {
        if (str.length > 0)
            str = str + ' ';
        str = str + customer.address.addressLine2;
    }

    if (customer.address.postCode.length > 0) {
        if (str.length > 0)
            str = str + ' ';
        str = str + customer.address.postCode;
    }

    if (customer.address.city.length > 0) {
        if (str.length > 0)
            str = str + ' ';
        str = str + customer.address.city;
    }

    if (customer.address.area.length > 0) {
        if (str.length > 0)
            str = str + ' ';
        str = str + customer.address.area;
    }

    if (customer.address.country.length > 0) {
        if (str.length > 0)
            str = str + ' ';
        str = str + customer.address.country;
    }

    return str;
  }

}
