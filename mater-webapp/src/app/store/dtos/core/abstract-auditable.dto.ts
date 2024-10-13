import {AbstractDto} from "./abstract.dto";

export class AbstractAuditableDto extends AbstractDto {
  createdAt: Date;
  editedAt?: Date;
}
