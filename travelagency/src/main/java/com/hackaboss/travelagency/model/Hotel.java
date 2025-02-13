package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.model.base.BaseEntity;
import com.hackaboss.travelagency.util.Booked;
import com.hackaboss.travelagency.util.RoomType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(name = "rate_per_night")
    private Double ratePerNight;

    @Column(name = "date_from", columnDefinition = "date")
    private LocalDate dateFrom;

    // Fecha y hora de fin
    @Column(name = "date_to")
    private LocalDate dateTo;

    //Por defecto que el hotel no esté reservado
    @Enumerated(EnumType.STRING)
    @Column(name = "booked", nullable = false)
    @Builder.Default
    private Booked booked = Booked.NO;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HotelBooking> listHotelBookings = new ArrayList<>();

}
