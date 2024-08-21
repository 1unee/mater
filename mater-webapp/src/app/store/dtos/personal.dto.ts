import {AbstractDto} from "./abstract.dto";

export class PersonalDto extends AbstractDto {
  firstName: string;
  lastName: string;
  middleName?: string;
  birthDate: Date;
}
