import {Route, Routes} from '@angular/router';
import {MarketCarPageComponent} from "./components/pages/market-car-page/market-car-page.component";
import {ProfilePageComponent} from "./components/pages/profile-page/profile-page.component";
import {ActionsPageComponent} from "./components/pages/actions-page/actions-page.component";
import {SupportPageComponent} from "./components/pages/support-page/support-page.component";
import {SettingsPageComponent} from "./components/pages/settings-page/settings-page.component";
import {AdministratingPageComponent} from "./components/pages/administrating-page/administrating-page.component";
import {GalleriaPageComponent} from "./components/pages/galleria-page/galleria-page.component";
import {adminGuard} from "./services/guards/admin.guard";
import {authGuard} from "./services/guards/auth.guard";
import {LoginPageComponent} from "./components/pages/login-page/login-page.component";
import {RegistrationPageComponent} from "./components/pages/registration-page/registration-page.component";

export const defaultRoute: Route = { path: 'login', component: LoginPageComponent };

export const routes: Routes = [
  defaultRoute,
  { path: 'registration', component: RegistrationPageComponent },
  { path: 'profile', component: ProfilePageComponent, canActivate: [authGuard] },
  { path: 'cars/market', component: MarketCarPageComponent, canActivate: [authGuard] },
  { path: 'actions', component: ActionsPageComponent, canActivate: [authGuard] },
  { path: 'settings', component: SettingsPageComponent, canActivate: [authGuard] },
  { path: 'support', component: SupportPageComponent, canActivate: [authGuard] },
  { path: 'administrating', component: AdministratingPageComponent, canActivate: [authGuard, adminGuard] },
  { path: 'galleria', component: GalleriaPageComponent, canActivate: [authGuard] },
  { path: '', redirectTo: defaultRoute.path, pathMatch: 'full' },
  { path: '**', redirectTo: defaultRoute.path },
];
