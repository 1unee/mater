import {AbstractAuditableDto} from "./core/abstract-auditable.dto";

export class UserTokenDto extends AbstractAuditableDto {
  value: number;
}
