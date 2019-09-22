import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { MaterialModule } from './material.module';

import { AppComponent } from './app.component';
import { httpInterceptorProviders } from './core/auth/auth-interceptor';

import 'hammerjs';
import { CustomerModule } from './customer/customer.module';
import { ConfirmDialogComponent } from './common/dialog/confirm-dialog/confirm-dialog.component';
import { ContractModule } from './contract/contract.module';
import { MAT_DATE_LOCALE } from '@angular/material';

@NgModule({
  declarations: [
    AppComponent,
    ConfirmDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    MaterialModule,
    CustomerModule,
    ContractModule
  ],
  entryComponents: [ ConfirmDialogComponent ],
  providers: [
    httpInterceptorProviders,
    { provide: MAT_DATE_LOCALE, useValue: 'fr-FR'}
  ],
  bootstrap: [ AppComponent ]
})

export class AppModule {


}
