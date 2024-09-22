package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "personal")
@SequenceGenerator(sequenceName = "personal_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class PersonalEntity extends AbstractEntity {
    String firstName;
    boolean isFirstNameSet;
    String lastName;
    boolean isLastNameSet;
    String middleName;
    boolean isMiddleNameSet;
    LocalDate birthDate;
    boolean isBirthDateSet;
}
