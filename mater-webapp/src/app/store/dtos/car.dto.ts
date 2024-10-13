import {AbstractDto} from "./core/abstract.dto";
import {FileDto} from "./file.dto";
import {GearboxEnum} from "../enums/gearbox.enum";
import {CarStateEnum} from "../enums/car-state.enum";
import {EngineOilTypeEnum} from "../enums/engine-oil-type.enum";
import {TransmissionEnum} from "../enums/transmission.enum";
import {SteeringWheelEnum} from "../enums/steering-wheel.enum";

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
  documentsColor: string;
  gearbox: GearboxEnum;
  state: CarStateEnum;
  engineOilType: EngineOilTypeEnum;
  transmission: TransmissionEnum;
  steeringWheel: SteeringWheelEnum;

  get title(): string {
    return (!!this.brand && !!this.model && !!this.productionYear)
      ? 'Информация о машине загружается...' : `${this.brand} ${this.model} (${this.productionYear})`;
  }
}
