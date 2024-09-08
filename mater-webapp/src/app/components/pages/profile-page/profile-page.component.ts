import {Component, OnInit} from '@angular/core';
import {UserRatingComponent} from "./user-rating/user-rating.component";
import {UserSearchHistoryComponent} from "./user-search-history/user-search-history.component";
import {UserContactsComponent} from "./user-contacts/user-contacts.component";
import {CardModule} from "primeng/card";
import {ButtonDirective} from "primeng/button";
import {AccordionModule} from "primeng/accordion";
import {UserDescriptionComponent} from "./user-description/user-description.component";
import {UserDto} from "../../../store/dtos/user.dto";
import {StorageService} from "../../../services/utils/storage.service";
import {AvatarModule} from "primeng/avatar";
import {BadgeModule} from "primeng/badge";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {SidebarModule} from "primeng/sidebar";
import {Ripple} from "primeng/ripple";
import {TooltipModule} from "primeng/tooltip";
import {NgIf} from "@angular/common";
import {FeedbackDialogComponent} from "../../dialogs/feedback-dialog/feedback-dialog.component";
import {DialogService} from "primeng/dynamicdialog";

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [
    UserRatingComponent,
    UserSearchHistoryComponent,
    UserContactsComponent,
    CardModule,
    ButtonDirective,
    AccordionModule,
    UserDescriptionComponent,
    AvatarModule,
    BadgeModule,
    SidebarModule,
    Ripple,
    TooltipModule,
    NgIf
  ],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.scss'
})
export class ProfilePageComponent implements OnInit {

  user: UserDto;
  searchHistory: any[];

  showUserDescriptionSupportSidebar: boolean = false;
  showContactsSupportSidebar: boolean = false;

  constructor(private storageService: StorageService,
              public messageService: OneuneMessageService,
              private dialogService: DialogService) {
  }

  async ngOnInit(): Promise<void> {
    await this._loadData();
  }

  private async _loadData(): Promise<void> {
    this.user = this.storageService.user;
  }

  openFeedbackForm(): void {
    this.dialogService.open(FeedbackDialogComponent, {
      header: `Обратная связь`,
      width: '80%',
      height: 'auto',
    });
  }
}
