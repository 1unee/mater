import {Component, Input} from '@angular/core';
import {CardModule} from "primeng/card";
import {RatingModule} from "primeng/rating";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-user-rating',
  standalone: true,
  imports: [
    CardModule,
    RatingModule,
    FormsModule
  ],
  templateUrl: './user-rating.component.html',
  styleUrl: './user-rating.component.scss'
})
export class UserRatingComponent {

  @Input() score: number;
}
