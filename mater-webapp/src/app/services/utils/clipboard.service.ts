import { Injectable } from '@angular/core';
import {OneuneMessageService} from "./oneune-message.service";
import {LogService} from "../https/log.service";

@Injectable({
  providedIn: 'root'
})
export class ClipboardService {

  constructor(private oneuneMessageService: OneuneMessageService,
              private logService: LogService) {
  }

  simpleCopy<T extends Object>(data: T | undefined): void {
    this.extCopy(!!data ? data.toString() : undefined, 'Скопировано!');
  }

  extCopy<T extends Object>(data: T | undefined, message: string): void {
    if (!!data) {
      navigator.clipboard.writeText(data.toString()).then((): void => {
        this.oneuneMessageService.showInfo(message);
      }).catch(async (e) => {
        this.oneuneMessageService.showError('Скопировать не получилось :(');
        await this.logService.post(e as Error);
      });
    } else {
      this.oneuneMessageService.showInfo('Сначала нужно заполнить поле!');
    }
  }
}
