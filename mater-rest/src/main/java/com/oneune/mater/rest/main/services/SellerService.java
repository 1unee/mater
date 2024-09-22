package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.SellerReader;
import com.oneune.mater.rest.main.repositories.SellerRepository;
import com.oneune.mater.rest.main.store.dtos.ContactDto;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.SellerDto;
import com.oneune.mater.rest.main.store.entities.ContactEntity;
import com.oneune.mater.rest.main.store.entities.SellerEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class SellerService implements CRUDable<SellerDto, SellerEntity> {

    ModelMapper modelMapper;
    SellerRepository sellerRepository;
    SellerReader sellerReader;
    ContactService contactService;

    @Transactional
    @Override
    public SellerDto post(SellerDto sellerDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    public SellerEntity post() {
        SellerEntity sellerEntity = new SellerEntity();
        sellerRepository.saveAndFlush(sellerEntity);
        return sellerEntity;
    }

    @Transactional
    @Override
    public SellerDto put(Long sellerId, SellerDto sellerDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    @Override
    public SellerDto deleteById(Long sellerId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SellerEntity getEntityById(Long sellerId) {
        return sellerReader.getEntityById(sellerId);
    }

    @Override
    public SellerDto getById(Long sellerId) {
        return sellerReader.getById(sellerId);
    }

    @Override
    public PageResponse<SellerDto> search(PageQuery pageQuery) {
        return sellerReader.search(pageQuery);
    }

    public SellerDto getByCarId(Long carId) {
        return sellerReader.getBarCarId(carId);
    }

    @Transactional
    public ContactDto postContactByParams(Long sellerId,
                                          ContactDto contactDto) {
        SellerEntity sellerEntity = sellerReader.getEntityById(sellerId);
        ContactEntity contactEntity = contactService.getEntityById(contactService.post(contactDto).getId());
        sellerEntity.getContacts().add(contactEntity);
        contactEntity.setSeller(sellerEntity);
        sellerRepository.saveAndFlush(sellerEntity);
        contactService.flush();
        return modelMapper.map(contactEntity, ContactDto.class);
    }
}
