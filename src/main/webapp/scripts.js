function showWeather() {
    var listsToSkip = document.getElementById("forecastInput").value * 8;
    if (listsToSkip == 40) 
        listsToSkip -= 1;

    document.getElementById("forecastData").style.display = "inline";
    $.getJSON("http://api.openweathermap.org/data/2.5/forecast?lat=54.38&lon=18.47&units=metric&lang=pl&APPID=2508abecb6d9037229e719a6adc445af", function (weatherCond) {
        document.getElementById("forecastDateTime").textContent = "Data: " + weatherCond.list[listsToSkip].dt_txt;
        document.getElementById("weatherImg").src = "http://openweathermap.org/img/w/" + weatherCond.list[listsToSkip].weather[0].icon + ".png";
        document.getElementById("temperature").textContent = "Temperatura: " + weatherCond.list[listsToSkip].main.temp + " °C";
        document.getElementById("weatherDesc").textContent = "Warunki: " + weatherCond.list[listsToSkip].weather[0].description;
        document.getElementById("pressure").textContent = "Ciśnienie: " + weatherCond.list[listsToSkip].main.pressure + " hPa";
        document.getElementById("humidity").textContent = "Wilgotność: " + weatherCond.list[listsToSkip].main.humidity + "%";
        document.getElementById("windSpeed").textContent = "Prędkość wiatru: " + weatherCond.list[listsToSkip].wind.speed + " m/s";
    });
}
    
function showDistance() {
    var originCity = document.getElementById("originCity").value;
    var destinationCity = document.getElementById("destinationCity").value;
    var travelMode = document.getElementById("travelMode").value;

    $.getJSON("airport/services/distance", {"originCity": originCity, "destinationCity": destinationCity, "travelMode": travelMode}, function(result) {
        document.getElementById("distanceInfo").style.display = "inline";
            
        document.getElementById("travelDistance").textContent = "Odległość: " + result.rows[0].elements[0].distance.humanReadable;
        document.getElementById("travelTime").textContent = "Przewidywany czas: " + result.rows[0].elements[0].duration.humanReadable;
        document.getElementById("travelCities").textContent = result.originAddresses[0] + " ---> " + result.destinationAddresses[0];
    }).fail(function() {alert ("Brak danych dla podanej trasy!")});
}
    
function showFlights() {
    var flightsCity = document.getElementById("flightsCity").value;
    var flightsType;
    var flightsRadios = document.getElementsByName("flightsTypes");
    for (var i = 0, length = flightsRadios.length; i < length; i++) {
        if (flightsRadios[i].checked) {
            flightsType = flightsRadios[i].value;
        }
    }
        
    $.getJSON("airport/services/flights", {"searchedAirport": flightsCity, "flightType": flightsType}, function(result) {
        document.getElementById("flightsInfo").style.display = "inline";
            
        if (result.length == 0) {       //Result is null
            document.getElementById("flightsTypeAndCity").textContent = "Nie znaleziono żadnych lotów!";
            document.getElementById("flightsTable").style.display = "none";
        }
        else {                          //Result is not null      
            document.getElementById("flightsTable").style.display = "table";
                
            if (flightsType == "arrivals") { 
                document.getElementById("flightsTypeAndCity").textContent = "Przyloty z lotniska " + flightsCity + ":";
            }
            else if (flightsType == "departures") {
                document.getElementById("flightsTypeAndCity").textContent = "Odloty do lotniska " + flightsCity + ":";
            }

            var table = document.getElementById("flightsTableBody");
            $("#flightsTable tbody tr").remove();       //Clear all but header row of the table
            $.each(result, function(i, flight) {
                var row = table.insertRow(-1);
                var airlineCell = row.insertCell(0);
                var flightNoCell = row.insertCell(1);
                var timeCell = row.insertCell(2);
                airlineCell.innerHTML = flight.airline;
                flightNoCell.innerHTML = flight.flightNo;
                timeCell.innerHTML = flight.time;
            });
        }
    });
}