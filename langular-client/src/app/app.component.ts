import {Component, Inject} from '@angular/core';
import {StorageService} from "./storage.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Langular';

  constructor(private storageService : StorageService) {
    console.log(storageService);
  }
}
