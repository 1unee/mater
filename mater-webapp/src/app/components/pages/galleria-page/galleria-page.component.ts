import {Component, Inject, OnInit} from '@angular/core';
import {GalleriaModule} from "primeng/galleria";
import {CarouselModule} from "primeng/carousel";
import {ImageModule} from "primeng/image";
import {TagModule} from "primeng/tag";
import {GLOBAL_CONFIG} from "../../../app.config";
import {GlobalConfig} from "../../../store/interfaces/global-config.interface";
import {ActivatedRoute} from "@angular/router";
import {CarService} from "../../../services/https/car.service";
import {CarDto} from "../../../store/dtos/car.dto";
import {FileDto} from "../../../store/dtos/file.dto";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {environment} from "../../../../environments/environment";
import {Button} from "primeng/button";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {SelectButtonModule} from "primeng/selectbutton";

@Component({
  selector: 'app-galleria-page',
  standalone: true,
  imports: [
    GalleriaModule,
    CarouselModule,
    ImageModule,
    TagModule,
    Button,
    LongClickDirective,
    SelectButtonModule
  ],
  templateUrl: './galleria-page.component.html',
  styleUrl: './galleria-page.component.scss'
})
export class GalleriaPageComponent implements OnInit {

  car: CarDto;

  constructor(@Inject(GLOBAL_CONFIG) public globalConfig: GlobalConfig,
              private activatedRoute: ActivatedRoute,
              private carService: CarService,
              private routerService: OneuneRouterService,
              public messageService: OneuneMessageService) {
  }

  async ngOnInit(): Promise<void> {
    await this._loadImages();
  }

  private async _loadImages(): Promise<void> {
    this.activatedRoute.queryParams.subscribe(async params => {
      const carId: number = params['car-id'];
      if (!!carId) {
        this.car = await this.carService.getById(carId);
      } else {
        this.messageService.showWarning('Открыть галерею без привязки к машине нельзя!');
        this.routerService.defaultRedirect();
      }
    });
  }

  get images(): FileDto[] {
    return !!this.car ? this.car.files.filter(file => file.type.startsWith('image')) : [];
  }

  onPreviousPage(): void {
    this.routerService.previousRedirect();
  }
}
