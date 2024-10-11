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
import {isInteger, isNumber, isText} from "../../../services/utils/validators";
import {StorageService} from "../../../services/utils/storage.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {Dropdown, DropdownChangeEvent, DropdownModule} from "primeng/dropdown";
import {GearboxEnum, GearboxTitle} from "../../../store/enums/gearbox.enum";
import {CarStateEnum, CarStateTitle} from "../../../store/enums/car-state.enum";
import {EngineOilTypeEnum, EngineOilTypeTitle} from "../../../store/enums/engine-oil-type.enum";
import {SteeringWheelEnum, SteeringWheelTitle} from "../../../store/enums/steering-wheel.enum";
import {TransmissionEnum, TransmissionTitle} from "../../../store/enums/transmission.enum";
import {LongClickDirective} from "../../../services/directives/long-click.directive";

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

  car: CarDto;
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
    super();
  }

  async ngOnInit(): Promise<void> {
    this._initializeEditingCar();
    this._initializeForm();
    this.cars = await this.carService.getAll();
  }

  private _initializeEditingCar(): void {
    this.car = this.dynamicDialogConfig.data.car;
  }

  protected override _initializeForm(): void {

    this.form = this.formBuilder.group({
      carBrand: [this.car.brand, [
        Validators.required, isText()
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
      ]],
      carDocumentsColor: [this.car.documentsColor, [
        Validators.required, isText()
      ]],
      carGearbox: [this.car.gearbox, [
        Validators.required, isText()
      ]],
      carState: [this.car.state, [
        Validators.required, isText()
      ]],
      carEngineOilType: [this.car.engineOilType, [
        Validators.required, isText()
      ]],
      carTransmission: [this.car.transmission, [
        Validators.required, isText()
      ]],
      carSteeringWheel: [this.car.steeringWheel, [
        Validators.required, isText()
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
    this.car.documentsColor = this.form.value.carDocumentsColor.substring(0, 1).toUpperCase()
      + this.form.value.carDocumentsColor.substring(1);
    this.car.gearbox = this.form.value.carGearbox;
    this.car.state = this.form.value.carState;
    this.car.engineOilType = this.form.value.carEngineOilType;
    this.car.transmission = this.form.value.carTransmission;
    this.car.steeringWheel = this.form.value.carSteeringWheel;
    return this.car;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
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
