package org.example;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlightAnalyzer {
    public static void analyzeFlights(JSONArray tickets) {
        List<Flight> vlToTaFlights = new ArrayList<>();

        for (int i = 0; i < tickets.length(); i++) {
            JSONObject ticket = tickets.getJSONObject(i);
            String origin = ticket.getString("origin");
            String destination = ticket.getString("destination");

            if (origin.equals("VVO") && destination.equals("TLV")) {
                String carrier = ticket.getString("carrier");
                int price = ticket.getInt("price");
                int flightTime = calculateFlightTime(ticket.getString("departure_time"), ticket.getString("arrival_time"));

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

    public static int calculateFlightTime(String departureTime, String arrivalTime) {
        int departureHour = Integer.parseInt(departureTime.split(":")[0]);
        int departureMinute = Integer.parseInt(departureTime.split(":")[1]);
        int arrivalHour = Integer.parseInt(arrivalTime.split(":")[0]);
        int arrivalMinute = Integer.parseInt(arrivalTime.split(":")[1]);

        int totalDepartureMinutes = departureHour * 60 + departureMinute;
        int totalArrivalMinutes = arrivalHour * 60 + arrivalMinute;

        return totalArrivalMinutes - totalDepartureMinutes;
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


