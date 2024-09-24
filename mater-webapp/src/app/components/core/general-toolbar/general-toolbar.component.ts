import {Component, ElementRef, OnInit} from '@angular/core';
import {MenubarModule} from "primeng/menubar";
import {BlockableUI, MenuItem} from "primeng/api";
import {ActivatedRoute, Router} from "@angular/router";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ActionService} from "../../../services/https/action.service";
import {BlockUI} from "primeng/blockui";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";

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

  constructor(private messageService: OneuneMessageService,
              private routerService: OneuneRouterService) {
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

  private _openProfilePage(): void {
    this.routerService.wrapRouting('/profile')
  }

  private _openCarsMarketPage(): void {
    this.routerService.wrapRouting('/cars/market');
  }

  private _openActionsPage(): void {
    this.routerService.wrapRouting('/actions');
  }

  private _openSettingsPage(): void {
    this.routerService.wrapRouting('/settings');
  }

  private _openSupportPage(): void {
    this.routerService.wrapRouting('/support');
  }
}
