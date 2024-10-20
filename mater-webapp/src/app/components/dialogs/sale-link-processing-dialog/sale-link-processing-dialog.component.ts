import {Component, Inject, OnInit} from '@angular/core';
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
import {FloatLabelModule} from "primeng/floatlabel";
import {InputTextareaModule} from "primeng/inputtextarea";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";

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
    RatingModule,
    FloatLabelModule,
    InputTextareaModule
  ],
  templateUrl: './sale-link-processing-dialog.component.html',
  styleUrl: './sale-link-processing-dialog.component.scss'
})
export class SaleLinkProcessingDialogComponent extends AbstractFormComponent<SaleLinkDto> implements OnInit {

  readonly MAX_LENGTH_SALE_LINK_NOTE: number = 4096;

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
              private sellerService: SellerService,
              @Inject(LOADING) public loadingReference: LoadingReference,) {
    super(formBuilder, messageService, loadingReference);
  }

  ngOnInit(): void {
    this._initializeSaleLink();
    this._initializeForm();
  }

  get formSaleLinkNote(): string {
    return this.form.value.saleLinkNote ?? '';
  }

  private _initializeSaleLink(): void {
    this.formObject = this.dynamicDialogConfig.data.saleLink;
  }

  protected override _initializeForm(): void {
    this.form = this.formBuilder.group({
      saleLinkStatus: [this.formObject.status, [Validators.required]],
      saleLinkScore: [this.formObject.score, [Validators.required]],
      saleLinkNote: [this.formObject.note, [Validators.maxLength(this.MAX_LENGTH_SALE_LINK_NOTE)]]
    });
  }

  protected override _buildModel(): SaleLinkDto {
    this.formObject.status = this.form.value.saleLinkStatus;
    this.formObject.score = this.form.value.saleLinkScore;
    this.formObject.note = this.form.value.saleLinkNote;
    return this.formObject;
  }

  public async onSubmit(closeDialog: boolean): Promise<void> {
    this.formObject = this._buildModel();
    await this.sellerService.putSaleLink(this.formObject);
    this.form.reset();
    if (closeDialog) {
      this.dynamicDialogRef.close();
    }
  }
}
