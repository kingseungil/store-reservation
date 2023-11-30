package com.zb.entity;

import com.zb.type.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authority")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_name", length = 50)
    private UserRole authorityName;

    @OneToOne(mappedBy = "authority")
    private Customer customer;

    @OneToOne(mappedBy = "authority")
    private Manager manager;
}
