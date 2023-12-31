package com.zb.entity;


import com.zb.dto.store.StoreDto;
import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.dto.user.ManagerInfoDto;
import com.zb.validator.StoreValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {

    @Id
    @Column(name = "id")
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

    @OneToMany(mappedBy = "store")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Review> reviews = new ArrayList<>();


    // from
    public static Store from(StoreRequest form, Manager manager) {
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
                            .id(store.getId())
                            .storeName(store.getStoreName())
                            .location(store.getLocation())
                            .description(store.getDescription())
                            .manager(ManagerInfoDto.from(store.getManager()))
                            .build();
    }

    public void validate(StoreValidator storeValidator) {
        storeValidator.validate(this);
    }
}
