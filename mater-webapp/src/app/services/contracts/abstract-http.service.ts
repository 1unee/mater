import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {businessVariables} from "../../app.config";
import {BusinessVariable} from "../../store/interfaces/business-variable.interface";
import {BusinessVariableKeyEnum} from "../../store/enums/business-variable-key.enum";

@Injectable({
  providedIn: 'root'
})
export class AbstractHttpService {

  private readonly _COMMON_REST_PATH: string = businessVariables
    .find((variable: BusinessVariable): boolean => variable.key === BusinessVariableKeyEnum.PREFIX_REST_PATH)
    ?.value;

  constructor(private _http: HttpClient) { }

  protected getExtendedRestPath(postfix: string, ...args: string[]): string {
    return `${this._COMMON_REST_PATH}/${postfix}${args.length === 0 ? '' : '/'}` + args.join('/');
  }
}
