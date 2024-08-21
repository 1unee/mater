import {Inject, Injectable} from '@angular/core';
import {DOCUMENT} from "@angular/common";

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
    return this.linkElement.href.includes('dark');
  }

  get isLightMode(): boolean {
    return this.linkElement.href.includes('light');
  }

  setDarkMode(): void {
    if (this.isLightMode) {
      this.linkElement.href = 'theme-dark.css';
    } else {
      console.log('Dark mode already set!');
    }
  }

  setLightMode(): void {
    if (this.isDarkMode) {
      this.linkElement.href = 'theme-light.css';
    } else {
      console.log('Light theme already set!')
    }
  }

  toggleThemeMode(): void {
    if (this.isDarkMode) {
      this.setLightMode();
    } else {
      this.setDarkMode();
    }
  }

  setThemeMode(mode: 'light' | 'dark'): void {
    if (mode === 'light') {
      this.setLightMode();
    } else {
      this.setDarkMode();
    }
  }
}
