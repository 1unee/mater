import {Component, OnInit} from '@angular/core';
import {TableModule} from "primeng/table";
import {CarDto} from "../../../store/dtos/car.dto";
import {NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {TooltipModule} from "primeng/tooltip";
import {CarService} from "../../../services/car.service";
import {OneuneMessageService} from "../../../services/oneune-message.service";

@Component({
  selector: 'app-car-table',
  standalone: true,
  imports: [
    TableModule,
    NgIf,
    ButtonDirective,
    Ripple,
    TooltipModule
  ],
  templateUrl: './car-table.component.html',
  styleUrl: './car-table.component.scss'
})
export class CarTableComponent implements OnInit{

  cars: CarDto[];
  selectedCar: CarDto;
  filter: boolean = false;

  constructor(private carService: CarService,
              private oneuneMessageService: OneuneMessageService) {
  }

  async ngOnInit(): Promise<void> {
    await this._load();
  }

  private async _load(): Promise<void> {
    this.cars = await this.carService.search();
  }

  async reloadTable(): Promise<void> {
    await this._load()
    this.oneuneMessageService.showInfoMessage('Таблица обновлена!');
  }

  add(from: boolean): void {
    this.oneuneMessageService.showInfoMessage('В разработке...');
  }

  openDetail(): void {
    this.oneuneMessageService.showInfoMessage('В разработке...');
  }

  edit(): void {
    this.oneuneMessageService.showInfoMessage('В разработке...');
  }

  remove(): void {
    this.oneuneMessageService.showInfoMessage('В разработке...');
  }
}
