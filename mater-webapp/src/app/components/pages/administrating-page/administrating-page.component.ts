import {Component, Inject, OnInit} from '@angular/core';
import {AccordionModule} from "primeng/accordion";
import {NgForOf, NgIf} from "@angular/common";
import {PrimeTemplate} from "primeng/api";
import {UserContactsComponent} from "../profile-page/user-contacts/user-contacts.component";
import {UserDescriptionComponent} from "../profile-page/user-description/user-description.component";
import {UserRatingComponent} from "../profile-page/user-rating/user-rating.component";
import {UserSearchHistoryComponent} from "../profile-page/user-search-history/user-search-history.component";
import {UserService} from "../../../services/https/user.service";
import {UserDto} from "../../../store/dtos/user.dto";
import {ListboxModule} from "primeng/listbox";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {LOADING} from "../../../app.config";
import {InputTextModule} from "primeng/inputtext";
import {DialogService} from "primeng/dynamicdialog";
import {FeedbackDialogComponent} from "../../dialogs/feedback-dialog/feedback-dialog.component";
import {UserProcessingDialogComponent} from "../../dialogs/user-processing-dialog/user-processing-dialog.component";

@Component({
  selector: 'app-administrating-page',
  standalone: true,
  imports: [
    AccordionModule,
    NgIf,
    PrimeTemplate,
    UserContactsComponent,
    UserDescriptionComponent,
    UserRatingComponent,
    UserSearchHistoryComponent,
    ListboxModule,
    FormsModule,
    Button,
    NgForOf,
    InputTextModule,
    ReactiveFormsModule
  ],
  templateUrl: './administrating-page.component.html',
  styleUrl: './administrating-page.component.scss'
})
export class AdministratingPageComponent implements OnInit {

  filtering: boolean = true;
  users: UserDto[];
  filteringUsername: string;
  filteredUsers: UserDto[];

  constructor(@Inject(LOADING) private loadingReference: LoadingReference,
              public userService: UserService,
              private dialogService: DialogService) {
  }

  async ngOnInit(): Promise<void> {
    await this._initialize();
  }

  private async _initialize(): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      this.users = await this.userService.getUsers();
      this.filteredUsers = this.users;
    } finally {
      this.loadingReference.value.next(false);
    }
  }

  onUserFiltering(): void {
    this.filteredUsers = !!this.filteringUsername ? this.users.filter(user => user.username.includes(this.filteringUsername)) : this.users;
  }

  onEditUser(user: UserDto): void {
    this.dialogService.open(UserProcessingDialogComponent, {
      header: `Редактирование пользователя`,
      width: '90%',
      height: 'auto',
      data: { user: user }
    }).onClose.subscribe(async (): Promise<UserDto[]> => this.users = await this.userService.getUsers());
  }
}
