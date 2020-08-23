import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from "../../common/model/user";
import { MatListOption } from "@angular/material/list";

export interface ContractTmAddAssigneeDialogData {
  title: string;
  users: User[];
}

@Component({
  selector: 'app-contract-tm-add-assignee-dialog',
  templateUrl: './contract-tm-add-assignee-dialog.component.html',
  styles: [
  ]
})
export class ContractTmAddAssigneeDialogComponent implements OnInit {

  title: string;
  users: User[];

  constructor(public dialogRef: MatDialogRef<ContractTmAddAssigneeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ContractTmAddAssigneeDialogData) {
    console.log('ContractTmAddAssigneeDialogComponent constructor');
    this.title = data.title;
    console.log(data.users);
    if (data.users) {
      this.users = data.users;
      console.log(this.users);
    }
  }

  ngOnInit(): void {
  }

  cancel() {
    this.dialogRef.close();
  }

  ok(selected: MatListOption[]) {
    console.log(selected);
    var ids: number[] = [];
    selected.map(selection => { ids.push(selection.value)});

    this.dialogRef.close( { assignees: ids});
  }
}
