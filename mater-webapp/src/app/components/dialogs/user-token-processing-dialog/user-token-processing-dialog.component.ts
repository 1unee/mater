import {Component, Inject, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {FloatLabelModule} from "primeng/floatlabel";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextareaModule} from "primeng/inputtextarea";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {NgIf} from "@angular/common";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {StorageService} from "../../../services/utils/storage.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {isNumber} from "../../../services/utils/validators";
import {UserService} from "../../../services/https/user.service";
import {UserDto} from "../../../store/dtos/user.dto";
import {TelegramService} from "../../../services/utils/telegram.service";
import {ClipboardService} from "../../../services/utils/clipboard.service";
import {UserTokenDto} from "../../../store/dtos/user-token.dto";

@Component({
  selector: 'app-user-token-processing-dialog',
  standalone: true,
  imports: [
    Button,
    FloatLabelModule,
    FormsModule,
    InputTextareaModule,
    LongClickDirective,
    NgIf,
    ReactiveFormsModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule
  ],
  templateUrl: './user-token-processing-dialog.component.html',
  styleUrl: './user-token-processing-dialog.component.scss'
})
export class UserTokenProcessingDialogComponent extends AbstractFormComponent<string> implements OnInit {

  userToken: string;

  constructor(private dynamicDialogRef: DynamicDialogRef,
              public messageService: OneuneMessageService,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              @Inject(LOADING) public loadingReference: LoadingReference,
              private userService: UserService,
              private telegramService: TelegramService,
              private clipboardService: ClipboardService) {
    super();
  }

  async ngOnInit(): Promise<void> {
    this._initializeForm();
    const token: UserTokenDto = await this.userService.getUserToken(this.storageService.user.id);
    this.clipboardService.copyWithCustomMessage(token.value, 'Код скопирован в буфер обмена!');
  }

  protected _initializeForm(): void {
    this.form = this.formBuilder.group({
      userToken: ['', [
        Validators.required, isNumber(), Validators.minLength(6), Validators.maxLength(6)
      ]],
    });
  }

  protected _buildModel(): string {
    this.userToken = this.form.value.userToken;
    return this.userToken;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
    this._buildModel();
    try {
      this.loadingReference.value.next(true);
      const user: UserDto = await this.userService.putByTelegram(
        this.storageService.user.id,
        Number(this.userToken),
        this.telegramService.user!,
        this.telegramService.telegramChatId!
      );
      this.userToken = '';
      this.storageService.updateUser(user);
      this.messageService.showSuccess('Данные о твоем аккаунте успешно обновлены!');
      this.form.reset();
      if (closeDialog) {
        this.dynamicDialogRef.close();
      }
    } catch (e) {
      this.messageService.showDefaultError();
    } finally {
      this.loadingReference.value.next(false);
    }
  }
}
