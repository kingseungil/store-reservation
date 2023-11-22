package com.zb.dto.user;

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
    private String username;
    private String phoneNumber;

    public static CustomerInfoDto from(String username, String phoneNumber) {
        return CustomerInfoDto.builder()
                              .username(username)
                              .phoneNumber(phoneNumber)
                              .build();
    }

    public static CustomerInfoDto from(String username) {
        return CustomerInfoDto.builder()
                              .username(username)
                              .build();
    }
}
