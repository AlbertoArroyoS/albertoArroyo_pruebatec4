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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Borrado lógico, se define la consulta para el borrado lógico
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
// Definición del filtro de Hibernate para el borrado lógico, se define el nombre del filtro y el parámetro que se va a utilizar
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanType.class))
@Filter(name = "activeFilter", condition = "active = :active")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    // Campo para el borrado lógico
    @Column(name = "active")
    private Boolean active = true;

}
