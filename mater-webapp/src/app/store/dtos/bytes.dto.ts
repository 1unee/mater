import {AbstractDto} from "./abstract.dto";

export class BytesDto extends AbstractDto {
  bytes: Uint8Array;
}
