package com.zb.dto.user;

import com.zb.entity.Manager;
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
    private Long id;
    private String username;

    public static ManagerInfoDto from(Manager manager) {
        return ManagerInfoDto.builder()
                             .id(manager.getId())
                             .username(manager.getUsername())
                             .build();
    }
}
