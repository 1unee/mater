import { Injectable } from '@angular/core';
import {Message, MessageService} from "primeng/api";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OneuneMessageService {

  private _lifeDuration: number = 5000; // in ms

  constructor(private messageService: MessageService) { }

  get messageObserver(): Observable<Message | Message[]> {
    return this.messageService.messageObserver;
  }

  private getCapitalized(line: string): string {
    return line[0].toUpperCase() + line.substring(1, line.length);
  }

  clearMessages(): void {
    this.messageService.clear();
  }

  private showLiteMessage(severity: MessageTypeEnum, detail: string, sticky: boolean): void {
    this.messageService.add({
      key: MessagePosition.TOP_RIGHT,
      severity: severity,
      summary: `${this.getCapitalized(severity)} (${new Date().toLocaleTimeString()})`,
      detail: detail,
      closable: true,
      life: this._lifeDuration,
      sticky: sticky
    });
  }

  showSuccessMessage(detailContent: string): void {
    this.showLiteMessage(MessageTypeEnum.SUCCESS, detailContent, false);
  }

  showInfoMessage(detailContent: string): void {
    this.showLiteMessage(MessageTypeEnum.INFO, detailContent, false);
  }

  showWarningMessage(detailContent: string): void {
    this.showLiteMessage(MessageTypeEnum.WARN, detailContent, true);
  }

  showErrorMessage(detailContent: string): void {
    this.showLiteMessage(MessageTypeEnum.ERROR, detailContent, true);
  }

  showCustomMessage(customizedMessage: Message): void {
    this.messageService.add(customizedMessage);
  }

  showMessageByHttpStatusCode(httpStatusCode: number): void {
    if (httpStatusCode.toString().startsWith('1')) {
      this.showInfoMessage('An informational response!');
    } else if (httpStatusCode.toString().startsWith('2')) {
      this.showSuccessMessage('A successful response!');
    } else if (httpStatusCode.toString().startsWith('3')) {
      this.showInfoMessage('A redirection completed!');
    } else if (httpStatusCode.toString().startsWith('4')) {
      this.showErrorMessage('A client error response!');
    } else if (httpStatusCode.toString().startsWith('5')) {
      this.showErrorMessage('A server error response!');
    } else {
      this.showErrorMessage('Unknown error!');
    }
  }

  showDtoPostedMessage(dtoClassReference: Function): void {
    const dtoName: string = dtoClassReference.name;
    this.showLiteMessage(
      MessageTypeEnum.SUCCESS, `${dtoName.substring(0, dtoName.length - 3)} successfully created!`, true
    );
  }

  showDtoPutMessage(dtoClassReference: Function): void {
    const dtoName: string = dtoClassReference.name;
    this.showLiteMessage(
      MessageTypeEnum.SUCCESS, `${dtoName.substring(0, dtoName.length - 3)} successfully edited!`, true
    );
  }

  showDtoDeletedMessage(dtoClassReference: Function): void {
    const dtoName: string = dtoClassReference.name;
    this.showLiteMessage(
      MessageTypeEnum.SUCCESS, `${dtoName.substring(0, dtoName.length - 3)} successfully removed!`, true
    );
  }
}

enum MessageTypeEnum {
  SUCCESS = 'success',
  INFO = 'info',
  WARN = 'warn',
  ERROR = 'error'
}

enum MessagePosition {
  TOP_LEFT = 'tl',
  TOP_CENTER = 'tc',
  TOP_RIGHT = 'tr',
  BOTTOM_LEFT = 'bl',
  BOTTOM_CENTER = 'bc',
  BOTTOM_RIGHT = 'br',
}
