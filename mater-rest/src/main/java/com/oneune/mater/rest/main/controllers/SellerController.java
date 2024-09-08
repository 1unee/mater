package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.services.ContactService;
import com.oneune.mater.rest.main.services.SellerService;
import com.oneune.mater.rest.main.store.dtos.ContactDto;
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
                                  @RequestBody ContactDto contact) {
        return sellerService.postContactByParams(sellerId, contact);
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
}
