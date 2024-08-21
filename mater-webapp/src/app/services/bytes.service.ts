import { Injectable } from '@angular/core';
import {AbstractService} from "./abstract.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BytesService extends AbstractService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('bytes');

  constructor(private http: HttpClient) {
    super(http);
  }
}
