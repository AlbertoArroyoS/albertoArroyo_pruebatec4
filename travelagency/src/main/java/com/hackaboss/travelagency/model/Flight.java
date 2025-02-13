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
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE flights SET active = false WHERE id = ?")
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class Flight {

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

    // Fecha y hora de inicio
    @Column(name = "departure_date")
    private LocalDateTime departureDate;

    // Fecha y hora de fin
    @Column(name = "return_date")
    private LocalDateTime returnDate;


    @OneToMany(mappedBy = "hotel", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FlightBooking> listFlightBookings = new ArrayList<>();

    @Column(name = "active")
    private Boolean active = true;

}