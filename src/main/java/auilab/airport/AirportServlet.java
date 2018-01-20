package auilab.airport;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Mateusz
 */
@Path("/services")
public class AirportServlet {
    @Context
    ServletContext context;

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    private static final String API_KEY = "AIzaSyA6pGlte4cxsHrP7YmD2-2kjXcnvxTLVb8";
    private static final GeoApiContext geoContext = new GeoApiContext().setApiKey(API_KEY);

    private static final int FLIGHTS_TO_GENERATE = 100;

    @GET
    @Path("/distance")
    @Produces(MediaType.APPLICATION_JSON)
    public DistanceMatrix calculateDistance(@QueryParam("originCity") String origin,
            @QueryParam("destinationCity") String destination,
            @QueryParam("travelMode") String travelMode) throws ApiException, InterruptedException, IOException {
        DistanceMatrixApiRequest matrixRequest = DistanceMatrixApi.newRequest(geoContext);
        DistanceMatrix matrix = matrixRequest.origins(origin)
                .destinations(destination)
                .mode(parseTravelMode(travelMode))
                .language("pl-PL")
                .await();

        return matrix;
    }

    private TravelMode parseTravelMode(String travelMode) {
        switch (travelMode) {
            case "car": return TravelMode.DRIVING;
            case "publicTransport": return TravelMode.TRANSIT;
            case "bike": return TravelMode.BICYCLING;
            case "byFoot": return TravelMode.WALKING;
            default: return TravelMode.UNKNOWN;
        }
    }

    @GET
    @Path("/flights")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> findFlights(@QueryParam("searchedAirport") String searchedAirport,
            @QueryParam("flightType") String flightType) throws IOException {
        List<Flight> generatedFlights = new ArrayList<>(flightGenerator(FLIGHTS_TO_GENERATE));
        List<Flight> filteredFlights = new ArrayList<>();

        for (Flight aFlight: generatedFlights)
            if (aFlight.getAirport().equals(searchedAirport))
                filteredFlights.add(aFlight);

        Collections.sort(filteredFlights);

        return filteredFlights;
    }

    private List<Flight> flightGenerator(int numberOfFlights) throws IOException {
        List<String> airports = Files.readAllLines(new File("G:\\MOJE DOKUMENTY\\NetBeansProjects\\Airport\\src\\main\\webapp\\airportsList.txt").toPath());

        List<Flight> flights = new ArrayList<>();
        List<String> airlines = new ArrayList<>(Arrays.asList("Lufthansa", "Ryanair", "Wizz Air", "PLL LOT", "SAS", "Norwegian", "Finnair"));

        Random random = new Random();

        for (int i = 0; i < numberOfFlights; i++) {
            Flight flight = new Flight();
            flight.setAirport(airports.get(random.nextInt(airports.size())));
            flight.setFlightNumber(random.nextInt(1000));
            flight.setAirline(airlines.get(random.nextInt(airlines.size())));
            flight.setTime(random.nextInt(24), random.nextInt(60));

            flights.add(flight);
        }

        return flights;
    }
}