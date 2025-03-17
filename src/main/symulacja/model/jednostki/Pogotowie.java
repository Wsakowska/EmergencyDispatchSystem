package main.symulacja.model.jednostki;

import main.symulacja.enums.StatusJednostki;
import main.symulacja.model.Lokalizacja;
import main.symulacja.model.Osoba;
import main.symulacja.model.Szpital;

public class Pogotowie extends JednostkaRatownicza {
    private boolean pacjentNaPokladzie = false;
    private Szpital docelowySzpital = null;

    public Pogotowie(String id, Lokalizacja baza) {
        super(id, baza);
    }

    public boolean isPacjentNaPokladzie() {
        return pacjentNaPokladzie;
    }

    public void setPacjentNaPokladzie(boolean pacjentNaPokladzie) {
        this.pacjentNaPokladzie = pacjentNaPokladzie;
    }

    public Szpital getDocelowySzpital() {
        return docelowySzpital;
    }

    public void setDocelowySzpital(Szpital docelowySzpital) {
        this.docelowySzpital = docelowySzpital;
    }

    @Override
    protected void dotarcieDoCelu() {
        super.dotarcieDoCelu();
        if (this.status != null) {
            switch (this.status) {
                case DOJEZDZAM_DO_ZDARZENIA:
                    // Gdy dojechaliśmy do miejsca wypadku
                    this.status = StatusJednostki.NA_MIEJSCU;
                    break;

                case TRANSPORT_DO_SZPITALA:
                    // Dotarcie do szpitala
                    if (docelowySzpital != null) {
                        if (pacjentNaPokladzie) {
                            // PRZYKŁADOWO przyjmujemy pacjenta w szpitalu
                            Osoba wirtualnyPacjent = new Osoba("dummyPesel" + System.nanoTime());
                            boolean przyjeto = docelowySzpital.przyjmijPacjenta(wirtualnyPacjent);

                            if (przyjeto) {
                                System.out.println("Szpital " + docelowySzpital.getNazwa()
                                        + " przyjął pacjenta z karetki " + this.id);
                                pacjentNaPokladzie = false;
                            } else {
                                System.out.println("Brak wolnych miejsc w szpitalu "
                                        + docelowySzpital.getNazwa() + " dla karetki " + this.id);
                                // Można ewentualnie szukać innego szpitala...
                            }
                        }

                        // Po zostawieniu pacjenta (lub nie) wracamy do bazy
                        wrocDoBazy();
                    }
                    break;

                case POWROT_DO_BAZY:
                    // Znowu dostępni w bazie
                    this.status = StatusJednostki.DOSTEPNA;
                    this.docelowySzpital = null;
                    break;

                default:
                    // nic
                    break;
            }
        }
    }
}
