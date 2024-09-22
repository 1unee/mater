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
import {getFileTypeTitle} from "../../../store/enums/file-type.enum";
import {TagModule} from "primeng/tag";
import {ConfirmationService} from "primeng/api";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {FileDto} from "../../../store/dtos/file.dto";

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

  readonly FILE_SIZE_PRECISION: number = 0;
  readonly FILE_MAX_SIZE: number = 500 * 1024 * 1024; // in bytes (100 mb)
  readonly getFileTypeTitle = getFileTypeTitle;

  @ViewChild('htmlFileInput') htmlFileInput: HTMLInputElement;

  car: CarDto;
  files: File[] = [];

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              public clipboardService: ClipboardService,
              public messageService: OneuneMessageService,
              private confirmationService: ConfirmationService,
              @Inject(LOADING) public loading: LoadingReference,
              private carService: CarService) {
  }

  ngOnInit(): void {
    this.initialize();
  }

  private initialize(): void {
    this.car = this.dynamicDialogConfig.data.car;
    this.files = this.car.files.map((file: FileDto): File => {
      return {
        name: file.name,
        size: file.size,
        type: file.type
      } as File;
    });
  }

  private _isFilesEqual(leftFile: File, rightFile: File): boolean {
    return leftFile.name === rightFile.name
      && leftFile.size === rightFile.size
      && leftFile.type === rightFile.type;
  }

  onChoose(event: any): void {
    for (const checkingFile of event.target.files) {
      const isFileUploaded = this.files.some(uploadedFile => this._isFilesEqual(checkingFile, uploadedFile));
      if (!isFileUploaded) {
        if (checkingFile.size <= this.FILE_MAX_SIZE) {
          this.files.push(checkingFile);
        } else {
          this.messageService.showInfo(
            `К сожалению, размер файла «${checkingFile.name}» превышает допустимую норму ${this.FILE_MAX_SIZE}`
          );
        }
      }
    }
  }

  private _getMultipartFiles(): FormData {
    const formData: FormData = new FormData();
    this.files.forEach((file: File): void => { formData.append('files', file); });
    return formData;
  }

  async onSave(): Promise<void> {
    try {
      this.loading.value.next(true);
      this.carService.putFiles(this.car.id, this._getMultipartFiles()).then(() => this.messageService.showInfo('Файлы успешно сохранены на сервер!'));
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
    this.files = this.files.filter(uploadedFile => !this._isFilesEqual(removingFile, uploadedFile));
  }

  getFormatedSize(sizeInBytes: number): string {
    if (sizeInBytes === 0) return '0 Б';
    const k: number = 1024;
    const sizes = ['Байт', 'КБ', 'МБ', 'ГБ', 'ТБ'];
    const i = Math.floor(Math.log(sizeInBytes) / Math.log(k));
    const formatedSize: number = sizeInBytes / Math.pow(k, i);
    return `${parseFloat(formatedSize.toFixed(this.FILE_SIZE_PRECISION))} ${sizes[i]}`;
  }

  private _confirmCleaning(): void {
    this.confirmationService.confirm({
      message: 'Ты уверен, что хочешь убрать выбранные файлы? Потом придется выбирать их заново',
      header: 'Подтверждение',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Да',
      accept: () => this.files = [],
      rejectLabel: 'Нет',
      reject: () => {}
    });
  }
}

