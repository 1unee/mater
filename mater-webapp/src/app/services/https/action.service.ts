import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {ActionTypeEnum} from "../../store/enums/action-type.enum";
import {UserDto} from "../../store/dtos/user.dto";

@Injectable({
  providedIn: 'root'
})
export class ActionService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('actions');

  constructor(private http: HttpClient) {
    super(http);
  }

  async track(actionType: ActionTypeEnum, value: string, user: UserDto): Promise<void> {
    const params: HttpParams = new HttpParams()
      .append('action-type', actionType)
      .append('value', value);
    return lastValueFrom(
      this.http.post<void>(`${this._REST_PATH}`, user, { params: params })
    );
  }
}
