package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.services.ContactService;
import com.oneune.mater.rest.main.services.SellerService;
import com.oneune.mater.rest.main.store.dtos.ContactDto;
import com.oneune.mater.rest.main.store.dtos.SaleLinkDto;
import com.oneune.mater.rest.main.store.enums.SaleStatusEnum;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.SellerDto;
import com.oneune.mater.rest.main.store.entities.SellerEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sellers")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SellerController implements CRUDable<SellerDto, SellerEntity> {

    SellerService sellerService;
    ContactService contactService;

    @PostMapping
    @Override
    public SellerDto post(@RequestBody SellerDto seller) {
        return sellerService.post(seller);
    }

    @PutMapping("{id}")
    @Override
    public SellerDto put(@PathVariable(name = "id") Long sellerId,
                         @RequestBody SellerDto seller) {
        return sellerService.put(sellerId, seller);
    }

    @DeleteMapping("{id}")
    @Override
    public SellerDto deleteById(@PathVariable(name = "id") Long sellerId) {
        return sellerService.deleteById(sellerId);
    }

    @GetMapping("{id}")
    @Override
    public SellerDto getById(@PathVariable(name = "id") Long sellerId) {
        return sellerService.getById(sellerId);
    }

    @GetMapping("by-car-id")
    public SellerDto get(@RequestParam(name = "carId") Long carId) {
        return sellerService.getByCarId(carId);
    }

    @PostMapping("search")
    @Override
    public PageResponse<SellerDto> search(@RequestBody PageQuery pageQuery) {
        return sellerService.search(pageQuery);
    }

    @PostMapping("{id}/contacts")
    public ContactDto postContact(@PathVariable(name = "id") Long sellerId,
                                  @RequestBody ContactDto contact,
                                  @RequestParam(name = "whatsapp-linked") boolean whatsappLinked,
                                  @RequestParam(name = "telegram-linked") boolean telegramLinked) {
        return sellerService.postContactByParams(sellerId, contact, whatsappLinked, telegramLinked);
    }

    @GetMapping("{id}/contacts")
    public List<ContactDto> getContactsBySellerId(@PathVariable(name = "id") Long sellerId) {
        return contactService.getContactsBySellerId(sellerId);
    }

    @PutMapping("{sellerId}/contacts/{contactId}")
    public ContactDto put(@PathVariable(name = "sellerId") Long sellerId,
                          @PathVariable(name = "contactId") Long contactId,
                          @RequestBody ContactDto contact) {
        return contactService.put(contactId, contact);
    }

    @DeleteMapping("{sellerId}/contacts/{contactId}")
    public ContactDto put(@PathVariable(name = "sellerId") Long sellerId,
                          @PathVariable(name = "contactId") Long contactId) {
        return contactService.deleteById(contactId);
    }

    @PostMapping("sales")
    public void postSaleLink(@RequestParam(name = "buyer-id") Long buyerId,
                             @RequestParam(name = "car-id") Long carId) {
        sellerService.processSaleLink(buyerId, carId, SaleStatusEnum.INTERESTED);
    }

    @GetMapping("sales")
    public List<SaleLinkDto> getSaleLinksByBuyerId(@RequestParam("buyer-id") Long buyerId) {
        return sellerService.getSalesByBuyerId(buyerId);
    }

    @PutMapping("sales/{sale-link-id}")
    public void putSaleLink(@PathVariable(name = "sale-link-id") Long saleLinkId,
                            @RequestBody SaleLinkDto saleLink) {
        sellerService.processSaleLink(saleLinkId, saleLink);
    }
}
