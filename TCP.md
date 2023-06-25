# Progetto Sistemi Distribuiti 2022-2023 - TCP

Documentazione del protoccolo socket su TCP. Si suggerisce di seguire il protocollo di Redis (https://redis.io/docs/reference/protocol-spec/), perché è molto semplice sia da comprendere sia da implementare. Non è necessario prendere tutti i punti, basta quello necessario per l'invio della richiesta e della risposta.

La documentazione può variare molto in base al tipo di protocollo che si vuole costruire. Se come Redis, è un protocollo di richieste e risposte, per cui è necessario indicare come inviare la richiesta (comando e dati) e la risposta. Si può anche utilizzare JSON al posto delle semplici stringhe, in tal caso andranno specificati bene la struttura degli oggetti scambiati tra client e server.

PROTOCOLLO DATABASE

Il protocollo implmentato per la comunicazione tra Server e DB si compone di 8 comandi che corrispondo a: le 4 operazioni di base CRUD, due operazioni ausiliarie e due operazioni per gestire la concorrenza. 
Il database è stato implementato nel pieno rispetto delle istruzioni fornite: i dati sono salvati in memory sotto forma di chiave-valore utilizzando una HashMap\<String, String> e supporta l'esecuzione concorrente di più Thread. 
La gestione della concorrenza adotta un approccio particolare. Sono forniti due comandi, LOCK e UNLOCK, che permettono di richiedere il LOCK su una particolare risorsa utilizzando la sua chiave. In questo modo non solo le operazioni che potrebbero creare problemi se eseguite in maniera concorrente risultano protette e stabili, ma si lascia anche la possibilità all'utilizzatore del db di utilizzare il comando LOCK per combinare più operazioni differenti e renderle Thread Safe.

Il protocollo è così specificato:
1. CREATE KEY / VALUE ;
  Il comando per creare una risorsa si compone semplicemente della coppia chiave-valore come argomenti, non è possibile creare due oggetti con la stessa chiave.
2. READ KEY ;
  Il comando per la lettura di un valore richiede semplicemente la chiave del valore che si vuole leggere come argomento.
3. [TOKEN] UPDATE KEY / VALUE ;
  Il comando per la modifica dei dati già inseriti richiede un token per verificare che la richiesta di update provenga da chi attualemente possiede il lock sulla risorsa.
4. [TOKEN] DELETE KEY ;
  Il comando per la cancellazione dei dati richiede anch'esso il token associato al LOCK.
5. EXISTS KEY ;
  Il comando exists invia risposta positivia  nel caso esista una valore del database identificato dalla chiave passata come parametro.
6. KEY\_FILTER FILTER;
  Il comando in questione restituisce una lista comprendente tutti i valori la cui chiave contiene la stringa passata nel parametro filter.
7. TOKEN LOCK KEY[ | KEYS]
  Il comando lock richiede un token da associare al lock che verrà creato sulla chiave (o chiavi) che vengono passate come argomento. Nel caso di chiavi multiple esse devono essere inviate in un unica stringa separate dal caratter "-"
8. TOKEN UNLOCK KEY[|KEYS] 
  == LOCK
