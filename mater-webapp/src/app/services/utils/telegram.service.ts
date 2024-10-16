import {Inject, Injectable} from '@angular/core';
import {WebAppDataEnum} from "../../store/enums/web-app-data.enum";
import {ThemeService} from "./theme.service";
import {LOADING} from "../../app.config";
import {ThemeEnum} from "../../store/enums/theme.enum";
import {LoadingReference} from "../../store/interfaces/loading-reference.interface";

/**
 * Auto-tunes on injecting in a start component;
 */
@Injectable({
  providedIn: 'root'
})
export class TelegramService {

  private readonly _tgWebApp: TelegramWebApp;

  constructor(private themeService: ThemeService,
              @Inject(LOADING) public loading: LoadingReference) {
    this._tgWebApp = window.Telegram.WebApp;
  }

  /**
   * Must have to call.
   * Fetches data from telegram bot.
   */
  tune(): boolean {
    if (!this._tgWebApp) {
      return false;
    } else {
      try {
        this.loading.value.next(true);
        this._tgWebApp.ready();
        this.themeService.setThemeMode(this._tgWebApp.colorScheme as ThemeEnum);
        this._tgWebApp.expand();
        this._tgWebApp.enableVerticalSwipes();
        return true;
      } catch (e) {
        return false;
      } finally {
        this.loading.value.next(false);
      }
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
    return this._tgWebApp.initDataUnsafe.user;
  }

  get telegramChatId(): number | undefined {
    return this.loadedChat ? this._tgWebApp.initDataUnsafe.chat?.id : undefined;
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
