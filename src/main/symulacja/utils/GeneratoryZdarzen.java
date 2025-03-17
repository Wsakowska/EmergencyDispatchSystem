package main.symulacja.utils;

import main.symulacja.model.Lokalizacja;
import main.symulacja.model.Wypadek;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratoryZdarzen {

    private static final Random RAND = new Random();

    // 5 typów, tak jak w wymaganiach: wypadek drogowy, pożar, katastrofa, wypadek w fabryce, awaria chemiczna
    private static final String[] TYPY_WYPADKOW = {
            "wypadek drogowy",
            "pożar",
            "katastrofa",
            "wypadek w fabryce",
            "awaria chemiczna"
    };

    /**
     * Generuje w jednej turze 0..maxLiczba wypadków (z prawdopodobieństwem 'szansa'),
     * każdy z losowym typem, współrzędnymi, liczbą poszkodowanych.
     *
     * @param tura      - numer tury (znacznikCzasu) dla nowo powstających wypadków
     * @param szansa    - prawdopodobieństwo (np. 0.3 => 30%) że w ogóle pojawi się cokolwiek
     * @param maxLiczba - maksymalna liczba wypadków w jednej turze
     * @return lista Wypadek
     */
    public static List<Wypadek> generujWypadkiLosowo(int tura, double szansa, int maxLiczba) {
        List<Wypadek> nowelizt = new ArrayList<>();

        // losujemy, czy w tej turze w ogóle coś się pojawi
        double r = RAND.nextDouble();
        if (r < szansa) {
            // ile wypadków? 1..maxLiczba
            int ile = 1 + RAND.nextInt(maxLiczba);

            for (int i = 0; i < ile; i++) {
                // losowy typ
                String typ = TYPY_WYPADKOW[RAND.nextInt(TYPY_WYPADKOW.length)];

                // losowa lokalizacja (np. w zakresie 0..20)
                double x = RAND.nextInt(21);
                double y = RAND.nextInt(21);

                // 1..6 poszkodowanych
                int poszkodowani = 1 + RAND.nextInt(6);

                Wypadek w = new Wypadek(
                        tura,
                        new Lokalizacja(x, y),
                        typ,
                        poszkodowani
                );
                nowelizt.add(w);
            }
        }
        return nowelizt;
    }
}
