package com.StefanSergiu.Licenta.entity;

import com.StefanSergiu.Licenta.dto.size.SizeDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="size")
@Data
@NoArgsConstructor
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "type_id",nullable = false)
    private Type type;
}
