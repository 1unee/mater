import {Component, Inject, OnInit} from '@angular/core';
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {CarDto} from "../../../store/dtos/car.dto";
import {InputGroupModule} from "primeng/inputgroup";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Button, ButtonDirective} from "primeng/button";
import {ClipboardService} from "../../../services/utils/clipboard.service";
import {CarService} from "../../../services/https/car.service";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {StorageService} from "../../../services/utils/storage.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {Dropdown, DropdownModule} from "primeng/dropdown";
import {GearboxEnum, GearboxTitle} from "../../../store/enums/gearbox.enum";
import {CarStateEnum, CarStateTitle} from "../../../store/enums/car-state.enum";
import {EngineOilTypeEnum, EngineOilTypeTitle} from "../../../store/enums/engine-oil-type.enum";
import {SteeringWheelEnum, SteeringWheelTitle} from "../../../store/enums/steering-wheel.enum";
import {TransmissionEnum, TransmissionTitle} from "../../../store/enums/transmission.enum";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {OneuneValidators} from "../../../services/utils/validators";

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
    FormsModule,
    DropdownModule,
    ButtonDirective,
    LongClickDirective
  ],
  templateUrl: './car-processing-dialog.component.html',
  styleUrl: './car-processing-dialog.component.scss'
})
export class CarProcessingDialogComponent extends AbstractFormComponent<CarDto> implements OnInit {

  cars: CarDto[];
  carGearboxes: { label: string, value: GearboxEnum }[] = [
    { label: GearboxTitle.AUTOMATIC, value: GearboxEnum.AUTOMATIC },
    { label: GearboxTitle.MANUAL, value: GearboxEnum.MANUAL },
    { label: GearboxTitle.SEMI_AUTOMATIC, value: GearboxEnum.SEMI_AUTOMATIC },
  ];
  carStates: { label: string, value: CarStateEnum }[] = [
    { label: CarStateTitle.NEW, value: CarStateEnum.NEW },
    { label: CarStateTitle.USED, value: CarStateEnum.USED },
    { label: CarStateTitle.DAMAGED, value: CarStateEnum.DAMAGED },
  ];
  carEngineOilTypes: { label: string, value: EngineOilTypeEnum }[] = [
    { label: EngineOilTypeTitle.PETROL, value: EngineOilTypeEnum.PETROL },
    { label: EngineOilTypeTitle.DIESEL, value: EngineOilTypeEnum.DIESEL },
    { label: EngineOilTypeTitle.ELECTRIC, value: EngineOilTypeEnum.ELECTRIC },
  ];
  carTransmissions: { label: string, value: TransmissionEnum }[] = [
    { label: TransmissionTitle.FWD, value: TransmissionEnum.FWD },
    { label: TransmissionTitle.AWD, value: TransmissionEnum.AWD },
    { label: TransmissionTitle.RWD, value: TransmissionEnum.RWD },
  ];
  carSteeringWheels: { label: string, value: SteeringWheelEnum }[] = [
    { label: SteeringWheelTitle.LEFT, value: SteeringWheelEnum.LEFT },
    { label: SteeringWheelTitle.RIGHT, value: SteeringWheelEnum.RIGHT },
  ];

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              private dynamicDialogRef: DynamicDialogRef,
              public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              private carService: CarService,
              private formBuilder: FormBuilder,
              private storageService: StorageService,
              @Inject(LOADING) public loadingReference: LoadingReference) {
    super(formBuilder, messageService, loadingReference);
  }

  async ngOnInit(): Promise<void> {
    this._initializeEditingCar();
    this._initializeForm();
    this.cars = await this.carService.getAll();
  }

  get uniqueCarBrands(): string[] {
    return [...new Set(this.cars.map((car: CarDto): string => car.brand))];
  }

  get uniqueCarModels(): string[] {
    return [...new Set(this.cars.map((car: CarDto): string => car.model))];
  }

  get uniqueCarDocumentsColor(): string[] {
    return [...new Set(this.cars.map((car: CarDto): string => car.documentsColor))];
  }

  private _initializeEditingCar(): void {
    this.formObject = this.dynamicDialogConfig.data.car;
  }

  protected override _initializeForm(): void {
    this.form = this.formBuilder.group({
      carBrand: [this.formObject.brand, [
        Validators.required, OneuneValidators.isText()
      ]],
      carModel: [this.formObject.model, [
        Validators.required
      ]],
      carProductionYear: [this.formObject.productionYear, [
        Validators.required,
        OneuneValidators.isNumber(),
        Validators.min(1500),
        Validators.max(2500),
        OneuneValidators.isInteger()
      ]],
      carPrice: [this.formObject.price, [
        Validators.required, OneuneValidators.isNumber(), Validators.min(0)
      ]],
      carMileage: [this.formObject.mileage, [
        Validators.required, OneuneValidators.isNumber(), Validators.min(0)
      ]],
      carVIN: [this.formObject.VIN, [
        Validators.required, Validators.minLength(17), Validators.maxLength(17)
      ]],
      carOwnersAmount: [this.formObject.ownersAmount, [
        Validators.required, OneuneValidators.isNumber(), Validators.min(0)
      ]],
      carPower: [this.formObject.power, [
        Validators.required, OneuneValidators.isNumber(), Validators.min(0)
      ]],
      carDocumentsColor: [this.formObject.documentsColor, [
        Validators.required, OneuneValidators.isText()
      ]],
      carGearbox: [this.formObject.gearbox, [
        Validators.required, OneuneValidators.isText()
      ]],
      carState: [this.formObject.state, [
        Validators.required, OneuneValidators.isText()
      ]],
      carEngineOilType: [this.formObject.engineOilType, [
        Validators.required, OneuneValidators.isText()
      ]],
      carTransmission: [this.formObject.transmission, [
        Validators.required, OneuneValidators.isText()
      ]],
      carSteeringWheel: [this.formObject.steeringWheel, [
        Validators.required, OneuneValidators.isText()
      ]]
    }, {
      validators: []
    });
  }

  protected override _buildModel(): CarDto {
    this.formObject.brand = this.form.value.carBrand;
    this.formObject.model = this.form.value.carModel;
    this.formObject.productionYear = this.form.value.carProductionYear;
    this.formObject.price = this.form.value.carPrice;
    this.formObject.mileage = this.form.value.carMileage;
    this.formObject.VIN = this.form.value.carVIN;
    this.formObject.ownersAmount = this.form.value.carOwnersAmount;
    this.formObject.power = this.form.value.carPower;
    this.formObject.documentsColor = this.form.value.carDocumentsColor.substring(0, 1).toUpperCase()
      + this.form.value.carDocumentsColor.substring(1);
    this.formObject.gearbox = this.form.value.carGearbox;
    this.formObject.state = this.form.value.carState;
    this.formObject.engineOilType = this.form.value.carEngineOilType;
    this.formObject.transmission = this.form.value.carTransmission;
    this.formObject.steeringWheel = this.form.value.carSteeringWheel;
    return this.formObject;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
    const carId: number = this.formObject.id;
    this.formObject = this._buildModel();
    try {
      this.loadingReference.value.next(true);
      if (!!this.formObject.id) {
        this.formObject = await this.carService.put(this.formObject);
        this.messageService.showSuccess('Данные о машине успешно изменены!');
      } else {
        this.formObject = await this.carService.post(this.storageService.user.seller.id, this.formObject);
        this.messageService.showSuccess('Машина успешно внесена в список!');
      }
      if (!carId) {
        this.formObject = new CarDto();
        this.form.reset();
      }
      if (closeDialog) {
        this.dynamicDialogRef.close();
      }
    } catch (e) {
      this.messageService.showDefaultError();
    } finally {
      this.loadingReference.value.next(false);
    }
  }

  onChange(dropdown: Dropdown): void {
    dropdown.hide();
  }
}
