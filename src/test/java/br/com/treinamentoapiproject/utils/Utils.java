package br.com.treinamentoapiproject.utils;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static String getContractsBasePath(String pack, String contract) {
        return System.getProperty("user.dir")
                + "/src/test/java/br/com/treinamentoapiproject/tests/"
                + pack
                + "/contracts/"
                + contract
                + ".json";
    }

    @Step("Gerar payload com Faker")
    public static JSONObject validPayloadBooking() throws Exception {
        JSONObject payload = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        Faker faker = new Faker(new Locale("pt-BR"));
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
        Date fakeDateFrom = DateUtils.addYears(new Date(),-5);
        Date fakeDateTo = DateUtils.addDays(new Date(), 161);
        Date checkin = faker.date().between(fakeDateFrom,fakeDateTo);
        fakeDateTo = DateUtils.addDays(checkin, 376);
        Date checkout = faker.date().between(checkin,fakeDateTo);


        bookingDates.put("checkin", format.format(checkin));
        bookingDates.put("checkout", format.format(checkout));

        payload.put("firstname", faker.name().firstName());
        payload.put("lastname", faker.name().lastName());
        payload.put("totalprice", faker.number().numberBetween(200,4000));
        payload.put("depositpaid", faker.bool().bool());
        payload.put("bookingdates", bookingDates);
        payload.put("additionalneeds", faker.food().dish() + " to lunch");

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

    @Step("Codificar e retornar basic Authorization ")
    public static String basicAuthorizationEncode(String username, String password) {

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);

    }

}
