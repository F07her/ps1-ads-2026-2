package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.repositories.CarRepository;
import br.edu.fatecfranca.api.repositories.CustomerRepository;


@Service
public class CarService {

    private final CarRepository repository;
    private final CustomerRepository customerRepository;

    public CarService(CarRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    private void copyToEntity(CarRequest request, Car car) {
        car.setModel(request.model());
        car.setBrand(request.brand());
        car.setColor(request.color());
        car.setManufactureYear(request.manufactureYear());
        car.setLicensePlate(request.licensePlate());
        car.setRegistrationDate(request.registrationDate());
        car.setImported(request.imported() != null ? request.imported() : false);

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Customer not found with id: " + request.customerId()));
        car.setCustomer(customer);
    }

    public long count() {
        return repository.count();
    }

    public Car create(CarRequest request) {
        Car car = new Car();
        copyToEntity(request, car);
        return repository.save(car);
    }

    public Car create(Car car) {
        return repository.save(car);
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    public Car update(Long id, CarRequest request) {
        Optional<Car> optionalCar = repository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            copyToEntity(request, car);
            return repository.save(car);
        } else {
            throw new RuntimeException("Car not found with id: " + id);
        }
    }

    public Car update(Car car) {
        return repository.save(car);
    }

    public boolean customerExistsById(Long id) {
        return customerRepository.existsById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
