import {Component, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {DynamicDialogConfig} from "primeng/dynamicdialog";
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
    NgIf
  ],
  templateUrl: './contact-processing-dialog.component.html',
  styleUrl: './contact-processing-dialog.component.scss'
})
export class ContactProcessingDialogComponent extends AbstractFormComponent<ContactDto> implements OnInit {

  readonly ContactTypeTitle = ContactTypeTitle;

  contact: ContactDto;
  contactTypeConfig: { options: { label: string, value: ContactTypeEnum, styleClass: string }[] } = {
    options: [
      { label: 'Телефон', value: ContactTypeEnum.PHONE, styleClass: 'p-1 text-sm' },
      { label: 'Почта', value: ContactTypeEnum.EMAIL, styleClass: 'p-1' },
      { label: 'VK', value: ContactTypeEnum.VKONTAKTE, styleClass: 'p-1' },
      { label: 'Телеграм', value: ContactTypeEnum.TELEGRAM, styleClass: 'p-1' },
      { label: 'Инстаграм', value: ContactTypeEnum.INSTAGRAM, styleClass: 'p-1' },
      { label: 'WhatsApp', value: ContactTypeEnum.WHATSAPP, styleClass: 'p-1' }
    ]
  };

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              private sellerService: SellerService,
              private storageService: StorageService,
              private formBuilder: FormBuilder) {
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
      contactReference: [this.contact.value, Validators.required]
    }, {
      validators: [contactReferenceValue]
    });
  }

  protected override _buildModel(): ContactDto {
    this.contact.type = this.form.value.contactType;
    this.contact.value = this.form.value.contactReference;
    return this.contact;
  }

  async onSubmit(): Promise<void> {
    this.contact = this._buildModel();

    console.log(this.contact);

    if (!!this.contact.id) {
      await this.sellerService.putContact(this.storageService.user.seller.id, this.contact);
      this.messageService.showSuccess('Данные о контакте успешно изменены!');
    } else {
      await this.sellerService.postContact(this.storageService.user.seller.id, this.contact);
      this.messageService.showSuccess('Контакт успешно внесен в список!');
    }
    this.contact = new ContactDto();
    this.form.reset();
  }
}
