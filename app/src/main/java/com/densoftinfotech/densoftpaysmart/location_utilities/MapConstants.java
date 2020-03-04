package com.densoftinfotech.densoftpaysmart.location_utilities;

public class MapConstants {
    public static final String workinghour_from = "9:00";
    public static final String workinghour_to = "18:00";

    public static final long update_after_every = 600000; //1 * 60 * 1000 millis
    public static final long distance_greater_than = 150; //150m



    public static final long location_request_interval = 10000; //10 * 1000 millis
    public static final long location_request_fastestinterval = 5000; //5 * 1000 millis

    public static final int PERMISSION_ACCESS_LOCATION_CODE = 21;

    public static final String LOCATION_MESSAGE = "LOCATION_DATA";

    public static final String ACTION_CURRENT_LOCATION_BROADCAST = "current.location";

    public static final String ACTION_PERMISSION_DEINED = "location.deined";
}
