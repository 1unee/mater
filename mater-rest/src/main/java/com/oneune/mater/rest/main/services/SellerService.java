package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.readers.SellerReader;
import com.oneune.mater.rest.main.readers.UserReader;
import com.oneune.mater.rest.main.repositories.SaleLinkRepository;
import com.oneune.mater.rest.main.repositories.SellerRepository;
import com.oneune.mater.rest.main.store.dtos.ContactDto;
import com.oneune.mater.rest.main.store.dtos.SaleLinkDto;
import com.oneune.mater.rest.main.store.dtos.SellerDto;
import com.oneune.mater.rest.main.store.entities.*;
import com.oneune.mater.rest.main.store.enums.ContactTypeEnum;
import com.oneune.mater.rest.main.store.enums.SaleStatusEnum;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class SellerService implements CRUDable<SellerDto, SellerEntity> {

    ModelMapper modelMapper;
    SellerRepository sellerRepository;
    SellerReader sellerReader;
    ContactService contactService;
    UserReader userReader;
    SaleLinkRepository saleLinkRepository;
    CarReader carReader;

    @Transactional
    @Override
    public SellerDto post(SellerDto sellerDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    public SellerEntity post(String telegramUsername) {
        SellerEntity sellerEntity = SellerEntity.builder().build();
        ContactEntity autoCreatedTelegramContactEntity = contactService.getEntityById(
                contactService.post(ContactDto.builder()
                        .type(ContactTypeEnum.TELEGRAM)
                        .value("https://t.me/%s".formatted(telegramUsername))
                        .build())
                        .getId()
        );
        autoCreatedTelegramContactEntity.setSeller(sellerEntity);
        sellerEntity.getContacts().add(autoCreatedTelegramContactEntity);
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
                                          ContactDto contactDto,
                                          boolean whatsappLinked,
                                          boolean telegramLinked) {
        SellerEntity sellerEntity = sellerReader.getEntityById(sellerId);
        ContactEntity contactEntity = contactService.getEntityById(contactService.post(contactDto).getId());
        sellerEntity.getContacts().add(contactEntity);
        if (whatsappLinked) {
            sellerEntity.getContacts().add(postLinkedWhatsappContact(sellerEntity, contactDto.getValue()));
        }
        if (telegramLinked) {
            sellerEntity.getContacts().add(postLinkedTelegramContact(sellerEntity, contactDto.getValue()));
        }
        contactEntity.setSeller(sellerEntity);
        sellerRepository.saveAndFlush(sellerEntity);
        contactService.flush();
        return modelMapper.map(contactEntity, ContactDto.class);
    }

    @Transactional
    protected ContactEntity postLinkedWhatsappContact(SellerEntity sellerEntity, String phone) {
        String formatedPhone = phone.startsWith("+7") ? phone : "+7" + phone.substring(1);
        ContactDto linkedWhatsappContactDto = contactService.post(ContactDto.builder()
                .type(ContactTypeEnum.WHATSAPP)
                .value("https://wa.me/%s".formatted(formatedPhone))
                .build());
        ContactEntity linkedWhatsappContactEntity = contactService.getEntityById(linkedWhatsappContactDto.getId());
        linkedWhatsappContactEntity.setSeller(sellerEntity);
        return linkedWhatsappContactEntity;
    }

    @Transactional
    protected ContactEntity postLinkedTelegramContact(SellerEntity sellerEntity, String phone) {
        String formatedPhone = phone.startsWith("+7") ? phone : "+7" + phone.substring(1);
        ContactDto linkedTelegramContactDto = contactService.post(ContactDto.builder()
                .type(ContactTypeEnum.TELEGRAM)
                .value("https://t.me/%s".formatted(formatedPhone))
                .build());
        ContactEntity linkedTelegramContactEntity = contactService.getEntityById(linkedTelegramContactDto.getId());
        linkedTelegramContactEntity.setSeller(sellerEntity);
        return linkedTelegramContactEntity;
    }

    @Transactional
    public void processSaleLink(Long buyerId, Long carId, SaleStatusEnum saleStatus) {
        Optional<SaleLinkEntity> optSaleLinkEntity = sellerReader.getSaleLinkByParams(buyerId, carId, saleStatus);
        if (optSaleLinkEntity.isEmpty()) {
            UserEntity buyerEntity = userReader.getEntityById(buyerId);
            CarEntity carEntity = carReader.getEntityById(carId);
            SaleLinkEntity saleLinkEntity = SaleLinkEntity.builder()
                    .buyer(buyerEntity)
                    .car(carEntity)
                    .status(saleStatus)
                    .build();
            saleLinkRepository.save(saleLinkEntity);
        }
    }

    @Transactional
    public void processSaleLink(Long saleLinkId, SaleLinkDto saleLinkDto) {
        SaleLinkEntity saleLinkEntity = sellerReader.getSaleLinkById(saleLinkId);
        saleLinkEntity.setStatus(saleLinkDto.getStatus());
        saleLinkEntity.setScore(saleLinkDto.getScore());
        saleLinkEntity.setNote(saleLinkDto.getNote());
        saleLinkRepository.save(saleLinkEntity);
    }

    public List<SaleLinkDto> getSalesByBuyerId(Long buyerId) {
        return sellerReader.getSalesByBuyerId(buyerId);
    }
}
