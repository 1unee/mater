import {Inject, Injectable} from '@angular/core';
import {UserDto} from "../../store/dtos/user.dto";
import {OneuneMessageService} from "./oneune-message.service";
import {LOCAL_STORAGE} from "../../app.config";
import {RoleDto} from "../../store/dtos/role.dto";
import {RoleEnum} from "../../store/enums/role.enum";
import {UserSettingsDto} from "../../store/dtos/settings/userSettingsDto";
import {ConfigDto} from "../../store/dtos/settings/config.dto";
import {SettingDto} from "../../store/dtos/settings/setting.dto";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  static readonly USER_KEY: string = 'user-key';
  static readonly USER_SETTINGS: string = 'user-settings';

  constructor(@Inject(LOCAL_STORAGE) private localStorage: Storage,
              private messageService: OneuneMessageService) {
  }

  setUser(user: UserDto): void {
    this.localStorage.removeItem(StorageService.USER_KEY);
    this.localStorage.setItem(StorageService.USER_KEY, JSON.stringify(user));
  }

  get user(): UserDto {
    return JSON.parse(this.localStorage.getItem(StorageService.USER_KEY)!);
  }

  get isUserAuthorized(): boolean {
    return !!this.user;
  }

  userHasRole(role: RoleEnum): boolean {
    return this.isUserAuthorized && this.user.roles.map((role: RoleDto): RoleEnum => role.name).includes(role);
  }

  logoutUser(): void {
    this.localStorage.removeItem(StorageService.USER_KEY);
  }

  setUserSettings(settings: UserSettingsDto): void {
    this.localStorage.setItem(StorageService.USER_SETTINGS, JSON.stringify(settings));
  }

  get userSettings(): UserSettingsDto {
    return JSON.parse(this.localStorage.getItem(StorageService.USER_SETTINGS)!);
  }

  getConfigByCode(code: number): ConfigDto {
    return this.userSettings.configs.find(config => config.code === code)!;
  }

  getSettingByCode(code: number): SettingDto {
    return this.userSettings.settings.find(setting => setting.code === code)!;
  }
}
