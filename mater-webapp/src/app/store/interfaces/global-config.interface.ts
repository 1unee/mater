import {ThemeEnum} from "../enums/theme.enum";

export interface GlobalConfig {
  configs: {
    largeFileOffset: number,
    messageLifeDuration: number
  };
  settings: {
    theme: ThemeEnum,
    showSortFilterButton: boolean,
    showCreateCarButton: boolean,
    showWarnPageQueryingMessage: boolean
    autoPlayImages: boolean,
  }
}
