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
@Table(name = "flight_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE flight_bookings SET active = false WHERE id = ?")
@Filter(name = "activeFilter", condition = "active = :active")
public class FlightBooking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con vuelos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    // Relación con usuarios
    @ManyToMany
    @JoinTable(name = "flight_booking_users",
            joinColumns = @JoinColumn(name = "flight_booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> passengers = new ArrayList<>();

}
