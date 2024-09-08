import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NotificationService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('notifications');

  constructor(private http: HttpClient) {
    super(http);
  }

  async sendSimpleMailToSelf(telegramUsername: string, messageBody: string): Promise<void> {
    const httpParams: HttpParams = new HttpParams()
      .append('username', telegramUsername)
      .append('text', messageBody);
    return lastValueFrom(
      this.http.post<void>(`${this._REST_PATH}/emails/self`, null, {params: httpParams})
    );
  }
}
