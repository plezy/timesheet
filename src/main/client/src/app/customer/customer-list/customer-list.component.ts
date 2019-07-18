import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/auth/auth.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/common/services/user.service';
import { User } from 'src/app/common/model/user';
import { CustomerService } from 'src/app/common/services/customer.service';
import { Page } from 'src/app/common/model/page';
import { Customer } from 'src/app/common/model/customer';
import { MatDialog } from '@angular/material';
import { CustomerAddEditDialogComponent } from '../cust-add-edit-dialog/cust-add-edit-dialog.component';
import { ConfirmDialogComponent } from 'src/app/common/dialog/confirm-dialog/confirm-dialog.component';

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

  showDeleted = false;

  constructor(private userService: UserService, private customerService: CustomerService,
              private authService: AuthService, public dialog: MatDialog, private router: Router) { }

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
    // console.log('loading data ...'); */
    if (this.showDeleted) {
      this.customerService.getAllCustomers(this.pageSize, this.pageIndex).subscribe(
        result =>  {
          this.page = result;
        }
      );
    } else {
      this.customerService.getCustomers(this.pageSize, this.pageIndex).subscribe(
        result =>  {
          this.page = result;
        }
      );
    }
    
  }

  getData(event) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  clickAdd() {
    const dialogRef = this.dialog.open(CustomerAddEditDialogComponent, {
      width: '700px', data: { title: 'Add Customer' }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.customer) {
          this.customerService.addCustomer(result.customer).subscribe(
            addedCustomer => {
              console.log('Customer added');
              this.loadData();
          });
        }
      }
    });
  }

  clickEdit(row: Customer) {
    const dialogRef = this.dialog.open(CustomerAddEditDialogComponent, {
      width: '700px', data: { title: 'Edit Customer', customer: row }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.customer) {
          this.customerService.updateCustomer(result.customer).subscribe(
            updatedCustomer => {
              console.log('Customer updated');
              this.loadData();
          });
        }
      }
    });
  }

  /** Delete user */
  clickDelete(row: Customer) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '450px', data: {title: 'Confirm Deletion',
        message: 'Are you sure you want to delete customer ' + row.name + ' ?',
        cancelText: 'Cancel', confirmText: 'OK'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        this.customerService.deleteCustomer(row).subscribe(
          res => {
            this.loadData();
          }
        );
      }
    });
  }

  clickUndelete(row: Customer) {
    if (row.deleted) {
      this.customerService.undeleteCustomer(row).subscribe(
        res => {
          this.loadData();
        }
      );
    }
  }


/**
 * UI
 */
  getAddress(customer: Customer): string {
    let str: string;
    str = '';
    if (customer.address.addressLine1.length > 0) {
        str = str + customer.address.addressLine1;
    }

    if (customer.address.addressLine2) {
      if (customer.address.addressLine2.length > 0) {
        if (str.length > 0) {
            str = str + ' ';
        }
        str = str + customer.address.addressLine2;
      }
    }

    if (customer.address.postCode) {
      if (customer.address.postCode.length > 0) {
          if (str.length > 0) {
              str = str + ' ';
          }
          str = str + customer.address.postCode;
      }
    }

    if (customer.address.city) {
      if (customer.address.city.length > 0) {
          if (str.length > 0) {
              str = str + ' ';
          }
          str = str + customer.address.city;
      }
    }

    if (customer.address.area) {
      if (customer.address.area.length > 0) {
          if (str.length > 0) {
              str = str + ' ';
          }
          str = str + customer.address.area;
      }
    }

    if (customer.address.country) {
      if (customer.address.country.length > 0) {
        if (str.length > 0) {
            str = str + ' ';
        }
        str = str + customer.address.country;
      }
    }

    // console.log('Adresse : ' + str);
    return str;
  }

  toggleShowDeleted() {
    this.showDeleted = ! this.showDeleted;
    this.loadData();
  }
}
