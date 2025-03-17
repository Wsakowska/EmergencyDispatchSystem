package main.symulacja.model;

import main.symulacja.enums.StatusZgloszenia;
import main.symulacja.model.jednostki.JednostkaRatownicza;
import main.symulacja.model.jednostki.Pogotowie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Zgloszenie {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final int id;
    private long znacznikCzasu;
    private Lokalizacja lokalizacja;
    private StatusZgloszenia status;

    private List<String> wymaganeJednostkiRatownicze;

    // Ile tur potrzeba, by akcja (np. gaszenie, zabezpieczenie) się zakończyła
    private int liczbaTurPotrzebnych;
    private int liczbaTurWykonanych;

    private List<JednostkaRatownicza> przypisaneJednostki;

    // Liczba osób wymagających pomocy
    private int liczbaOsobWymagajacychPomocy;
    private int liczbaOsobUdzielonoPomocy;

    // Liczba osób wymagających hospitalizacji
    private int liczbaOsobWymagajacychHospitalizacji;
    private int liczbaOsobOdwiezionychDoSzpitala;

    public Zgloszenie(long znacznikCzasu,
                      Lokalizacja lokalizacja,
                      List<String> wymaganeJednostkiRatownicze,
                      int liczbaTurPotrzebnych,
                      int liczbaOsobWymagajacychPomocy,
                      int liczbaOsobWymagajacychHospitalizacji) {
        this.id = COUNTER.incrementAndGet();
        this.znacznikCzasu = znacznikCzasu;
        this.lokalizacja = lokalizacja;
        this.status = StatusZgloszenia.AKTYWNE;
        this.wymaganeJednostkiRatownicze = new ArrayList<>(wymaganeJednostkiRatownicze);
        this.liczbaTurPotrzebnych = liczbaTurPotrzebnych;
        this.liczbaTurWykonanych = 0;
        this.przypisaneJednostki = new ArrayList<>();
        this.liczbaOsobWymagajacychPomocy = liczbaOsobWymagajacychPomocy;
        this.liczbaOsobUdzielonoPomocy = 0;
        this.liczbaOsobWymagajacychHospitalizacji = liczbaOsobWymagajacychHospitalizacji;
        this.liczbaOsobOdwiezionychDoSzpitala = 0;
    }

    public int getId() {
        return id;
    }

    public long getZnacznikCzasu() {
        return znacznikCzasu;
    }

    public Lokalizacja getLokalizacja() {
        return lokalizacja;
    }

    public StatusZgloszenia getStatus() {
        return status;
    }

    public void setStatus(StatusZgloszenia status) {
        this.status = status;
    }

    public List<String> getWymaganeJednostkiRatownicze() {
        return wymaganeJednostkiRatownicze;
    }

    public int getLiczbaTurPotrzebnych() {
        return liczbaTurPotrzebnych;
    }

    public int getLiczbaTurWykonanych() {
        return liczbaTurWykonanych;
    }

    public List<JednostkaRatownicza> getPrzypisaneJednostki() {
        return przypisaneJednostki;
    }

    public int getLiczbaOsobWymagajacychPomocy() {
        return liczbaOsobWymagajacychPomocy;
    }

    public int getLiczbaOsobUdzielonoPomocy() {
        return liczbaOsobUdzielonoPomocy;
    }

    public int getLiczbaOsobWymagajacychHospitalizacji() {
        return liczbaOsobWymagajacychHospitalizacji;
    }

    public int getLiczbaOsobOdwiezionychDoSzpitala() {
        return liczbaOsobOdwiezionychDoSzpitala;
    }

    public void assignJednostka(JednostkaRatownicza jednostka) {
        if (!przypisaneJednostki.contains(jednostka)) {
            przypisaneJednostki.add(jednostka);
        }
    }

    /**
     * Postęp działań - w każdej turze:
     *  - zwiększamy licznik tur,
     *  - jeśli Pogotowie jest na miejscu, może obsłużyć 1 poszkodowanego (1 tura).
     */
    public void postepDzialan() {
        if (status == StatusZgloszenia.AKTYWNE) {
            liczbaTurWykonanych++;

            // Pogotowie obsługuje 1 osobę na turę
            for (JednostkaRatownicza jr : przypisaneJednostki) {
                if (jr instanceof Pogotowie) {
                    Pogotowie amb = (Pogotowie) jr;
                    // Tylko jeśli jest NA_MIEJSCU i jest jeszcze ktoś do obsłużenia
                    if (amb.getStatus().name().equals("NA_MIEJSCU") 
                        && liczbaOsobWymagajacychPomocy > 0) {

                        liczbaOsobUdzielonoPomocy++;
                        liczbaOsobWymagajacychPomocy--;

                        // Jeśli wymaga hospitalizacji
                        if (liczbaOsobWymagajacychHospitalizacji > 0) {
                            amb.setPacjentNaPokladzie(true);
                            liczbaOsobOdwiezionychDoSzpitala++;
                            liczbaOsobWymagajacychHospitalizacji--;
                        }
                    }
                }
            }

            // Jeśli minęła wymagana liczba tur i nikt już nie czeka na pomoc -> koniec
            if (liczbaTurWykonanych >= liczbaTurPotrzebnych 
                && liczbaOsobWymagajacychPomocy == 0) {
                status = StatusZgloszenia.ZAKONCZONE;
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Zgloszenie{id=%d, czas=%d, lok=%s, status=%s, " +
                "turPotrzebne=%d, turWykonane=%d, pomocy=%d/%d, hosp=%d, odwiezionych=%d}",
                id, znacznikCzasu, lokalizacja, status,
                liczbaTurPotrzebnych, liczbaTurWykonanych,
                liczbaOsobUdzielonoPomocy, liczbaOsobWymagajacychPomocy,
                liczbaOsobWymagajacychHospitalizacji, liczbaOsobOdwiezionychDoSzpitala
        );
    }
}
