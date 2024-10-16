import {Component, OnInit} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {ButtonDirective} from "primeng/button";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {StorageService} from "../../../services/utils/storage.service";
import {BlockUIModule} from "primeng/blockui";
import {ImageModule} from "primeng/image";
import {TagModule} from "primeng/tag";
import {FeedbackDialogComponent} from "../../dialogs/feedback-dialog/feedback-dialog.component";
import {DialogService} from "primeng/dynamicdialog";
import {LoaderComponent} from "../../core/loader/loader.component";
import {RoleDto} from "../../../store/dtos/role.dto";
import {RoleEnum} from "../../../store/enums/role.enum";
import {
  UserTokenProcessingDialogComponent
} from "../../dialogs/user-token-processing-dialog/user-token-processing-dialog.component";

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

  constructor(private dialogService: DialogService,
              private storageService: StorageService) {
  }

  async ngOnInit(): Promise<void> {
    if (!this.storageService.user.registeredByTelegram) {
      this.openTokenDialog();
    }
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

  openTokenDialog(): void {
    this.dialogService.open(UserTokenProcessingDialogComponent, {
      header: `Синхронизация с телеграммом`,
      width: '80%',
      height: 'auto',
    });
  }
}
