import { Routes } from '@angular/router';
import {MarketCarPageComponent} from "./components/pages/market-car-page/market-car-page.component";
import {ProfilePageComponent} from "./components/pages/profile-page/profile-page.component";
import {ActionsPageComponent} from "./components/pages/actions-page/actions-page.component";
import {SupportPageComponent} from "./components/pages/support-page/support-page.component";
import {SettingsPageComponent} from "./components/pages/settings-page/settings-page.component";
import {authGuard} from "./services/guards/auth.guard";
import {AdministratingPageComponent} from "./components/pages/administrating-page/administrating-page.component";

export const routes: Routes = [
  { path: 'profile', component: ProfilePageComponent, canActivate: [authGuard] },
  { path: 'cars/market', component: MarketCarPageComponent, canActivate: [authGuard] },
  { path: 'actions', component: ActionsPageComponent, canActivate: [authGuard] },
  { path: 'settings', component: SettingsPageComponent, canActivate: [authGuard] },
  { path: 'support', component: SupportPageComponent },
  { path: 'administrating', component: AdministratingPageComponent },
  { path: '', redirectTo: '/support', pathMatch: 'full' },
  { path: '**', redirectTo: '/support' },
];
