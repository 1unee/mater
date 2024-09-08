import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LogService extends AbstractHttpService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('logs');

  constructor(private http: HttpClient) {
    super(http);
  }

  async post(error: Error): Promise<void> {
    return lastValueFrom(
      this.http.post<void>(`${this._REST_PATH}`, JSON.stringify(error))
    );
  }
}
