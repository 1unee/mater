import {Inject, Injectable} from '@angular/core';
import {Message, MessageService} from "primeng/api";
import {Observable} from "rxjs";
import {GLOBAL_CONFIG} from "../../app.config";
import {GlobalConfig} from "../../store/interfaces/global-config.interface";
import {MessageTypeEnum} from "../../store/enums/message-type.enum";
import {MessagePositionEnum} from "../../store/enums/message-position.enum";

@Injectable({
  providedIn: 'root'
})
export class OneuneMessageService {

  constructor(private messageService: MessageService,
              @Inject(GLOBAL_CONFIG) private globalConfig: GlobalConfig) {
  }

  get creatingObserver(): Observable<Message | Message[]> {
    return this.messageService.messageObserver;
  }

  get deletingObserver(): Observable<string> {
    return this.messageService.clearObserver;
  }

  private getCapitalized(line: string): string {
    return line[0].toUpperCase() + line.substring(1, line.length);
  }

  clearScreen(): void {
    this.messageService.clear();
  }

  private _getLocalizedSummary(severity: MessageTypeEnum): string {
    let localeSummary: string = 'Непонятно...';
    switch (severity) {
      case MessageTypeEnum.SUCCESS: {
        localeSummary = 'Успех';
        break;
      }
      case MessageTypeEnum.INFO: {
        localeSummary = 'Информация';
        break;
      }
      case MessageTypeEnum.WARN: {
        localeSummary = 'Внимание';
        break;
      }
      case MessageTypeEnum.ERROR: {
        localeSummary = 'Ошибка';
        break;
      }
    }
    return localeSummary;
  }

  private _showLite<T extends Object>(severity: MessageTypeEnum, detail: T, sticky: boolean): void {
    this.messageService.add({
      key: MessagePositionEnum.TOP_RIGHT,
      severity: severity,
      summary: `${this._getLocalizedSummary(severity)}`,
      detail: detail.toString(),
      closable: true,
      life: this.globalConfig.configs.messageLifeDuration,
      sticky: sticky
    });
  }

  showSuccess<T extends Object>(detailContent: T): void {
    this._showLite(MessageTypeEnum.SUCCESS, detailContent, false);
  }

  showInfo<T extends Object>(detailContent: T): void {
    this._showLite(MessageTypeEnum.INFO, detailContent, false);
  }

  showWarning<T extends Object>(detailContent: T): void {
    this._showLite(MessageTypeEnum.WARN, detailContent, true);
  }

  showError<T extends Object>(detailContent: T): void {
    this._showLite(MessageTypeEnum.ERROR, detailContent, true);
  }

  showDefaultError(): void {
    this._showLite(MessageTypeEnum.ERROR, 'Произошла ошибка на сервере...', true);
  }

  showCustom(customMessage: Message): void {
    this.messageService.add(customMessage);
  }

  showByHttpStatusCode(httpStatusCode: number): void {
    const code: string = httpStatusCode.toString();
    if (code.startsWith('1')) {
      this.showInfo('Информационный ответ');
    } else if (code.startsWith('2')) {
      this.showSuccess('Успешное действие!');
    } else if (code.startsWith('3')) {
      this.showInfo('Перенаправление завершено!');
    } else if (code.startsWith('4')) {
      this.showError('Ошибка на клиенте...');
    } else if (code.startsWith('5')) {
      this.showError('Ошибка на сервере...');
    } else {
      this.showWarning('Непонятный результат...');
    }
  }

  informAboutDeveloping(): void {
    this.showInfo('В разработке! Скоро добавим :)');
  }
}
