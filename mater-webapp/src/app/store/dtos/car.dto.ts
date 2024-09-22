import {AbstractDto} from "./abstract.dto";
import {FileDto} from "./file.dto";

export class CarDto extends AbstractDto {
  brand: string;
  model: string;
  productionYear: number;
  price: number;
  mileage: number;
  VIN: string;
  ownersAmount: number;
  power: number;
  files: FileDto[];
}
