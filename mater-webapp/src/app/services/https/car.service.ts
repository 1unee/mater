import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {CarDto} from "../../store/dtos/car.dto";
import {PhotoDto} from "../../store/dtos/photo.dto";
import {VideoDto} from "../../store/dtos/video.dto";
import {VideoPartDto} from "../../store/dtos/video-part.dto";
import {LargeFileService} from "../utils/large-file.service";
import {PageResponse} from "../../store/pagination/page.response.pagination";
import {PageQuery} from "../../store/pagination/page-query.pagination";
import {CarCategorySortEnum} from "../../store/enums/car-category-sort.enum";

@Injectable({
  providedIn: 'root'
})
export class CarService extends AbstractHttpService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('cars');

  constructor(private http: HttpClient,
              private largeFileService: LargeFileService) {
    super(http);
  }

  async post(sellerId: number, car: CarDto): Promise<CarDto> {
    const httpParams: HttpParams = new HttpParams().append('seller-id', sellerId)
    return lastValueFrom(
      this.http.post<CarDto>(`${this._REST_PATH}/by-params`, car, {params: httpParams})
    );
  }

  async search(pageQuery: PageQuery): Promise<PageResponse<CarDto>> {
    return lastValueFrom(
      this.http.post<PageResponse<CarDto>>(`${this._REST_PATH}/search`, pageQuery)
    );
  }

  async delete(carId: number): Promise<void> {
    return lastValueFrom(
      this.http.delete<void>(`${this._REST_PATH}/${carId}`)
    );
  }

  async put(car: CarDto): Promise<CarDto> {
    return lastValueFrom(
      this.http.put<CarDto>(`${this._REST_PATH}/${car.id}`, car)
    );
  }

  async putPhotos(carId: number, photos: PhotoDto[]): Promise<PhotoDto[]> {
    return lastValueFrom(
      this.http.put<PhotoDto[]>(`${this._REST_PATH}/${carId}/photos`, photos)
    );
  }

  async deleteVideos(carId: number): Promise<void> {
    return lastValueFrom(
      this.http.delete<void>(`${this._REST_PATH}/${carId}/videos`)
    );
  }

  async putVideos(carId: number, videos: VideoDto[]): Promise<void> {
    await this.deleteVideos(carId);
    const videoParts: VideoPartDto[] = this.largeFileService.cutVideos(videos);
    for (const videoPart of videoParts) {
      await lastValueFrom(
        this.http.put<void>(`${this._REST_PATH}/${carId}/videos`, videoPart)
      );
    }
  }
}
