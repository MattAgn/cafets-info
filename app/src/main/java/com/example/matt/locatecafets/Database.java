package com.example.matt.locatecafets;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Database {

    // TODO: issue with natura, futura and borealis, all at the same address
    //Initialising data
    static private Cafeteria aura = new Cafeteria(
            "Aura",
            62.6036007, 29.7420141,
            "Yliopistokatu 2 , 80100 Joensuu",
            "http://www.amica.fi/aura");
    static private Cafeteria carelia = new Cafeteria(
            "Carelia",
            62.6036502, 29.7442225,
            "Yliopistokatu 4, 80100 Joensuu",
            "http://www.amica.fi/en/restaurants/ravintolat-kaupungeittain/joensuu/studentrestaurant_carelia/");
    static private Cafeteria futura = new Cafeteria(
            "Futura",
            62.6050013, 29.7388577,
            "Yliopistokatu 7 , 80100 Joensuu",
            "http://www.amica.fi/ravintolat/ravintolat-kaupungeittain/joensuu/Futura/");
    static private Cafeteria louhi = new Cafeteria(
            "Louhi",
            62.5982607, 29.743439,
            "Länsikatu 15, 80110 Joensuu",
            "http://mehtimakiravintolat.fi/ravintola-louhi/");
    static private Cafeteria puisto = new Cafeteria(
            "Puisto",
            62.5986341, 29.7425244,
            "Länsikatu 15, 80110 Joensuu",
            "http://mehtimakiravintolat.fi/ravintola-puisto/");
    static private Cafeteria forum = new Cafeteria(
            "Kahvila Forum",
            62.5979712, 29.7403693,
            "Linnunlahdentie 2, 80110 Joensuu",
            "http://mehtimakiravintolat.fi/kahvila-forum/");
    static private Cafeteria metla = new Cafeteria(
            "Metla",
            62.6049134, 29.7414604,
            "Yliopistokatu 6 , 80100 Joensuu",
            "https://www.visiitti.fi/lounasravintolat/");

    static private List<Cafeteria> cafetList = Arrays.asList(aura, carelia, futura, louhi, puisto, forum, metla);

    static private List<Integer> distanceValues = Arrays.asList(-1, 100, 250, 500, 1000);

    static public List<Cafeteria> getCafeterias() { return cafetList;}

    static public  List<Integer> getDistanceValues() {return distanceValues;}

}
