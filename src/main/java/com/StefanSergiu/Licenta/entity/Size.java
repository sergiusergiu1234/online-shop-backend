package com.StefanSergiu.Licenta.entity;

import com.StefanSergiu.Licenta.dto.size.SizeDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity

@Table(name = "size", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"value", "type_id"})
})
@Data
@NoArgsConstructor
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String value;

    @OneToMany(mappedBy = "size",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductSize> productSizes;

    @ManyToOne
    @JoinColumn(name = "type_id",nullable = false)
    private Type type;

}
