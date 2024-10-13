import {AbstractSettingItem} from "./abstract-setting.dto";
import {OptionDto} from "./option.dto";

export class SettingDto extends AbstractSettingItem {
  options: OptionDto[];
  selectedOption: OptionDto;
}
