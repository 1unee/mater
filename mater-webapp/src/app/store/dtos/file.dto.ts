import {AbstractDto} from "./core/abstract.dto";

export class FileDto extends AbstractDto {
  name: string;
  type: string;
  size: number;
  url: string;
}
