import {AbstractDto} from "./abstract.dto";

export class ContactDto extends AbstractDto {
  method: string;
  phoneNumber: string;
  socialNetworkReference: string;
}
