package com.driver.service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;

import java.util.*;

public class AirportService {
    AirportRepository airportRepository = new AirportRepository();
    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public Optional<Airport> getLargestAirport() {
        List<Airport> allAirports = airportRepository.getAllAirports();
        Collections.sort(allAirports,(a,b)->{
            if(b.getNoOfTerminals() == a.getNoOfTerminals()){
                return a.getAirportName().compareTo(b.getAirportName());
            }
            return b.getNoOfTerminals() - a.getNoOfTerminals();
        });

        if(allAirports.get(0)!=null)
            return Optional.of(allAirports.get(0));
        else return Optional.empty();
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }


    public Optional<Flight> getShortestFlight(City fromCity, City toCity) {
        List<Flight> allFlights = airportRepository.getAllFlights();
        double min = Double.MAX_VALUE;
        Flight ans = null;
        for(Flight i : allFlights){
            if(i.getFromCity().equals(fromCity) && i.getToCity().equals(toCity)){
                if(i.getDuration()<min)
                    ans = i;
            }
        }
        if(ans!=null)
            return Optional.of(ans);
        else return Optional.empty();
    }

    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public int getNumberOfPeople(String airportName, Date date) {
        return airportRepository.getNumberOfPeople(airportName,date);

    }

    public boolean bookTicket(Integer flightId, Integer passengerId) {
        boolean status = airportRepository.bookticket(flightId,passengerId);
        return status;
    }

    public boolean cancelTicket(Integer flightId, Integer passengerId) {
        boolean status = airportRepository.cancelTicket(flightId,passengerId);
        return status;
    }

    public int calcFare(Integer flightId) {
        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price
        int num = airportRepository.getNoOfBookings(flightId);
        int price = 3000;
        return price + (num * 50);
    }

    public int getCountOfBookings(Integer passengerId) {
        return airportRepository.getCountOfBookings(passengerId);
    }

    public Optional<Airport> getTakeOff(Integer flightId) {
        Airport temp = airportRepository.getTakeOff(flightId);
        if(temp!=null)return Optional.of(temp);
        else return Optional.empty();
    }

    public int calcRevenue(Integer flightId) {
        int num = airportRepository.getNoOfBookings(flightId);
        int count = 0;
        for(int i =0;i<num;i++){
            count += 3000;
            count += (i * 50);
        }
        return count;
    }
}
