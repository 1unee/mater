import {RoleDto} from "./role.dto";
import {PersonalDto} from "./personal.dto";
import {AbstractDto} from "./core/abstract.dto";
import {SellerDto} from "./seller.dto";
import {UserRegistrationStateEnum} from "../enums/user-registration-state.enum";

export class UserDto extends AbstractDto {
  username: string;
  isUsernameSet: boolean;
  password: string;
  email: string;
  isEmailSet: boolean;
  personal: PersonalDto;
  seller: SellerDto;
  telegramId: number;
  telegramChatId: number;
  registeredAt: Date;
  status: UserRegistrationStateEnum;
  roles: RoleDto[];
}
