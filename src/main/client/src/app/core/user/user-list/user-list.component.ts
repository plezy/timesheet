import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { Page } from 'src/app/common/model/page';
import { User } from 'src/app/common/model/user';
import { UserService } from 'src/app/common/services/user.service';
import { ShowOnDirtyErrorStateMatcher } from '@angular/material';

const LOCK_ICON = 'lock';
const UNLOCK_ICON = 'lock_open';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  page: Page<User>;

  me: User;

  pageSizeOptions = [2, 5, 10, 20];
  pageSize = 10;
  pageIndex = 0;
  pageFirstLast = true;

  displayedColumns: string[] = ['selector', 'username', 'firstname', 'lastname', 'email', 'action'];

  /* selection contient les ids des éléments du tableau sélectionnés */
  selection: Array<number> = new Array();

  showDeleted = false;

  constructor(private userService: UserService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_USERS')) {
      this.router.navigate(['/home']);
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
    this.cleanSelection();
    // console.log('loading data ...');
    if (this.showDeleted) {
      this.userService.getAllUsers(this.pageSize, this.pageIndex).subscribe(
        result =>  {
          this.page = result;
        }
      );
    } else {
      this.userService.getUsers(this.pageSize, this.pageIndex).subscribe(
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

  /** Delete user */
  clickDelete(row: User) {
    this.userService.deleteUser(row).subscribe(
      res => {
        this.loadData();
      }
    );
  }

  /**
   * Visual Elements
   */
  toggleShowDeleted() {
    this.showDeleted = ! this.showDeleted;
    this.loadData();
  }

  isMe(row?: User): boolean {
    let result = false;
    if (this.me) {
      if (row) {
        if (row.id === this.me.id) {
          result = true;
        }
      }
    }
    return result;
  }

  getLockTitle(row?: User): string {
    let result = 'Lock';
    if (row) {
      if (row.locked) {
        result = 'Unlock';
      }
    }
    result += ' User';
    return result;
  }

  getLockIcon(row?: User): string {
    if (row) {
      if (row.locked) {
        return LOCK_ICON;
      }
    }
    return UNLOCK_ICON;
  }

  /**
   *  All the code below handles checkbox behaviour
   */

  /** Clean selections array */
  cleanSelection() {
    // console.log('in cleanSelection');
    if (this.selection) {
      if (this.selection.length > 0) {
        this.selection.splice(0, this.selection.length);
      }
    }
  }

  /** Checks if there is an element selected in the table */
  hasSelection(): boolean {
    // console.log('in hasSelection');
    if (this.selection) {
      return this.selection.length > 0;
    }
    return false;
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    // console.log('in isAllSelected');
    if (this.selection) {
      const numSelected = this.selection.length;
      const numRows = (this.page) ? this.page.numberOfElements : 0;
      // console.log('numSelected : ' + numSelected);
      // console.log('numRows     : ' + numRows);
      return numSelected === numRows;
    }
    return false;
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isSelected(row: User): boolean {
    // console.log('in isSelected for row ' + row.username);
    if (this.selection) {
      return this.selection.includes(row.id);
    }
    return false;
  }

  /** Togle selection of a row */
  toggleSelection(row: User) {
    // console.log('in toggleSelection for row ' + row.username);
    if (this.isSelected(row)) {
      this.selection.splice(this.selection.indexOf(row.id), 1);
    } else {
      if (!this.selection) {
        this.selection = new Array();
      }
      this.selection.push(row.id);
    }
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    // console.log('in masterToggle');
    if (this.isAllSelected()) {
      this.cleanSelection();
    } else {
      this.cleanSelection();
      this.page.content.forEach(row => this.selection.push(row.id));
    }
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: User): string {
    // console.log('in checkboxLabel for row ' + (!row ? 'undefined' : row.username));
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.isSelected(row)} ? 'deselect' : 'select'} row ${row.id}`;
  }

}
