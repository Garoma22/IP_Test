package org.example;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlightAnalyzer {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("[H:mm][HH:mm]");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");

    public static void analyzeFlights(JSONArray tickets) {
        List<Flight> vlToTaFlights = new ArrayList<>();

        for (int i = 0; i < tickets.length(); i++) {
            JSONObject ticket = tickets.getJSONObject(i);
            String origin = ticket.getString("origin");
            String destination = ticket.getString("destination");

            if (origin.equals("VVO") && destination.equals("TLV")) {
                String carrier = ticket.getString("carrier");
                int price = ticket.getInt("price");

                LocalDate departureDate = LocalDate.parse(ticket.getString("departure_date"), DATE_FORMATTER);
                LocalTime departureTime = LocalTime.parse(ticket.getString("departure_time"), TIME_FORMATTER);
                LocalDate arrivalDate = LocalDate.parse(ticket.getString("arrival_date"), DATE_FORMATTER);
                LocalTime arrivalTime = LocalTime.parse(ticket.getString("arrival_time"), TIME_FORMATTER);

                ZonedDateTime departureDateTime = ZonedDateTime.of(departureDate, departureTime, ZoneId.of("Asia/Vladivostok"));
                ZonedDateTime arrivalDateTime = ZonedDateTime.of(arrivalDate, arrivalTime, ZoneId.of("Asia/Jerusalem"));

                int flightTime = calculateFlightTime(departureDateTime, arrivalDateTime);
                vlToTaFlights.add(new Flight(carrier, price, flightTime));
            }
        }

        if (vlToTaFlights.isEmpty()) {
            System.out.println("No flights found between Vladivostok and Tel Aviv.");
            return;
        }

        calculateAndPrintMinFlightTimes(vlToTaFlights);
        calculateAndPrintPriceStats(vlToTaFlights);
    }

    public static int calculateFlightTime(ZonedDateTime departureDateTime, ZonedDateTime arrivalDateTime) {
        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        return (int) duration.toMinutes();
    }

    public static void calculateAndPrintMinFlightTimes(List<Flight> flights) {
        Map<String, Integer> minFlightTimes = new HashMap<>();

        for (Flight flight : flights) {
            String carrier = flight.getCarrier();
            int flightTime = flight.getFlightTime();

            if (!minFlightTimes.containsKey(carrier) || flightTime < minFlightTimes.get(carrier)) {
                minFlightTimes.put(carrier, flightTime);
            }
        }

        System.out.println("Minimum flight times between Vladivostok and Tel Aviv for each carrier:");
        for (Map.Entry<String, Integer> entry : minFlightTimes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " minutes");
        }
    }

    public static void calculateAndPrintPriceStats(List<Flight> flights) {
        List<Integer> prices = flights.stream().map(Flight::getPrice).sorted().collect(Collectors.toList());
        double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double medianPrice;

        int size = prices.size();
        if (size % 2 == 0) {
            medianPrice = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            medianPrice = prices.get(size / 2);
        }

        System.out.println("Average price: " + averagePrice);
        System.out.println("Median price: " + medianPrice);
        System.out.println("Difference between average price and median price: " + (averagePrice - medianPrice));
    }
}