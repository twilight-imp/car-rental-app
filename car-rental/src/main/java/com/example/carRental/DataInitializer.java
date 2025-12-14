package com.example.carRental;

import com.example.carRental.models.Car;
import com.example.carRental.models.Customer;
import com.example.carRental.repository.CarRepository;
import com.example.carRental.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private CarRepository carRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        initializeCars();
        initializeCustomers();
    }

    private void initializeCars() {
        if (carRepository.getAll().isEmpty()) {
            List<Car> cars = Arrays.asList(
                    new Car("Toyota", "Camry", 2022, "Black", "А123ВС77", 2500),
                    new Car("BMW", "X5", 2023, "White", "В456ОР77", 5000),
                    new Car("Hyundai", "Solaris", 2021, "Silver", "С789ТК77", 2000),
                    new Car("Mercedes", "E-Class", 2023, "Black", "Е321АВ77", 6000),
                    new Car("Kia", "Rio", 2022, "Red", "К159УС77", 2200),
                    new Car("Audi", "Q7", 2023, "Blue", "А753МР77", 5500),
                    new Car("Lada", "Vesta", 2021, "White", "Л951НС77", 1500),
                    new Car("Skoda", "Octavia", 2022, "Gray", "С357ВН77", 2800)
            );

            cars.forEach(car -> {
                car.setAvailable(true);
                carRepository.create(car);
            });

        }
    }

    private void initializeCustomers() {
        if (customerRepository.getAll().isEmpty()) {
            List<Customer> customers = Arrays.asList(
                    new Customer("Иван", "Петров", LocalDate.of(1990, 5, 15), 8, "ivan.petrov@mail.ru", "+79161234567"),
                    new Customer("Мария", "Сидорова", LocalDate.of(1985, 8, 22), 12, "maria.sidorova@mail.ru", "+79167654321"),
                    new Customer("Алексей", "Козлов", LocalDate.of(1995, 3, 10), 5, "alex.kozlov@mail.ru", "+79169998877"),
                    new Customer("Елена", "Васильева", LocalDate.of(1988, 11, 30), 10, "elena.vasileva@mail.ru", "+79165554433"),
                    new Customer("Дмитрий", "Николаев", LocalDate.of(1992, 7, 8), 6, "dmitry.nikolaev@mail.ru", "+79163332211"),
                    new Customer("Ольга", "Иванова", LocalDate.of(1998, 1, 25), 3, "olga.ivanova@mail.ru", "+79164447788")
            );

            customers.forEach(customerRepository::create);
        }
    }
}

