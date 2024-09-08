import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {StorageService} from "../utils/storage.service";

@Injectable({
  providedIn: 'root'
})
export class ActionService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('actions');

  constructor(private http: HttpClient,
              private storageService: StorageService) {
    super(http);
  }

  async track(route: string): Promise<void> {
    const params: HttpParams = new HttpParams().append('route', route);
    return lastValueFrom(
      this.http.post<void>(`${this._REST_PATH}`, this.storageService.user, {params})
    );
  }
}
