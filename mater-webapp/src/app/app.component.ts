import {Component, OnInit} from '@angular/core';
import {StartPageComponent} from "./components/pages/start-page/start-page.component";
import {RouterOutlet} from "@angular/router";
import {ToastModule} from "primeng/toast";
import {Message} from "primeng/api";
import {OneuneMessageService} from "./services/oneune-message.service";
import {NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {TooltipModule} from "primeng/tooltip";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    StartPageComponent,
    RouterOutlet,
    ToastModule,
    NgIf,
    ButtonDirective,
    Ripple,
    TooltipModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  messages: Message[] = [];

  constructor(private oneuneMessageService: OneuneMessageService) {
    this._subscribeOnCreatingMessages();
  }

  ngOnInit(): void {
  }

  private _subscribeOnCreatingMessages(): void {
    this.oneuneMessageService.messageObserver.subscribe((messages: Message | Message[]): void => {
      if (messages instanceof Array) {
        this.messages.push(...messages);
      } else {
        this.messages.push(messages);
      }
    })
  }

  clearMessages(): void {
    this.oneuneMessageService.clearMessages();
    this.messages = [];
  }
}
