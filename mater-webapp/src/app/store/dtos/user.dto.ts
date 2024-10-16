import {RoleDto} from "./role.dto";
import {PersonalDto} from "./personal.dto";
import {AbstractDto} from "./core/abstract.dto";
import {SellerDto} from "./seller.dto";

export class UserDto extends AbstractDto {
  username: string;
  isUsernameSet: boolean;
  email: string;
  isEmailSet: boolean;
  personal: PersonalDto;
  seller: SellerDto;
  registeredByTelegram: boolean;
  telegramId: number;
  telegramChatId: number;
  registeredAt: Date;
  roles: RoleDto[];
}
