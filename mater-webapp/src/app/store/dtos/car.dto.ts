import {AbstractDto} from "./abstract.dto";
import {SellerDto} from "./seller.dto";
import {PhotoDto} from "./photo.dto";
import {VideoDto} from "./video.dto";

export class CarDto extends AbstractDto {
  seller: SellerDto;
  brand: string;
  model: string;
  productionYear: number;  // Using number to represent Year
  price: number;  // BigDecimal can be mapped to number
  mileage: number;
  VIN: string;
  ownersAmount: number;
  power: number;
  photos: PhotoDto[];
  videos: VideoDto[];
}
