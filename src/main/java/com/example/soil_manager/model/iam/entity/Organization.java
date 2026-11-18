package com.example.soil_manager.model.iam.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organizations", schema = "iam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private Boolean enabled = true;
}