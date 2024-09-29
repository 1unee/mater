import {Inject, Injectable} from '@angular/core';
import {DOCUMENT} from "@angular/common";
import {ThemeEnum} from "../../store/enums/theme.enum";

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  constructor(@Inject(DOCUMENT) private _document: Document) {
  }

  private get linkElement(): HTMLLinkElement {
    return this._document.getElementById('app-theme') as HTMLLinkElement;
  }

  get isSystemDark(): boolean {
    return window?.matchMedia?.('(prefers-color-scheme:dark)')?.matches;
  }

  get isDarkMode(): boolean {
    return this.linkElement.href.includes(ThemeEnum.DARK);
  }

  get isLightMode(): boolean {
    return this.linkElement.href.includes(ThemeEnum.LIGHT);
  }

  setSystemMode(): void {
    if (this.isSystemDark) {
      this.setDarkMode();
    } else {
      this.setLightMode();
    }
  }

  setDarkMode(): void {
    if (this.isLightMode) {
      this.linkElement.href = 'theme-dark.css';
    }
  }

  setLightMode(): void {
    if (this.isDarkMode) {
      this.linkElement.href = 'theme-light.css';
    }
  }

  toggleThemeMode(): void {
    if (this.isDarkMode) {
      this.setLightMode();
    } else {
      this.setDarkMode();
    }
  }

  setThemeMode(mode: ThemeEnum): void {
    switch (mode) {
      case ThemeEnum.SYSTEM: {
        this.setSystemMode();
        break
      }
      case ThemeEnum.DARK: {
        this.setDarkMode();
        break;
      }
      case ThemeEnum.LIGHT: {
        this.setLightMode();
        break;
      }
    }
  }
}
