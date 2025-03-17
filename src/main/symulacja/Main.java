package main.symulacja;

import java.util.ArrayList;
import java.util.List;
import main.symulacja.controller.Dyspozytor;
import main.symulacja.model.Lokalizacja;
import main.symulacja.model.Szpital;
import main.symulacja.model.Wypadek;
import main.symulacja.model.jednostki.*;
import main.symulacja.utils.GeneratoryZdarzen;

public class Main {

    public static void main(String[] args) {

        Dyspozytor dyspozytor = new Dyspozytor();

        // Dodajemy przykładowe jednostki (Policja/Pogotowie/Straz)
        dyspozytor.dodajJednostke(new Policja("POL-1", new Lokalizacja(0,0)));
        dyspozytor.dodajJednostke(new Policja("POL-2", new Lokalizacja(3,3)));
        dyspozytor.dodajJednostke(new Pogotowie("AMB-1", new Lokalizacja(0,5)));
        dyspozytor.dodajJednostke(new Pogotowie("AMB-2", new Lokalizacja(3,3)));
        dyspozytor.dodajJednostke(new Pogotowie("AMB-3", new Lokalizacja(3,7)));
        dyspozytor.dodajJednostke(new Pogotowie("AMB-4", new Lokalizacja(2,10)));
        dyspozytor.dodajJednostke(new StrazPozarna("STR-1", new Lokalizacja(10,10)));
        dyspozytor.dodajJednostke(new StrazPozarna("STR-2", new Lokalizacja(12,10)));

        // Szpitale
        Szpital szpitalA = new Szpital("Szpital A", 3, new Lokalizacja(2,2));
        Szpital szpitalB = new Szpital("Szpital B", 5, new Lokalizacja(8,8));
        dyspozytor.dodajSzpital(szpitalA);
        dyspozytor.dodajSzpital(szpitalB);

        // LISTA zaplanowanych wypadków
        List<Wypadek> zaplanowaneWypadki = new ArrayList<>();
        // (możesz wstawić więcej)
        zaplanowaneWypadki.add(new Wypadek(0, new Lokalizacja(3,3), "wypadek drogowy", 2));
        zaplanowaneWypadki.add(new Wypadek(2, new Lokalizacja(10,9), "pożar", 3));
        zaplanowaneWypadki.add(new Wypadek(3, new Lokalizacja(5,5), "katastrofa", 5));
        zaplanowaneWypadki.add(new Wypadek(4, new Lokalizacja(2,10), "wypadek w fabryce", 4));
        zaplanowaneWypadki.add(new Wypadek(5, new Lokalizacja(12,3), "awaria chemiczna", 6));

        int liczbaTur = 20;
        for (int aktualnaTura = 0; aktualnaTura < liczbaTur; aktualnaTura++) {
            System.out.println("=== Tura " + aktualnaTura + " ===");

            // 1. Obsługa zaplanowanych wypadków w tej turze
            for (Wypadek w : zaplanowaneWypadki) {
                if (w.getZnacznikCzasu() == aktualnaTura) {
                    dyspozytor.zglaszajWypadek(w);
                    dyspozytor.generujZgloszenieZWypadku(w);
                    System.out.println("Zgłoszono (zaplanowany) wypadek: " + w);
                }
            }

            // 2. Losowe zdarzenia (np. 20% szansy, max 1 wypadki na turę)
            //    (Możesz zmienić 0.2 -> 0.3, itp.)
            List<Wypadek> losoweWypadki = GeneratoryZdarzen.generujWypadkiLosowo(
                    aktualnaTura,
                    0.1,
                    1
            );
            for (Wypadek w : losoweWypadki) {
                dyspozytor.zglaszajWypadek(w);
                dyspozytor.generujZgloszenieZWypadku(w);
                System.out.println("Zgłoszono (losowo) wypadek: " + w);
            }

            // 3. Dyspozytor przypisuje jednostki
            dyspozytor.przypiszJednostki();

            // 4. Jednostki się poruszają
            dyspozytor.ruchJednostek();

            // 5. Monitorowanie (czy karetki dotarły, czy zabrały do szpitala, czy zgłoszenia się kończą)
            dyspozytor.monitorujZgloszeniaIWypadki();

            // 6. Wypisujemy stan (opcjonalnie)
            wypiszStan(dyspozytor);
        }
    }

    private static void wypiszStan(Dyspozytor d) {
        System.out.println("-- Aktywne zgłoszenia --");
        d.getAktywneZgloszenia().forEach(z -> System.out.println("   " + z));

        System.out.println("-- Zakończone zgłoszenia --");
        d.getZakonczoneZgloszenia().forEach(z -> System.out.println("   " + z));

        System.out.println("-- Jednostki --");
        d.getWszystkieJednostki().forEach(j -> System.out.println("   " + j));

        System.out.println("-- Szpitale --");
        d.getSzpitale().forEach(s -> System.out.println("   " + s));

        System.out.println();
    }
}