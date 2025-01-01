package com.group09.sushix_api.dto;

public interface ReservationsDTO {
    Integer getRsId();
    Integer getCustId();
    Integer getNumOfGuests();
    String getRsDateTime();
    String getArrivalDateTime();
    String getRsNotes();
    String getCustName();
    String getCustPhoneNumber();
    String getCustEmail();
}
