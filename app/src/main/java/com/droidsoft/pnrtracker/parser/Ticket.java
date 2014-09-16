package com.droidsoft.pnrtracker.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mitesh on 14. 9. 10.
 */
public class Ticket implements Serializable {

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
    private ArrayList<PassengerData> passengerData;


    public Ticket(Boolean isValid) {
        this.isValid = isValid;

        passengerData = new ArrayList<PassengerData>();
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

    public class PassengerData implements Serializable {

        private static final long serialVersionUID = 7146366791438575635L;
        private String seatNumber, bookingStatus;

        public String getSeatNumber() {
            return seatNumber;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }
    }
}
