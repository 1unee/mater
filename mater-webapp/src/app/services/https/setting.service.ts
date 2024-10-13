import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {UserSettingsDto} from "../../store/dtos/settings/userSettingsDto";
import {lastValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SettingService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('settings');

  constructor(private http: HttpClient) {
    super(http);
  }

  async postDefault(userId: number): Promise<UserSettingsDto> {
    const params: HttpParams = new HttpParams().append('user-id', userId);
    return lastValueFrom(
      this.http.post<UserSettingsDto>(`${this._REST_PATH}/default`, {}, { params: params })
    );
  }

  async getByUserId(userId: number): Promise<UserSettingsDto> {
    return lastValueFrom(
      this.http.get<UserSettingsDto>(`${this._REST_PATH}/users/${userId}`)
    );
  }

  async put(settings: UserSettingsDto): Promise<void> {
    return lastValueFrom(
      this.http.put<void>(`${this._REST_PATH}/${settings.id}`, settings)
    );
  }
}
