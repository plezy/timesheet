import { Component, Inject, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { TaskTimetrack } from "../../common/model/taskTimetrack";

export interface TimeTrackAddEditDialogComponentData {
  title: string;
  taskTimeTrack: TaskTimetrack;
}

@Component({
  selector: 'app-time-track-add-edit-dialog',
  templateUrl: './time-track-add-edit-dialog.component.html',
  styles: [`
    .task-description {
      padding: 0px 0px 10px 15px;
    }
  `]
})
export class TimeTrackAddEditDialogComponent implements OnInit {

  public editMode = false;
  public title: string;
  public form: FormGroup;

  private taskTimeTrack: TaskTimetrack;

  constructor(private fb: FormBuilder,
              public dialogRef: MatDialogRef<TimeTrackAddEditDialogComponentData>,
              @Inject(MAT_DIALOG_DATA) public data: TimeTrackAddEditDialogComponentData) {
    this.title = data.title;

    if (data.taskTimeTrack) {
      this.editMode = true;
      this.taskTimeTrack = data.taskTimeTrack;
    } else {
      this.taskTimeTrack = new TaskTimetrack();
    }
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      duration: [this.taskTimeTrack.duration, [Validators.required]],
      note: [this.taskTimeTrack.note, []],
    });
  }

  save() {
    this.dialogRef.close();
  }

  cancel() {
    this.dialogRef.close();
  }
}
