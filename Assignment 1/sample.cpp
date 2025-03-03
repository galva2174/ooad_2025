#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <ctime>
using namespace std;

enum class FuelType {
    ELECTRIC, PETROL, DIESEL
};

enum class TransmissionType {
    MANUAL, AUTOMATIC
};

enum class CarType {
    HATCHBACK, SEDAN, SUV
};

class Vehicle {
protected:
    string regnNumber;
    FuelType fuelType;
    double perDayRent;
    tm rentalDate;

public:
    Vehicle(string regnNumber, FuelType fuelType, double perDayRent, tm rentalDate) {
        this->regnNumber = regnNumber;
        this->fuelType = fuelType;
        this->perDayRent = perDayRent;
        this->rentalDate = rentalDate;
    }

    string getRegnNumber() const {
        return regnNumber;
    }

    tm getRentalDate() const {
        return rentalDate;
    }

    double calculateRent(int days) const {
        return perDayRent * days;
    }

    virtual void displayDetails() const = 0;
};

class Car : public Vehicle {
private:
    TransmissionType transmissionType;
    CarType carType;
    int numberOfSeats;

public:
    Car(string regnNumber, FuelType fuelType, double perDayRent, tm rentalDate,
        TransmissionType transmissionType, CarType carType, int numberOfSeats)
        : Vehicle(regnNumber, fuelType, perDayRent, rentalDate) {
        this->transmissionType = transmissionType;
        this->carType = carType;
        this->numberOfSeats = numberOfSeats;
    }

    void displayDetails() const override {
        cout << "Vehicle Type: Car" << endl;
        cout << "Registration Number: " << getRegnNumber() << endl;
        cout << "Fuel Type: " << static_cast<int>(fuelType) << endl;
        cout << "Per Day Rent:" << perDayRent << endl;
        cout << "Rental Date: " << rentalDate.tm_mday << "/" << (rentalDate.tm_mon + 1) << "/" << (rentalDate.tm_year + 1900) << endl;
        cout << "Transmission Type: " << static_cast<int>(transmissionType) << endl;
        cout << "Car Type: " << static_cast<int>(carType) << endl;
        cout << "Number of Seats: " << numberOfSeats << endl;
        cout << endl;
    }
};

class Motorcycle : public Vehicle {
private:
    int engineDisplacement;
    double weight;

public:
    Motorcycle(string regnNumber, FuelType fuelType, double perDayRent, tm rentalDate,
               int engineDisplacement, double weight)
        : Vehicle(regnNumber, fuelType, perDayRent, rentalDate) {
        this->engineDisplacement = engineDisplacement;
        this->weight = weight;
    }

    void displayDetails() const override {
        cout << "Vehicle Type: Motorcycle" << endl;
        cout << "Registration Number: " << getRegnNumber() << endl;
        cout << "Fuel Type: " << static_cast<int>(fuelType) << endl;
        cout << "Per Day Rent: " << perDayRent << endl;
        cout << "Rental Date: " << rentalDate.tm_mday << "/" << (rentalDate.tm_mon + 1) << "/" << (rentalDate.tm_year + 1900) << endl;
        cout << "Engine Displacement: " << engineDisplacement << " cc" << endl;
        cout << "Weight: " << weight << " kg" << endl;
        cout << endl;
    }
};

class User {
private:
    string userId;
    string name;
    string contactNumber;

public:
    User(string userId, string name, string contactNumber) {
        this->userId = userId;
        this->name = name;
        this->contactNumber = contactNumber;
    }

    string getUserId() const {
        return userId;
    }

    string getName() const {
        return name;
    }

    string getContactNumber() const {
        return contactNumber;
    }
};

class RentalManager {
private:
    struct Rental {
        User user;
        Vehicle* vehicle;

        Rental(User user, Vehicle* vehicle) : user(user), vehicle(vehicle) {}
    };

    vector<Rental> rentals;

    static bool rentalComparator(const Rental& r1, const Rental& r2) {
        tm date1 = r1.vehicle->getRentalDate();
        tm date2 = r2.vehicle->getRentalDate();

        if (date1.tm_year != date2.tm_year) return date1.tm_year < date2.tm_year;
        if (date1.tm_mon != date2.tm_mon) return date1.tm_mon < date2.tm_mon;
        return date1.tm_mday < date2.tm_mday;
    }

public:
    void addRental(User user, Vehicle* vehicle) {
        rentals.push_back(Rental(user, vehicle));
    }

    void displayUserRentals(User user) {
        vector<Rental> userRentals;

        for (const Rental& rental : rentals) {
            if (rental.user.getUserId() == user.getUserId()) {
                userRentals.push_back(rental);
            }
        }

        sort(userRentals.begin(), userRentals.end(), rentalComparator);

        cout << "Rentals for " << user.getName() << " (ID: " << user.getUserId() << "):" << endl;
        if (userRentals.empty()) {
            cout << "No vehicles rented." << endl;
        } else {
            for (const Rental& rental : userRentals) {
                rental.vehicle->displayDetails();
            }
        }
    }
};

int main() {
    User user1("U001", "Rahul Sharma", "9876543210");
    User user2("U002", "Priya Patel", "8765432109");

    tm rentalDate1 = {};
    rentalDate1.tm_year = 2025 - 1900;
    rentalDate1.tm_mon = 3 - 1;
    rentalDate1.tm_mday = 1;

    tm rentalDate2 = {};
    rentalDate2.tm_year = 2025 - 1900;
    rentalDate2.tm_mon = 2 - 1;
    rentalDate2.tm_mday = 15;

    tm rentalDate3 = {};
    rentalDate3.tm_year = 2025 - 1900;
    rentalDate3.tm_mon = 2 - 1;
    rentalDate3.tm_mday = 20;

    tm rentalDate4 = {};
    rentalDate4.tm_year = 2025 - 1900;
    rentalDate4.tm_mon = 3 - 1;
    rentalDate4.tm_mday = 2;

    Car car1("KA01MN5678", FuelType::PETROL, 2500, rentalDate1,
             TransmissionType::AUTOMATIC, CarType::SEDAN, 4);

    Car car2("KA02AB1234", FuelType::DIESEL, 3000, rentalDate2,
             TransmissionType::MANUAL, CarType::SUV, 6);

    Motorcycle bike1("TN05XY9876", FuelType::PETROL, 1000, rentalDate3,
                     150, 125.5);

    Motorcycle bike2("KL10CD4567", FuelType::ELECTRIC, 1200, rentalDate4,
                     200, 140.0);

    RentalManager rentalManager;

    rentalManager.addRental(user1, &car1);
    rentalManager.addRental(user1, &bike1);
    rentalManager.addRental(user2, &car2);
    rentalManager.addRental(user1, &bike2);

    rentalManager.displayUserRentals(user1);
    cout << "----------------------------------" << endl;
    rentalManager.displayUserRentals(user2);

    return 0;
}
