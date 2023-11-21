package com.zb.entity;


import com.zb.dto.store.StoreDto;
import com.zb.dto.user.ManagerInfoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {

    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", length = 50)
    private String storeName;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "description", length = 100)
    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    // from
    public static Store from(StoreDto.Request form, Manager manager) {
        return Store.builder()
                    .storeName(form.getStoreName())
                    .location(form.getLocation())
                    .description(form.getDescription())
                    .manager(manager)
                    .build();
    }

    // to
    public static StoreDto.Info to(Store store) {
        return StoreDto.Info.builder()
                            .storeName(store.getStoreName())
                            .location(store.getLocation())
                            .description(store.getDescription())
                            .manager(ManagerInfoDto.from(store.getManager().getUsername()))
                            .build();
    }

    public void updateStore(StoreDto.Request form) {
        this.storeName = form.getStoreName();
        this.location = form.getLocation();
        this.description = form.getDescription();
    }
}
