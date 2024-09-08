import {AbstractDto} from "./abstract.dto";

export class PersonalDto extends AbstractDto {
  firstName: string;
  isFirstNameSet: boolean;
  lastName: string;
  isLastNameSet: boolean;
  middleName: string;
  isMiddleNameSet: boolean;
  birthDate: Date;
  isBirthDateSet: boolean;
}
