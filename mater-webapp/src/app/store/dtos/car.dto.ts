import {AbstractDto} from "./abstract.dto";
import {PhotoDto} from "./photo.dto";
import {VideoDto} from "./video.dto";

export class CarDto extends AbstractDto {
  brand: string;
  model: string;
  productionYear: number;
  price: number;
  mileage: number;
  VIN: string;
  ownersAmount: number;
  power: number;
  photos: PhotoDto[];
  videos: VideoDto[];
}
