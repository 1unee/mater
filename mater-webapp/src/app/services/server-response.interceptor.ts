import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {catchError, Observable, throwError} from "rxjs";
import {inject} from "@angular/core";
import {OneuneMessageService} from "./oneune-message.service";

export const serverResponseInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {

  const oneuneMessageService: OneuneMessageService = inject(OneuneMessageService);

  return next(req).pipe(
    catchError((_error: any): Observable<never> => {

      if (_error && _error.error && _error.error.message && _error.error.message !== 'No message available') {
        oneuneMessageService.showErrorMessage(_error.error.message);
        return throwError(_error.error.message);
      } else {
        oneuneMessageService.showMessageByHttpStatusCode(_error.status);
        return throwError(_error.status);
      }
    })
  );
};
