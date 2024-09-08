import {Component, ElementRef, OnInit} from '@angular/core';
import {MenubarModule} from "primeng/menubar";
import {BlockableUI, MenuItem} from "primeng/api";
import {ActivatedRoute, Router} from "@angular/router";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ActionService} from "../../../services/https/action.service";
import {BlockUI} from "primeng/blockui";

@Component({
  selector: 'app-general-toolbar',
  standalone: true,
  imports: [
    MenubarModule
  ],
  templateUrl: './general-toolbar.component.html',
  styleUrl: './general-toolbar.component.scss'
})
export class GeneralToolbarComponent implements OnInit {

  items: MenuItem[];

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private messageService: OneuneMessageService,
              private actionService: ActionService,
              private elementRef: ElementRef) {
  }

  ngOnInit(): void {
    this.items = [
      {
        label: 'Профиль',
        icon: 'pi pi-user',
        command: () => this._openProfilePage()
      },
      {
        label: 'Машины',
        icon: 'pi pi-car',
        items: [
          {
            label: 'Рынок',
            icon: 'pi pi-search',
            command: () => this._openCarsMarketPage()
          }
        ]
      },
      {
        label: 'Функции',
        icon: 'pi pi-star',
        items: [
          {
            label: 'Уведомления',
            icon: 'pi pi-list-check',
            command: () => this.messageService.informAboutDeveloping()
          }
        ]
      },
      {
        label: 'Настройки',
        icon: 'pi pi-wrench',
        command: () => this._openSettingsPage()
      },
      {
        label: 'Помощь',
        icon: 'pi pi-question',
        command: () => this._openSupportPage()
      }
    ]
  }

  private _wrapRouting(futureRoute: string): void {
    const pastUrl: string = this.router.url;
    this.router.navigate([futureRoute]).then(async (): Promise<void> => {
      await this.actionService.track(`${pastUrl} -> ${futureRoute}`);
    });
  }

  private _openProfilePage(): void {
    this._wrapRouting('/profile')
  }

  private _openCarsMarketPage(): void {
    this._wrapRouting('/cars/market');
  }

  private _openActionsPage(): void {
    this._wrapRouting('/actions');
  }

  private _openSettingsPage(): void {
    this._wrapRouting('/settings');
  }

  private _openSupportPage(): void {
    this._wrapRouting('/support');
  }
}
