import {Component, OnInit, ViewChild} from '@angular/core';
import {Table, TableModule, TablePageEvent} from "primeng/table";
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
export class CarTableComponent implements OnInit {

  @ViewChild('htmlCarsTable') htmlCarsTable: Table;

  filter: boolean = false;

  pageNumber: number = 0;
  pageSize: number = 1;

  cars: CarDto[];
  selectedCar: CarDto;

  constructor(private carService: CarService,
              private oneuneMessageService: OneuneMessageService) {
  }

  async ngOnInit(): Promise<void> {
    await this._loadFirstPage();

    // this.htmlCarsTable.
  }

  async reloadTable(): Promise<void> {
    await this._loadFirstPage();
    this.oneuneMessageService.showInfoMessage('Таблица обновлена!');
  }

  protected async _loadFirstPage(): Promise<void> {
    this.cars = await this.carService.search(this.pageNumber, this.pageSize);
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

  async search(direction: 'left' | 'right'): Promise<void> {
    let futurePageNumber: number = direction === 'left' ? this.pageNumber - 1 : this.pageNumber + 1;
    futurePageNumber = futurePageNumber < 0 ? 0 : futurePageNumber;
    this.cars = await this.carService.search(futurePageNumber, this.pageSize);
  }
}
