import { Injectable } from '@angular/core';
import {AbstractService} from "./abstract.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {CarDto} from "../store/dtos/car.dto";

@Injectable({
  providedIn: 'root'
})
export class CarService extends AbstractService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('cars');

  constructor(private http: HttpClient) {
    super(http);
  }

  async search(page: number, size: number): Promise<CarDto[]> {
    const params: HttpParams = new HttpParams()
      .append('page', page)
      .append('size', size);
    return lastValueFrom(
      this.http.get<CarDto[]>(`${this._REST_PATH}/search`, {params})
    );
  }

}
