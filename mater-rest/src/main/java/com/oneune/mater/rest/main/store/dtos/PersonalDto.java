package com.oneune.mater.rest.main.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * @see com.oneune.mater.rest.main.store.entities.PersonalEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class PersonalDto extends AbstractDto {

    String firstName;

    @JsonProperty("isFirstNameSet")
    boolean isFirstNameSet;

    String lastName;

    @JsonProperty("isLastNameSet")
    boolean isLastNameSet;

    String middleName;

    @JsonProperty("isMiddleNameSet")
    boolean isMiddleNameSet;

    LocalDate birthDate;

    @JsonProperty("isBirthDateSet")
    boolean isBirthDateSet;
}
