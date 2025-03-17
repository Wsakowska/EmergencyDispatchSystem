package main.symulacja.model.jednostki;

import main.symulacja.enums.StatusJednostki;
import main.symulacja.model.Lokalizacja;

public class StrazPozarna extends JednostkaRatownicza {
    public StrazPozarna(String id, Lokalizacja baza) {
        super(id, baza);
    }

    @Override
    protected void dotarcieDoCelu() {
        super.dotarcieDoCelu();
        if (this.status == StatusJednostki.DOJEZDZAM_DO_ZDARZENIA) {
            this.status = StatusJednostki.NA_MIEJSCU;
        } else if (this.status == StatusJednostki.POWROT_DO_BAZY) {
            this.status = StatusJednostki.DOSTEPNA;
        }
    }
}
