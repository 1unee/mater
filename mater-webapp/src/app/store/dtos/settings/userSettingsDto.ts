import {ConfigDto} from "./config.dto";
import {SettingDto} from "./setting.dto";
import {AbstractAuditableDto} from "../core/abstract-auditable.dto";

export class UserSettingsDto extends AbstractAuditableDto {
  configs: ConfigDto[];
  settings: SettingDto[];
}
