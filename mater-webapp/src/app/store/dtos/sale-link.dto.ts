import {UserDto} from "./user.dto";
import {CarDto} from "./car.dto";
import {SaleLinkStatusEnum} from "../enums/sale-link-status.enum";
import {AbstractDto} from "./abstract.dto";

export class SaleLinkDto extends AbstractDto {
  buyer: UserDto;
  car: CarDto;
  createdAt: Date;
  status: SaleLinkStatusEnum;
  score: number;
}
