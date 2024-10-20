import {Component, Inject, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ClipboardService} from "../../../services/utils/clipboard.service";
import {ContactDto} from "../../../store/dtos/contact.dto";
import {StorageService} from "../../../services/utils/storage.service";
import {SellerService} from "../../../services/https/seller.service";
import {ContactTypeEnum, ContactTypeTitle} from "../../../store/enums/contact-type.enum";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {contactReferenceValue} from "../../../services/utils/validators";
import {NgIf} from "@angular/common";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {CheckboxModule} from "primeng/checkbox";
import {ContactTypeConfig} from "../../../store/interfaces/contact-type-config.interface";
import {SelectButtonModule} from "primeng/selectbutton";
import {ConfirmationService} from "primeng/api";
import {VariableFieldEnum} from "../../../store/enums/variable-field.enum";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {UserDto} from "../../../store/dtos/user.dto";
import {UserService} from "../../../services/https/user.service";

@Component({
  selector: 'app-contact-processing-dialog',
  standalone: true,
  imports: [
    Button,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    PaginatorModule,
    ReactiveFormsModule,
    NgIf,
    LongClickDirective,
    CheckboxModule,
    SelectButtonModule
  ],
  templateUrl: './contact-processing-dialog.component.html',
  styleUrl: './contact-processing-dialog.component.scss'
})
export class ContactProcessingDialogComponent extends AbstractFormComponent<ContactDto> implements OnInit {

  readonly ContactTypeEnum = ContactTypeEnum;
  readonly ContactTypeTitle = ContactTypeTitle;

  contact: ContactDto;
  contactTypeConfig: ContactTypeConfig = {
    options: [
      { label: 'Телефон', value: ContactTypeEnum.PHONE, styleClass: 'p-1 text-sm' },
      { label: 'Почта', value: ContactTypeEnum.EMAIL, styleClass: 'p-1' },
      { label: 'VK', value: ContactTypeEnum.VKONTAKTE, styleClass: 'p-1' },
      { label: 'Телеграм', value: ContactTypeEnum.TELEGRAM, styleClass: 'p-1' },
      { label: 'Инстаграм', value: ContactTypeEnum.INSTAGRAM, styleClass: 'p-1' },
      { label: 'WhatsApp', value: ContactTypeEnum.WHATSAPP, styleClass: 'p-1' }
    ]
  };

  disableCarListRedirectButton: boolean = true;

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              private dynamicDialogRef: DynamicDialogRef,
              public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              private sellerService: SellerService,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              private routerService: OneuneRouterService,
              private confirmationService: ConfirmationService,
              @Inject(LOADING) public loadingReference: LoadingReference,
              private userService: UserService) {
    super();
  }

  ngOnInit(): void {
    this._initializeContact();
    this._initializeForm();
  }

  get contactType(): ContactTypeEnum {
    return this.form.value.contactType;
  }

  private _initializeContact(): void {
    this.contact = this.dynamicDialogConfig.data.contact;
  }

  protected override _initializeForm(): void {
    this.form = this.formBuilder.group({
      contactType: [this.contact.type, Validators.required],
      contactReference: [this.contact.value, Validators.required],
      whatsappLinked: [false, []],
      telegramLinked: [false, []]
    }, {
      validators: [contactReferenceValue]
    });
  }

  protected override _buildModel(): ContactDto {
    this.contact.type = this.form.value.contactType;
    this.contact.value = this.form.value.contactReference;
    return this.contact;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
    this.contact = this._buildModel();
    if (!!this.contact.id) {
      await this.sellerService.putContact(this.storageService.user.seller.id, this.contact);
      this.messageService.showSuccess('Данные о контакте успешно изменены!');
    } else {
      await this._askAboutLinkingMailIfNeeded();
      await this.sellerService.postContact(
        this.storageService.user.seller.id,
        this.contact,
        this.form.value.whatsappLinked,
        this.form.value.telegramLinked
      );
      this.messageService.showSuccess('Контакт успешно внесен в список!');
    }
    if (!this.contact.id) {
      this.contact = new ContactDto();
      this.form.reset();
    }
    this.disableCarListRedirectButton = false;
    if (closeDialog) {
      this.dynamicDialogRef.close();
    }
  }

  async openCarsMarketPage(): Promise<void> {
    await this.onSubmit(true);
    this.routerService.relativeRedirect('/cars/market');
    this.disableCarListRedirectButton = true;
  }

  private async _askAboutLinkingMailIfNeeded(): Promise<void> {
    if (this.contact.type === ContactTypeEnum.EMAIL) {
      this.confirmationService.confirm({
        message: 'Привязать эту почту к профилю? Если да, то учти, что потом это поле нельзя будет поменять.',
        header: 'Подтверждение',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Да',
        acceptButtonStyleClass: 'p-1',
        accept: async () => this._linkEmail(),
        rejectLabel: 'Нет',
        rejectButtonStyleClass: 'p-1',
        reject: () => this.messageService.showInfo('Понял, тогда не привязываем')
      });
    }
    return Promise.resolve();
  }

  private async _linkEmail(): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      let user: UserDto = this.storageService.user;
      user.email = this.contact.value;
      user = await this.userService.putByParams(user, VariableFieldEnum.EMAIL);
      this.storageService.updateUser(user);
      this.messageService.showSuccess('Данные успешно обновлены!');
    } catch (e) {
      this.messageService.showError('Не удалось обновить данные!');
    } finally {
      this.loadingReference.value.next(false);
    }
  }
}
