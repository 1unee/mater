import {Component, OnDestroy, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {SelectButtonModule} from "primeng/selectbutton";
import {ThemeService} from "../../../services/utils/theme.service";
import {DropdownChangeEvent, DropdownModule} from "primeng/dropdown";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {StorageService} from "../../../services/utils/storage.service";
import {ConfirmationService} from "primeng/api";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
import {SettingService} from "../../../services/https/setting.service";
import {UserSettingsDto} from "../../../store/dtos/settings/userSettingsDto";
import {NgForOf} from "@angular/common";
import {SettingDto} from "../../../store/dtos/settings/setting.dto";
import {ThemeEnum} from "../../../store/enums/theme.enum";

@Component({
  selector: 'app-settings-page',
  standalone: true,
  imports: [
    Button,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    ReactiveFormsModule,
    SelectButtonModule,
    FormsModule,
    DropdownModule,
    NgForOf
  ],
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.scss'
})
export class SettingsPageComponent implements OnInit, OnDestroy {

  settings: UserSettingsDto;

  constructor(private themeService: ThemeService,
              public messageService: OneuneMessageService,
              private storageService: StorageService,
              private confirmationService: ConfirmationService,
              private routerService: OneuneRouterService,
              private settingService: SettingService) {
  }

  async ngOnInit(): Promise<void> {
    await this._initializeSettings();
  }

  async ngOnDestroy(): Promise<void> {
    this.settingService.put(this.settings).then((): void => this.messageService.showInfo('Настройки обновлены'));
    this.storageService.setUserSettings(this.settings);
  }

  private async _initializeSettings(): Promise<void> {
    this.settings = await this.settingService.getByUserId(this.storageService.user.id);
    this.settings.settings = this.settings.settings.sort((a, b): number => a.id - b.id);
  }

  private _confirmRedirecting(): void {
    this.confirmationService.confirm({
      message: 'К твоему аккаунту не привязана почта. Открыть твой профиль?',
      accept: async () => this.routerService.relativeRedirect('/profile', {'target-profile-tab': 'user-description'}),
      reject: () => this.messageService.showWarning('Окей, тогда пока уведомлений не будет')
    });
  }

  onSettingChange(setting: SettingDto, event: DropdownChangeEvent): void {
    this._additionActionOnSettingChange(setting, event.value);
    setting.selectedOption = setting.options.find(option => option.value === event.value)!;
  }

  private _additionActionOnSettingChange(setting: SettingDto, newValue: string): void {
    if (setting.code === 6) {
      if (!this.storageService.user.isEmailSet && newValue !== 'TELEGRAM_CHAT') {
        this._confirmRedirecting();
      }
    } else if (setting.title.startsWith('Тема')) {
      this.themeService.setThemeMode(newValue as ThemeEnum);
    }
  }
}
