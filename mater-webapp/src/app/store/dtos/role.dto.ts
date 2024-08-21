import {RoleEnum} from "../enums/role.enum";
import {AbstractDto} from "./abstract.dto";

export class RoleDto extends AbstractDto {
  name: RoleEnum;
}
