package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.enums.ContactTypeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ContactDto extends AbstractDto {
    ContactTypeEnum type;
    String value;

    @QueryProjection
    public ContactDto(Long id, ContactTypeEnum type, String value) {
        super(id);
        this.type = type;
        this.value = value;
    }
}
