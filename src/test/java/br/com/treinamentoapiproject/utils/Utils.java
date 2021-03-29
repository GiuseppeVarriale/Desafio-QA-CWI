package br.com.treinamentoapiproject.utils;

import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public static String getContractsBasePath(String pack, String contract) {
        return System.getProperty("user.dir")
                + "/src/test/java/br/com/treinamentoapiproject/tests/"
                + pack
                + "/contracts/"
                + contract
                + ".json";
    }

    public static JSONObject validPayloadBooking() {
        JSONObject payload = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        bookingDates.put("checkin", "2021-03-09");
        bookingDates.put("checkout", "2021-03-11");

        payload.put("firstname", "Bob");
        payload.put("lastname", "Esponja");
        payload.put("totalprice", 123);
        payload.put("depositpaid", true);
        payload.put("bookingdates", bookingDates);
        payload.put("additionalneeds", "Breakfast");

        return payload;
    }

    public static Boolean dateIsBeforeOrEqualThan(String toCompare, String control) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateToCompare = format.parse(toCompare);
        Date dateControl = format.parse(control);

        return dateToCompare.before(dateControl) || dateToCompare.equals(control);
    }


    public static Boolean dateIsAfterOrEqualThan(String toCompare, String control) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateToCompare = format.parse(toCompare);
        Date dateControl = format.parse(control);

        return dateToCompare.after(dateControl) || dateToCompare.equals(control);
    }
}
