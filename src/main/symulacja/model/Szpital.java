package main.symulacja.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Szpital {
    private String nazwa;
    private int pojemnosc;            
    private int dostepneMiejsca;      
    private Lokalizacja lokalizacja;

    // Zamiast List<Osoba>, zróbmy mapę: (Osoba -> ile tur leży)
    private Map<Osoba, Integer> pacjenciNaOddziale;

    public Szpital(String nazwa, int pojemnosc, Lokalizacja lokalizacja) {
        this.nazwa = nazwa;
        this.pojemnosc = pojemnosc;
        this.dostepneMiejsca = pojemnosc;
        this.lokalizacja = lokalizacja;
        // Zamiast new ArrayList<>:
        this.pacjenciNaOddziale = new HashMap<>();
    }

    public boolean przyjmijPacjenta(Osoba osoba) {
        if (dostepneMiejsca > 0) {
            // Załóżmy, że pacjent leży 5 tur (do testów)
            pacjenciNaOddziale.put(osoba, 5);
            dostepneMiejsca--;
            System.out.println("Szpital " + nazwa + " przyjął pacjenta " + osoba.getPesel());
            return true;
        }
        return false;
    }

    // Nie musimy zmieniać tu nic, jeśli chcemy dalej używać tej metody
    public boolean wypiszPacjenta(Osoba osoba) {
        if (pacjenciNaOddziale.containsKey(osoba)) {
            pacjenciNaOddziale.remove(osoba);
            dostepneMiejsca++;
            System.out.println("Szpital " + nazwa + " wypisał pacjenta " + osoba.getPesel());
            return true;
        }
        return false;
    }

    // Nowa metoda - "co turę" ją wywołujemy
    public void updateSzpital() {
        // Tworzymy listę do usunięcia
        List<Osoba> doWypisu = new ArrayList<>();
        
        // Zmniejszamy liczniki
        for (Map.Entry<Osoba, Integer> e : pacjenciNaOddziale.entrySet()) {
            Osoba o = e.getKey();
            int tury = e.getValue();
            tury--;
            e.setValue(tury);

            // jeśli tury spadły do 0 -> wypis
            if (tury <= 0) {
                doWypisu.add(o);
            }
        }
        // Wypisujemy
        for (Osoba o : doWypisu) {
            wypiszPacjenta(o);
        }
    }

    // get/set…
    public int getDostepneMiejsca() {
        return dostepneMiejsca;
    }

    public Lokalizacja getLokalizacja() {
        return lokalizacja;
    }

    public String getNazwa() {
        return nazwa;
    }

    @Override
    public String toString() {
        return String.format("Szpital %s, dost. miejsc: %d/%d, lok=%s",
                nazwa, dostepneMiejsca, pojemnosc, lokalizacja);
    }
}
