package com.droidsoft.pnrtracker.webinterface;

import com.droidsoft.pnrtracker.datatypes.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {

	static String regexExpressvalidTicket = "<TD class=\"table_border_both\">(.*)</TD>";
	static Pattern pattern = Pattern.compile(regexExpressvalidTicket);

	public static Ticket parseHTMLtoTicketType(String htmlTicket) {
		Ticket ticket = new Ticket(false);

		Matcher matcher = pattern.matcher(htmlTicket);

/*		while(matcher.find())
			System.out.println(matcher.group(1));*/
		
		if (matcher.find()) {
			parseIternary(matcher,ticket);
			
			parsePassengerInfo(matcher, ticket);

		}

		return ticket;

	}

	private static void parsePassengerInfo(Matcher matcher, Ticket ticket) {
		
		while(matcher.find()) {
			String seatNumber = null;
			String status = null;
			
			//Seating Details
			matcher.find();
			seatNumber = matcher.group(1).trim();
			seatNumber = removeBoldTags(seatNumber);
			
			//Ticket Status
			matcher.find();
			status = matcher.group(1).trim().toUpperCase();
			status = removeBoldTags(status);
			
			ticket.addPassengerData(seatNumber, status);
			
		}
		
		
		if(ticket.getPassengerCount() > 0)
			ticket.setIsValid(true);
		
	}

	/**
	 * @param taggedString
	 * @return
	 */
	private static String removeBoldTags(String taggedString) {
		taggedString = taggedString.replace("</B>", "");
		taggedString = taggedString.replace("<B>", "");
		taggedString = taggedString.trim();
		return taggedString;
	}

	private static void parseIternary(Matcher matcher, Ticket ticket) {
		
		//Train Name
		ticket.setTrainNumber(matcher.group(1).trim());
		
		//Train Name
		matcher.find();
		ticket.setTrainName(matcher.group(1).trim());
		
		//Travel Date
		matcher.find();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		try {
			ticket.setTravelDate(sdf.parse(matcher.group(1).trim()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//From Station
		matcher.find();
		ticket.setFromStation(matcher.group(1).trim());
		
		//To Station
		matcher.find();
		ticket.setToStation(matcher.group(1).trim());
		
		//Reserved Upto
		matcher.find();
		ticket.setReserved_toStation(matcher.group(1).trim());
		
		//Boarding Station
		matcher.find();
		ticket.setBoardingStation(matcher.group(1).trim());
		
		//Travel Class
		matcher.find();
		ticket.setReservationClass(matcher.group(1).trim());
		
		//Sync Date time;
		ticket.setDataTime(new Date(System.currentTimeMillis()));
	}

}
