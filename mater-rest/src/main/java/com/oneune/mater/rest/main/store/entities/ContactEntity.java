package com.oneune.mater.rest.main.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.net.URI;

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
    @ToString.Exclude
    SellerEntity seller;

    String method;
    String phoneNumber;
    URI socialNetworkReference;
}
