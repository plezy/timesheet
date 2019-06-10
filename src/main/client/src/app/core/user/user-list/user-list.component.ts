import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { Page } from 'src/app/common/model/page';
import { User } from 'src/app/common/model/user';
import { UserService } from 'src/app/common/services/user.service';
import { ShowOnDirtyErrorStateMatcher, MatDialog } from '@angular/material';
import { ConfirmDialogComponent } from 'src/app/common/dialog/confirm-dialog/confirm-dialog.component';
import { UserAddEditDialogComponent } from '../user-add-edit-dialog/user-add-edit-dialog.component';
import { UserSetPasswordDialogComponent } from '../user-set-password-dialog/user-set-password-dialog.component';
import { UserEditRolesDialogComponent } from '../user-edit-roles-dialog/user-edit-roles-dialog.component';

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

  constructor(private userService: UserService, private authService: AuthService, public dialog: MatDialog, private router: Router) { }

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
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '450px', data: {title: 'Confirm Deletion',
        message: 'Are you sure you want to delete user ' + row.username + ' [ ' + row.firstName + ' ' + row.lastName + ' ] ?',
        cancelText: 'Cancel', confirmText: 'OK'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        this.userService.deleteUser(row).subscribe(
          res => {
            this.loadData();
          }
        );
      }
    });
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

  clickLock(row: User) {
    if (row.locked) {
      this.userService.unlockUser(row).subscribe(
        res => {
          this.loadData();
        }
      );
    } else {
      this.userService.lockUser(row).subscribe(
        res => {
          this.loadData();
        }
      );
    }
  }

  clickUndelete(row: User) {
    if (row.deleted) {
      this.userService.undeleteUser(row).subscribe(
        res => {
          this.loadData();
        }
      );
    }
  }

  clickAdd() {
    const dialogRef = this.dialog.open(UserAddEditDialogComponent, {
      width: '700px', data: { title: 'Add User' }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.user) {
          this.userService.addUser(result.user).subscribe(
            addedUser => {
              this.userService.updateUserPassword(addedUser.id, result.pwd).subscribe(
                res => {
                  console.log('User added');
                  this.loadData();
              });
          });
        }
      }
    });
  }

  clickEdit(row: User) {
    const dialogRef = this.dialog.open(UserAddEditDialogComponent, {
      width: '700px', data: { title: 'Edit User', user: row }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.user) {
          this.userService.updateUser(result.user).subscribe(
            updatedUser => {
              console.log('User updated');
              this.loadData();
          });
        }
      }
    });
  }

  clickPassword(row: User) {
    const dialogRef = this.dialog.open(UserSetPasswordDialogComponent, {
      width: '500px', data: { user: row }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        this.userService.updateUserPassword(row.id, result.pwd).subscribe(
          user => {
            console.log('Password updated');
        });
      }
    });
  }

  clickRoles(row: User) {
    const dialogRef = this.dialog.open(UserEditRolesDialogComponent, {
      width: '600px', data: { user: row }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('the dialog was closed');
      if (result) {
        console.log(result);
        this.userService.updateUserRoles(row.id, result.roles).subscribe(res => {
          console.log('User roles updated');
          this.loadData();
        });
      }
    });
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

  /** Delete selected ids */
  clickDeleteSelected() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '450px', data: {title: 'Confirm Mutiple Deletions',
        message: 'Are you sure you want to delete the ' + this.selection.length + ' selected users ?',
        cancelText: 'Cancel', confirmText: 'OK'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log('deleting ', this.selection);
        this.userService.deleteUsers(this.selection).subscribe(
          data => { this.loadData(); }
        );
      }
    });
  }
}
