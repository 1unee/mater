import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AbstractHttpService {

  private readonly _COMMON_REST_PATH: string = environment.apiUrl;

  constructor(private _http: HttpClient) { }

  protected getExtendedRestPath(postfix: string, ...args: string[]): string {
    return `${this._COMMON_REST_PATH}/${postfix}${args.length === 0 ? '' : '/'}` + args.join('/');
  }
}
