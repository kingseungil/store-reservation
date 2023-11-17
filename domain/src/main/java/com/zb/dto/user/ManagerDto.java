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
public class ManagerDto {

    private Long id;
    private String username;

    public static ManagerDto from(Manager manager) {
        return ManagerDto.builder()
                         .id(manager.getManagerId())
                         .username(manager.getUsername())
                         .build();
    }

    public static Manager to(ManagerDto managerDto) {
        return Manager.builder()
                      .username(managerDto.getUsername())
                      .build();
    }
}
