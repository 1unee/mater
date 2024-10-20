import {Component, Inject, OnInit} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {ToastModule} from "primeng/toast";
import {Message, PrimeNGConfig} from "primeng/api";
import {OneuneMessageService} from "./services/utils/oneune-message.service";
import {NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {TooltipModule} from "primeng/tooltip";
import {GeneralToolbarComponent} from "./components/core/general-toolbar/general-toolbar.component";
import {BlockUIModule} from "primeng/blockui";
import {LOADING} from "./app.config";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {StyleClassModule} from "primeng/styleclass";
import {LoadingReference} from "./store/interfaces/loading-reference.interface";
import {LoaderComponent} from "./components/core/loader/loader.component";
import {ThemeService} from "./services/utils/theme.service";
import {LongClickDirective} from "./services/directives/long-click.directive";
import {StorageService} from "./services/utils/storage.service";
import {SelectButtonModule} from "primeng/selectbutton";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    ToastModule,
    NgIf,
    ButtonDirective,
    Ripple,
    TooltipModule,
    GeneralToolbarComponent,
    BlockUIModule,
    ConfirmDialogModule,
    StyleClassModule,
    LoaderComponent,
    LongClickDirective,
    SelectButtonModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  messages: Message[] = [];

  constructor(public messageService: OneuneMessageService,
              @Inject(LOADING) public loading: LoadingReference,
              private primengConfig: PrimeNGConfig,
              private themeService: ThemeService,
              private storageService: StorageService) {
    this._subscribeOnCreatingMessages();
  }

  ngOnInit(): void {
    this._localize();
    this.themeService.setSystemMode();
  }

  private _localize(): void {
    this.primengConfig.setTranslation({
      accept: 'Да',
      reject: 'Нет',
      dayNames: ["Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"],
      dayNamesShort: ["Вск", "Пнд", "Втр", "Срд", "Чтв", "Птн", "Суб"],
      dayNamesMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
      monthNames: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
      monthNamesShort: ["Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"],
      today: 'Сегодня',
      clear: 'Очистить',
      dateFormat: 'dd.mm.yy',
      weekHeader: 'Нед',
      firstDayOfWeek: 1
    });
  }

  private _subscribeOnCreatingMessages(): void {
    this.messageService.creatingObserver.subscribe((messages: Message | Message[]): void => {
      if (messages instanceof Array) {
        this.messages.push(...messages);
      } else {
        this.messages.push(messages);
      }
      this._removeAsyncAfter(this.messages);
    });
  }

  private _removeAsyncAfter(messages: Message[]): void {
    const lifeDurations: number[] = messages.map((message: Message): number => message.life ?? Number.MAX_SAFE_INTEGER);
    const maxLifeDuration: number = Math.max(...lifeDurations);
    setTimeout((): void => {
      this.clearMessages();
    }, Number(this.storageService.getConfigByCode(2).value) * 2.5);
  }

  clearMessages(): void {
    this.messageService.clearScreen();
    this.messages = [];
  }

    protected readonly JSON = JSON;
}
