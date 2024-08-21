import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import BusinessVariableModel from "./store/business-variable.model";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {DialogService} from "primeng/dynamicdialog";
import {provideAnimations} from "@angular/platform-browser/animations";
import {ConfirmationService, MessageService} from "primeng/api";
import {serverResponseInterceptor} from "./services/server-response.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([serverResponseInterceptor])),
    DialogService,
    provideAnimations(),
    MessageService,
    ConfirmationService
  ]
};

export const businessVariables: BusinessVariableModel[] = [
  {
    key: 'prefix-rest-path',
    value: 'https://oneune.duckdns.org:9033',
    description: 'Prefix of common rest path.'
  }
];
