import {AbstractDto} from "./abstract.dto";

export class FileDto extends AbstractDto {
  name: string;
  type: string;
  size: number;
  url: string;
}
