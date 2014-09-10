package com.droidsoft.pnrtracker.parser;

import android.util.JsonReader;

import com.droidsoft.pnrtracker.views.Ticket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by mitesh on 14. 9. 10.
 */
public class TicketDataParser {


    private static void readPassengerData(JsonReader reader, Ticket ticket) throws IOException {

        reader.beginArray();

        while (reader.hasNext()) {
            String seat_number = null, status = null;

            reader.beginObject();

            String name = reader.nextName();
            if (name.equals("seat_number")) {
                seat_number = reader.nextString();
            }

            name = reader.nextName();
            if (name.equals("status")) {
                status = reader.nextString();
            }
            reader.endObject();

            ticket.addPassengerData(seat_number, status);

        }

        reader.endArray();
    }

    public static void readTicketResponse(String input) {

        JsonReader jsonReader;
        String name, isValid;

        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        jsonReader = new JsonReader(new InputStreamReader(inputStream));
        Ticket ticket = new Ticket(false);

        try {
            jsonReader.beginObject();

            name = jsonReader.nextName();
            if (name.equals("status")) {
                readTicketStatus(jsonReader, ticket);
            }

            name = jsonReader.nextName();//data
            if (name.equals("data"))
                readTicketData(jsonReader, ticket);


            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

    private static void readTicketData(JsonReader jsonReader, Ticket ticket) throws IOException {
        String name;

        jsonReader.beginObject();

        name = jsonReader.nextName(); //pnr
        if (name.equals("pnr")) {
            readPNR(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //train_name
        if (name.equals("train_name")) {
            readTrainName(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //train_number
        if (name.equals("train_number")) {
            readTrainNumber(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //from
        if (name.equals("from")) {
            readSourceStation(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //to
        if (name.equals("to")) {
            readDestinationStation(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //reservedto
        if (name.equals("reservedto")) {
            readReservedUptoStation(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //board
        if (name.equals("board")) {
            readBoardingStation(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //class
        if (name.equals("class")) {
            readReservationClass(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //travel_date
        if (name.equals("travel_date")) {
            readTrainNumber(jsonReader, ticket);
        }

        name = jsonReader.nextName(); //passenger
        if (name.equals("passenger")) {
            readPassengerData(jsonReader, ticket);
        }


        jsonReader.endObject();
    }


    private static void readReservationClass(JsonReader jsonReader, Ticket ticket) throws IOException {
        String data = jsonReader.nextString();

        ticket.setReservationClass(data);
    }

    private static void readBoardingStation(JsonReader jsonReader, Ticket ticket) throws IOException {
        String data = jsonReader.nextString();

        ticket.setBoardingStation(data);
    }

    private static void readReservedUptoStation(JsonReader jsonReader, Ticket ticket) throws IOException {
        String data = jsonReader.nextString();

        ticket.setReserved_toStation(data);
    }

    private static void readDestinationStation(JsonReader jsonReader, Ticket ticket) throws IOException {
        String data = jsonReader.nextString();

        ticket.setToStation(data);
    }

    private static void readSourceStation(JsonReader jsonReader, Ticket ticket) throws IOException {
        String data = jsonReader.nextString();

        ticket.setFromStation(data);
    }

    private static void readTrainNumber(JsonReader jsonReader, Ticket ticket) throws IOException {
        String data = jsonReader.nextString();

        ticket.setTrainNumber(data);
    }

    private static void readTrainName(JsonReader jsonReader, Ticket ticket) throws IOException {
        String trainName = jsonReader.nextString();

        ticket.setTrainName(trainName);
    }

    private static void readPNR(JsonReader jsonReader, Ticket ticket) throws IOException {
        String pnr = jsonReader.nextString();

        ticket.setPnrNo(pnr);
    }

    private static void readTicketStatus(JsonReader jsonReader, Ticket ticket) throws IOException {
        String status = jsonReader.nextString();

        if (status.equals("OK")) {
            ticket.setIsValid(true);
        }
    }
}
