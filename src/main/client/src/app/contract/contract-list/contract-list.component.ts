import { Component, OnInit } from '@angular/core';
import { Page } from 'src/app/common/model/page';
import { ContractDto } from 'src/app/common/model/contract';
import { User } from 'src/app/common/model/user';
import { UserService } from 'src/app/common/services/user.service';
import { AuthService } from 'src/app/core/auth/auth.service';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { ContractService } from 'src/app/common/services/contract.service';
import { ContractAddEditDialogComponent } from '../contract-add-edit-dialog/contract-add-edit-dialog.component';

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

  displayedColumns: string[] = [/*'selector',*/ 'name', 'type', 'customer', 'action'];

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
        }
      );
    } else {
      if (this.showDeleted) {
        this.contractService.getAllContracts(this.pageSize, this.pageIndex).subscribe(
          result =>  {
            this.page = result;
          }
        );
      } else {
        this.contractService.getContracts(this.pageSize, this.pageIndex).subscribe(
          result =>  {
            this.page = result;
          }
        );
      }
    }
  }

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
        /*
        if (result.contract) {
          this.contractService.addContract(result.contract).subscribe(
            addedContract => {
              console.log('Contract added');
              this.loadData();
          });
        }
        */
      }
    });
  }
}
