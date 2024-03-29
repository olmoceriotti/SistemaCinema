# Progetto Sistemi Distribuiti 2022-2023

## Descrizione

Per questo progetto abbiamo creato un sistema distributo relativo alla gestione di CINEmah,  un cinema multisala gratuito che non si prende troppo sul serio. I singoli componenti software sono descritti in questo file.

## Componenti del gruppo

* Olmo Ceriotti (886140) <o.ceriotti@campus.unimib.it>
* Sara De Marchi (879188) <s.demarchi7@campus.unimib.it>

## Compilazione ed esecuzione

I tre componenti necessitano dei seguenti passaggi per essere eseguiti correttamente:

1. Client: È sufficiente aprire il file `index.html` in Chrome (v114 o superiore) e disabilitare CORS con un'estensione Chrome analoga a ["CORS Unblock"](https://chrome.google.com/webstore/detail/cors-unblock/lfhmikememgdcahcdlaciloancbhjino).
2. Server: È necessario eseguire i comandi `mvn clean`, `mvn compile` e `mvn exec:java` nella cartella "server-web".
3. Database: È necessario eseguire i comandi `mvn clean`, `mvn compile` e `mvn exec:java` nella cartella "database".

I tre componenti devono essere necessariamente eseguiti sulla stessa macchina per funzionare correttamente. L'esecuzione su diverse macchine è possibile ma necessiterebbe modifiche agli indirizzi scritti nel codice.

Si consiglia di eseguire il codice su un dispositivo con un monitor di almeno 15 pollici.

## Porte e indirizzi

1. Database Port: 3030 Address: localhost
2. Server Port: 8080 Address: localhost

## Descrizione Dettagliata

### Database

Il database implementato per questo progetto utilizza il paradigma chiave-valore richiesto utilizzando, sia come chiave che come valore, delle stringhe. Le entries vengono conservate in-memory utilizzando una `HashMap<String, String>`. La gestione del database è affidata allo sviluppatore che lo utilizza nel modo che preferisce, non c'è una formattazione obbligatoria da rispettare ne altre costrizioni. In questo progetto le stringhe salvate sono tutte in formato JSON, sulla falsa riga di [RedisJSON](https://redis.io/docs/stack/json/).

Sono presenti tre metodi per l'interazione con la memoria secondaria: `restoreFromBackup()`, `startSnapshotDaemon()`, `saveSnapshot()`. Il primo si occupa di popolare la HashMap con i dati salvati nel file `database.dat`. Gli altri due metodi lavorano insieme per salvare lo stato del database nel suddetto file. Il funzionamento è analogo alla funzione di snapshot implementata da Redis: invece di salvare in memoria secondaria ogni singola modifica ai dati si fa uno snapshot, una fotografia dello stato corrente. Si crea quindi un Daemon incaricato di controllare quando non sono presenti `LOCK` ogni N secondi e quando ciò avviene si esegue la procedura di snapshot. Questa funzione è disattivabile cambiando il valore della variabile boolean statica `snapshots` da `true` a `false`.

Per la gestione della concorrenza e delle operazioni di composizione `LOCK` e `UNLOCK` è presente una tabella, rappresentata da una `HashMap<String, Set<String>>`, per registrare i token e le chiavi su cui detengono il lock. L'aggiornamento di questa tabella è delegato ai metodi `LOCK` e `UNLOCK`, il cui utilizzo è documentato nel file apposito.

Il protocollo di comunicazione con il database e le operazioni permesse sono documentate nel file [TCP.md](./TCP.md).

### Server

Il server si occupa della gestione delle operazioni richieste dal cliente riguardo il cinema. Sono presenti tre principali endpoints: `/film`, `/proiezione` e `/prenotazione`.

Su questi endpoints sono modellate tutte le operazioni che l'utente necessita per l'implementazione delle funzionalità richieste. È presente un'operazione aggiuntiva che permette di ottenere una lista di tutte le prenotazioni effettuate per permettere di testare al meglio il Client e l'API.

Sono presenti tre classi che modellano i dati trattati e implementano tutte le operazioni per effettuare conversioni, controlli ed elaborazioni.

Non sono presenti metodi per la gestione amministrativa del server, ovvero aggiungere o rimuovere film e proiezioni. Questa scelta è stata fatta per evitare interazioni non volute da parte dell'utente. Si presume che l'eventuale amministratore non commetta errori nel caricamento di film o proiezioni.

L'API REST, gli endpoints e le operazioni su di essi disponibili sono documentate nel file [REST.md](./REST.md).

### Client

Il client implementa l'interfaccia per permettere all'utente l'interazione con il sistema CINEmah. È composto da diverse pagine `html`, gli stili sono stati compilati dal preprocessore `SCSS` (senza influenzare in alcun modo il comportamento degli script) e la parte di script è stata, come richiesto, implementata in `javascript`. Ogni pagina del client è modellata intorno ad una delle funzionalità richieste, con qualche aggiunta per migliorare l'esperienza dell'utente.

La pagina `index.html` permette di consultare tutti i film disponibili, richiedere informazioni aggiuntive per ognuno di essi e scegliere se visualizzare tutte le proiezioni o gestire una prenotazione.

La pagina `proiezione.html` permette di visualizzare tutte le proiezioni disponibili, vedere le informazioni relative al film e effettuare una nuova prenotazione. Tutte le sale hanno una grandezza fissa di 150 posti, prenotabili attraverso l'apposita rappresentazione. Vista la gratuità del biglietto non è possibile prenotare più di 5 posti alla volta e i posti già occupati sono evidenziati in rosso. Una volta inviata la prenotazione, se i posti sono ancora disponibili e la prenotazione va a buon fine, verrà mostrato l'ID univoco della prenotazione e il QR code che lo rappresenta, con la possibilità di copiare il codice e/o scaricare il QR.

La pagina `prenotazione.html` permette di modificare la propria prenotazione dando la possibilità di cercarla per codice univoco oppure utilizzando il QR code generato nel momento della prenotazione. Si possono solo rimuovere posti dalla prenotazione, nel caso in cui si tenti di rimuoverli tutti verrà richiesto se si desidera cancellarla.

La pagine `about.html` contiene informazioni relative all'API esterne utilizzate per ottenere le informazioni sui film, la creazione e lettura dei QR code e i dati relativi all'MCU.

### Screenshots

Gli screenshot dell'interfaccia utente sono raccolti nella cartella "Images/screenshotUI" per mantenere una struttura più ordinata. Nella cartella QRTests sono presenti dei codici QR per testare il client.
