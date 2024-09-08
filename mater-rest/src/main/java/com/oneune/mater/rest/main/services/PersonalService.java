package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.PersonalReader;
import com.oneune.mater.rest.main.repositories.PersonalRepository;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.PersonalDto;
import com.oneune.mater.rest.main.store.entities.PersonalEntity;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class PersonalService implements CRUDable<PersonalDto, PersonalEntity> {

    ModelMapper modelMapper;
    PersonalRepository personalRepository;
    PersonalReader personalReader;

    @Transactional
    @Override
    public PersonalDto post(PersonalDto personalDto) {
        PersonalEntity personalEntity = new PersonalEntity();
        modelMapper.map(personalDto, personalEntity);
        personalRepository.saveAndFlush(personalEntity);
        return personalReader.getById(personalEntity.getId());
    }

    @Transactional
    public PersonalEntity postByTelegramUser(User telegramUser,
                                             @Nullable String middleName,
                                             @Nullable LocalDate birthDate) {
        PersonalEntity personalEntity = buildByTelegramUser(telegramUser, middleName, birthDate);
        personalRepository.saveAndFlush(personalEntity);
        return personalEntity;
    }

    private PersonalEntity buildByTelegramUser(User telegramUser,
                                               @Nullable String middleName,
                                               @Nullable LocalDate birthDate) {
        return PersonalEntity.builder()
                .firstName(telegramUser.getFirstName())
                .lastName(telegramUser.getLastName())
                .middleName(middleName)
                .birthDate(birthDate)
                .build();
    }

    @Transactional
    @Override
    public PersonalDto put(Long personalId, PersonalDto personalDto) {
        PersonalEntity personalEntity = personalReader.getEntityById(personalId);
        modelMapper.map(personalDto, personalEntity);
        personalRepository.saveAndFlush(personalEntity);
        return personalReader.getById(personalEntity.getId());
    }

    @Transactional
    @Override
    public PersonalDto deleteById(Long personalId) {
        PersonalEntity personalEntity = personalReader.getEntityById(personalId);
        personalRepository.delete(personalEntity);
        personalRepository.flush();
        return modelMapper.map(personalEntity, PersonalDto.class);
    }

    public PersonalEntity getEntityById(Long personalId) {
        return personalReader.getEntityById(personalId);
    }

    @Override
    public PersonalDto getById(Long personalId) {
        return personalReader.getById(personalId);
    }

    @Override
    public PageResponse<PersonalDto> search(PageQuery pageQuery) {
        return personalReader.search(pageQuery);
    }
}
