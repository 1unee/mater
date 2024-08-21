import { Injectable } from '@angular/core';
import {WebAppDataEnum} from "../store/enums/web-app-data.enum";
import {ThemeService} from "./theme.service";

@Injectable({
  providedIn: 'root'
})
export class TelegramService {

  private _tgWebApp: TelegramWebApp;

  constructor(private themeService: ThemeService) {
    this._tgWebApp = window.Telegram.WebApp;
    this._tune();
  }

  private _tune(): void {
    this._tgWebApp.ready();
    this.themeService.setThemeMode(this._tgWebApp.colorScheme);
    this._tgWebApp.expand();
    this._tgWebApp.enableVerticalSwipes();
    this._tgWebApp.enableClosingConfirmation();
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
    console.log('stringified object: ' + internalVar);
    this._tgWebApp.sendData(internalVar);
  }
}
