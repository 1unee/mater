import {Component, OnInit} from '@angular/core';
import {StartPageComponent} from "./components/pages/start-page/start-page.component";
import {RouterOutlet} from "@angular/router";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    StartPageComponent,
    RouterOutlet,
    ToastModule
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
