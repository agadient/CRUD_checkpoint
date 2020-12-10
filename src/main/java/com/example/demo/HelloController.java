package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@RestController
public class HelloController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello from Spring!";
    }

    @GetMapping("/math/pi")
    public String pi() {
        return "3.141592653589793!";
    }

    @GetMapping("/math/calculate")
    public String calculate(@RequestParam Map querystring) {
        return new MathService().eval(querystring);
    }

    @PostMapping("/math/sum")
    public String sum(@RequestParam String[] n) {
        String result = "";
        Integer sum = 0;
        for (int i = 0; i < n.length-1; i++) {
            sum += Integer.parseInt(n[i]);
            result = result.concat(String.format("%s + ", n[i]));
        }
        sum += Integer.parseInt(n[n.length-1]);
        result = result.concat(String.format("%s = %d", n[n.length-1], sum));
        return result;
    }

    @RequestMapping("/math/volume/{l}/{w}/{h}")
    public String volume(@PathVariable int l, @PathVariable int w, @PathVariable int h) {
        return String.format("The volume of a %dx%dx%d rectangle is %d", l, w, h, l*w*h);
    }

    @PostMapping("/math/area")
    public String area(@RequestParam(value = "type", defaultValue = "circle") String type,
                       @RequestParam(value = "radius", defaultValue = "0") Integer radius,
                       @RequestParam(value = "length", defaultValue = "0") Integer len,
                       @RequestParam(value = "height", defaultValue = "0") Integer height) {
        String result = "";
        if (type.equals("circle")) {
            result = String.format("Area of a circle with a radius of %d is %.5f", radius,
                    Math.pow(radius, 2)*3.141592653589793);
        } else {
            result = String.format("Area of a %dx%d rectangle is %d", len, height, len*height);
        }
        return result;
    }

    @GetMapping("/flights/flight")
    public Flight getFlight() throws ParseException {
        return constructFlightOne();
    }

    @GetMapping("/flights")
    public Flight[] getFlights() throws ParseException {
        Flight[] flights = {constructFlightOne(), constructFlightTwo()};
        return flights;
    }

    @PostMapping("/flights/tickets/total")
    public Sum getTotal(@RequestBody Flight flight) {
        Sum cost = new Sum();
        cost.result = 0;
        for (int i = 0; i < flight.tickets.length; i++) {
                cost.result += flight.tickets[i].price;
        }
        return cost;
    }


    public static Flight constructFlightOne() {
        Passenger passenger = new Passenger();
        passenger.firstName = "Some Name";
        passenger.lastName = "Some other name";
        Ticket ticket = new Ticket();
        ticket.passenger = passenger;
        ticket.price = new Integer(200);
        Ticket[] tickets = {ticket};
        Flight flight = new Flight();
        flight.tickets = tickets;
        Calendar departdate = new Calendar.Builder().setTimeZone(TimeZone.getTimeZone("UTC"))
                .setDate(2017,3,21).setTimeOfDay(14,34,00).build();
        flight.departs = departdate.getTime();
        return flight;
    }

    public static Flight constructFlightTwo() {
        Passenger passenger = new Passenger();
        passenger.firstName = "Some Name";
        passenger.lastName = null;
        Ticket ticket = new Ticket();
        ticket.passenger = passenger;
        ticket.price = new Integer(400);
        Ticket[] tickets = {ticket};
        Flight flight = new Flight();
        flight.tickets = tickets;
        Calendar departdate = new Calendar.Builder().setTimeZone(TimeZone.getTimeZone("UTC"))
                .setDate(2017,3,21).setTimeOfDay(14,34,00).build();
        flight.departs = departdate.getTime();
        return flight;
    }

    public static class Sum {
        Integer result = 0;
        public Integer getResult() {
            return result;
        }
    }

    public static class Flight {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="UTC")

        private Date departs;
        private Ticket[] tickets;

        public Ticket[] getTickets() {
            return tickets;
        }

        public Date getDeparts() {
            return departs;
        }}
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Passenger {
        private String firstName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }}

    public static class Ticket {
        private Passenger passenger;
        private Integer price;

        public Passenger getPassenger() {
            return passenger;
        }

        public Integer getPrice() {
            return price;
        }

    }
}