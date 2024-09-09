import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Button} from "primeng/button";
import {NgForOf, NgIf} from "@angular/common";
import {CarDto} from "../../../store/dtos/car.dto";
import {DynamicDialogConfig} from "primeng/dynamicdialog";
import {ClipboardService} from "../../../services/utils/clipboard.service";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {CarService} from "../../../services/https/car.service";
import {SelectButtonModule} from "primeng/selectbutton";
import {FormsModule} from "@angular/forms";
import {FileTypeEnum, FileTypeTitle} from "../../../store/enums/file-type.enum";
import {TagModule} from "primeng/tag";
import {ConfirmationService} from "primeng/api";
import {base64StringToBlob, blobToBase64String} from "blob-util";
import {LOADING} from "../../../app.config";
import {SelectButtonState} from "../../../store/interfaces/select-button-state.interface";
import {FileDto} from "../../../store/dtos/file.dto";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-oneune-file-upload',
  standalone: true,
  imports: [
    Button,
    NgIf,
    NgForOf,
    SelectButtonModule,
    FormsModule,
    TagModule
  ],
  templateUrl: './oneune-file-upload.component.html',
  styleUrl: './oneune-file-upload.component.scss'
})
export class OneuneFileUploadComponent implements OnInit {

  readonly FileTypeEnum = FileTypeEnum;
  readonly FILE_SIZE_PRECISION: number = 1;
  readonly PHOTO_MAX_SIZE: number = 5 * 1024 * 1024; // in bytes
  readonly VIDEO_MAX_SIZE: number = (environment.production ? 35 : 500) * 1024 * 1024; // in bytes

  @ViewChild('htmlFileInput') htmlFileInput: HTMLInputElement;

  car: CarDto;

  selectButtonConfig: {states: SelectButtonState[], selectedValue: FileTypeEnum} = {
    states: [
      { label: FileTypeTitle.PHOTO, value: FileTypeEnum.PHOTO, styleClass: "p-1 p-button-secondary", accept: 'image/*', files: [], disabled: false},
      { label: FileTypeTitle.VIDEO, value: FileTypeEnum.VIDEO, styleClass: "p-1 p-button-secondary", accept: 'video/*', files: [], disabled: environment.production}
    ],
    selectedValue: FileTypeEnum.PHOTO
  }

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              public clipboardService: ClipboardService,
              public messageService: OneuneMessageService,
              private carService: CarService,
              private confirmationService: ConfirmationService,
              @Inject(LOADING) public loading: LoadingReference) {}

  ngOnInit(): void {
    this.car = this.dynamicDialogConfig.data.car;
    this.getSelectedState(FileTypeEnum.PHOTO).files = this.car.photos
      .map(photo => new File([base64StringToBlob(photo.base64)], photo.name, {type: photo.type}));
    this.getSelectedState(FileTypeEnum.VIDEO).files = this.car.videos
      .map(video => new File([base64StringToBlob(video.base64)], video.name, {type: video.type}));
    if (environment.production) {
      this.messageService.showInfo('Работа с видеофайлами в разработке!');
    }
  }

  get selectedState(): SelectButtonState {
    return this.selectButtonConfig.states.find(state => state.value === this.selectButtonConfig.selectedValue)!;
  }

  getSelectedState(fileType: FileTypeEnum): SelectButtonState {
    return this.selectButtonConfig.states.find(state => state.value === fileType)!;
  }

  isFilesEqual(leftFile: File, rightFile: File): boolean {
    return leftFile.name === rightFile.name
      && leftFile.size === rightFile.size
      && leftFile.type === rightFile.type;
  }

  onChoose(event: any) {
    const selectedState: SelectButtonState = this.selectedState;
    for (const checkingFile of event.target.files) {
      const isFileUploaded = selectedState.files.some(uploadedFile => this.isFilesEqual(checkingFile, uploadedFile));
      if (!isFileUploaded) {
        if ((selectedState.value === FileTypeEnum.PHOTO && checkingFile.size <= this.PHOTO_MAX_SIZE)
          || (selectedState.value === FileTypeEnum.VIDEO && checkingFile.size <= this.VIDEO_MAX_SIZE)) {
          selectedState.files.push(checkingFile);
        } else {
          this.messageService.showInfo(`К сожалению, размер файла «${checkingFile.name}»
          превышает допустимую норму ${selectedState.value === FileTypeEnum.PHOTO ? this.getFormatedSize(this.PHOTO_MAX_SIZE) : this.getFormatedSize(this.VIDEO_MAX_SIZE)}`);
        }
      }
    }
  }

  async onSave(): Promise<void> {
    try {
      this.loading.value.next(true);
      this.messageService.showInfo('Сохранение файлов или их изменение на сервере началось, подожди чуть-чуть...');
      switch (this.selectButtonConfig.selectedValue) {
        case FileTypeEnum.PHOTO: {
          this.car.photos = await this._getMultimediaFiles();
          this.car.photos = await this.carService.putPhotos(this.car.id, this.car.photos);
          break;
        }
        case FileTypeEnum.VIDEO: {
          this.car.videos = await this._getMultimediaFiles();
          console.log(this.car.videos);
          await this.carService.putVideos(this.car.id, this.car.videos);
          break;
        }
      }
      this.messageService.showSuccess('Файлы успешно отправлены на сервер. ' +
        'Если ты загрузил слишком большие файлы, то придется немного подождать (примерно пару минут), пока сервер их обработает.');
    } catch (e) {
      this.messageService.showError('Произошла ошибка при сохранении файлов...');
    } finally {
      this.loading.value.next(false);
    }
  }

  onClean(): void {
    this._confirmCleaning();
  }

  removeFile(removingFile: any): void {
    const selectedState: SelectButtonState = this.selectedState;
    selectedState.files = selectedState.files.filter(uploadedFile => !this.isFilesEqual(removingFile, uploadedFile));
  }

  getFormatedSize(sizeInBytes: number): string {
    if (sizeInBytes === 0) return '0 Байт';
    const k: number = 1024;
    const sizes = ['Байт', 'КБ', 'МБ', 'ГБ', 'ТБ'];
    const i = Math.floor(Math.log(sizeInBytes) / Math.log(k));
    const formatedSize: number = sizeInBytes / Math.pow(k, i);
    return `${parseFloat(formatedSize.toFixed(this.FILE_SIZE_PRECISION))} ${sizes[i]}`;
  }

  private async _getMultimediaFiles(): Promise<FileDto[]> {
    const fileDtos: FileDto[] = [];
    for (const file of this.selectedState.files) {
      const fileDto: FileDto = new FileDto();
      fileDto.name = file.name;
      fileDto.type = file.type;
      fileDto.base64 = await blobToBase64String(new Blob([file], { type: file.type }));
      fileDtos.push(fileDto);
    }
    return fileDtos;
  }

  private _confirmCleaning(): void {
    this.confirmationService.confirm({
      message: 'Ты уверен, что хочешь убрать выбранные файлы? Потом придется выбирать их заново',
      header: 'Подтверждение',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Да',
      accept: () => this.selectedState.files = [],
      rejectLabel: 'Нет',
      reject: () => {}
    });
  }
}

