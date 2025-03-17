package main.symulacja.model;

import main.symulacja.enums.StatusWypadku;

import java.util.concurrent.atomic.AtomicInteger;

public class Wypadek {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final int id;
    private long znacznikCzasu;
    private Lokalizacja lokalizacja;
    private StatusWypadku status;
    private String typWypadku;
    private int liczbaOsobPoszkodowanych;

    public Wypadek(long znacznikCzasu, Lokalizacja lokalizacja,
                   String typWypadku, int liczbaOsobPoszkodowanych) {
        this.id = COUNTER.incrementAndGet();
        this.znacznikCzasu = znacznikCzasu;
        this.lokalizacja = lokalizacja;
        this.typWypadku = typWypadku;
        this.liczbaOsobPoszkodowanych = liczbaOsobPoszkodowanych;
        this.status = StatusWypadku.AKTYWNY;
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

    public StatusWypadku getStatus() {
        return status;
    }

    public void setStatus(StatusWypadku status) {
        this.status = status;
    }

    public String getTypWypadku() {
        return typWypadku;
    }

    public int getLiczbaOsobPoszkodowanych() {
        return liczbaOsobPoszkodowanych;
    }

    @Override
    public String toString() {
        return String.format("Wypadek{id=%d, czas=%d, lokalizacja=%s, typ=%s, poszkodowani=%d, status=%s}",
                id, znacznikCzasu, lokalizacja, typWypadku, liczbaOsobPoszkodowanych, status);
    }
}
