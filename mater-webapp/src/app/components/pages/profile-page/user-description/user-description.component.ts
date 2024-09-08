import {Component, Inject, OnInit} from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {CardModule} from "primeng/card";
import {UserDto} from "../../../../store/dtos/user.dto";
import {ListboxModule} from "primeng/listbox";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {DatePipe, NgClass, NgIf} from "@angular/common";
import '@angular/common/locales/global/ru';
import {StyleClassModule} from "primeng/styleclass";
import {OneuneMessageService} from "../../../../services/utils/oneune-message.service";
import {ClipboardService} from "../../../../services/utils/clipboard.service";
import {ProgressBarModule} from "primeng/progressbar";
import {CalendarModule} from "primeng/calendar";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {ConfirmationService} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {UserService} from "../../../../services/https/user.service";
import {LOADING} from "../../../../app.config";
import {StorageService} from "../../../../services/utils/storage.service";
import {VariableFieldEnum} from "../../../../store/enums/variable-field.enum";
import {LoadingReference} from "../../../../store/interfaces/loading-reference.interface";
import {LogService} from "../../../../services/https/log.service";

@Component({
  selector: 'app-user-description',
  standalone: true,
  imports: [
    ButtonDirective,
    CardModule,
    ListboxModule,
    InputGroupAddonModule,
    InputGroupModule,
    NgIf,
    DatePipe,
    StyleClassModule,
    NgClass,
    ProgressBarModule,
    CalendarModule,
    FormsModule,
    InputTextModule,
    ConfirmDialogModule
  ],
  templateUrl: './user-description.component.html',
  styleUrl: './user-description.component.scss'
})
export class UserDescriptionComponent implements OnInit {

  readonly VariableFieldEnum = VariableFieldEnum;

  user: UserDto;

  constructor(public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              private confirmationService: ConfirmationService,
              private userService: UserService,
              @Inject(LOADING) public loading: LoadingReference,
              private storageService: StorageService) {
  }

  ngOnInit(): void {
    this._loadData();
  }

  private _loadData(): void {
    this.user = this.storageService.user;
  }

  confirmEditing(variableField: VariableFieldEnum): void {
    if (this._isEmailValid(this.user.email)) {
      this.confirmationService.confirm({
        message: 'Потом это поле нельзя будет поменять. Ты уверен в своем выборе?',
        header: 'Подтверждение',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Да',
        accept: async () => this._editUser(variableField),
        rejectLabel: 'Нет',
        reject: () => this.messageService.showInfo('Понял, пока не сохраняем!')
      });
    } else {
      this.messageService.showWarning('Формат почты неправильный, перепроверь себя и введи заново!');
    }
  }

  private _isEmailValid(email: string): boolean {
    const emailPattern: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
  }

  private async _editUser(variableField: VariableFieldEnum): Promise<void> {
    try {
      this.loading.value.next(true);
      this.user = await this.userService.putByParams(this.user, variableField);
      this.storageService.updateUser(this.user);
      this.messageService.showSuccess('Данные успешно обновлены!');
    } catch (e) {
      this.messageService.showError('Не удалось обновить данные!');
    } finally {
      this.loading.value.next(false);
    }
  }
}
