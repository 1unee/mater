import {Route, Routes} from '@angular/router';
import {MarketCarPageComponent} from "./components/pages/market-car-page/market-car-page.component";
import {ProfilePageComponent} from "./components/pages/profile-page/profile-page.component";
import {ActionsPageComponent} from "./components/pages/actions-page/actions-page.component";
import {SupportPageComponent} from "./components/pages/support-page/support-page.component";
import {SettingsPageComponent} from "./components/pages/settings-page/settings-page.component";
import {authGuard} from "./services/guards/auth.guard";
import {AdministratingPageComponent} from "./components/pages/administrating-page/administrating-page.component";
import {GalleriaPageComponent} from "./components/pages/galleria-page/galleria-page.component";

export const defaultRoute: Route = { path: 'support', component: SupportPageComponent };

export const routes: Routes = [
  { path: 'profile', component: ProfilePageComponent, canActivate: [authGuard] },
  { path: 'cars/market', component: MarketCarPageComponent, canActivate: [authGuard] },
  { path: 'actions', component: ActionsPageComponent, canActivate: [authGuard] },
  { path: 'settings', component: SettingsPageComponent, canActivate: [authGuard] },
  defaultRoute,
  { path: 'administrating', component: AdministratingPageComponent, canActivate: [authGuard] },
  { path: 'galleria', component: GalleriaPageComponent, canActivate: [authGuard] },
  { path: '', redirectTo: defaultRoute.path, pathMatch: 'full' },
  { path: '**', redirectTo: defaultRoute.path },
];
