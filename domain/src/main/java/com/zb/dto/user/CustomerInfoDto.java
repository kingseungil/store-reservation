package com.zb.dto.user;

import com.zb.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoDto {

    // 나중에 필요하면 추가
    private Long id;
    private String username;
    private String phoneNumber;

    public static CustomerInfoDto from(Customer customer) {
        return CustomerInfoDto.builder()
                              .id(customer.getId())
                              .username(customer.getUsername())
                              .phoneNumber(customer.getPhoneNumber())
                              .build();
    }
}
