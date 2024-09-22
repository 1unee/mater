import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {FileDto} from "../../store/dtos/file.dto";

@Injectable({
  providedIn: 'root'
})
export class FileService extends AbstractHttpService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('selectel-s3/objects');

  constructor(private http: HttpClient) {
    super(http);
  }

  async post(formData: FormData): Promise<FileDto[]> {
    return lastValueFrom(
      this.http.post<FileDto[]>(`${this._REST_PATH}/upload`, formData)
    );
  }
}
