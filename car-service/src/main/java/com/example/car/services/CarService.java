package com.example.car.services;

import com.example.car.entities.Car;
import com.example.car.models.CarResponse;
import com.example.car.models.Client;
import com.example.car.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    // URL du service client
    private static final String CLIENT_SERVICE_URL = "http://localhost:8081/api/client";
    
    /**
     * Récupère tous les véhicules avec les informations clients
     * @return Liste de CarResponse avec les détails clients
     */
    public List<CarResponse> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère un véhicule par son ID avec les informations client
     * @param id Identifiant du véhicule
     * @return CarResponse avec les détails client
     * @throws Exception Si le véhicule n'est pas trouvé
     */
    public CarResponse findById(Long id) throws Exception {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new Exception("Voiture non trouvée avec l'ID: " + id));
        return mapToCarResponse(car);
    }
    
    /**
     * Ajoute un nouveau véhicule
     * @param car Le véhicule à ajouter
     * @return Le véhicule sauvegardé
     */
    public Car addCar(Car car) {
        return carRepository.save(car);
    }
    
    /**
     * Convertit une entité Car en CarResponse avec les données client
     * @param car L'entité Car
     * @return CarResponse avec les informations client
     */
    private CarResponse mapToCarResponse(Car car) {
        Client client = null;
        
        // Appel au service client via RestTemplate
        if (car.getClient_id() != null) {
            try {
                client = restTemplate.getForObject(
                    CLIENT_SERVICE_URL + "/" + car.getClient_id(),
                    Client.class
                );
            } catch (Exception e) {
                // En cas d'erreur, on crée un client avec juste l'ID
                client = new Client();
                client.setId(car.getClient_id());
            }
        }
        
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricule(car.getMatricule())
                .client(client)
                .build();
    }
}
