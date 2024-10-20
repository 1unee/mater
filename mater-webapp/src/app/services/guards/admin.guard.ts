import {CanActivateFn} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../utils/storage.service";
import {OneuneMessageService} from "../utils/oneune-message.service";
import {RoleEnum} from "../../store/enums/role.enum";

export const adminGuard: CanActivateFn = async (route, state): Promise<any> => {

  const storageService: StorageService = inject(StorageService);
  const messageService: OneuneMessageService = inject(OneuneMessageService);

  if (storageService.isUserAuthorized && storageService.userHasRole(RoleEnum.ADMIN)) {
    return true;
  } else {
    messageService.showInfo('Недостаточно прав...');
    return false;
  }
};
