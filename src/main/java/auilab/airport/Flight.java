package auilab.airport;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Mateusz
 */
public class Flight implements Comparable<Flight>{

    @XmlAttribute(name = "airport")
    private String arrivalDepartureAirport;        //airport from which (or to which) the flight to (or from) Gdansk is taking place

    @XmlAttribute(name = "airline")
    private String airline;

    @XmlAttribute(name = "flightNo")
    private int flightNumber;

    @XmlAttribute(name = "time")
    private String arrivalDepartureTime;

    private int arrivalDepartureTimeNumeric;

    public Flight() {
    }

    public Flight(String arrDeptAirport, String airline, int flightNo, String arrDeptTime) {
        this.arrivalDepartureAirport = arrDeptAirport;
        this.airline = airline;
        this.flightNumber = flightNo;
        this.arrivalDepartureTime = arrDeptTime;
    }

    public String getAirport() {
        return arrivalDepartureAirport;
    }

    public void setAirport(String arrDeptAirport) {
        this.arrivalDepartureAirport = arrDeptAirport;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNo) {
        this.flightNumber = flightNo;
    }

    public int getNumericTime() {
        return arrivalDepartureTimeNumeric;
    }

    public String getTime() {
        return arrivalDepartureTime;
    }

    public void setTime(int hours, int minutes) {
        if (minutes < 10)
            this.arrivalDepartureTime = Integer.toString(hours) + ":0" + Integer.toString(minutes);
        else
            this.arrivalDepartureTime = Integer.toString(hours) + ":" + Integer.toString(minutes);

        this.arrivalDepartureTimeNumeric = hours*60+minutes;
    }

    @Override
    public int compareTo(Flight other) {
        return Integer.compare(this.arrivalDepartureTimeNumeric, other.arrivalDepartureTimeNumeric);
    }
}
