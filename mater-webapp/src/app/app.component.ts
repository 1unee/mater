import {Component, NgZone} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {TestService} from "./services/test.service";
import {ButtonDirective} from "primeng/button";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ButtonDirective],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  public event: string = 'not inited';
  public field: string = 'not touched';

  constructor(private testService: TestService,
              private ngZone: NgZone) {
  }

  async test(from: string): Promise<void> {
    this.zone(from);
    this.field = `${new Date().toLocaleTimeString()} (from ${from})`;
    try {
      await this.testService.test();
    } catch (e) {
      this.event = JSON.stringify(e);
    }
  }

  private zone(from: string): void {
    this.ngZone.run((): void => { this.event = from; });
  }
}
