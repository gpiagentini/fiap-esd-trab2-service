package br.com.gpiagentini.api.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_asset")
@Getter
@Setter
@NoArgsConstructor
public class UserAssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @Column(name="display_order")
    private Integer displayOrder;

    public UserAssetEntity(String symbol, Integer displayOrder) {
        this.symbol = symbol;
        this.displayOrder = displayOrder;
    }

}
