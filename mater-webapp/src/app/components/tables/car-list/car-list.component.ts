import {Component, Inject, OnInit} from '@angular/core';
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
import {ActivatedRoute, RouterLink} from "@angular/router";
import {PageResponse} from "../../../store/pagination/page.response.pagination";
import {ContactTypeEnum, ContactTypeTitle} from "../../../store/enums/contact-type.enum";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {FilterType} from "../../../store/pagination/filter-type.pagination";
import {ColumnQuery} from "../../../store/pagination/column-query.pagination";
import {PaginationDirection} from "../../../store/pagination/pagination-direction.enum";
import {CarCategorySortEnum, CarCategorySortTitle} from "../../../store/enums/car-category-sort.enum";
import {PageQueryProcessingComponent} from "../../core/page-query-processing/page-query-processing.component";
import {GalleriaModule} from "primeng/galleria";
import {FileDto} from "../../../store/dtos/file.dto";
import {environment} from "../../../../environments/environment";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
import {getGearboxTitle} from "../../../store/enums/gearbox.enum";
import {getCarStateTitle} from "../../../store/enums/car-state.enum";
import {getEngineOilTypeTitle} from "../../../store/enums/engine-oil-type.enum";
import {getTransmissionTitle} from "../../../store/enums/transmission.enum";
import {getSteeringWheelTitle} from "../../../store/enums/steering-wheel.enum";
import {AccordionModule} from "primeng/accordion";
import {ContactDto} from "../../../store/dtos/contact.dto";
import {MenubarModule} from "primeng/menubar";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {ForeignLink} from "../../../store/interfaces/foreign-link.interface";
import {ActionService} from "../../../services/https/action.service";
import {ActionTypeEnum} from "../../../store/enums/action-type.enum";

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
    GalleriaModule,
    AccordionModule,
    LongClickDirective,
    MenubarModule
  ],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.scss'
})
export class CarListComponent implements OnInit {

  readonly CarCategorySortEnum = CarCategorySortEnum;
  readonly ContactTypeTitle = ContactTypeTitle;
  readonly PaginationDirection = PaginationDirection;
  readonly getGearboxTitle = getGearboxTitle;
  readonly getCarStateTitle = getCarStateTitle;
  readonly getEngineOilTypeTitle = getEngineOilTypeTitle;
  readonly getTransmissionTitle = getTransmissionTitle;
  readonly getSteeringWheelTitle = getSteeringWheelTitle;
  readonly JSON = JSON;

  paginationConfig: {
    pageNumber: number,
    pageSize: number,
    processing: boolean,
    columnQueries: ColumnQuery[],
    defaultColumnQueries: { carId: ColumnQuery, mineCars: ColumnQuery, allCars: ColumnQuery },
    pageResponse: PageResponse<CarDto>
  } = {
    pageNumber: 0,
    pageSize: 3,
    processing: false,
    columnQueries: [],
    defaultColumnQueries: {
      carId: {
        name: 'id',
        filterType: FilterType.EQUALS,
        filterValue: null
      } as ColumnQuery,
      mineCars: {
        name: 'seller.id',
        filterType: FilterType.EQUALS,
        filterValue: null
      } as ColumnQuery,
      allCars: {
        name: 'seller.id',
        filterType: FilterType.NOT_EQUALS,
        filterValue: null
      } as ColumnQuery
    },
    pageResponse: {} as PageResponse<CarDto>
  };

  selectButtonStatesConfig: { states: { label: string, value: CarCategorySortEnum, styleClass: string }[], selectedValue: CarCategorySortEnum } = {
    states: [
      { label: CarCategorySortTitle.MINE, value: CarCategorySortEnum.MINE, styleClass: 'p-2 p-button-secondary p-button-outlined' },
      { label: CarCategorySortTitle.ALL, value: CarCategorySortEnum.ALL, styleClass: 'p-2 p-button-secondary p-button-outlined' },
      // { label: CarCategorySortTitle.FAVORITES, value: CarCategorySortEnum.FAVORITES, styleClass: 'p-2 p-button-secondary' },
    ],
    selectedValue: CarCategorySortEnum.MINE
  }

  seller: SellerDto;
  userFavoriteCars: CarDto[];
  foreignLink: ForeignLink;

  constructor(private carService: CarService,
              public messageService: OneuneMessageService,
              public clipboardService: ClipboardService,
              public storageService: StorageService,
              private confirmationService: ConfirmationService,
              private dialogService: DialogService,
              private sellerService: SellerService,
              @Inject(LOADING) public loadingReference: LoadingReference,
              private routerService: OneuneRouterService,
              private activatedRoute: ActivatedRoute,
              private actionService: ActionService) {
  }

  async ngOnInit(): Promise<void> {
    await this.onChange();
    this.userFavoriteCars = await this.carService.getFavoriteCars(this.storageService.user.id);
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

  getCarImages(car: CarDto): FileDto[] {
    return car.files.filter(file => file.type.startsWith('image'));
  }

  getCarVideos(car: CarDto): FileDto[] {
    return car.files.filter(file => file.type.startsWith('video'));
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
    this.paginationConfig.pageNumber =
      this.paginationConfig.pageNumber + (direction === PaginationDirection.PREV ? -1 : 1);
    await this._paginateCars();
  }

  async applyPageQuery(columnQueries: ColumnQuery[]): Promise<void> {
    const existedColumnQueries: ColumnQuery[] = this.paginationConfig.columnQueries;
    this.paginationConfig.columnQueries = [...columnQueries, ...existedColumnQueries];
    await this.loadCurrentPage();
  }

  async onCarProcessing(car: CarDto | null): Promise<void> {
    if (!!this.seller?.contacts && this.seller?.contacts.length > 0) {
      const processingCar: CarDto = !car ? new CarDto() : car;
      this._openCarProcessingDialog(processingCar);
    } else {
      this._confirmRedirecting();
    }
  }

  async onCarCreatingFromExisting(car: CarDto): Promise<void> {
    const processingCar: CarDto = JSON.parse(JSON.stringify(car)) as CarDto;
    processingCar.id = null as any;
    this._openCarProcessingDialog(processingCar);
  }

  private _confirmRedirecting(): void {
    this.confirmationService.confirm({
      message: 'Чтобы добавлять машины, сначала нужно оставить свои контакты для связи в профиле. Открыть профиль?',
      accept: async () => this._openProfilePage('user-contacts'),
      reject: () => {}
    });
  }

  private _openProfilePage(
    targetProfileTab: 'user-description' | 'user-rating' | 'user-search-history' | 'user-contacts' | 'none' = 'none'
  ): void {
    this.routerService.relativeRedirect(`/profile`, {'target-profile-tab': targetProfileTab})
  }

  onUpload(car: CarDto): void {
    this._openUploadFilesDialog(car);
  }

  private _openUploadFilesDialog(car: CarDto): void {
    this.dialogService.open(OneuneFileUploadComponent, {
      header: `${car.brand} ${car.model} (${car.productionYear})`,
      width: '90%',
      data: { car: car }
    }).onClose.subscribe(async (): Promise<void> => await this.loadCurrentPage());
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
      accept: async () => this._removeCar(carId),
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

  async onOpenContacts(event: MouseEvent,
                       overlayPanel: OverlayPanel,
                       carId: number): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      this.seller = await this.sellerService.getByCarId(carId);
      overlayPanel.toggle(event);
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

  private async _initializeForeignLink(): Promise<ForeignLink> {
    return new Promise(resolve => {
      let foreignLink: ForeignLink;
      this.activatedRoute.queryParams.subscribe(params => {
        const carId: number = params['car-id'];
        if (carId) {
          foreignLink = { isForeignLink: true, carId };
          resolve(foreignLink);
        } else {
          foreignLink = { isForeignLink: false, carId: undefined }
          resolve(foreignLink);
        }
      });
      this.foreignLink = foreignLink!;
    });
  }

  async onChange(): Promise<void> {
    const foreignLink: ForeignLink = await this._initializeForeignLink();
    if (foreignLink.isForeignLink) {
      const foreignReferenceColumnQuery: ColumnQuery = this.paginationConfig.defaultColumnQueries.carId;
      foreignReferenceColumnQuery.filterValue = foreignLink.carId;
      if (this.paginationConfig.columnQueries.findIndex(cq => cq === foreignReferenceColumnQuery) === -1) {
        this.paginationConfig.columnQueries.push(foreignReferenceColumnQuery);
      }
      await this.loadCurrentPage();
    } else {
      if (this.selectButtonStatesConfig.selectedValue === CarCategorySortEnum.MINE) {
        const mineCarsColumnQuery: ColumnQuery = this.paginationConfig.defaultColumnQueries.mineCars;
        mineCarsColumnQuery.filterValue = this.storageService.user.id;
        if (this.paginationConfig.columnQueries.findIndex(cq => cq === mineCarsColumnQuery) === -1) {
          this.paginationConfig.columnQueries = [mineCarsColumnQuery];
        }
        await this.loadCurrentPage();
      } else {
        const allCarsColumnQueries: ColumnQuery = this.paginationConfig.defaultColumnQueries.allCars;
        allCarsColumnQueries.filterValue = this.storageService.user.id;
        if (this.paginationConfig.columnQueries.findIndex(cq => cq === allCarsColumnQueries) === -1) {
          this.paginationConfig.columnQueries = [allCarsColumnQueries];
        }
        await this.loadCurrentPage();
      }
    }
  }

  onTogglePageQuerying(): void {
    this.paginationConfig.processing = !this.paginationConfig.processing;
    if (JSON.parse(this.storageService.getSettingByCode(4).selectedOption.value)
      && this.paginationConfig.processing) {
      this.messageService.showWarning(
        'Фильтрация и сортировка работают в тестовом режиме.' +
        'Об ошибках сообщать в форме обратной связи ' +
        '(либо на странице помощи, либо в описании каждого раздела на странице личной информации).'
      );
    }
  }

  private _confirmOpeningContactUrlReference(contact: ContactDto): void {
    this.confirmationService.confirm({
      message: 'Открыть ссылку?',
      accept: () => this.routerService.absoluteRedirect(contact.value),
      reject: () => this.messageService.showInfo('Понял, но контакт все равно скопирован в буфер обмена!')
    });
  }

  onClickContact(contact: ContactDto, car: CarDto, overlayPanel: OverlayPanel): void {
    const isUrlReference: boolean = [
      ContactTypeEnum.TELEGRAM,
      ContactTypeEnum.WHATSAPP,
      ContactTypeEnum.INSTAGRAM,
      ContactTypeEnum.VKONTAKTE
    ].includes(contact.type);
    if (isUrlReference) {
      this._confirmOpeningContactUrlReference(contact);
    } else {
      this.clipboardService.copyWithCustomMessage(contact.value, 'Скопировано!')
    }
    this.clipboardService.copyWithoutMessage(contact.value)
    this.sellerService.postSaleLink(this.storageService.user.id, car.id).finally();
    overlayPanel.overlayVisible = false;
  }

  private _chooseCarImagesDemonstratingType(event: MouseEvent, overlayPanel: OverlayPanel, car: CarDto): void {
    this.confirmationService.confirm({
      message: 'Открыть в галерее прикрепленные фото машины?',
      accept: () => this.routerService.relativeRedirect('galleria', { 'car-id': car.id }),
      reject: () => overlayPanel.toggle(event)
    });
  }

  onCarImagesShow(event: MouseEvent, overlayPanel: OverlayPanel, car: CarDto): void {
    this._chooseCarImagesDemonstratingType(event, overlayPanel, car);
  }

  isCarFavoriteForCurrentUser(questioningCar: CarDto): boolean {
    return this.userFavoriteCars
      .map((car: CarDto): number => car.id)
      .includes(questioningCar.id);
  }

  async onProcessingCarFavorite(car: CarDto): Promise<void> {
    if (!this.isCarFavoriteForCurrentUser(car)) {
      await this.carService.postFavoriteCar(this.storageService.user.id, car.id);
      this.messageService.showSuccess('Машина добавлена в список избранных')
    } else {
      await this.carService.deleteFavoriteCar(this.storageService.user.id, car.id);
      this.messageService.showSuccess('Машина убрана из списка избранных')
    }
    this.userFavoriteCars = await this.carService.getFavoriteCars(this.storageService.user.id);
  }

  onCarForeignLinkCopy(car: CarDto): void {
    const href: string = `${window.location.href}?car-id=${car.id}`;
    this.clipboardService.copyWithCustomMessage(href, 'Ссылка на машину скопирована!');
  }

  async onCloseOpenedForeignLink(): Promise<void> {
    window.location.href = window.location.href.split('?')[0];
    await this.onChange();
  }

  async onCommonSearchValue(commonSearchValue: string): Promise<void> {
    this.actionService.track(ActionTypeEnum.COMMON_SEARCH, commonSearchValue, this.storageService.user).finally();
    const carBrand: string = commonSearchValue.split(' ')[0];
    const carModel: string | undefined = !!commonSearchValue.split(' ')[1] ? commonSearchValue.split(' ')[1] : undefined;
    const carProductionYear: string | undefined
      = commonSearchValue.split(' ')[2] ? commonSearchValue.split(' ')[2].replace('(', '').replace(')', '') : undefined;
    const brandColumnQuery: ColumnQuery = {
      name: 'brand',
      filterType: FilterType.CONTAINS,
      filterValue: carBrand
    } as ColumnQuery;
    const modelColumnQuery: ColumnQuery = {
      name: 'model',
      filterType: FilterType.CONTAINS,
      filterValue: carModel
    } as ColumnQuery;
    const productionYearColumnQuery: ColumnQuery = {
      name: 'productionYear',
      filterType: FilterType.EQUALS,
      filterValue: carProductionYear
    } as ColumnQuery;
    this.paginationConfig.columnQueries = [];
    this.paginationConfig.columnQueries.push(brandColumnQuery);
    if (!!carModel) this.paginationConfig.columnQueries.push(modelColumnQuery);
    if (!!carProductionYear) this.paginationConfig.columnQueries.push(productionYearColumnQuery);
    await this.loadCurrentPage();
  }
}

