import {Injectable} from '@angular/core';
import {UserDto} from "../../store/dtos/user.dto";
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {VariableFieldEnum} from "../../store/enums/variable-field.enum";
import {UserTokenDto} from "../../store/dtos/user-token.dto";
import {UserRegistrationRequestDto} from "../../store/dtos/user-registration-request.dto";
import {UserLoginRequestDto} from "../../store/dtos/user-login-request.dto";

@Injectable({
  providedIn: 'root'
})
export class UserService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('users');

  constructor(private http: HttpClient) {
    super(http);
  }

  async registerOrGet(telegramUser: WebAppUser, telegramChatId: number): Promise<UserDto> {
    const params: HttpParams = new HttpParams().append('telegram-chat-id', telegramChatId);
    return lastValueFrom(
      this.http.post<UserDto>(`${this._REST_PATH}/by-telegram`, telegramUser, { params: params })
    );
  }

  async putByTelegram(userId: number,
                      token: number,
                      telegramUser: WebAppUser,
                      telegramChatId: number): Promise<UserDto> {
    const params: HttpParams = new HttpParams()
      .append('user-id', userId)
      .append('token', token)
      .append('telegram-chat-id', telegramChatId);
    return lastValueFrom(
      this.http.put<UserDto>(`${this._REST_PATH}/by-telegram`, telegramUser, { params: params })
    );
  }

  async registerByForeignLink(userRegistration: UserRegistrationRequestDto): Promise<UserDto> {
    return lastValueFrom(
      this.http.post<UserDto>(`${this._REST_PATH}`, userRegistration)
    );
  }

  async login(userLogin: UserLoginRequestDto): Promise<UserDto> {
    const params: HttpParams = new HttpParams()
      .append('username', userLogin.username)
      .append('password', userLogin.password);
    return lastValueFrom(
      this.http.get<UserDto>(`${this._REST_PATH}/authorization`, { params: params })
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

  async getUserToken(userId: number): Promise<UserTokenDto> {
    return lastValueFrom(
      this.http.get<UserTokenDto>(`${this._REST_PATH}/${userId}/token`)
    );
  }
}
