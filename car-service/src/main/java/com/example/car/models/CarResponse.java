package com.example.car.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modèle de réponse incluant les détails de la voiture et du client associé
 */
@Builder  // Permet d'utiliser le pattern Builder pour créer des instances
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private String matricule;
    private Client client;  // Objet client complet, pas juste l'ID
}
