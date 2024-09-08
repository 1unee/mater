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
    FormsModule
  ],
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.scss'
})
export class SettingsPageComponent implements OnInit {

  themeSelectButtonConfig: any = {
    states: [
      { label: ThemeTitle.light, value: ThemeEnum.LIGHT, styleClass: "p-1 p-button-secondary"},
      { label: ThemeTitle.system, value: ThemeEnum.SYSTEM, styleClass: "p-1 p-button-secondary"},
      { label: ThemeTitle.dark, value: ThemeEnum.DARK, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: undefined
  };

  sortFilterSelectButtonConfig: any = {
    states: [
      { label: 'Да', value: true, styleClass: "p-1 p-button-secondary"},
      { label: 'Нет', value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: undefined
  };

  createCarSelectButtonConfig: any = {
    states: [
      { label: 'Да', value: true, styleClass: "p-1 p-button-secondary"},
      { label: 'Нет', value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: undefined
  };

  warnMessageAboutTestStagePageQueryingConfig: any = {
    states: [
      { label: 'Да', value: true, styleClass: "p-1 p-button-secondary"},
      { label: 'Нет', value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: undefined
  }

  autoPlayImages: any = {
    states: [
      { label: 'Да', value: true, styleClass: "p-1 p-button-secondary"},
      { label: 'Нет', value: false, styleClass: "p-1 p-button-secondary"}
    ],
    selectedValue: undefined
  };

  constructor(@Inject(GLOBAL_CONFIG) private globalConfig: GlobalConfig,
              private themeService: ThemeService) {
  }

  ngOnInit(): void {
    this.themeSelectButtonConfig.selectedValue = this.globalConfig.settings.theme;
    this.sortFilterSelectButtonConfig.selectedValue = this.globalConfig.settings.showSortFilterButton;
    this.createCarSelectButtonConfig.selectedValue = this.globalConfig.settings.showCreateCarButton;
    this.warnMessageAboutTestStagePageQueryingConfig.selectedValue = this.globalConfig.settings.showWarnPageQueryingMessage;
    this.autoPlayImages.selectedValue = this.globalConfig.settings.autoPlayImages;
  }

  onThemeChange(event: SelectButtonChangeEvent): void {
    this.themeService.setThemeMode(event.value);
  }

  onSortFilterStateChange(event: SelectButtonChangeEvent): void {
    this.sortFilterSelectButtonConfig.selectedValue = event.value;
    this.globalConfig.settings.showSortFilterButton = event.value;
  }

  onCarAddingStateChange(event: SelectButtonChangeEvent): void {
    this.createCarSelectButtonConfig.selectedValue = event.value;
    this.globalConfig.settings.showCreateCarButton = event.value;
  }

  onWarnPageQueryingStateChange(event: SelectButtonChangeEvent): void {
    this.warnMessageAboutTestStagePageQueryingConfig.selectedValue = event.value;
    this.globalConfig.settings.showWarnPageQueryingMessage = event.value;
  }

  onAutoPlayImagesChange(event: SelectButtonChangeEvent): void {
    this.autoPlayImages.selectedValue = event.value;
    this.globalConfig.settings.autoPlayImages = event.value;
  }
}
