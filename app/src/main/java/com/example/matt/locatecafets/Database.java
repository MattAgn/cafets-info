package com.example.matt.locatecafets;

/**
 * Created by matt on 20/04/18.
 */

public class Database {

    //Initialising data
    static private Cafeteria aura = new Cafeteria("Aura",
            62.6036007, 29.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    static private Cafeteria carelia = new Cafeteria("Carelia",
            62.6048478, 29.7422755,
            "Yliopistokatu 4, 80100 Joensuu");
    static private Cafeteria futura = new Cafeteria("Futura, Natura and Metria",
            62.6036007, 29.73201292,
            "Yliopistokatu 7 , 80100 Joensuu");
    static private Cafeteria pipetti = new Cafeteria("Pipetti",
            62.6036007, 29.730141,
            "Yliopistokatu 2 , 80100 Joensuu");
    static private Cafeteria kuutti = new Cafeteria("Kuutti",
            62.6036007, 26.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    static private Cafeteria pihlaja = new Cafeteria("Pihlaja",
            62.6036007, 20.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");
    static private Cafeteria metla = new Cafeteria("Metla",
            62.6136007, 29.8270141,
            "Yliopistokatu 2 , 80100 Joensuu");
    static private Cafeteria verola = new Cafeteria("Verola",
            62.6036007, 22.7420141,
            "Yliopistokatu 2 , 80100 Joensuu");

    static private Cafeteria[] cafetList = {aura, carelia, futura, pipetti, kuutti, pihlaja, metla, verola};

    static public Cafeteria[] getCafeterias() { return cafetList;}

}
