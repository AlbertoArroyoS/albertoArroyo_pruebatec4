package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE flights SET active = false WHERE id = ?")
@Filter(name = "activeFilter", condition = "active = :active")
public class Flight extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private String origin;
    private String destination;

    @Column(name = "seat_type")
    private String seatType;

    @Column(name = "rate_per_person")
    private Double ratePerPerson;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    // Relación con reservas de vuelo
    @OneToMany(mappedBy = "flight")
    private List<FlightBooking> listFlightBookings = new ArrayList<>();

}
