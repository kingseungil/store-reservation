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
public class ManagerInfoDto {

    // 나중에 필요하면 추가
    private String username;

    public static ManagerInfoDto from(String username) {
        return ManagerInfoDto.builder()
                             .username(username)
                             .build();
    }
}