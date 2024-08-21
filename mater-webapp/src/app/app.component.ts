import {Component, OnInit} from '@angular/core';
import {ThemeService} from "./services/theme.service";
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

  constructor(private themeService: ThemeService) {
  }

  ngOnInit(): void {
    this.themeService.setThemeMode(window.Telegram.WebApp.colorScheme);
  }
}
