import {Component, OnInit} from '@angular/core';
import {CardModule} from "primeng/card";
import {ListboxModule} from "primeng/listbox";
import {ContactDto} from "../../../../store/dtos/contact.dto";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {NgForOf, NgIf} from "@angular/common";
import {PaginatorModule} from "primeng/paginator";
import {DialogService} from "primeng/dynamicdialog";
import {
  ContactProcessingDialogComponent
} from "../../../dialogs/contact-processing-dialog/contact-processing-dialog.component";
import {Button} from "primeng/button";
import {DataViewModule} from "primeng/dataview";
import {ClipboardService} from "../../../../services/utils/clipboard.service";
import {SellerService} from "../../../../services/https/seller.service";
import {StorageService} from "../../../../services/utils/storage.service";
import {OneuneMessageService} from "../../../../services/utils/oneune-message.service";
import {ContactTypeEnum} from "../../../../store/enums/contact-type.enum";
import {SelectButtonModule} from "primeng/selectbutton";
import {LongClickDirective} from "../../../../services/directives/long-click.directive";

@Component({
  selector: 'app-user-contacts',
  standalone: true,
  imports: [
    CardModule,
    ListboxModule,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    NgIf,
    PaginatorModule,
    Button,
    DataViewModule,
    NgForOf,
    SelectButtonModule,
    LongClickDirective
  ],
  templateUrl: './user-contacts.component.html',
  styleUrl: './user-contacts.component.scss'
})
export class UserContactsComponent implements OnInit {

  contacts: ContactDto[];

  constructor(private dialogService: DialogService,
              public clipboardService: ClipboardService,
              private sellerService: SellerService,
              private storageService: StorageService,
              public messageService: OneuneMessageService) {
  }

  async ngOnInit(): Promise<void> {
    await this._loadContacts();
  }

  private async _loadContacts(): Promise<void> {
    this.contacts = await this.sellerService.getContactsBySellerId(this.storageService.user.seller.id);
  }

  getContactTypeLocalized(contactType: ContactTypeEnum): string {
    switch (contactType) {
      case ContactTypeEnum.PHONE: return 'Телефон';
      case ContactTypeEnum.VKONTAKTE: return 'VK';
      case ContactTypeEnum.EMAIL: return 'Почта';
      case ContactTypeEnum.TELEGRAM: return 'Телеграмм';
      case ContactTypeEnum.INSTAGRAM: return 'Инстаграм';
      case ContactTypeEnum.WHATSAPP: return 'WhatsApp';
    }
  }

  onContactProcessing(contact: ContactDto | null): void {
    const processingContact: ContactDto = !!contact ? contact : new ContactDto();
    this._openContactProcessingDialog(processingContact);
  }

  private _openContactProcessingDialog(contact: ContactDto): void {
    this.dialogService.open(ContactProcessingDialogComponent, {
      header: `Контакт`,
      width: '90%',
      data: {contact}
    }).onClose.subscribe(async () => await this._loadContacts());
  }

  async onContactDeleting(deletingContact: ContactDto): Promise<void> {
    await this.sellerService.deleteContact(this.storageService.user.seller.id, deletingContact.id);
    await this._loadContacts();
    this.messageService.showSuccess('Контакт успешно удален!')
  }

  async reload(): Promise<void> {
    await this._loadContacts();
    this.messageService.showInfo('Список твоих контактов перезагружен!');
  }
}
