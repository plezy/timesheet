import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreRoutingModule } from './core-routing.module';

import { HomeComponent } from './home/home.component';
import { GitComponent } from './version/git/git.component';
import { ProfileComponent } from './user/profile/profile.component';
import { PasswordComponent } from './user/password/password.component';
import { LoginComponent } from './auth/login/login.component';
import { EnvComponent } from './version/env/env.component';
import { JavaComponent } from './version/java/java.component';
import { BuildComponent } from './version/build/build.component';
import { UserListComponent } from './user/user-list/user-list.component';
import { UserAddEditDialogComponent } from './user/user-add-edit-dialog/user-add-edit-dialog.component';
import { UserSetPasswordDialogComponent } from './user/user-set-password-dialog/user-set-password-dialog.component';
import { UserEditRolesDialogComponent } from './user/user-edit-roles-dialog/user-edit-roles-dialog.component';
import { MaterialModule } from '../material.module';
import { SettingsComponent } from './settings/settings.component';

@NgModule({
  declarations: [
    LoginComponent,
    HomeComponent,
    GitComponent,
    ProfileComponent,
    PasswordComponent,
    EnvComponent,
    JavaComponent,
    BuildComponent,
    UserListComponent,
    UserAddEditDialogComponent,
    UserSetPasswordDialogComponent,
    UserEditRolesDialogComponent,
    SettingsComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    CoreRoutingModule,
  ],
  entryComponents: [ UserAddEditDialogComponent, UserSetPasswordDialogComponent, UserEditRolesDialogComponent ],
})

export class CoreModule { }

