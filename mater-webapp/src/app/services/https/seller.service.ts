import { Injectable } from '@angular/core';
import {AbstractHttpService} from "../contracts/abstract-http.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {SellerDto} from "../../store/dtos/seller.dto";
import {ContactDto} from "../../store/dtos/contact.dto";
import {SaleLinkDto} from "../../store/dtos/sale-link.dto";

@Injectable({
  providedIn: 'root'
})
export class SellerService extends AbstractHttpService {

  private readonly _REST_PATH = this.getExtendedRestPath('sellers');

  constructor(private http: HttpClient) {
    super(http);
  }

  async getById(sellerId: number): Promise<SellerDto> {
    return lastValueFrom(
      this.http.get<SellerDto>(`${this._REST_PATH}/${sellerId}`)
    );
  }

  async getByCarId(carId: number): Promise<SellerDto> {
    const params: HttpParams = new HttpParams().append('carId', carId);
    return lastValueFrom(
      this.http.get<SellerDto>(`${this._REST_PATH}/by-car-id`, {params})
    );
  }

  async postContact(sellerId: number, contact: ContactDto): Promise<ContactDto> {
    return lastValueFrom(
      this.http.post<ContactDto>(`${this._REST_PATH}/${sellerId ?? null}/contacts`, contact)
    );
  }

  async putContact(sellerId: number, contact: ContactDto): Promise<ContactDto> {
    return lastValueFrom(
      this.http.put<ContactDto>(`${this._REST_PATH}/${sellerId}/contacts/${contact.id}`, contact)
    );
  }

  async deleteContact(sellerId: number, contactId: number): Promise<ContactDto> {
    return lastValueFrom(
      this.http.delete<ContactDto>(`${this._REST_PATH}/${sellerId}/contacts/${contactId}`)
    );
  }

  async getContactsBySellerId(sellerId: number): Promise<ContactDto[]> {
    return lastValueFrom(
      this.http.get<ContactDto[]>(`${this._REST_PATH}/${sellerId}/contacts`)
    );
  }

  async postSaleLink(buyerId: number, carId: number): Promise<void> {
    const params: HttpParams = new HttpParams()
      .append('buyer-id', buyerId)
      .append('car-id', carId);
    return lastValueFrom(
      this.http.post<void>(`${this._REST_PATH}/sales`, {}, { params: params })
    );
  }

  async getSaleLinksByBuyerId(buyerId: number): Promise<SaleLinkDto[]> {
    const params: HttpParams = new HttpParams()
      .append('buyer-id', buyerId);
    return lastValueFrom(
      this.http.get<SaleLinkDto[]>(`${this._REST_PATH}/sales`, { params: params })
    )
  }

  async putSaleLink(saleLink: SaleLinkDto): Promise<void> {
    return lastValueFrom(
      this.http.put<void>(`${this._REST_PATH}/sales/${saleLink.id}`, saleLink)
    );
  }
}
