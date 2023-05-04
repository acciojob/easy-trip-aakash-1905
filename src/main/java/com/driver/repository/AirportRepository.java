package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {

    HashSet<Flight> flights = new HashSet<>();
    HashMap<Integer,Flight> flightsMap = new HashMap<>();
    HashMap<Integer,Integer> numofbookings = new HashMap<>();
    HashSet<Airport> airports = new HashSet<>();
    HashMap<String,Airport> airportsMap = new HashMap<>();
    HashSet<Passenger> passengers = new HashSet<>();
    HashMap<Integer,Passenger> passengersMap = new HashMap<>();
    HashMap<Integer, Set<Integer>> ticketMap = new HashMap<>();
    public void addAirport(Airport airport) {
        airports.add(airport);
        airportsMap.put(airport.getAirportName(),airport);
    }

    public List<Airport> getAllAirports() {
        List<Airport> allAirports = new ArrayList<>(airports);
        return allAirports;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
        flightsMap.put(flight.getFlightId(),flight);
    }

    public List<Flight> getAllFlights() {
        List<Flight> allFlights = new ArrayList<>(flights);
        return allFlights;
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passengersMap.put(passenger.getPassengerId(),passenger);
    }

    public boolean bookticket(Integer flightId, Integer passengerId) {
        Flight temp = flightsMap.get(flightId);

        if(ticketMap.containsKey(flightId)){
            if(ticketMap.get(flightId).size()>=temp.getMaxCapacity())return false;
            if(ticketMap.get(flightId).contains(passengerId))return false;
            ticketMap.get(flightId).add(passengerId);
            numofbookings.put(flightId,numofbookings.getOrDefault(flightId,0)+1);
            return true;
        }
        Set<Integer> set = new HashSet<>();
        set.add(passengerId);
        ticketMap.put(flightId,set);
        return true;
    }

    public boolean cancelTicket(Integer flightId, Integer passengerId) {

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId
        if(ticketMap.containsKey(flightId)){
            if(!ticketMap.get(flightId).contains(passengerId))return false;
        }else{
            return false;
        }
        Set<Integer> st = ticketMap.get(flightId);
        st.remove(passengerId);
        ticketMap.put(flightId,st);
        numofbookings.put(flightId,numofbookings.getOrDefault(flightId,0)-1);
        if(numofbookings.get(flightId)==0)numofbookings.remove(flightId);
        return true;
    }

    public Flight getFlight(Integer flightId) {
        return flightsMap.get(flightId);
    }

    public int getCountOfBookings(Integer passengerId) {
        int count =0;
        for(Set<Integer> i : ticketMap.values()){
            if(i.contains(passengerId))count++;
        }
        return count;
    }

    public Airport getTakeOff(Integer flightId) {
        Flight temp = flightsMap.getOrDefault(flightId,null);
        if (temp == null) return null;
       for(Airport i : airports){
           if(i.getCity()==temp.getFromCity())return i;
       }
       return null;
    }

    public int getNoOfBookings(Integer flightId) {
        return ticketMap.getOrDefault(flightId,new HashSet<>()).size();
    }

    public int getNumberOfPeople(String airportName, Date date) {
        int count =0;
        Airport curr = airportsMap.get(airportName);
        if(curr!=null) {
            City city = curr.getCity();
            for (Flight i : flights) {
                if (i.getFromCity().equals(city) || i.getToCity().equals(city) && i.getFlightDate().equals(date)) {
                    count = +ticketMap.getOrDefault(i.getFlightId(), new HashSet<>()).size();
                }
            }
        }
        return count;
    }
}
