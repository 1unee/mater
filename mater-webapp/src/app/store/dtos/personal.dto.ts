import {AbstractDto} from "./abstract.dto";

export class PersonalDto extends AbstractDto {
  firstName: string;
  firstNameSet: boolean;
  lastName: string;
  lastNameSet: boolean;
  middleName: string;
  middleNameSet: boolean;
  birthDate: Date;
  birthDateSet: boolean;
}
