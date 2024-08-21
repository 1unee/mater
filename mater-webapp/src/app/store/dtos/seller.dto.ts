import {AbstractDto} from "./abstract.dto";
import {CarDto} from "./car.dto";
import {UserDto} from "./user.dto";

export class SellerDto extends AbstractDto {
  user: UserDto;
  score: number;
  cars: CarDto[];
}
