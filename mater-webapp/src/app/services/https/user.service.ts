import { Injectable } from '@angular/core';
import {UserDto} from "../../store/dtos/user.dto";
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {VariableFieldEnum} from "../../store/enums/variable-field.enum";

@Injectable({
  providedIn: 'root'
})
export class UserService extends AbstractHttpService{

  private readonly _REST_PATH = this.getExtendedRestPath('users');

  constructor(private http: HttpClient) {
    super(http);
  }

  async registerOrGet(telegramUser: WebAppUser, telegramChatId: number): Promise<UserDto> {
    const params: HttpParams = new HttpParams().append('telegram-chat-id', telegramChatId);
    return lastValueFrom(
      this.http.post<UserDto>(`${this._REST_PATH}/by-telegram-user`, telegramUser, { params: params })
    );
  }

  async put(user: UserDto): Promise<UserDto> {
    return lastValueFrom(
      this.http.put<UserDto>(`${this._REST_PATH}/${user.id}`, user)
    );
  }

  async putByParams(user: UserDto, variableField: VariableFieldEnum): Promise<UserDto> {
    return lastValueFrom(
      this.http.put<UserDto>(`${this._REST_PATH}/${user.id}/fields/${variableField}`, user)
    );
  }

  async getUsers(): Promise<UserDto[]> {
    return lastValueFrom(
      this.http.get<UserDto[]>(`${this._REST_PATH}`)
    );
  }
}
