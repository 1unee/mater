import {Inject, Injectable} from '@angular/core';
import {WebAppDataEnum} from "../../store/enums/web-app-data.enum";
import {ThemeService} from "./theme.service";
import {OneuneMessageService} from "./oneune-message.service";
import {UserDto} from "../../store/dtos/user.dto";
import {UserService} from "../https/user.service";
import {StorageService} from "./storage.service";
import {LOADING} from "../../app.config";
import {ThemeEnum} from "../../store/enums/theme.enum";
import {LoadingReference} from "../../store/interfaces/loading-reference.interface";
import {LogService} from "../https/log.service";
import {environment} from "../../../environments/environment";

/**
 * Auto-tunes on injecting in a start component;
 */
@Injectable({
  providedIn: 'root'
})
export class TelegramService {

  private readonly _tgWebApp: TelegramWebApp;

  constructor(private themeService: ThemeService,
              private messageService: OneuneMessageService,
              private userService: UserService,
              private storageService: StorageService,
              @Inject(LOADING) public loading: LoadingReference,
              private logService: LogService) {
    this._tgWebApp = window.Telegram.WebApp;
  }

  /**
   * Must have to call.
   * Fetches data form telegram bot.
   */
  async tune(): Promise<void> {
    try {
      this.loading.value.next(true);
      this._tgWebApp.ready();
      this.themeService.setThemeMode(this._tgWebApp.colorScheme as ThemeEnum);
      this._tgWebApp.expand();
      this._tgWebApp.enableVerticalSwipes();
      await this._authenticateUser();
    } catch (e) {
      this.messageService.showError('При получении данных из телеграмма произошла ошибка...');
      await this.logService.post(e as Error);
    } finally {
      this.loading.value.next(false);
    }
  }

  get tgWebApp(): TelegramWebApp {
    return this._tgWebApp;
  }

  get mainBtn(): MainButton {
    return this._tgWebApp.MainButton;
  }

  get backBtn(): BackButton {
    return this._tgWebApp.BackButton;
  }

  get settingsBtn(): SettingsButton {
    return this._tgWebApp.SettingsButton;
  }

  public sendData<D>(type: WebAppDataEnum, data: D): void {
    const internalVar: string = JSON.stringify({type: type, data: data});
    this._tgWebApp.sendData(internalVar);
  }

  get user(): WebAppUser | undefined {
    if (environment.featureFlags.mockTelegramUser) {
      return this._getMockUserForDeveloping(0);
    } else {
      return this._tgWebApp.initDataUnsafe.user;
    }
  }

  get loadedUser(): boolean {
    return !!this.user;
  }

  get chat(): WebAppChat | undefined {
    return this._tgWebApp.initDataUnsafe.chat;
  }

  get loadedChat(): boolean {
    return !!this.chat;
  }

  private async _authenticateUser(): Promise<void> {
    if (this.loadedUser) {
      try {
        const user: UserDto = await this.userService.registerOrGet(this.user!);
        this.storageService.authenticateUser(user);
      } catch (e) {
        this.messageService.showError('Произошла ошибка при авторизации/регистрации.');
      }
    } else {
      this.messageService.showInfo('Данные о пользователе не загружены :(');
    }
  }

  private _getMockUserForDeveloping(index: number): WebAppUser {
    console.log(`Using test user[${index}] mock data for developing!`);
    const mockWebAppUsers: WebAppUser[] = [
      {
        id: 1173195533,
        firstName: 'Igor',
        lastName: '',
        username: 'ptrbg',
        languageCode: 'ru'
      } as WebAppUser,
      {
        id: 1173195535,
        firstName: 'Igor',
        lastName: '',
        username: 'metango',
        languageCode: 'ru'
      } as WebAppUser
    ];
    return mockWebAppUsers[index];
  }
}
