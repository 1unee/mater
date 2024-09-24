import {Component, Inject, OnInit} from '@angular/core';
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CarDto} from "../../../store/dtos/car.dto";
import {InputGroupModule} from "primeng/inputgroup";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Button} from "primeng/button";
import {ClipboardService} from "../../../services/utils/clipboard.service";
import {CarService} from "../../../services/https/car.service";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {isInteger, isNumber} from "../../../services/utils/validators";
import {StorageService} from "../../../services/utils/storage.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";

@Component({
  selector: 'app-car-processing-dialog',
  standalone: true,
  imports: [
    InputGroupModule,
    InputGroupAddonModule,
    InputTextModule,
    NgIf,
    ReactiveFormsModule,
    Button,
    FormsModule
  ],
  templateUrl: './car-processing-dialog.component.html',
  styleUrl: './car-processing-dialog.component.scss'
})
export class CarProcessingDialogComponent extends AbstractFormComponent<CarDto> implements OnInit {

  car: CarDto;

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              private dynamicDialogRef: DynamicDialogRef,
              public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              private carService: CarService,
              private formBuilder: FormBuilder,
              private storageService: StorageService,
              @Inject(LOADING) public loadingReference: LoadingReference) {
    super();
  }

  ngOnInit(): void {
    this._initializeEditingCar();
    this._initializeForm();
  }

  private _initializeEditingCar(): void {
    this.car = this.dynamicDialogConfig.data.car;
  }

  protected override _initializeForm(): void {

    this.form = this.formBuilder.group({
      carBrand: [this.car.brand, [
        Validators.required
      ]],
      carModel: [this.car.model, [
        Validators.required
      ]],
      carProductionYear: [this.car.productionYear, [
        Validators.required, isNumber(), Validators.min(1500), Validators.max(2500), isInteger()
      ]],
      carPrice: [this.car.price, [
        Validators.required, isNumber(), Validators.min(0)
      ]],
      carMileage: [this.car.mileage, [
        Validators.required, isNumber(), Validators.min(0)
      ]],
      carVIN: [this.car.VIN, [
        Validators.required, Validators.minLength(17), Validators.maxLength(17)
      ]],
      carOwnersAmount: [this.car.ownersAmount, [
        Validators.required, isNumber(), Validators.min(0)
      ]],
      carPower: [this.car.power, [
        Validators.required, isNumber(), Validators.min(0)
      ]]
    }, {
      validators: []
    });
  }

  protected override _buildModel(): CarDto {
    this.car.brand = this.form.value.carBrand;
    this.car.model = this.form.value.carModel;
    this.car.productionYear = this.form.value.carProductionYear;
    this.car.price = this.form.value.carPrice;
    this.car.mileage = this.form.value.carMileage;
    this.car.VIN = this.form.value.carVIN;
    this.car.ownersAmount = this.form.value.carOwnersAmount;
    this.car.power = this.form.value.carPower;
    return this.car;
  }

  async onSubmit(): Promise<void> {
    this.car = this._buildModel();
    try {
      this.loadingReference.value.next(true);
      if (!!this.car.id) {
        this.car = await this.carService.put(this.car);
        this.messageService.showSuccess('Данные о машине успешно изменены!');
      } else {
        this.car = await this.carService.post(this.storageService.user.seller.id, this.car);
        this.messageService.showSuccess('Машина успешно внесена в список!');
      }
      this.car = new CarDto();
      this.form.reset();
      this.dynamicDialogRef.close();
    } catch (e) {
      this.messageService.showDefaultError();
    } finally {
      this.loadingReference.value.next(false);
    }
  }
}
