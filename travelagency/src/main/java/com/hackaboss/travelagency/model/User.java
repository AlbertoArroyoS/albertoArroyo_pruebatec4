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

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    private String email;

    private String password;

    private String name;
    private String surname;
    private String phone;
    private String dni;

    // Relación con reservas de hotel
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelBooking> listHotelBookings = new ArrayList<>();

    // Relación con reservas de vuelo
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightBooking> listFlightBookings = new ArrayList<>();

    @Column(name = "role")
    private Role role;
    
}
