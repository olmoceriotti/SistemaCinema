package it.unimib.finalproject.server.beans;

import java.util.List;
import java.util.UUID;

public class Prenotazione {
    private UUID id;
    private Proiezione proiezione;
    private int numeroPosti;
    private List<String> posti;
}
