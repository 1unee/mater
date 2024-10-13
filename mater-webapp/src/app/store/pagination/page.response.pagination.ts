import {AbstractDto} from "../dtos/core/abstract.dto";

export class PageResponse<D extends AbstractDto> {
  totalElements: number;
  totalPages: number;
  content: D[];
}
