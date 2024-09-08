package com.oneune.mater.rest.main.mappers;

import com.oneune.mater.rest.main.store.dtos.*;
import com.oneune.mater.rest.main.store.entities.*;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class QueryMappers {

    public UserDto mapUser(UserEntity userEntity,
                           boolean joinRoles,
                           boolean joinPersonal,
                           boolean joinSeller,
                           boolean joinContacts,
                           boolean joinCars,
                           boolean joinPhotos,
                           boolean joinVideos) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isEmailSet(userEntity.isEmailSet())
                .registeredAt(userEntity.getRegisteredAt())
                .personal(joinPersonal ? mapPersonal(userEntity.getPersonal()) : null)
                .seller(joinSeller ? mapSeller(userEntity.getSeller(), joinContacts, joinCars, joinPhotos, joinVideos) : null)
                .roles(joinRoles ? mapRoles(userEntity.getUserRoleLinks()) : null)
                .build();
    }

    public List<UserDto> mapUsers(List<UserEntity> userEntities,
                                  boolean joinRoles,
                                  boolean joinPersonal,
                                  boolean joinSeller,
                                  boolean joinContacts,
                                  boolean joinCars,
                                  boolean joinPhotos,
                                  boolean joinVideos) {
        return userEntities.stream().map(userEntity -> mapUser(
                userEntity, joinRoles, joinPersonal, joinSeller, joinContacts, joinCars, joinPhotos, joinVideos
        )).toList();
    }

    public RoleDto mapRole(RoleEntity roleEntity) {
        return RoleDto.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .build();
    }

    public List<RoleDto> mapRoles(List<UserRoleLinkEntity> userRoleLinkEntities) {
        return userRoleLinkEntities.stream().map(userRoleLinkEntity -> mapRole(
                userRoleLinkEntity.getRole()
        )).toList();
    }

    public PersonalDto mapPersonal(PersonalEntity personalEntity) {
        return PersonalDto.builder()
                .id(personalEntity.getId())
                .birthDate(personalEntity.getBirthDate())
                .isBirthDateSet(personalEntity.isBirthDateSet())
                .firstName(personalEntity.getFirstName())
                .isFirstNameSet(personalEntity.isFirstNameSet())
                .middleName(personalEntity.getMiddleName())
                .lastName(personalEntity.getLastName())
                .isLastNameSet(personalEntity.isLastNameSet())
                .build();
    }

    public List<PersonalDto> mapPersonals(List<PersonalEntity> personalEntities) {
        return personalEntities.stream().map(QueryMappers::mapPersonal).toList();
    }

    public SellerDto mapSeller(SellerEntity sellerEntity,
                               boolean joinContacts,
                               boolean joinCars,
                               boolean joinPhotos,
                               boolean joinVideos) {
        return SellerDto.builder()
                .id(sellerEntity.getId())
                .score(sellerEntity.getScore())
                .contacts(joinContacts ? mapContacts(sellerEntity.getContacts()) : null)
                .cars(joinCars ? mapCars(sellerEntity.getCars(), joinPhotos, joinVideos) : null)
                .build();
    }

    public List<SellerDto> mapSellers(List<SellerEntity> sellerEntities,
                                      boolean joinContacts,
                                      boolean joinCars,
                                      boolean joinPhotos,
                                      boolean joinVideos) {
        return sellerEntities.stream().map(sellerEntity -> mapSeller(
                sellerEntity, joinContacts, joinCars, joinPhotos, joinVideos
        )).toList();
    }

    public ContactDto mapContact(ContactEntity contactEntity) {
        return ContactDto.builder()
                .id(contactEntity.getId())
                .type(contactEntity.getType())
                .value(contactEntity.getValue())
                .build();
    }

    public List<ContactDto> mapContacts(List<ContactEntity> contactEntities) {
        return contactEntities.stream().map(QueryMappers::mapContact).toList();
    }

    public CarDto mapCar(CarEntity carEntity,
                         boolean joinPhotos,
                         boolean joinVideos) {
        return CarDto.builder()
                .id(carEntity.getId())
                .productionYear(carEntity.getProductionYear())
                .model(carEntity.getModel())
                .VIN(carEntity.getVIN())
                .power(carEntity.getPower())
                .brand(carEntity.getBrand())
                .mileage(carEntity.getMileage())
                .ownersAmount(carEntity.getOwnersAmount())
                .price(carEntity.getPrice())
                .photos(joinPhotos ? mapPhotos(carEntity.getPhotos()) : null)
                .videos(joinVideos ? mapVideos(carEntity.getVideos()) : null)
                .build();
    }

    public List<CarDto> mapCars(List<CarEntity> carEntities,
                                boolean joinPhotos,
                                boolean joinVideos) {
        return carEntities.stream().map(carEntity -> mapCar(
                carEntity, joinPhotos, joinVideos
        )).toList();
    }

    public PhotoDto mapPhoto(PhotoEntity photoEntity) {
        return PhotoDto.builder()
                .id(photoEntity.getId())
                .name(photoEntity.getName())
                .build();
    }

    public List<PhotoDto> mapPhotos(List<PhotoEntity> photoEntities) {
        return photoEntities.stream().map(QueryMappers::mapPhoto).toList();
    }

    public VideoDto mapVideo(VideoEntity videoEntity) {
        return VideoDto.builder()
                .id(videoEntity.getId())
                .name(videoEntity.getName())
                .build();
    }

    public List<VideoDto> mapVideos(List<VideoEntity> videoEntities) {
        return videoEntities.stream().map(QueryMappers::mapVideo).toList();
    }
}
