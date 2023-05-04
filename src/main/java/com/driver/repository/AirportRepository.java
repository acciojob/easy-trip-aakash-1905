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
        if(temp == null)return false;
        if(temp.getMaxCapacity() < numofbookings.getOrDefault(flightId,0))return false;
        if(ticketMap.containsKey(flightId)){
            if(ticketMap.get(flightId).contains(passengerId))return false;
            else {
                Set<Integer> st = ticketMap.get(flightId);
                st.add(passengerId);
                ticketMap.put(flightId,st);
                numofbookings.put(flightId,numofbookings.getOrDefault(flightId,0)+1);
            }
        }else{
            Set<Integer> st = new HashSet<>();
            st.add(passengerId);
            ticketMap.put(flightId,st);
            numofbookings.put(flightId,numofbookings.getOrDefault(flightId,0)+1);
            return true;
        }
        return false;
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
        return numofbookings.getOrDefault(flightId,0);
    }

    public int getNumberOfPeople(String airportName, Date date) {
        int count =0;
        Airport curr = airportsMap.getOrDefault(airportName,null);
        if(curr==null)return 0;
        City city = curr.getCity();
        for(Flight i : flights){
            if(i.getFromCity()==city || i.getToCity()==city && i.getFlightDate().compareTo(date)==0){
                count =+ numofbookings.get(i.getFlightId());
            }
        }
        return count;
    }
}
