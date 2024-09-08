import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../utils/storage.service";
import {OneuneMessageService} from "../utils/oneune-message.service";

export const authGuard: CanActivateFn = (route, state): any => {

  const storageService: StorageService = inject(StorageService);
  const messageService: OneuneMessageService = inject(OneuneMessageService);
  const router: Router = inject(Router);

  if (storageService.isAuthorized) {
    return true;
  } else {
    messageService.showWarning('Ты не авторизован!');
    router.navigate(['/support']).then((): void => {});
  }
};
