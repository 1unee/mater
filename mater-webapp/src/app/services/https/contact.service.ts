import { Injectable } from '@angular/core';
import {ContactDto} from "../../store/dtos/contact.dto";
import {lastValueFrom} from "rxjs";
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ContactService extends AbstractHttpService {

  private readonly _REST_PATH: string = this.getExtendedRestPath('contacts');

  constructor(private http: HttpClient) {
    super(http);
  }

  async put(contact: ContactDto): Promise<ContactDto> {
    return lastValueFrom(
      this.http.put<ContactDto>(`${this._REST_PATH}/${contact.id}`, contact)
    );
  }
}
