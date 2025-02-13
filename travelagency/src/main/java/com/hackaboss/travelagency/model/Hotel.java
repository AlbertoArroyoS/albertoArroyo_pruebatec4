package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.util.Booked;
import com.hackaboss.travelagency.util.RoomType;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Borrado lógico: se actualiza el campo 'active' a false en lugar de eliminar el registro físicamente
@SQLDelete(sql = "UPDATE hotels SET active = false WHERE id = ?")
// Definición del filtro para el borrado lógico, usando BooleanJavaType para el parámetro
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanJavaType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotelCode;
    private String name;
    private String city;

    @Column(name = "room_type")
    private RoomType roomType;

    @Column(name = "rate_per_night")
    private Double ratePerNight;

    // Fecha y hora de inicio
    @Column(name = "date_from")
    private LocalDateTime dateFrom;

    // Fecha y hora de fin
    @Column(name = "date_to")
    private LocalDateTime dateTo;

    private Booked booked;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HotelBooking> listHotelBookings = new ArrayList<>();

    @Column(name = "active")
    private Boolean active = true;
}
