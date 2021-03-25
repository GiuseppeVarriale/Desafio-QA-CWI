package br.com.treinamentoapiproject.tests.base.tests;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;

public class BaseTest {

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI= "https://treinamento-api.herokuapp.com/";
    }
}
