import { Component } from '@angular/core';
import {CarListComponent} from "../../tables/car-list/car-list.component";
import {StyleClassModule} from "primeng/styleclass";

@Component({
  selector: 'app-market-car-page',
  standalone: true,
  imports: [
    CarListComponent,
    StyleClassModule
  ],
  templateUrl: './market-car-page.component.html',
  styleUrl: './market-car-page.component.scss'
})
export class MarketCarPageComponent {

}
