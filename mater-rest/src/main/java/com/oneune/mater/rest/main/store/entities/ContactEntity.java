package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import com.oneune.mater.rest.main.store.enums.ContactTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "contact")
@SequenceGenerator(sequenceName = "contact_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ContactEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "seller_id")
    SellerEntity seller;

    @Enumerated(EnumType.STRING)
    ContactTypeEnum type;
    String value;
}
