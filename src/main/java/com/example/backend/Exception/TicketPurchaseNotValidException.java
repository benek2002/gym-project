package com.example.backend.Exception;

public class TicketPurchaseNotValidException extends  RuntimeException{

    public TicketPurchaseNotValidException(String message){
        super(message);
    }
}
