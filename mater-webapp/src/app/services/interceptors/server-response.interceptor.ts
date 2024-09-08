import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {catchError, Observable, throwError} from "rxjs";
import {inject} from "@angular/core";
import {OneuneMessageService} from "../utils/oneune-message.service";

export const serverResponseInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>, next: HttpHandlerFn
) => {

  const oneuneMessageService: OneuneMessageService = inject<OneuneMessageService>(OneuneMessageService);

  return next(req).pipe(
    catchError((_error: any): Observable<never> => {
      if (_error && _error.error && _error.error.message && _error.error.message !== 'Нет информации') {
        oneuneMessageService.showError(`${_error.error.message}: ${JSON.stringify(_error)}`);
        return throwError(_error.error.message);
      } else {
        oneuneMessageService.showByHttpStatusCode(_error.status);
        return throwError(_error.status);
      }
    })
  );
};
