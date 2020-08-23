import { Component, OnInit, ɵConsole } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ContractService } from 'src/app/common/services/contract.service';
import { AuthService } from 'src/app/core/auth/auth.service';
import { UserService } from 'src/app/common/services/user.service';
import { ContractProfileService } from 'src/app/common/services/contract-profile.service';
import { Contract } from 'src/app/common/model/contract';
import { User } from 'src/app/common/model/user';
import { ContractProfile } from 'src/app/common/model/contractProfile';
import { ContractAddEditTmProfileDialogComponent } from '../contract-add-edit-tm-profile-dialog/contract-add-edit-tm-profile-dialog.component';
import { MatDialog, throwMatDialogContentAlreadyAttachedError } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table'
import {ContractTmAddAssigneeDialogComponent} from "../contract-tm-add-assignee-dialog/contract-tm-add-assignee-dialog.component";

@Component({
  selector: 'app-contract-list-tm-details',
  templateUrl: './contract-list-tm-details.component.html',
  styleUrls: ['./contract-list-tm-details.component.css']
})
export class ContractListTmDetailsComponent implements OnInit {

  contract: Contract;
  contractId: number;
  me: User;
  //profiles: ContractProfile[];
  datasource: MatTableDataSource<ContractProfile>;

  displayedColumns: string[] = [ 'name', 'description', 'hourlyRate', 'minimumDailyInvoiced',
          'maximumDailyInvoiced', 'multipleUnitInvoiced', 'assignments', 'completed', 'action'];
  saveDisabled = true;

  constructor(private contractService: ContractService,
    private profileService: ContractProfileService,
    private authService: AuthService, private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router, private location: Location) { }

  ngOnInit() {
    console.log('ngOnInit');
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

    this.activatedRoute.paramMap.subscribe(params => {
      console.log(params);
      // + needed to covert string to number
      this.contractId = +params.get('contractId');
      this.contractService.getContract(this.contractId).subscribe(contract => {
          this.contract = contract;
          console.log(contract);
        });
    });

    this.loadData();
  }

  /**
   * Data management
   */

  /** retrieve rows */
  loadData() {
      this.profileService.getProfileForCOntract(this.contractId).subscribe( profiles =>{
        console.log(profiles);
        this.datasource = new MatTableDataSource(profiles);
        this.saveDisabled = true;
      });
  }

  /* UI interface */

  cancel() {
    this.location.back();
  }

  save() {
    this.location.back();
  }

  toggleCompleted(index: number) {
    this.datasource.data[index].completed = ! this.datasource.data[index].completed
  }

  /**
   * Click Add
   */
  clickAdd() {
    const dialogRef = this.dialog.open(ContractAddEditTmProfileDialogComponent, {
      width: '700px', data: { title: 'Add Profile' }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.profile) {
          let profiles: ContractProfile[] = this.datasource.data;
          profiles.push(result.profile);
          this.datasource = new MatTableDataSource(profiles);
          this.saveDisabled = false;
        }
      }
    });
  }

  /**
   * Click Edit
   */
  clickEdit(row: ContractProfile, index: number) {
    console.log('Editing row index : ' + index);
    const dialogRef = this.dialog.open(ContractAddEditTmProfileDialogComponent, {
      width: '700px', data: { title: 'Edit Profile', profile: row }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        console.log(result);
        if (result.profile) {
          this.datasource.data[index] = result.profile;
          this.saveDisabled = false;
          console.log(this.datasource.data);
        }
      }
    });
  }

  /** add task assignment */
  addAssignment(profile: ContractProfile) {
    console.log("add assignment on profile " + profile.name);

    this.profileService.getAssignable(profile.id).subscribe(users => {
        const dialogRef = this.dialog.open(ContractTmAddAssigneeDialogComponent, {
          width: '700px', data: { title: 'Add Assignee', users: users }
        });
        dialogRef.afterClosed().subscribe(result => {
          console.log('dialog closed');
          if (result) {
            if (result.assignees) {
              if (result.assignees.length > 0) {
                this.profileService.addAssignees(this.contract.id, profile.id, result.assignees).subscribe(result => { this.loadData() });
              }
            }
          }
        });
    });

  }

  /** remove assignment */
  removeAssignment(profile: ContractProfile, assignee: User) {
    console.log("remove '" + assignee.firstName + " " + assignee.lastName + "' ("+ assignee.id + ") from contract ID: " + this.contract.id + ", profile ID : " + profile.id);
    this.profileService.removeAssignee(this.contract.id, profile.id, [ assignee.id ]).subscribe(result => { this.loadData() });
  }
}
