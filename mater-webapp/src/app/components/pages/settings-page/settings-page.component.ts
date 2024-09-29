import {Component, Inject, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {SelectButtonChangeEvent, SelectButtonModule} from "primeng/selectbutton";
import {ThemeService} from "../../../services/utils/theme.service";
import {ThemeEnum, ThemeTitle} from "../../../store/enums/theme.enum";
import {GLOBAL_CONFIG} from "../../../app.config";
import {GlobalConfig} from "../../../store/interfaces/global-config.interface";
import {DropdownModule} from "primeng/dropdown";
import {DoubleOptionEnum} from "../../../store/enums/double-option.enum";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";

@Component({
  selector: 'app-settings-page',
  standalone: true,
  imports: [
    Button,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    ReactiveFormsModule,
    SelectButtonModule,
    FormsModule,
    DropdownModule
  ],
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.scss'
})
export class SettingsPageComponent implements OnInit {

  themeConfig: any = {
    states: [
      { label: ThemeTitle.light, value: ThemeEnum.LIGHT, styleClass: "p-1 p-button-secondary"},
      { label: ThemeTitle.system, value: ThemeEnum.SYSTEM, styleClass: "p-1 p-button-secondary"},
      { label: ThemeTitle.dark, value: ThemeEnum.DARK, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: ThemeTitle.system
  };

  sortFilterConfig: any = {
    states: [
      { label: DoubleOptionEnum.YES, value: true, styleClass: "p-1 p-button-secondary"},
      { label: DoubleOptionEnum.NO, value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: true
  };

  createCarConfig: any = {
    states: [
      { label: DoubleOptionEnum.YES, value: true, styleClass: "p-1 p-button-secondary"},
      { label: DoubleOptionEnum.NO, value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: true
  };

  warnMessageAboutTestStagePageQueryingConfig: any = {
    states: [
      { label: DoubleOptionEnum.YES, value: true, styleClass: "p-1 p-button-secondary"},
      { label: DoubleOptionEnum.NO, value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: true
  }

  autoPlayImagesConfig: any = {
    states: [
      { label: DoubleOptionEnum.YES, value: true, styleClass: "p-1 p-button-secondary"},
      { label: DoubleOptionEnum.NO, value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: true
  };

  constructor(@Inject(GLOBAL_CONFIG) private globalConfig: GlobalConfig,
              private themeService: ThemeService,
              public messageService: OneuneMessageService) {
  }

  ngOnInit(): void {
    this.themeConfig.selectedValue = this.globalConfig.settings.theme;
    this.sortFilterConfig.selectedValue = this.globalConfig.settings.showSortFilterButton;
    this.createCarConfig.selectedValue = this.globalConfig.settings.showCreateCarButton;
    this.warnMessageAboutTestStagePageQueryingConfig.selectedValue = this.globalConfig.settings.showWarnPageQueryingMessage;
    this.autoPlayImagesConfig.selectedValue = this.globalConfig.settings.autoPlayImages;
  }

  onThemeChange(event: any): void {
    this.themeService.setThemeMode(event.value);
  }

  onSortFilterStateChange(event: any): void {
    this.sortFilterConfig.selectedValue = event.value;
    this.globalConfig.settings.showSortFilterButton = event.value;
  }

  onCarAddingStateChange(event: any): void {
    this.createCarConfig.selectedValue = event.value;
    this.globalConfig.settings.showCreateCarButton = event.value;
  }

  onWarnPageQueryingStateChange(event: any): void {
    this.warnMessageAboutTestStagePageQueryingConfig.selectedValue = event.value;
    this.globalConfig.settings.showWarnPageQueryingMessage = event.value;
  }

  onAutoPlayImagesChange(event: any): void {
    this.autoPlayImagesConfig.selectedValue = event.value;
    this.globalConfig.settings.autoPlayImages = event.value;
  }
}
