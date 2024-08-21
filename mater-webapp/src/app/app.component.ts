import {Component, OnInit} from '@angular/core';
import {StartPageComponent} from "./components/start-page/start-page.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    StartPageComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }
}
