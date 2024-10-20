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
import {RoleEnum} from "../../../store/enums/role.enum";

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
  }

  get isSupport(): boolean {
    return this.storageService.userHasRole(RoleEnum.SUPPORT);
  }

  get isAdmin(): boolean {
    return this.storageService.userHasRole(RoleEnum.ADMIN);
  }

  onOpenFeedbackDialog(): void {
    this.dialogService.open(FeedbackDialogComponent, {
      header: `Обратная связь`,
      width: '80%',
      height: 'auto',
    });
  }
}
