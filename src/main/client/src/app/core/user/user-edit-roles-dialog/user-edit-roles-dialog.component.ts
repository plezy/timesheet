import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { User } from 'src/app/common/model/user';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Role } from 'src/app/common/model/role';
import { UserService } from 'src/app/common/services/user.service';

export class UserEditRolesDialogData {
  user: User;
}

@Component({
  selector: 'app-user-edit-roles-dialog',
  templateUrl: './user-edit-roles-dialog.component.html',
  styles: [ `
    .fill-space {
      flex: 1 1 auto;
    }

    .check-box-label {
      padding-left: 10px;
      font-size: 14px;
      font-weight: 400;
    }
  `]
})
export class UserEditRolesDialogComponent implements OnInit {

  private form: FormGroup;
  private user: User;
  private listRoles: Role[];
  private selectedRolesID: string[];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    public dialogRef: MatDialogRef<UserEditRolesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UserEditRolesDialogData) {
      this.user = data.user;
  }

  ngOnInit() {
    this.form = this.fb.group({
    });

    this.userService.getRoles().subscribe(
      allRoles => {
        // console.log(allRoles);
        this.listRoles = allRoles;
        const formControls = this.listRoles.map(role => {
          this.form.addControl(role.roleId, new FormControl(false));
        });
        if (this.user.roles) {
          this.user.roles.forEach(role => {
            this.form.get(role).setValue(true);
          });
        }
        // console.log(this.form);
    });

    this.selectedRolesID = new Array();
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save() {
    if (this.form.valid) {
      // console.log(this.form);
      this.listRoles.forEach(role => {
        if (this.form.get(role.roleId).value) {
          // console.log(role.roleId);
          this.selectedRolesID.push(role.roleId);
        }
      });
      // console.log(this.selectedRolesID);
      this.dialogRef.close( { roles: this.selectedRolesID } );
    }
  }

}
