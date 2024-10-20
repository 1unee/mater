import {CanActivateFn} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../utils/storage.service";
import {OneuneMessageService} from "../utils/oneune-message.service";
import {OneuneRouterService} from "../utils/oneune-router.service";

export const authGuard: CanActivateFn = async (route, state): Promise<any> => {

  const storageService: StorageService = inject(StorageService);
  const messageService: OneuneMessageService = inject(OneuneMessageService);
  const routerService: OneuneRouterService = inject(OneuneRouterService);

  if (storageService.isUserAuthorized) {
    return true;
  } else {
    routerService.relativeRedirect('authorization');
    messageService.showInfo('Сначала нужно зайти в свой аккаунт.');
    return false;
  }
};
