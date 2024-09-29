import {Inject, Injectable} from '@angular/core';
import {UserDto} from "../../store/dtos/user.dto";
import {OneuneMessageService} from "./oneune-message.service";
import {LOCAL_STORAGE} from "../../app.config";
import {RoleDto} from "../../store/dtos/role.dto";
import {RoleEnum} from "../../store/enums/role.enum";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  static readonly USER_KEY: string = 'user-key';

  constructor(@Inject(LOCAL_STORAGE) private localStorage: Storage,
              private messageService: OneuneMessageService) {
  }

  get isAuthorized(): boolean {
    return !!this.localStorage.getItem(StorageService.USER_KEY);
  }

  get user(): UserDto {
    if (this.isAuthorized) {
      return JSON.parse(this.localStorage.getItem(StorageService.USER_KEY)!);
    } else {
      this.messageService.showError('Информация о пользователе не получена!');
      throw new Error("Не удалось авторизоваться!");
    }
  }

  authenticateUser(user: UserDto): void {
    this.localStorage.removeItem(StorageService.USER_KEY);
    this.localStorage.setItem(StorageService.USER_KEY, JSON.stringify(user));
  }

  logoutUser(): void {
    this.localStorage.removeItem(StorageService.USER_KEY);
  }

  updateUser(user: UserDto): void {
    this.localStorage.setItem(StorageService.USER_KEY, JSON.stringify(user));
  }

  userHasRole(role: RoleEnum): boolean {
    return this.user.roles.map((role: RoleDto): RoleEnum => role.name).includes(role);
  }
}
