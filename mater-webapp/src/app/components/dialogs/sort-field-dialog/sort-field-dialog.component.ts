import {Component, EventEmitter, Output} from '@angular/core';
import {Button} from "primeng/button";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-sort-field-dialog',
  standalone: true,
    imports: [
        Button,
        InputGroupAddonModule,
        InputGroupModule,
        InputTextModule,
        ReactiveFormsModule
    ],
  templateUrl: './sort-field-dialog.component.html',
  styleUrl: './sort-field-dialog.component.scss'
})
export class SortFieldDialogComponent {

  @Output() onSortOrFilterChange = new EventEmitter();

}
