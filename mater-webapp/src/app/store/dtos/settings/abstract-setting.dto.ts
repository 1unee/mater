import {AbstractAuditableDto} from "../core/abstract-auditable.dto";

export class AbstractSettingItem extends AbstractAuditableDto {
  code: number;
  title: string;
  value: string;
  description: string;
}
