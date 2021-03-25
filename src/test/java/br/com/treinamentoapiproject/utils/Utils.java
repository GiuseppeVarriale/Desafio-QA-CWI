package br.com.treinamentoapiproject.utils;

public class Utils {


    public static  String getContractsBasePath(String pack, String contract) {
        return System.getProperty("user.dir")
                + "/src/test/java/br/com/treinamentoapiproject/tests/"
                + pack
                + "/contracts/"
                + contract
                + ".json";
    }
}
