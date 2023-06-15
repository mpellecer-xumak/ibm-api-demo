package com.wdm.configuration.api.controller;

public final class HttpCodes {

    public static final String HTTP_200_STATUS = "200";
    public static final String HTTP_201_STATUS = "201";
    public static final String HTTP_400_STATUS = "400";
    public static final String HTTP_404_STATUS = "404";
    public static final String HTTP_409_STATUS = "409";
    public static final String HTTP_500_STATUS = "500";

    public static final String HTTP_400_MESSAGE = "There are problems with the request payload";
    public static final String HTTP_404_MESSAGE = "Requested information not found";
    public static final String HTTP_409_MESSAGE = "There are issues with the data provided in the payload";
    public static final String HTTP_500_MESSAGE = "Problem handling the request.";


    private HttpCodes() {}
}
