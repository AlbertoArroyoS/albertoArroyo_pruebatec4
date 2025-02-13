package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.model.base.BaseEntity;
import com.hackaboss.travelagency.util.Booked;
import com.hackaboss.travelagency.util.RoomType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE hotels SET active = false WHERE id = ?")
@Filter(name = "activeFilter", condition = "active = :active")
public class Hotel extends BaseEntity {

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

}
