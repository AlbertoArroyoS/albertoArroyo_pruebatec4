package com.hackaboss.travelagency.model;
import com.sun.jdi.BooleanType;
import jakarta.persistence.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE hotels SET active = false WHERE id = ?")
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotelCode;
    private String name;
    private String city;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "rate_per_night")
    private Double ratePerNight;

    // Fecha y hora de inicio
    @Column(name = "date_from")
    private LocalDateTime dateFrom;

    // Fecha y hora de fin
    @Column(name = "date_to")
    private LocalDateTime dateTo;

    private String booked;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HotelBooking> listHotelBookings = new ArrayList<>();

    @Column(name = "active")
    private Boolean active = true;

}
