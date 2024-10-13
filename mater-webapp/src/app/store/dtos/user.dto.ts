import {RoleDto} from "./role.dto";
import {PersonalDto} from "./personal.dto";
import {AbstractDto} from "./core/abstract.dto";
import {SellerDto} from "./seller.dto";

export class UserDto extends AbstractDto {
  username: string;
  email: string;
  isEmailSet: boolean;
  personal: PersonalDto;
  seller: SellerDto;
  telegramId: number;
  telegramChatId: number;
  registeredAt: Date;
  roles: RoleDto[];
}
