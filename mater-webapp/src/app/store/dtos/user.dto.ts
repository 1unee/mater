import {RoleDto} from "./role.dto";
import {PersonalDto} from "./personal.dto";
import {AbstractDto} from "./abstract.dto";

export class UserDto extends AbstractDto {
  username: string;
  email: string;
  telegramId: number;
  registeredAt: Date;
  roles: RoleDto[];
  personal: PersonalDto;
}
