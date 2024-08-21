import { Component } from '@angular/core';
import {CarTableComponent} from "../../tables/car-table/car-table.component";

@Component({
  selector: 'app-car-menu-page',
  standalone: true,
  imports: [
    CarTableComponent
  ],
  templateUrl: './car-menu-page.component.html',
  styleUrl: './car-menu-page.component.scss'
})
export class CarMenuPageComponent {

}
