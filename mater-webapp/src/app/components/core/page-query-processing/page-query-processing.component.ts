import {Component, EventEmitter, Output} from '@angular/core';
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {SortDirection, SortDirectionTitle} from "../../../store/pagination/sort-direction.pagination";
import {FilterType, FilterTypeTitle} from "../../../store/pagination/filter-type.pagination";
import {InputTextModule} from "primeng/inputtext";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ColumnQuery} from "../../../store/pagination/column-query.pagination";
import {LongClickDirective} from "../../../services/directives/long-click.directive";

@Component({
  selector: 'app-page-query-processing',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    Button,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    LongClickDirective
  ],
  templateUrl: './page-query-processing.component.html',
  styleUrl: './page-query-processing.component.scss'
})
export class PageQueryProcessingComponent {

  readonly STRING_FILTER_TYPE_KEY = 'string';
  readonly NUMBER_FILTER_TYPE_KEY = 'number';

  @Output() onApply: EventEmitter<ColumnQuery[]> = new EventEmitter<ColumnQuery[]>();
  @Output() onCommonSearchValue: EventEmitter<string> = new EventEmitter<string>();

  commonSearchValue: string;

  filterTypeMap: Map<string, Array<any>> = new Map([
    [this.STRING_FILTER_TYPE_KEY, [
      { label: FilterTypeTitle.STARTS_WITH, value: FilterType.STARTS_WITH },
      { label: FilterTypeTitle.ENDS_WITH, value: FilterType.ENDS_WITH },
      { label: FilterTypeTitle.CONTAINS, value: FilterType.CONTAINS }
    ]],
    [this.NUMBER_FILTER_TYPE_KEY, [
      { label: FilterTypeTitle.EQUALS, value: FilterType.EQUALS },
      { label: FilterTypeTitle.GREATER_THAN, value: FilterType.GREATER_THAN },
      { label: FilterTypeTitle.LESS_THAN, value: FilterType.LESS_THAN },
    ]]
  ]);

  pageQueryProcessingConfig = {
    fields: [
      { label: 'Производитель', value: 'brand' },
      { label: 'Модель', value: 'model' },
      { label: 'Год выпуска', value: 'productionYear' },
      { label: 'Цена', value: 'price' },
      { label: 'Пробег', value: 'mileage' },
      { label: 'ВИН', value: 'VIN' },
      { label: 'Количество владельцев', value: 'ownersAmount' }
    ],
    selectedField: null,
    filters: new Map([
      ['brand', this.filterTypeMap.get(this.STRING_FILTER_TYPE_KEY)],
      ['model', this.filterTypeMap.get(this.STRING_FILTER_TYPE_KEY)],
      ['productionYear', this.filterTypeMap.get(this.NUMBER_FILTER_TYPE_KEY)],
      ['price', this.filterTypeMap.get(this.NUMBER_FILTER_TYPE_KEY)],
      ['mileage', this.filterTypeMap.get(this.NUMBER_FILTER_TYPE_KEY)],
      ['VIN', this.filterTypeMap.get(this.STRING_FILTER_TYPE_KEY)],
      ['ownersAmount', this.filterTypeMap.get(this.NUMBER_FILTER_TYPE_KEY)],
    ]),
    selectedFilter: null,
    filterValue: null,
    sorts: [
      { label: SortDirectionTitle.ASCENDING, value: SortDirection.ASCENDING },
      { label: SortDirectionTitle.DESCENDING, value: SortDirection.DESCENDING },
    ],
    selectedSort: null,
    columnQueries: [] as ColumnQuery[]
  };

  constructor(public messageService: OneuneMessageService) {
  }

  private _findProcessingColumnQuery(): ColumnQuery {
    return this.pageQueryProcessingConfig.columnQueries
      .find(columnQuery => columnQuery.name === this.pageQueryProcessingConfig.selectedField)!;
  }

  private _clearInputs(): void {
    this.pageQueryProcessingConfig.selectedFilter = null;
    this.pageQueryProcessingConfig.filterValue = null;
    this.pageQueryProcessingConfig.selectedSort = null;
  }

  onFieldChange(field: string): void {
    this._clearInputs();
    if (!this._findProcessingColumnQuery()) {
      this.pageQueryProcessingConfig.columnQueries.push({
        name: field
      } as ColumnQuery);
    }
  }

  onFilterChange(filter: FilterType): void {
    const currentColumnQuery: ColumnQuery = this._findProcessingColumnQuery();
    currentColumnQuery.filterType = filter;
  }

  onFilterValueChange(filterValue: any): void {
    const currentColumnQuery: ColumnQuery = this._findProcessingColumnQuery();
    currentColumnQuery.filterValue = filterValue;
  }

  onSortChange(sort: SortDirection): void {
    const currentColumnQuery: ColumnQuery = this._findProcessingColumnQuery();
    currentColumnQuery.sortDirection = sort;
  }

  apply(): void {
    this.pageQueryProcessingConfig.columnQueries.forEach(columnQuery => {
      if (!columnQuery.sortDirection) {
        columnQuery.sortDirection = SortDirection.ASCENDING;
      }
    });
    this.onApply.emit(this.pageQueryProcessingConfig.columnQueries);
  }

  clear(): void {
    this._clearInputs();
    this.pageQueryProcessingConfig.selectedField = null;
    this.pageQueryProcessingConfig.selectedFilter = null;
    this.pageQueryProcessingConfig.filterValue = null;
    this.pageQueryProcessingConfig.selectedSort = null;
    this.pageQueryProcessingConfig.columnQueries = [];
    this.onApply.emit([]);
  }

  onCommonSearchChange(commonSearchValue: string): void {
    this.onCommonSearchValue.emit(commonSearchValue);
  }
}
