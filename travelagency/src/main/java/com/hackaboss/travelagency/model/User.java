package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.model.base.BaseEntity;
import com.hackaboss.travelagency.util.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
@Filter(name = "activeFilter", condition = "active = :active")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    private String email;

    private String password;

    private String name;
    private String surname;
    private String phone;
    private String dni;

    // Si quieres que el user sepa sus reservas de hotel
    @ManyToMany(mappedBy = "hosts")
    private List<HotelBooking> hotelBookings = new ArrayList<>();

    // Si quieres que el user sepa sus reservas de vuelo
    @ManyToMany(mappedBy = "passengers")
    private List<FlightBooking> flightBooking = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.USER;


}
