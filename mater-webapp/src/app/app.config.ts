import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import BusinessVariableModel from "./store/business-variable.model";
import {provideHttpClient} from "@angular/common/http";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
};

export const businessVariables: BusinessVariableModel[] = [
  {
    key: 'prefix-rest-path',
    value: 'http://localhost:8080',
    description: 'Prefix of common rest path.'
  }
];
