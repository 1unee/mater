import {Component, Inject, Input, OnDestroy, OnInit} from '@angular/core';
import {NgIf, NgStyle} from "@angular/common";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {Subscription} from "rxjs";
import {StyleClassModule} from "primeng/styleclass";

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [
    NgIf,
    StyleClassModule,
    NgStyle
  ],
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.scss'
})
export class LoaderComponent implements OnInit, OnDestroy {

  @Input() icon: string = 'pi-spinner';
  @Input() rotateIcon: boolean = true;

  private _loadingSubscription: Subscription;
  block: boolean = false;

  constructor(@Inject(LOADING) public loading: LoadingReference) {
  }

  ngOnInit(): void {
    this._loadingSubscription = this.loading.value.subscribe((value: boolean): void => {
      this.block = value;
    });
  }

  ngOnDestroy(): void {
    if (this._loadingSubscription) {
      this._loadingSubscription.unsubscribe();
    }
  }
}
