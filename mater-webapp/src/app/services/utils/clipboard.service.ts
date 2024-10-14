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

  copyWithoutMessage<T extends Object>(data: T | undefined): void {
    if (!!data) {
      navigator.clipboard.writeText(data.toString()).catch(async (e) => {
        this.oneuneMessageService.showError('Скопировать не получилось :(');
        await this.logService.post(e as Error);
      });
    } else {
      this.oneuneMessageService.showInfo('Сначала нужно заполнить поле!');
    }
  }

  copyWithCustomMessage<T extends Object>(data: T | undefined, message: string): void {
    this.copyWithoutMessage(data);
    this.oneuneMessageService.showInfo(message);
  }

  copyWithDefaultMessage<T extends Object>(data: T | undefined): void {
    this.copyWithCustomMessage(!!data ? data.toString() : undefined, 'Скопировано!');
  }
}
