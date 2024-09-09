import {FileTypeEnum} from "../enums/file-type.enum";

export interface SelectButtonState {
  label: string;
  value: FileTypeEnum;
  styleClass: string;
  accept: string;
  files: File[];
  disabled: boolean
}
