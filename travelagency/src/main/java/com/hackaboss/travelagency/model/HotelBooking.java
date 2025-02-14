package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "hotel_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE hotel_bookings SET active = false WHERE id = ?")
@Filter(name = "activeFilter", condition = "active = :active")
public class HotelBooking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con hoteles
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    // Relación con usuarios
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "hotel_booking_users",
            joinColumns = @JoinColumn(name = "hotel_booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> hosts = new ArrayList<>();


}
