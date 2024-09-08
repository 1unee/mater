import { Injectable } from '@angular/core';
import {UserDto} from "../../store/dtos/user.dto";
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient} from "@angular/common/http";
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

  async registerOrGet(telegramUser: WebAppUser): Promise<UserDto> {
    return lastValueFrom(
      this.http.post<UserDto>(`${this._REST_PATH}/by-telegram-user`, telegramUser)
    );
  }

  async putByParams(user: UserDto, variableField: VariableFieldEnum): Promise<UserDto> {
    return lastValueFrom(
      this.http.put<UserDto>(`${this._REST_PATH}/${user.id}/fields/${variableField}`, user)
    );
  }
}
