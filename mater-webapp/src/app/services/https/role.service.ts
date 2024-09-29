import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {RoleDto} from "../../store/dtos/role.dto";

@Injectable({
  providedIn: 'root'
})
export class RoleService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('roles');

  constructor(private http: HttpClient) {
    super(http);
  }

  async getRoles(): Promise<RoleDto[]> {
    return lastValueFrom(
      this.http.get<RoleDto[]>(`${this._REST_PATH}`)
    );
  }
}
