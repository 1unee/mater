import { Routes } from '@angular/router';
import {StartPageComponent} from "./components/start-page/start-page.component";

export const routes: Routes = [
  {path: 'start', component: StartPageComponent},
  {path: '', redirectTo: '/main', pathMatch: 'full'},
  {path: '**', redirectTo: '/main'},
];
