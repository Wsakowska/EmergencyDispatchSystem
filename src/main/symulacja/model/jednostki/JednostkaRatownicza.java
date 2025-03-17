package main.symulacja.model.jednostki;

import main.symulacja.enums.StatusJednostki;
import main.symulacja.model.Lokalizacja;

public abstract class JednostkaRatownicza {
    protected String id;
    protected Lokalizacja baza;                
    protected Lokalizacja aktualnaLokalizacja;
    protected StatusJednostki status;

    protected Lokalizacja celPodrozy;
    protected double odlegloscDoCelu;
    protected static final double PREDKOSC = 6.0; 

    public JednostkaRatownicza(String id, Lokalizacja baza) {
        this.id = id;
        this.baza = baza;
        this.aktualnaLokalizacja = baza;
        this.status = StatusJednostki.DOSTEPNA;
        this.celPodrozy = null;
        this.odlegloscDoCelu = 0.0;
    }

    public String getId() {
        return id;
    }

    public Lokalizacja getBaza() {
        return baza;
    }

    public Lokalizacja getAktualnaLokalizacja() {
        return aktualnaLokalizacja;
    }

    public StatusJednostki getStatus() {
        return status;
    }

    public void setStatus(StatusJednostki status) {
        this.status = status;
    }

    public void ustawCelPodrozy(Lokalizacja cel, StatusJednostki nowyStatus) {
        this.celPodrozy = cel;
        this.odlegloscDoCelu = aktualnaLokalizacja.distanceTo(cel);
        this.status = nowyStatus;
    }

    /**
     * Wykonanie ruchu o PREDKOSC w kierunku celu.
     */
    public void wykonajRuch() {
        if (celPodrozy == null || odlegloscDoCelu <= 0) {
            return;
        }

        double doPokonania = Math.min(PREDKOSC, odlegloscDoCelu);

        double dx = celPodrozy.getX() - aktualnaLokalizacja.getX();
        double dy = celPodrozy.getY() - aktualnaLokalizacja.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist > 0) {
            double nx = dx / dist;
            double ny = dy / dist;
            double rx = aktualnaLokalizacja.getX() + nx * doPokonania;
            double ry = aktualnaLokalizacja.getY() + ny * doPokonania;
            aktualnaLokalizacja = new Lokalizacja(rx, ry);
            odlegloscDoCelu -= doPokonania;
        }

        if (odlegloscDoCelu <= 0.00001) {
            aktualnaLokalizacja = celPodrozy;
            odlegloscDoCelu = 0;
            dotarcieDoCelu();
        }
    }

    protected void dotarcieDoCelu() {
        // domyślnie nic
    }

    public void wrocDoBazy() {
        ustawCelPodrozy(baza, StatusJednostki.POWROT_DO_BAZY);
    }

    @Override
    public String toString() {
        return String.format("%s{id=%s, status=%s, lok=%s, celPodrozy=%s, dist=%.2f}",
                this.getClass().getSimpleName(), id, status, 
                aktualnaLokalizacja, celPodrozy, odlegloscDoCelu);
    }
}
