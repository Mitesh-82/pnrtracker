package com.droidsoft.pnrtracker.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mitesh on 14. 9. 10.
 */
public class Ticket implements Serializable, Comparable<Ticket> {

    private static final long serialVersionUID = 3251254925733195351L;
    private Boolean isValid;
    private String pnrNo;
    private String trainName;
    private String trainNumber;
    private String fromStation;
    private String toStation;
    private String reserved_toStation;
    private String boardingStation;
    private String reservationClass;
    private Date travelDate;
    private Date dataTime;
    private int racCount;
    private int waitingCount;
    private ArrayList<PassengerData> passengerData;

    public Ticket(Boolean isValid) {
        this.isValid = isValid;

        passengerData = new ArrayList<PassengerData>();
    }

    public static Boolean IsValidTicket(Ticket ticket) {
        if ((ticket != null) && (ticket.getIsValid()))
            return true;
        return false;
    }

    public int getRacCount() {
        return racCount;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getPnrNo() {
        return pnrNo;
    }

    public void setPnrNo(String pnrNo) {
        this.pnrNo = pnrNo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getReserved_toStation() {
        return reserved_toStation;
    }

    public void setReserved_toStation(String reserved_toStation) {
        this.reserved_toStation = reserved_toStation;
    }

    public String getBoardingStation() {
        return boardingStation;
    }

    public void setBoardingStation(String boardingStation) {
        this.boardingStation = boardingStation;
    }

    public String getReservationClass() {
        return reservationClass;
    }

    public void setReservationClass(String reservationClass) {
        this.reservationClass = reservationClass;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public ArrayList<PassengerData> getPassengerData() {
        return passengerData;
    }

    public void addPassengerData(String seatNumber, String status) {
        PassengerData pd = new PassengerData();
        pd.seatNumber = seatNumber;
        pd.bookingStatus = status;

        if (status.contains("W/L")) {
            pd.statusType = PassengerData.STATUS_WAITING;
            waitingCount++;
        } else if (status.contains("RAC")) {
            pd.statusType = PassengerData.STATUS_RAC;
            racCount++;
        } else {
            pd.statusType = PassengerData.STATUS_CONFIRMED;
        }

        passengerData.add(pd);
    }

    public int getPassengerCount() {
        if (passengerData == null)
            return 0;

        return passengerData.size();
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    @Override
    public int compareTo(Ticket another) {
        return travelDate.compareTo(another.getTravelDate());
    }

    public boolean equals(Ticket ticket) {
        boolean isEqual = true;

        if (!pnrNo.equals(ticket.getPnrNo())) {
            return false;
        }

        return isEqual;
    }

    public class PassengerData implements Serializable {

        public static final int STATUS_CONFIRMED = 0;
        public static final int STATUS_RAC = 1;
        public static final int STATUS_WAITING = 2;


        private static final long serialVersionUID = 7146366791438575635L;
        private String seatNumber, bookingStatus;
        private int statusType;

        public int getStatusType() {
            return statusType;
        }

        public String getSeatNumber() {
            return seatNumber;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public boolean equals(PassengerData passengerData) {
            return (seatNumber.equals(passengerData.getSeatNumber()) && bookingStatus.equals(passengerData.getBookingStatus()));
        }
    }
}
