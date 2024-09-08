import {Component, Input} from '@angular/core';
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";

@Component({
  selector: 'app-user-search-history',
  standalone: true,
  imports: [
    TableModule,
    CardModule
  ],
  templateUrl: './user-search-history.component.html',
  styleUrl: './user-search-history.component.scss'
})
export class UserSearchHistoryComponent {

  @Input() searchHistory: any[];
}
