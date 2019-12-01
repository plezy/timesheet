import { Component, OnInit } from '@angular/core';
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
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-contract-list-tm-details',
  templateUrl: './contract-list-tm-details.component.html',
  styleUrls: ['./contract-list-tm-details.component.css']
})
export class ContractListTmDetailsComponent implements OnInit {

  contract: Contract;
  contractId: number;
  me: User;
  profiles: ContractProfile[];
  displayedColumns: string[] = [ 'name', 'description', 'hourlyRate', 'minimumDailyInvoiced',
          'maximumDailyInvoiced', 'completed', 'action'];
  saveDisabled = true;

  constructor(private contractService: ContractService, 
    private profileService: ContractProfileService,
    private authService: AuthService, private userService: UserService,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog,
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
    /*
    this.contract = this.activatedRoute.paramMap.pipe(
      switchMap(params => {
        console.log(params);
        this.contractId = +params.get('contractId');
        return this.contractService.getContract(this.contractId);
      })
    );
    */
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
        this.profiles = profiles;
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

  toggleCompleted(profile: ContractProfile) {
    this.profiles.some(element => {
      if (element.id == profile.id) {
        console.log('Toggling profile id : ' + element.id);
        element.completed = !profile.completed;
        this.saveDisabled = false;
        return true; // exit if found
      }
      return false; //  continue if not found
    });
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
          this.profiles.push(result.profile);
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
          this.profiles[index] = result.profile;
          this.saveDisabled = false;
        }
      }
    });
  }


}
