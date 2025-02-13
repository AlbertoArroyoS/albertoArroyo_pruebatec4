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
@Table(name = "hotel_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Borrado lógico: se actualiza el campo 'active' a false en lugar de eliminar el registro físicamente
@SQLDelete(sql = "UPDATE hotel_bookings SET active = false WHERE id = ?")
// Definición del filtro de Hibernate para el borrado lógico, usando la clase BooleanJavaType
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanJavaType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "active")
    private Boolean active = true;
}
