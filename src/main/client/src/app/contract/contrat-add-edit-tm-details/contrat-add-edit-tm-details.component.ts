import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ContractService } from 'src/app/common/services/contract.service';
import { Contract } from 'src/app/common/model/contract';
import { AuthService } from 'src/app/core/auth/auth.service';
import { UserService } from 'src/app/common/services/user.service';
import { User } from 'src/app/common/model/user';
import { Location } from '@angular/common';
import { ContractProfileService } from 'src/app/common/services/contract-profile.service';
import { ContractProfile } from 'src/app/common/model/contractProfile';

@Component({
  selector: 'app-contrat-add-edit-tm-details',
  templateUrl: './contrat-add-edit-tm-details.component.html',
  styleUrls: ['./contrat-add-edit-tm-details.component.css']
})
export class ContratAddEditTmDetailsComponent implements OnInit {

  contract: Contract;
  contractId: number;
  me: User;
  profiles: ContractProfile[];
  displayedColumns: string[] = [ 'name', 'description', 'hourlyRate', 'minimumDailyInvoiced',
          'maximumDailyInvoiced', 'completed', 'action'];
  
  constructor(private contractService: ContractService, 
    private profileService: ContractProfileService,
    private authService: AuthService, private userService: UserService,
    private activatedRoute: ActivatedRoute,
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
    );*/
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
      });
  }

  cancel() {
    this.location.back();
  }

  save() {
    this.location.back();
  }
}
