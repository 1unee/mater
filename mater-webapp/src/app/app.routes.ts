import { Routes } from '@angular/router';
import {StartPageComponent} from "./components/pages/start-page/start-page.component";
import {CarMenuPageComponent} from "./components/pages/car-menu-page/car-menu-page.component";

export const routes: Routes = [
  {path: 'start', component: StartPageComponent},
  {path: 'cars', component: CarMenuPageComponent},
  {path: '', redirectTo: '/start', pathMatch: 'full'},
  {path: '**', redirectTo: '/start'},
];
