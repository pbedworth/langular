import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { TexteditComponent } from './textedit/textedit.component';
import { FocusDirective } from './focus.directive';
import {StorageService} from "./storage.service";

@NgModule({
  declarations: [
    AppComponent,
    TexteditComponent,
    FocusDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [StorageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
