package com.hackaboss.travelagency.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Borrado lógico: se actualiza el campo 'active' a false en lugar de eliminar el registro físicamente
@SQLDelete(sql = "UPDATE flights SET active = false WHERE id = ?")
// Definición del filtro de Hibernate para el borrado lógico, usando la clase BooleanJavaType
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanJavaType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número del vuelo
    private String number;

    // Aerolínea del vuelo
    private String airline;

    // Campo para el borrado lógico
    @Column(name = "active")
    private Boolean active = true;
}
