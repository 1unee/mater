import {ApplicationConfig, InjectionToken} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {DialogService, DynamicDialogConfig} from "primeng/dynamicdialog";
import {provideAnimations} from "@angular/platform-browser/animations";
import {ConfirmationService, MessageService} from "primeng/api";
import {serverResponseInterceptor} from "./services/interceptors/server-response.interceptor";
import {LoadingReference} from "./store/interfaces/loading-reference.interface";
import {BehaviorSubject} from "rxjs";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([serverResponseInterceptor])),
    DialogService,
    provideAnimations(),
    MessageService,
    ConfirmationService,
    DynamicDialogConfig
  ]
};

/**
 * For using like an injectable bean.
 */
export const LOCAL_STORAGE: InjectionToken<Storage> = new InjectionToken<Storage>('LOCAL_STORAGE', {
  providedIn: 'root',
  factory: (): Storage => window.localStorage
});

/**
 * For blocking UI.
 */
export const LOADING: InjectionToken<LoadingReference> = new InjectionToken<LoadingReference>('LOADING', {
  providedIn: 'root',
  factory: (): LoadingReference => ({
    value: new BehaviorSubject<boolean>(false)
  })
});
