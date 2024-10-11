import {Component, OnInit} from '@angular/core';
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {SaleLinkDto} from "../../../store/dtos/sale-link.dto";
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {NgIf} from "@angular/common";
import {SaleLinkStatusEnum, SaleLinkStatusTitle} from "../../../store/enums/sale-link-status.enum";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {SellerService} from "../../../services/https/seller.service";
import {RatingModule} from "primeng/rating";

@Component({
  selector: 'app-sale-link-processing-dialog',
  standalone: true,
  imports: [
    Button,
    DropdownModule,
    FormsModule,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    LongClickDirective,
    NgIf,
    ReactiveFormsModule,
    RatingModule
  ],
  templateUrl: './sale-link-processing-dialog.component.html',
  styleUrl: './sale-link-processing-dialog.component.scss'
})
export class SaleLinkProcessingDialogComponent extends AbstractFormComponent<SaleLinkDto> implements OnInit {

  saleLink: SaleLinkDto;
  saleLinkStatusConfig: { options: { label: string, value: SaleLinkStatusEnum, styleClass: string }[] } = {
    options: [
      { label: SaleLinkStatusTitle.INTERESTED, value: SaleLinkStatusEnum.INTERESTED, styleClass: 'p-1 text-sm' },
      { label: SaleLinkStatusTitle.PROCESSING, value: SaleLinkStatusEnum.PROCESSING, styleClass: 'p-1 text-sm' },
      { label: SaleLinkStatusTitle.CANCELLED, value: SaleLinkStatusEnum.CANCELLED, styleClass: 'p-1 text-sm' },
      { label: SaleLinkStatusTitle.BOUGHT, value: SaleLinkStatusEnum.BOUGHT, styleClass: 'p-1 text-sm' },
    ]
  };

  constructor(public messageService: OneuneMessageService,
              private formBuilder: FormBuilder,
              private dynamicDialogConfig: DynamicDialogConfig,
              private dynamicDialogRef: DynamicDialogRef,
              private sellerService: SellerService) {
    super();
  }

  ngOnInit(): void {
    this._initializeSaleLink();
    this._initializeForm();
  }

  private _initializeSaleLink(): void {
    this.saleLink = this.dynamicDialogConfig.data.saleLink;
  }

  protected override _initializeForm(): void {
    this.form = this.formBuilder.group({
      saleLinkStatus: [this.saleLink.status, [Validators.required]],
      saleLinkScore: [this.saleLink.score, [Validators.required]]
    });
  }

  protected override _buildModel(): SaleLinkDto {
    this.saleLink.status = this.form.value.saleLinkStatus;
    this.saleLink.score = this.form.value.saleLinkScore;
    return this.saleLink;
  }

  public async onSubmit(closeDialog: boolean): Promise<void> {
    this.saleLink = this._buildModel();
    await this.sellerService.putSaleLink(this.saleLink);
    this.form.reset();
    if (closeDialog) {
      this.dynamicDialogRef.close();
    }
  }
}
