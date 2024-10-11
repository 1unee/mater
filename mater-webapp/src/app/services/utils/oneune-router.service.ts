import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {ActionService} from "../https/action.service";
import {ActionTypeEnum} from "../../store/enums/action-type.enum";
import {defaultRoute} from "../../app.routes";
import {Location} from "@angular/common";

@Injectable({
  providedIn: 'root'
})
export class OneuneRouterService {

  constructor(private router: Router,
              private actionService: ActionService,
              private location: Location) {
  }

  private async _trackRedirect(currentUrl: string, futureUrl: string): Promise<void> {
    await this.actionService.track(ActionTypeEnum.REDIRECT, `${currentUrl} -> ${futureUrl}`);
  }

  public relativeRedirect(futureRoute: string, queryParams?: { [key: string]: any }): void {
    this.router.navigate([futureRoute], { queryParams: queryParams })
      .then(async (): Promise<void> => { await this._trackRedirect(this.router.url, futureRoute); });
  }

  public absoluteRedirect(url: string): void {
    window.location.href = url;
    this._trackRedirect(this.router.url, url).finally((): void => {});
  }

  public defaultRedirect(): void {
    this.router.navigate([defaultRoute.path!])
      .then(async (): Promise<void> => { await this._trackRedirect(this.router.url, defaultRoute.path!); });
  }

  public previousRedirect(): void {
    this.location.back();
  }
}
