import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserComponent } from './user/user.component';

import { CoreRoutingModule } from './core-routing.module';

@NgModule({
  declarations: [ UserComponent ],
  imports: [
    CoreRoutingModule,
    CommonModule
  ]
})

export class CoreModule { }

