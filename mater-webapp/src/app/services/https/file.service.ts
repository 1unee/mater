import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FileService extends AbstractHttpService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('files');

  constructor(private http: HttpClient) {
    super(http);
  }
}
