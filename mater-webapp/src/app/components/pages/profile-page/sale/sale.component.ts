import {Component, OnInit} from '@angular/core';
import {DataViewModule} from "primeng/dataview";
import {SellerService} from "../../../../services/https/seller.service";
import {SaleLinkDto} from "../../../../store/dtos/sale-link.dto";
import {StorageService} from "../../../../services/utils/storage.service";
import {NgForOf, NgIf} from "@angular/common";
import {SaleLinkStatusTitle} from "../../../../store/enums/sale-link-status.enum";
import {LongClickDirective} from "../../../../services/directives/long-click.directive";
import {OneuneMessageService} from "../../../../services/utils/oneune-message.service";
import {DialogService} from "primeng/dynamicdialog";
import {
  SaleLinkProcessingDialogComponent
} from "../../../dialogs/sale-link-processing-dialog/sale-link-processing-dialog.component";

@Component({
  selector: 'app-sale',
  standalone: true,
  imports: [
    DataViewModule,
    NgForOf,
    LongClickDirective,
    NgIf
  ],
  templateUrl: './sale.component.html',
  styleUrl: './sale.component.scss'
})
export class SaleComponent implements OnInit {

  readonly SaleLinkStatusTitle = SaleLinkStatusTitle;

  saleLinks: SaleLinkDto[];

  constructor(private sellerService: SellerService,
              private storageService: StorageService,
              public messageService: OneuneMessageService,
              private dialogService: DialogService) {
  }

  async ngOnInit(): Promise<void> {
    await this._initializeSaleLinks();
  }

  private async _initializeSaleLinks(): Promise<void> {
    this.saleLinks = await this.sellerService.getSaleLinksByBuyerId(this.storageService.user.id);
    this.saleLinks.forEach(saleLink => saleLink.createdAt = new Date(saleLink.createdAt));
  }

  async onSaleLinkProcessing(saleLink: SaleLinkDto): Promise<void> {
    this._openSaleLinkProcessingDialog(saleLink);
  }

  private _openSaleLinkProcessingDialog(saleLink: SaleLinkDto): void {
    this.dialogService.open(SaleLinkProcessingDialogComponent, {
      header: `Процесс покупки машины`,
      width: '90%',
      data: { saleLink }
    }).onClose.subscribe(async (): Promise<void> => await this._initializeSaleLinks());
  }
}
