import {AbstractDto} from "../dtos/abstract.dto";

export class PageResponse<D extends AbstractDto> {
  totalElements: number;
  totalPages: number;
  content: D[];
}
