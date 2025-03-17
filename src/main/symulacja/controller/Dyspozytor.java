package main.symulacja.controller;

import java.util.*;
import java.util.stream.Collectors;
import main.symulacja.enums.StatusJednostki;
import main.symulacja.enums.StatusWypadku;
import main.symulacja.enums.StatusZgloszenia;
import main.symulacja.model.*;
import main.symulacja.model.jednostki.JednostkaRatownicza;
import main.symulacja.model.jednostki.Pogotowie;

public class Dyspozytor {

    private List<Wypadek> aktywneWypadki;
    private List<Wypadek> historyczneWypadki;
    private List<Zgloszenie> aktywneZgloszenia;
    private List<Zgloszenie> zakonczoneZgloszenia;

    private List<JednostkaRatownicza> wszystkieJednostki;
    private List<Szpital> szpitale;

    public Dyspozytor() {
        this.aktywneWypadki = new ArrayList<>();
        this.historyczneWypadki = new ArrayList<>();
        this.aktywneZgloszenia = new ArrayList<>();
        this.zakonczoneZgloszenia = new ArrayList<>();
        this.wszystkieJednostki = new ArrayList<>();
        this.szpitale = new ArrayList<>();
    }

    public void dodajJednostke(JednostkaRatownicza jednostka) {
        wszystkieJednostki.add(jednostka);
    }

    public void dodajSzpital(Szpital szpital) {
        szpitale.add(szpital);
    }

    public List<Szpital> getSzpitale() {
        return szpitale;
    }

    public void zglaszajWypadek(Wypadek wypadek) {
        aktywneWypadki.add(wypadek);
    }

    public void generujZgloszenieZWypadku(Wypadek wypadek) {
        List<String> wymaganeJednostki = okreslWymaganeSluzby(wypadek.getTypWypadku());

        // Proste określenie liczby tur
        int tur = 2;
        String lower = wypadek.getTypWypadku().toLowerCase();
        if (lower.contains("pożar") || lower.contains("pozar")) {
            tur = 3;
        } else if (lower.contains("katastrofa") || lower.contains("chemiczna")) {
            tur = 4;
        }

        int wymagHosp = wypadek.getLiczbaOsobPoszkodowanych() / 2;

        Zgloszenie zgloszenie = new Zgloszenie(
                wypadek.getZnacznikCzasu(),
                wypadek.getLokalizacja(),
                wymaganeJednostki,
                tur,
                wypadek.getLiczbaOsobPoszkodowanych(),
                wymagHosp
        );
        aktywneZgloszenia.add(zgloszenie);
    }

    /**
     * Prosty priorytet:
     * priorytet = (liczbaOsobWymagajacychPomocy) + 2*(liczbaOsobWymagajacychHospitalizacji).
     * Sortujemy malejąco.
     */
    public List<Zgloszenie> okreslPriorytet() {
        return aktywneZgloszenia.stream()
                .sorted((z1, z2) -> {
                    int p1 = z1.getLiczbaOsobWymagajacychPomocy()
                               + 2 * z1.getLiczbaOsobWymagajacychHospitalizacji();
                    int p2 = z2.getLiczbaOsobWymagajacychPomocy()
                               + 2 * z2.getLiczbaOsobWymagajacychHospitalizacji();
                    return Integer.compare(p2, p1);
                })
                .collect(Collectors.toList());
    }

    /**
     * Przypisujemy jednostki wolne (DOSTEPNE) do zgłoszeń, zaczynając od najwyższego priorytetu.
     */
    public void przypiszJednostki() {
        List<Zgloszenie> posortowane = okreslPriorytet();
        for (Zgloszenie z : posortowane) {
            for (String typ : z.getWymaganeJednostkiRatownicze()) {
                // Ile już mamy jednostek tego typu?
                long ileJest = z.getPrzypisaneJednostki().stream()
                        .filter(j -> j.getClass().getSimpleName().equalsIgnoreCase(typ))
                        .count();

                if (!typ.equalsIgnoreCase("Pogotowie")) {
                    // Policja / Straż: wystarczy 1
                    if (ileJest == 0) {
                        JednostkaRatownicza wolna = znajdzNajblizszaWolnaJednostke(typ, z.getLokalizacja());
                        if (wolna != null) {
                            z.assignJednostka(wolna);
                            wolna.ustawCelPodrozy(z.getLokalizacja(), StatusJednostki.DOJEZDZAM_DO_ZDARZENIA);
                        }
                    }
                } else {
                    // Pogotowie: tyle karetek, ilu poszkodowanych (o ile mamy tyle wolnych)
                    int brakujace = z.getLiczbaOsobWymagajacychPomocy() - (int) ileJest;
                    while (brakujace > 0) {
                        JednostkaRatownicza wolna = znajdzNajblizszaWolnaJednostke("Pogotowie", z.getLokalizacja());
                        if (wolna != null) {
                            z.assignJednostka(wolna);
                            wolna.ustawCelPodrozy(z.getLokalizacja(), StatusJednostki.DOJEZDZAM_DO_ZDARZENIA);
                            brakujace--;
                        } else {
                            // Brak wolnych karetek
                            break;
                        }
                    }
                }
            }
        }
    }

    private JednostkaRatownicza znajdzNajblizszaWolnaJednostke(String typ, Lokalizacja cel) {
        // Filtrujemy wolne jednostki, danego typu
        List<JednostkaRatownicza> kandydaci = wszystkieJednostki.stream()
                .filter(j -> j.getStatus() == StatusJednostki.DOSTEPNA)
                .filter(j -> j.getClass().getSimpleName().equalsIgnoreCase(typ))
                .collect(Collectors.toList());

        if (kandydaci.isEmpty()) return null;

        JednostkaRatownicza najlepsza = null;
        double minDist = Double.MAX_VALUE;
        for (JednostkaRatownicza j : kandydaci) {
            double dist = j.getAktualnaLokalizacja().distanceTo(cel);
            if (dist < minDist) {
                minDist = dist;
                najlepsza = j;
            }
        }
        return najlepsza;
    }

    /**
     * Każda jednostka wykonuje ruch (o PREDKOSC).
     */
    public void ruchJednostek() {
        for (JednostkaRatownicza j : wszystkieJednostki) {
            j.wykonajRuch();
        }
    }
    //rozdzielic ruch
    /**
     * Główna metoda co turę:
     *  1) aktualizujemy szpitale (updateSzpital),
     *  2) postęp działań (Zgłoszenie.postepDzialan()),
     *  3) obsługa Pogotowia: jeśli pacjent na pokładzie -> znajdź wolny szpital lub czekaj,
     *  4) sprawdzamy, czy jakieś Zgłoszenie się zakończyło -> przenosimy do historii.
     */
    public void monitorujZgloszeniaIWypadki() {
        // 1. Każdy szpital: update pacjentów (zwolnienia łóżek itp.)
        for (Szpital s : szpitale) {
            s.updateSzpital();  // nowa metoda w Szpital
        }

        // 2. Postęp działań
        for (Zgloszenie z : aktywneZgloszenia) {
            z.postepDzialan();
        }

        // 3. Obsługa Pogotowia z pacjentem
        for (Zgloszenie z : aktywneZgloszenia) {
            for (JednostkaRatownicza jr : z.getPrzypisaneJednostki()) {
                if (jr instanceof Pogotowie) {
                    Pogotowie amb = (Pogotowie) jr;

                    // KARETKA STOI NA MIEJSCU, Z PACJENTEM
                    if (amb.getStatus() == StatusJednostki.NA_MIEJSCU && amb.isPacjentNaPokladzie()) {
                        Szpital s = znajdzNajblizszyWolnySzpital(amb.getAktualnaLokalizacja());
                        if (s != null) {
                            // Jedziemy do szpitala
                            amb.setDocelowySzpital(s);
                            amb.ustawCelPodrozy(s.getLokalizacja(), StatusJednostki.TRANSPORT_DO_SZPITALA);
                        } else {
                            // Gdy brak miejsc, przechodzimy w OCZEKUJE_NA_SZPITAL
                            System.out.println("Brak wolnych miejsc w szpitalach dla karetki " + amb.getId()
                                               + ". Oczekuje na zwolnienie łóżka.");
                            amb.setStatus(StatusJednostki.OCZEKUJE_NA_SZPITAL);
                        }
                    }
                    // KARETKA STOI I OCZEKUJE NA SZPITAL
                    else if (amb.getStatus() == StatusJednostki.OCZEKUJE_NA_SZPITAL) {
                        // co turę sprawdźmy ponownie
                        Szpital s = znajdzNajblizszyWolnySzpital(amb.getAktualnaLokalizacja());
                        if (s != null) {
                            amb.setDocelowySzpital(s);
                            amb.ustawCelPodrozy(s.getLokalizacja(), StatusJednostki.TRANSPORT_DO_SZPITALA);
                            System.out.println("Zwolniło się miejsce w " + s.getNazwa()
                                               + ". Karetka " + amb.getId() + " jedzie do szpitala.");
                        }
                    }
                }
            }
        }

        // 4. Zakończenie zgłoszeń
        List<Zgloszenie> doZakonczonych = new ArrayList<>();
        for (Zgloszenie z : aktywneZgloszenia) {
            if (z.getStatus() == StatusZgloszenia.ZAKONCZONE) {
                doZakonczonych.add(z);

                // Odszukaj wypadek
                Optional<Wypadek> optW = aktywneWypadki.stream()
                        .filter(w -> w.getZnacznikCzasu() == z.getZnacznikCzasu())
                        .findFirst();
                if (optW.isPresent()) {
                    Wypadek w = optW.get();
                    w.setStatus(StatusWypadku.HISTORYCZNY);
                    historyczneWypadki.add(w);
                    aktywneWypadki.remove(w);
                }
            }
        }
        aktywneZgloszenia.removeAll(doZakonczonych);
        zakonczoneZgloszenia.addAll(doZakonczonych);
    }

    private Szpital znajdzNajblizszyWolnySzpital(Lokalizacja start) {
        Szpital best = null;
        double minDist = Double.MAX_VALUE;
        for (Szpital s : szpitale) {
            if (s.getDostepneMiejsca() > 0) {
                double d = start.distanceTo(s.getLokalizacja());
                if (d < minDist) {
                    minDist = d;
                    best = s;
                }
            }
        }
        return best;
    }

    public List<Wypadek> getAktywneWypadki() {
        return aktywneWypadki;
    }

    public List<Wypadek> getHistoryczneWypadki() {
        return historyczneWypadki;
    }

    public List<Zgloszenie> getAktywneZgloszenia() {
        return aktywneZgloszenia;
    }

    public List<Zgloszenie> getZakonczoneZgloszenia() {
        return zakonczoneZgloszenia;
    }

    public List<JednostkaRatownicza> getWszystkieJednostki() {
        return wszystkieJednostki;
    }

    /**
     * Określamy wymaganą liczbę służb w zależności od typu wypadku.
     */
    private List<String> okreslWymaganeSluzby(String typWypadku) {
        List<String> wynik = new ArrayList<>();
        String lower = typWypadku.toLowerCase();

        if (lower.contains("wypadek drogowy")) {
            wynik.add("Policja");
            wynik.add("Pogotowie");
        }
        if (lower.contains("pożar") || lower.contains("pozar")) {
            wynik.add("StrazPozarna");
            wynik.add("Pogotowie");
        }
        if (lower.contains("katastrofa")) {
            wynik.add("Policja");
            wynik.add("Pogotowie");
            wynik.add("StrazPozarna");
        }
        // Nowy typ 4: wypadek w fabryce
        if (lower.contains("fabryce") || lower.contains("fabryka")) {
            wynik.add("StrazPozarna");
            wynik.add("Pogotowie");
        }
        // Nowy typ 5: awaria chemiczna
        if (lower.contains("chemiczna")) {
            wynik.add("Policja");
            wynik.add("StrazPozarna");
            wynik.add("Pogotowie");
        }

        // Jeżeli nie pasuje do niczego powyżej, minimalnie Policja
        if (wynik.isEmpty()) {
            wynik.add("Policja");
        }
        return wynik;
    }
}
