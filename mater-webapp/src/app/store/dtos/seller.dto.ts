import {AbstractDto} from "./core/abstract.dto";
import {CarDto} from "./car.dto";
import {ContactDto} from "./contact.dto";

export class SellerDto extends AbstractDto {
  score: number;
  contacts: ContactDto[];
  cars: CarDto[];
}
