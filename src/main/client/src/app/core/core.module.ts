import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserComponent } from './user/user.component';

import { CoreRoutingModule } from './core-routing.module';
import { HomeComponent } from './home/home.component';
import { GitComponent } from './version/git/git.component';

@NgModule({
  declarations: [ UserComponent, HomeComponent, GitComponent ],
  imports: [
    CommonModule,
    CoreRoutingModule
  ]
})

export class CoreModule { }

