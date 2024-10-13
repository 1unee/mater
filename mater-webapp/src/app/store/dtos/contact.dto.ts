import {AbstractDto} from "./core/abstract.dto";
import {ContactTypeEnum} from "../enums/contact-type.enum";

export class ContactDto extends AbstractDto {
  type: ContactTypeEnum;
  value: string;
}
