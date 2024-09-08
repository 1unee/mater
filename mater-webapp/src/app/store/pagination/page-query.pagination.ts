import {ColumnQuery} from "./column-query.pagination";

export class PageQuery {
  page: number;
  size: number;
  columns: ColumnQuery[];
}
