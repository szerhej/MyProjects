import { Component } from '@angular/core';
import { HeroesModule }     from './heroes/heroes.module';

@Component({
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
}
