package com.oneune.mater.rest.main.store.dtos;

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
    boolean firstNameSet;
    String lastName;
    boolean lastNameSet;
    String middleName;
    boolean middleNameSet;
    LocalDate birthDate;
    boolean birthDateSet;
}
