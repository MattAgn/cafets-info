package com.example.matt.locatecafets;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Database {

    // Joensuu
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

    // Kuopio
    static private Cafeteria round = new Cafeteria(
            "Round",
            62.889085, 27.630472,
            "Microkatu 1, 70210 Kuopio",
            "https://www.antell.fi/ravintolat/ravintolahaku/ravintola/antell-ravintola-round-kuopio.html");

    static private Cafeteria bistro = new Cafeteria(
            "Log in Bistro",
            62.888494,  27.630003,
            "Microkatu 1, 70210 Kuopio",
            "https://www.antell.fi/ravintolat/ravintolahaku/ravintola/antell-ravintola-log-in-bistro-kuopio.html");

    static private Cafeteria tietoteknia = new Cafeteria(
            "Tietoteknia",
            62.890957, 27.636484,
            "Savilahdentie 6 , 70210 Kuopio",
            "http://www.amica.fi/ravintolat/ravintolat-kaupungeittain/kuopio/ita-suomen-yliopistotietoteknia/\n");

    static private Cafeteria snellmania = new Cafeteria(
            "Snellmania",
            62.892494, 27.635557,
            "Yliopistonranta 1 E , 70210 Kuopio",
            "http://www.amica.fi/ravintolat/ravintolat-kaupungeittain/kuopio/ita-suomen-yliopisto--snellmania/");

    static private Cafeteria canthia = new Cafeteria(
            "Canthia",
            62.895815, 27.641444,
            "Yliopistonranta 1C,Bporras, 70210 Kuopio",
            "http://www.amica.fi/en/restaurants/ravintolat-kaupungeittain/kuopio/ita-suomen-yliopistocanthia/");

    static private Cafeteria mediteknia = new Cafeteria(
            "Mediteknia",
            62.895857,  27.640444,
            "Yliopistonranta 1 B , 70210 Kuopio",
            "http://www.amica.fi/en/restaurants/ravintolat-kaupungeittain/kuopio/ita-suomen-yliopisto--mediteknia/");

    static private Cafeteria kaarre = new Cafeteria(
            "Kaarre",
            62.898102, 27.650024,
            "KYS, rakennus 2, 1 krs., 70210 Kuopio",
            "https://www.sydanmerkki.fi/ravintolat/lounasravintola-kaarre");

    static private Cafeteria musiikkikeskuksen = new Cafeteria(
            "Musiikkikeskuksen",
            62.888233, 27.678886,
            "Kuopionlahdenkatu 23, 70100 Kuopio",
            "http://www.kanresta.fi/lounas/kuopio/kuopion+musiikkikeskuksen+ravintola/");

    static private Cafeteria hilima = new Cafeteria(
            "Hilima",
            62.897372, 27.646994,
            "Puijonlaaksontie 2, 70210 Kuopio",
            "https://www.sydanmerkki.fi/ravintolat/lounasravintola-hilima");

    static private Cafeteria savonia = new Cafeteria(
            "Savonia AMK",
            62.898029, 27.663021,
            "Opistotie 2 70200 Kuopio",
            "https://www.sodexo.fi/savonia");

    static private List<Cafeteria> cafetList = Arrays.asList(
            aura, carelia, futura, louhi, puisto, forum, metla,
            savonia, hilima, musiikkikeskuksen, kaarre, mediteknia, round, bistro, canthia, snellmania, tietoteknia);

    static private List<Integer> distanceValues = Arrays.asList(30000, 100, 250, 500, 1000);

    static public List<Cafeteria> getCafeterias() { return cafetList;}

    static public  List<Integer> getDistanceValues() {return distanceValues;}

}
