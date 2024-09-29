import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {ActionService} from "../https/action.service";

@Injectable({
  providedIn: 'root'
})
export class OneuneRouterService {

  constructor(private router: Router,
              private actionService: ActionService) {
  }

  public wrapRouting(futureRoute: string): void {
    const pastUrl: string = this.router.url;
    this.router.navigate([futureRoute]).then(async (): Promise<void> => {
      await this.actionService.track(`${pastUrl} -> ${futureRoute}`);
    });
  }
}
