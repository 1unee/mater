import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {TableModule} from "primeng/table";
import {CarDto} from "../../../store/dtos/car.dto";
import {NgClass, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {Button, ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {TooltipModule} from "primeng/tooltip";
import {CarService} from "../../../services/https/car.service";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {DataViewModule} from "primeng/dataview";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {TagModule} from "primeng/tag";
import {ConfirmationService} from "primeng/api";
import {ImageModule} from "primeng/image";
import {BadgeModule} from "primeng/badge";
import {CarouselModule} from "primeng/carousel";
import {OverlayPanel, OverlayPanelModule} from "primeng/overlaypanel";
import {ClipboardService} from "../../../services/utils/clipboard.service";
import {VideoPlayerComponent} from "../../core/video-player/video-player.component";
import {StyleClassModule} from "primeng/styleclass";
import {StorageService} from "../../../services/utils/storage.service";
import {DialogService} from "primeng/dynamicdialog";
import {CarProcessingDialogComponent} from "../../dialogs/car-processing-dialog/car-processing-dialog.component";
import {SelectButtonModule} from "primeng/selectbutton";
import {SellerService} from "../../../services/https/seller.service";
import {SellerDto} from "../../../store/dtos/seller.dto";
import {OneuneFileUploadComponent} from "../../core/oneune-file-upload/oneune-file-upload.component";
import {InputTextModule} from "primeng/inputtext";
import {AutoFocus} from "primeng/autofocus";
import {RouterLink} from "@angular/router";
import {PageResponse} from "../../../store/pagination/page.response.pagination";
import {GlobalConfig} from "../../../store/interfaces/global-config.interface";
import {ContactDto} from "../../../store/dtos/contact.dto";
import {ContactTypeEnum, ContactTypeTitle} from "../../../store/enums/contact-type.enum";
import {GLOBAL_CONFIG, LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {FilterType} from "../../../store/pagination/filter-type.pagination";
import {ColumnQuery} from "../../../store/pagination/column-query.pagination";
import {PaginationDirection} from "../../../store/pagination/pagination-direction.enum";
import {CarCategorySortEnum, CarCategorySortTitle} from "../../../store/enums/car-category-sort.enum";
import {LogService} from "../../../services/https/log.service";
import {PageQueryProcessingComponent} from "../../core/page-query-processing/page-query-processing.component";
import {GalleriaModule} from "primeng/galleria";

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [
    TableModule,
    NgIf,
    ButtonDirective,
    Ripple,
    TooltipModule,
    DataViewModule,
    DropdownModule,
    FormsModule,
    TagModule,
    Button,
    NgClass,
    NgForOf,
    NgOptimizedImage,
    ImageModule,
    BadgeModule,
    CarouselModule,
    OverlayPanelModule,
    VideoPlayerComponent,
    StyleClassModule,
    SelectButtonModule,
    OneuneFileUploadComponent,
    InputTextModule,
    AutoFocus,
    RouterLink,
    PageQueryProcessingComponent,
    GalleriaModule
  ],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.scss'
})
export class CarListComponent implements OnInit {

  readonly CarCategorySortEnum = CarCategorySortEnum;
  readonly ContactTypeTitle = ContactTypeTitle;
  readonly PaginationDirection = PaginationDirection;
  readonly console = console;

  @ViewChild('contactsOverlayPanel') contactsOverlayPanel: OverlayPanel;

  paginationConfig: {
    pageNumber: number,
    pageSize: number,
    processing: boolean,
    columnQueries: ColumnQuery[],
    pageResponse: PageResponse<CarDto>
  } = {
    pageNumber: 0,
    pageSize: 3,
    processing: false,
    columnQueries: [],
    pageResponse: {} as PageResponse<CarDto>
  };

  selectButtonStatesConfig: { states: { label: string, value: CarCategorySortEnum, styleClass: string }[], selectedValue: CarCategorySortEnum } = {
    states: [
      { label: CarCategorySortTitle.MINE, value: CarCategorySortEnum.MINE, styleClass: 'p-2 p-button-secondary' },
      { label: CarCategorySortTitle.ALL, value: CarCategorySortEnum.ALL, styleClass: 'p-2 p-button-secondary' },
    ],
    selectedValue: CarCategorySortEnum.MINE
  }

  seller: SellerDto;
  starIconFlag: boolean = false;

  constructor(private carService: CarService,
              public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              public storageService: StorageService,
              private confirmationService: ConfirmationService,
              private dialogService: DialogService,
              private sellerService: SellerService,
              @Inject(GLOBAL_CONFIG) public globalConfig: GlobalConfig,
              @Inject(LOADING) public loadingReference: LoadingReference) {
  }

  async ngOnInit(): Promise<void> {
    await this.onChange();
    this.seller = await this.sellerService.getById(this.storageService.user.seller.id);
  }

  get carListHeader(): string {
    const totalElements: number = this.paginationConfig.pageResponse.totalElements;
    if (this.loadingReference.value.getValue()) {
      return 'Загрузка';
    } else if (totalElements === 0) {
      return 'Машин нет';
    } else {
      return `Машины (${totalElements > 1000 ? Math.floor(totalElements / 1000) + 1 + ' тыс.' : totalElements})`;
    }
  }

  protected async _paginateCars(): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      this.paginationConfig.pageResponse = await this.carService.search({
        page: this.paginationConfig.pageNumber,
        size: this.paginationConfig.pageSize,
        columns: this.paginationConfig.columnQueries
      });
    } catch (e) {
      this.messageService.showError('Не удалось получить данные!');
    } finally {
      this.loadingReference.value.next(false);
    }
  }

  async loadFirstPage(): Promise<void> {
    this.paginationConfig.pageNumber = 0;
    await this._paginateCars();
  }

  async loadCurrentPage(): Promise<void> {
    await this._paginateCars();
  }

  async loadLastPage(): Promise<void> {
    this.paginationConfig.pageNumber = this.paginationConfig.pageResponse.totalPages - 1;
    await this._paginateCars();
  }

  async search(direction: PaginationDirection): Promise<void> {
    this.paginationConfig.pageNumber = this.paginationConfig.pageNumber + (direction === PaginationDirection.PREV ? -1 : 1);
    await this._paginateCars();
  }

  async applyPageQuery(columnQueries: ColumnQuery[]): Promise<void> {
    this.paginationConfig.columnQueries = columnQueries;
    await this.loadCurrentPage();
  }

  async onCarProcessing(car: CarDto | null): Promise<void> {
    if (!!this.seller?.contacts && this.seller?.contacts.length > 0) {
      const processingCar: CarDto = !car ? new CarDto() : car;
      this._openCarProcessingDialog(processingCar);
    } else {
      this.messageService.showInfo('Чтобы добавлять машины, сначала нужно оставить свои контакты для связи в профиле.');
    }
  }

  onUpload(car: CarDto): void {
    this._openUploadFilesDialog(car);
  }

  private _openUploadFilesDialog(car: CarDto): void {
    this.dialogService.open(OneuneFileUploadComponent, {
      header: `${car.brand} ${car.model} (${car.productionYear})`,
      width: '90%',
      data: {car}
    }).onClose.subscribe(async () => await this.loadCurrentPage());
  }

  onRemove(carId: number): void {
    this._confirmRemoving(carId);
  }

  private _openCarProcessingDialog(car: CarDto): void {
    this.dialogService.open(CarProcessingDialogComponent, {
      header: `${!!car.id ? car.brand + ' ' + car.model + ' (' + car.productionYear + ')' : 'Новая машина'}`,
      width: '90%',
      data: {car}
    }).onClose.subscribe(async () => {
      await this.loadCurrentPage();
    });
  }

  private _confirmRemoving(carId: number): void {
    this.confirmationService.confirm({
      message: 'Ты уверен, что хочешь удалить машину? Потом это действие отменить нельзя будет.',
      header: 'Подтверждение',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Да',
      accept: async () => this._removeCar(carId),
      rejectLabel: 'Нет',
      reject: () => this.messageService.showInfo('Понял, оставляем как есть')
    });
  }

  private async _removeCar(carId: number): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      await this.carService.delete(carId);
      this.messageService.showSuccess('Машина успешно удалена!');
      await this.loadCurrentPage();
    } catch (e) {
      this.messageService.showDefaultError();
    } finally {
      this.loadingReference.value.next(false);
    }
  }

  async onOpenContacts(event: MouseEvent, carId: number): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      this.seller = await this.sellerService.getByCarId(carId);
      this.contactsOverlayPanel.toggle(event);
    } catch (e) {
      this.messageService.showDefaultError();
    } finally {
      this.loadingReference.value.next(false);
    }
  }

  toggleOverlayPanelState(state: 'show' | 'hide'): void {
    if (state === 'show') {
      document.body.classList.add('overlay-open');
    } else {
      document.body.classList.remove('overlay-open');
    }
  }

  async onPageReload(): Promise<void> {
    await this.loadCurrentPage();
    this.messageService.showInfo('Список машин перезагружен!');
  }

  async onChange(): Promise<void> {
    if (this.selectButtonStatesConfig.selectedValue === CarCategorySortEnum.MINE) {

      const columnQuery: ColumnQuery = {
        name: 'seller.id',
        filterType: FilterType.EQUALS,
        filterValue: this.storageService.user.seller.id
      } as ColumnQuery;

      if (this.paginationConfig.columnQueries.findIndex(cq => cq === columnQuery) === -1) {
        const columnQuery: ColumnQuery = {
          name: 'seller.id',
          filterType: FilterType.EQUALS,
          filterValue: this.storageService.user.seller.id
        } as ColumnQuery;
        this.paginationConfig.columnQueries.push(columnQuery);
      }

      await this.loadCurrentPage();
    } else {
      this.paginationConfig.columnQueries = [];
      await this.loadCurrentPage();
    }
  }

  onTogglePageQuerying(): void {
    this.paginationConfig.processing = !this.paginationConfig.processing;
    if (this.globalConfig.settings.showWarnPageQueryingMessage && this.paginationConfig.processing) {
      this.messageService.showWarning(
        'Фильтрация и сортировка работают в тестовом режиме.' +
        'Об ошибках сообщать в форме обратной связи ' +
        '(либо на странице помощи, либо в описании каждого раздела на странице личной информации).'
      );
    }
  }
}

