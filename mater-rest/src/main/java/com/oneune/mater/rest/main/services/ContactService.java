package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.ContactReader;
import com.oneune.mater.rest.main.readers.SellerReader;
import com.oneune.mater.rest.main.repositories.ContactRepository;
import com.oneune.mater.rest.main.store.dtos.ContactDto;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.entities.ContactEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ContactService implements Command, CRUDable<ContactDto, ContactEntity> {

    ModelMapper modelMapper;
    ContactRepository contactRepository;
    ContactReader contactReader;
    SellerReader sellerReader;

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
        TelegramBotUtils.informAboutDeveloping(bot, update);
    }

    @Transactional
    @Override
    public ContactDto post(ContactDto contactDto) {
        ContactEntity contactEntity = new ContactEntity();
        modelMapper.map(contactDto, contactEntity);
        contactRepository.saveAndFlush(contactEntity);
        return contactReader.getById(contactEntity.getId());
    }

    @Transactional
    @Override
    public ContactDto put(Long contactId, ContactDto contactDto) {
        ContactEntity contactEntity = contactReader.getEntityById(contactId);
        modelMapper.map(contactDto, contactEntity);
        contactRepository.saveAndFlush(contactEntity);
        return contactReader.getById(contactEntity.getId());
    }

    @Transactional
    @Override
    public ContactDto deleteById(Long contactId) {
        ContactEntity contactEntity = contactReader.getEntityById(contactId);
        contactRepository.delete(contactEntity);
        contactRepository.flush();
        return modelMapper.map(contactEntity, ContactDto.class);
    }

    public ContactEntity getEntityById(Long contactId) {
        return contactReader.getEntityById(contactId);
    }

    @Override
    public ContactDto getById(Long contactId) {
        return contactReader.getById(contactId);
    }

    @Override
    public PageResponse<ContactDto> search(PageQuery pageQuery) {
        return contactReader.search(pageQuery);
    }

    public List<ContactDto> getContactsBySellerId(Long sellerId) {
        return contactReader.getContactsBySellerId(sellerId);
    }

    public void flush() {
        contactRepository.flush();
    }
}
