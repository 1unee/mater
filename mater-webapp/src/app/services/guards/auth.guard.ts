import {CanActivateFn} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "../utils/storage.service";
import {OneuneMessageService} from "../utils/oneune-message.service";
import {UserService} from "../https/user.service";
import {UserDto} from "../../store/dtos/user.dto";
import {TelegramService} from "../utils/telegram.service";
import {SettingService} from "../https/setting.service";
import {UserTokenDto} from "../../store/dtos/user-token.dto";
import {ClipboardService} from "../utils/clipboard.service";

export const authGuard: CanActivateFn = async (route, state): Promise<any> => {

  const storageService: StorageService = inject(StorageService);
  const messageService: OneuneMessageService = inject(OneuneMessageService);
  const userService = inject(UserService);
  const telegramService: TelegramService = inject(TelegramService);
  const settingService: SettingService = inject(SettingService);
  const clipboardService: ClipboardService = inject(ClipboardService);

  if (storageService.isUserAuthorized) {
    return true;
  } else {
    try {
      let user: UserDto;
      const telegramTuningResult: boolean = telegramService.tune();

      console.log(telegramTuningResult);
      console.log(telegramService.tgWebApp);

      if (telegramTuningResult) {
        user = await userService.registerOrGet(telegramService.user!, telegramService.telegramChatId!);
      } else {
        user = await userService.registerByForeignLink();
        const userToken: UserTokenDto = await userService.getUserToken(user.id);
        messageService.showInfo(`
          Автоматическая регистрация произошла по ссылке.
          Чтобы получить доступ к телеграмм-боту, нужно зайти через него и ввести код ${userToken.value}.
        `);
        clipboardService.copyWithCustomMessage(userToken.value, 'Твой код скопирован в буфер обмена.');
        storageService.authorizeUser(user);
      }
      storageService.authorizeUser(user);
      storageService.setUserSettings(await settingService.getByUserId(user.id));
      return true;
    } catch (e) {
      messageService.showInfo(
        'Авторизация не удалась... ' +
        'Попробуй позже или обратись к технической поддержке по почте: mater-sender@rambler.ru'
      );
      return false;
    }
  }
};
