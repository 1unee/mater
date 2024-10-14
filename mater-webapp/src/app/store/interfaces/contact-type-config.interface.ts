import {ContactTypeEnum} from "../enums/contact-type.enum";

export interface ContactTypeConfig {
  options: { label: string, value: ContactTypeEnum, styleClass: string }[];
}
