import { Component, OnInit } from '@angular/core';
import { Page } from 'src/app/common/model/page';
import { ContractDto } from 'src/app/common/model/contract';
import { User } from 'src/app/common/model/user';
import { UserService } from 'src/app/common/services/user.service';
import { AuthService } from 'src/app/core/auth/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ContractService } from 'src/app/common/services/contract.service';
import { ContractAddEditDialogComponent } from '../contract-add-edit-dialog/contract-add-edit-dialog.component';
import { ConfirmDialogComponent } from 'src/app/common/dialog/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-contract-list',
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.css']
})
export class ContractListComponent implements OnInit {
  page: Page<ContractDto>;

  me: User;

  pageSizeOptions = [5, 10, 20];
  pageSize = 10;
  pageIndex = 0;
  pageFirstLast = true;

  displayedColumns: string[] = [ 'selector', 'name', 'type', 'customer', 'action'];

  showDeleted = false;
  showArchived = false;

  /* selection contient les ids des éléments du tableau sélectionnés */
  selection: Array<number> = new Array();

  constructor(private contractService: ContractService, private userService: UserService,
    private authService: AuthService, public dialog: MatDialog, private router: Router) { }

  ngOnInit() {
    if (! this.authService.isAuthenticated()) {
      this.router.navigate(['/core/login']);
      return;
    }

    if (! this.authService.hasAuthority('MANAGE_CONTRACTS')) {
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
    if (this.showArchived) {
      this.contractService.getArchivedContracts(this.pageSize, this.pageIndex).subscribe(
        result =>  {
          this.page = result;
          console.log('getArchivedContracts result : ');
          console.log(result);
        }
      );
    } else {
      if (this.showDeleted) {
        this.contractService.getAllContracts(this.pageSize, this.pageIndex).subscribe(
          result =>  {
            this.page = result;
            console.log('getAllContracts result : ');
            console.log(result);
          }
        );
      } else {
        this.contractService.getContracts(this.pageSize, this.pageIndex).subscribe(
          result =>  {
            this.page = result;
            console.log('getContracts result : ');
            console.log(result);
          }
        );
      }
    }
  }

  getData(event) {
    console.log('getData');
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  /** Delete contract */
  clickDelete(row: ContractDto) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '450px', data: {title: 'Confirm Deletion',
        message: 'Are you sure you want to delete contract ' + row.name + ' with customer ' + row.customerName + ' ?',
        cancelText: 'Cancel', confirmText: 'OK'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        this.contractService.deleteContract(row).subscribe(
          res => {
            this.loadData();
          }
        );
      }
    });
  }

  clickUndelete(row: ContractDto) {
    if (row.deleted) {
      this.contractService.undeleteContract(row).subscribe(
        res => {
          this.loadData();
        }
      );
    }
  }

  clickArchive(row: ContractDto) {
    if (!row.archived) {
      console.log('Archiving contract');
      this.contractService.archiveContract(row).subscribe(
        res => {
          this.loadData();
        }
      )
    }
  }

  clickUnarchive(row: ContractDto) {
    if (row.archived) {
      console.log('Unarchiving customer');
      this.contractService.unarchiveContract(row).subscribe(
        res => {
          this.loadData();
        }
      )
    }
  }

  /**
   * UI
   */
  toggleShowDeleted() {
    this.showDeleted = ! this.showDeleted;
    this.loadData();
  }

  toggleShowArchived() {
    this.showArchived = ! this.showArchived;
    this.loadData();
  }

  clickAdd() {
    const dialogRef = this.dialog.open(ContractAddEditDialogComponent, {
      width: '700px', data: { title: 'Add Contract' }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.contract) {
          this.contractService.addContract(result.contract).subscribe(
            addedContract => {
              console.log('Contract added');
              this.loadData();
          });
        }
      }
    });
  }

  clickEdit(row: ContractDto) {
    this.contractService.getContract(row.id).subscribe(contract => {
      const dialogRef = this.dialog.open(ContractAddEditDialogComponent, {
        width: '700px', data: { title: 'Edit Contract', contract: contract }
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        if (result) {
          console.log(result);
          if (result.contract) {
            this.contractService.updateContract(result.contract).subscribe(
              updatedContract => {
                console.log('Contract updated');
                this.loadData();
            });
          }
        }
      });
    });
  }

  /**
   * UI multiple delete
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
  isSelected(row: ContractDto): boolean {
    // console.log('in isSelected for row ' + row.username);
    if (this.selection) {
      return this.selection.includes(row.id);
    }
    return false;
  }

  /** Togle selection of a row */
  toggleSelection(row: ContractDto) {
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
  checkboxLabel(row?: ContractDto): string {
    // console.log('in checkboxLabel for row ' + (!row ? 'undefined' : row.name));
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.isSelected(row) ? 'select' : 'deselect'} row ${row.id}`;
  }

  /** Delete selected ids */
  clickDeleteSelected() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '450px', data: {title: 'Confirm Mutiple Deletions',
        message: 'Are you sure you want to delete the ' + this.selection.length + ' selected contracts ?',
        cancelText: 'Cancel', confirmText: 'OK'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log('deleting ', this.selection);
        this.contractService.deleteContracts(this.selection).subscribe(
          data => { this.loadData(); }
        );
        this.cleanSelection();
      }
    });
  }

}
