/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auilab.airport;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import java.io.IOException;
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
}
