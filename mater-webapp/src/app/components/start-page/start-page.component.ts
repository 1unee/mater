import {Component, OnDestroy, OnInit} from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {AccordionModule} from "primeng/accordion";
import {FieldsetModule} from "primeng/fieldset";
import {ImageModule} from "primeng/image";
import {NgOptimizedImage} from "@angular/common";
import {RoleEnum} from "../../store/enums/role.enum";
import {TelegramService} from "../../services/telegram.service";
import {WebAppDataEnum} from "../../store/enums/web-app-data.enum";

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [
    ButtonDirective,
    AccordionModule,
    FieldsetModule,
    ImageModule,
    NgOptimizedImage
  ],
  templateUrl: './start-page.component.html',
  styleUrl: './start-page.component.scss'
})
export class StartPageComponent implements OnInit, OnDestroy {

  selectedRole: RoleEnum;

  constructor(private telegramService: TelegramService) {
  }

  readonly RoleEnum = RoleEnum;

  ngOnInit(): void {
    this.telegramService.mainBtn.setText('Продолжить');
    this.telegramService.mainBtn.showProgress(true);
    this.telegramService.mainBtn.onClick(() => {
      this.telegramService.sendData(WebAppDataEnum.REGISTER_USER, this.selectedRole);
    });
  }

  ngOnDestroy(): void {
    this.telegramService.mainBtn.offClick(() => {
      this.telegramService.sendData(WebAppDataEnum.REGISTER_USER, this.selectedRole);
    });
  }

  get thirdTabHeader(): string {
    return !!this.selectedRole ? 'Ты - ' + this.selectedRole.toLowerCase() : 'Сначала определись с ролью ниже';
  }

  setUserRole(role: RoleEnum): void {
    this.selectedRole = role;
    this.telegramService.mainBtn.show();
  }
}
