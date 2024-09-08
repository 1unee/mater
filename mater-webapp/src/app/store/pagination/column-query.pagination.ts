import {SortDirection} from "./sort-direction.pagination";
import {FilterType} from "./filter-type.pagination";

export class ColumnQuery {
  name: string;
  sortDirection: SortDirection;
  filterType: FilterType;
  filterValue: any;
}
