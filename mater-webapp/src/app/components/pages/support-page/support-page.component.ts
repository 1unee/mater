import {ChangeDetectorRef, Component, Inject, OnInit} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {ButtonDirective} from "primeng/button";
import {NgOptimizedImage} from "@angular/common";
import {TelegramService} from "../../../services/utils/telegram.service";
import {Router} from "@angular/router";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {UserService} from "../../../services/https/user.service";
import {StorageService} from "../../../services/utils/storage.service";
import {BlockUIModule} from "primeng/blockui";
import {ImageModule} from "primeng/image";
import {TagModule} from "primeng/tag";
import {FeedbackDialogComponent} from "../../dialogs/feedback-dialog/feedback-dialog.component";
import {DialogService} from "primeng/dynamicdialog";
import {LoaderComponent} from "../../core/loader/loader.component";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";

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
    LoaderComponent
  ],
  templateUrl: './support-page.component.html',
  styleUrl: './support-page.component.scss'
})
export class SupportPageComponent implements OnInit {

  constructor(private telegramService: TelegramService,
              private dialogService: DialogService) {
  }

  async ngOnInit(): Promise<void> {
    await this.telegramService.tune();
  }

  onOpenFeedbackDialog(): void {
    this.dialogService.open(FeedbackDialogComponent, {
      header: `Обратная связь`,
      width: '80%',
      height: 'auto',
    });
  }
}
