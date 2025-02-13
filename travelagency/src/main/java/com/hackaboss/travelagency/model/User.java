package com.hackaboss.travelagency.model;

import com.hackaboss.travelagency.util.Role;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Borrado lógico: se actualiza el campo 'active' a false en lugar de eliminar el registro físicamente
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
// Definición del filtro de Hibernate para el borrado lógico, usando BooleanJavaType para el parámetro
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanJavaType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class User {

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

    // Campo para el borrado lógico
    @Column(name = "active")
    private Boolean active = true;
}
