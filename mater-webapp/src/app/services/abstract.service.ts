import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {businessVariables} from "../app.config";
import BusinessVariableModel from "../store/business-variable.model";

@Injectable({
  providedIn: 'root'
})
export class AbstractService {

  private readonly _COMMON_REST_PATH: string = businessVariables
    .find((businessVariable: BusinessVariableModel): boolean => businessVariable.key === 'prefix-rest-path')
    ?.value;

  constructor(private _http: HttpClient) { }

  protected getExtendedRestPath(postfix: string, ...args: string[]): string {
    return `${this._COMMON_REST_PATH}/${postfix}${args.length === 0 ? '' : '/'}` + args.join('/');
  }
}
