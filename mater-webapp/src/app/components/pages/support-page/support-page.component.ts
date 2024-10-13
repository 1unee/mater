import {Component, OnInit} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {ButtonDirective} from "primeng/button";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {TelegramService} from "../../../services/utils/telegram.service";
import {StorageService} from "../../../services/utils/storage.service";
import {BlockUIModule} from "primeng/blockui";
import {ImageModule} from "primeng/image";
import {TagModule} from "primeng/tag";
import {FeedbackDialogComponent} from "../../dialogs/feedback-dialog/feedback-dialog.component";
import {DialogService} from "primeng/dynamicdialog";
import {LoaderComponent} from "../../core/loader/loader.component";
import {RoleDto} from "../../../store/dtos/role.dto";
import {RoleEnum} from "../../../store/enums/role.enum";
import {SettingService} from "../../../services/https/setting.service";
import {UserSettingsDto} from "../../../store/dtos/settings/userSettingsDto";

@Component({
  selector: 'app-support-page',
  standalone: true,
  imports: [
    AccordionModule,
    ButtonDirective,
    NgOptimizedImage,
    BlockUIModule,
    ImageModule,
    TagModule,
    LoaderComponent,
    NgIf
  ],
  templateUrl: './support-page.component.html',
  styleUrl: './support-page.component.scss'
})
export class SupportPageComponent implements OnInit {

  constructor(private telegramService: TelegramService,
              private dialogService: DialogService,
              private storageService: StorageService,
              private settingService: SettingService) {
  }

  async ngOnInit(): Promise<void> {
    await this.telegramService.tune();
    const userSettings: UserSettingsDto = await this.settingService.getByUserId(this.storageService.user.id);
    this.storageService.setUserSettings(userSettings);
  }

  get isSupport(): boolean {
    return this.storageService.user.roles.map((role: RoleDto): RoleEnum => role.name).includes(RoleEnum.SUPPORT);
  }

  get isAdmin(): boolean {
    return this.storageService.user.roles.map((role: RoleDto): RoleEnum => role.name).includes(RoleEnum.ADMIN);
  }

  onOpenFeedbackDialog(): void {
    this.dialogService.open(FeedbackDialogComponent, {
      header: `Обратная связь`,
      width: '80%',
      height: 'auto',
    });
  }
}
