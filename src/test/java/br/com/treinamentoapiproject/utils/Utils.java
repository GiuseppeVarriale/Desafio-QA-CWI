package br.com.treinamentoapiproject.utils;

import org.json.simple.JSONObject;

public class Utils {


    public static  String getContractsBasePath(String pack, String contract) {
        return System.getProperty("user.dir")
                + "/src/test/java/br/com/treinamentoapiproject/tests/"
                + pack
                + "/contracts/"
                + contract
                + ".json";
    }

    public static JSONObject validPayloadBooking(){
        JSONObject payload = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        bookingDates.put("checkin", "2021-01-01");
        bookingDates.put("checkout","2021-03-06");

        payload.put("firstname", "Ronaldo");
        payload.put("lastname", "Fenomeno");
        payload.put("totalprice", 123);
        payload.put("depositpaid", true);
        payload.put("bookingdates", bookingDates);
        payload.put("additionalneeds", "Breakfast");

        return payload;
    }
}
