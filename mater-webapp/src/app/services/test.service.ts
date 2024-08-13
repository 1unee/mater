import { Injectable } from '@angular/core';
import {AbstractService} from "./abstract.service";
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TestService extends AbstractService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('test');

  constructor(private http: HttpClient) {
    super(http);
  }

  async test(): Promise<void> {
    return lastValueFrom(
      this.http.get<void>(`${this._REST_PATH}`)
    );
  }
}
