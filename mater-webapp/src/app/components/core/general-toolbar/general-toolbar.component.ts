import {Component, OnInit} from '@angular/core';
import {MenubarModule} from "primeng/menubar";
import {MenuItem} from "primeng/api";
import {Router} from "@angular/router";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ActionService} from "../../../services/https/action.service";
import {StorageService} from "../../../services/utils/storage.service";
import {RoleEnum} from "../../../store/enums/role.enum";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
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

  constructor(private routerService: OneuneRouterService,
              private messageService: OneuneMessageService,
              private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.initializeToolbarItems();
  }

  private initializeToolbarItems(): void {
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
            label: 'Проверки по ВИН-коду',
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
        label: 'Администрирование',
        icon: 'pi pi-globe',
        visible: this.storageService.userHasRole(RoleEnum.ADMIN),
        items: [
          {
            label: 'Пользователи',
            icon: 'pi pi-users',
            command: () => this._openAdministratingPage()
          }
        ]
      },
      {
        label: 'Помощь',
        icon: 'pi pi-question',
        command: () => this._openSupportPage()
      }
    ];
  }

  private _openAdministratingPage(): void {
    this.routerService.relativeRedirect('/administrating');
  }

  private _openProfilePage(): void {
    this.routerService.relativeRedirect('/profile');
  }

  private _openCarsMarketPage(): void {
    this.routerService.relativeRedirect('/cars/market');
  }

  private _openSettingsPage(): void {
    this.routerService.relativeRedirect('/settings');
  }

  private _openSupportPage(): void {
    this.routerService.relativeRedirect('/support');
  }
}
