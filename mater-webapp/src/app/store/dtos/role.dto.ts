import {RoleEnum} from "../enums/role.enum";
import {AbstractDto} from "./core/abstract.dto";

export class RoleDto extends AbstractDto {
  name: RoleEnum;
}
