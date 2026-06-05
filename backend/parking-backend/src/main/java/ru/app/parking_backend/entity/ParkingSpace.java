package ru.app.parking_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "parking_spaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number_space", nullable = false, length = 15, unique = true)
    private String numberSpace;

    @OneToMany(mappedBy = "parking_space", cascade = CascadeType.ALL)
    private List<Reservations> reservations;
}