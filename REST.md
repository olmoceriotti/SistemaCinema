# Progetto Sistemi Distribuiti 2022-2023 - API REST

Documentazione delle API REST di esempio. Si assume che i dati vengano scambiati in formato JSON.

## `/film`

Nell'endpoint `/film` sono raggruppate tutte le operazioni relative ai film

### GET

**Descrizione**: il metodo GET restituisce una lista di film in formato JSON.

**Parametri**: Non sono previsti parametri per questa richiesta.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: La risposta è un array JSON di oggetti film. Un oggetto JSON film ha i seguenti campi: id, nome, durata, attori, regista ed ExternalID. Tutti i campi sono stringhe ad eccezione della durata che è un intero. Il campo ExternalID è un identificatore esterno utilizzato per ottenere ulteriori informazioni sul film utilizzando l'API TheMovieDB.

**Codici di stato restituiti**:

* 200 OK

## `/film/{id}`

### GET

**Descrizione**: Restituisce un oggetto JSON che rappresenta un film.

**Parametri**: `{id}` è un parametro di percorso che corrisponde all'identificativo univoco del film da restituire.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: Nel caso in cui l'ID corrisponda con quello di un film esistente, verrà restituito l'oggetto rappresentante il film richiesto.

**Codici di stato restituiti**:

* 200 OK
* 404 NOT FOUND: non esiste un film con l'ID specificato nel percorso.

### delete?

## `/proiezione`

Nell'endpoint `/proiezione` sono raggruppate tutte le operazioni relative alle proiezioni.

### GET

**Descrizione**: il metodo GET restituisce una lista le proiezioni in formato JSON.

**Parametri**: Non sono previsti parametri per questa richiesta.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: La risposta è un array JSON di oggetti proiezione. Un oggetto JSON film ha i seguenti campi: id, orario, sala, filmID, totale posti occupati e posti prenotati. Il campo orario è un array di interi che rappresentano, in ordine, anno, mese, giorno, ora e minuti. Il campo posti prenotati è un array di stringhe contenente solo stringhe che rispettano la seguente espressione regolare: `^[A-O][0-9]$`

**Codici di stato restituiti**:

* 200 OK

## `/proiezione/{id}`

### GET

**Descrizione**: il metodo GET restituisce una singola proiezione in formato JSON.

**Parametri**: `{id}` è un parametro di percorso che corrisponde all'identificativo univoco della proiezione da restituire.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: Nel caso in cui l'ID corrisponda con quello di una proiezione esistente, verrà restituito l'oggetto rappresentante la proiezione richiesta.

**Codici di stato restituiti**:

* 200 OK
* 404 NOT FOUND: non esiste una proiezione con l'ID specificato nel percorso.

### delete ?

## `/prenotazione`

### GET

**Descrizione**: il metodo GET restituisce tutte le prenotazioni effettuate. Questo metodo, anche se non è mai chiamato dal client, è stato implementato per permettere di ottenere le informazioni delle prenotazioni su cui testare l'API.

**Parametri**: Non sono previsti parametri per questa richiesta.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: La risposta è un array JSON di oggetti prenotazione. Un oggetto JSON prenotazione ha i seguenti campi: id, proiezioneID, numeroPosti, posti. Tutti i campi sono in formato stringa ad eccezione di numeroPosti che è un intero e posti che è un'array di stringhe che rispettano la seguente espressione regolare: `^[A-O][0-9]$`. La stringa id rappresenta un UUIDv4.

**Codici di stato restituiti**:

* 200 OK

### POST

**Descrizione**: il metodo POST permette di creare una nuova prenotazione.

**Parametri**: Non sono previsti parametri per questa richiesta.

**Body richiesta**: Il body previsto per questa richiesta è un oggetto JSON con i campi proiezioneID e posti. I campi devono rispettare la tipizzazione sopra descritta.

**Risposta**: La risposta è una stringa JSON contenente l'ID della nuova prenotazione.

**Codici di stato restituiti**:

* 201 CREATED
* 400 BAD REQUEST: l'oggetto JSON non rispetta il formato richiesto, l'array contenente i posti è vuoto, i posti che si sta cercando prenotare sono già occupati, si sta cercando di prenotare posti che non rispettano la seguente espressione regolare: `^[A-O][0-9]$` oppure si sta cercando di prenotare una proiezione inesistente.
* 500 INTERNAL SERVER ERROR: in caso di request timeout.

## `/prenotazione/{id}`

### GET

**Descrizione**: il metodo GET restituisce una singola prenotazione in formato JSON.

**Parametri**: `{id}` è un parametro di percorso che corrisponde all'identificativo univoco della prenotazione da restituire.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: Nel caso in cui l'ID corrisponda con quello di una prenotazione esistente, verrà restituito l'oggetto rappresentante la prenotazione richiesta.

**Codici di stato restituiti**:

* 200 OK
* 404 NOT FOUND: non esiste una prenotazione con l'ID specificato nel percorso.

### PUT

**Descrizione**: il metodo PUT permette di modificare una prenotazione esistente eliminando i posti passati nel body.

**Parametri**: `{id}` è un parametro di percorso che corrisponde all'identificativo univoco della prenotazione da modificare.

**Body richiesta**: Il body previsto per questa richiesta è un array di stringhe che rispettano l'espressione regolare che descrive il formato dei posti.

**Risposta**: Non è prevista risposta al di fuori del codice di stato.

**Codici di stato restituiti**:

* 204 NO CONTENT
* 400 BAD REQUEST: l'oggetto JSON non rispetta il formato richiesto, l'array contenente i posti è vuoto, i posti che si sta cercando di eliminare non sono presenti nella prenotazione oppure si sta cercando di eliminare posti che non rispettano la seguente espressione regolare: `^[A-O][0-9]$`.
* 404 NOT FOUND: non esiste una prenotazione con l'ID specificato nel percorso.
* 500 INTERNAL SERVER ERROR: in caso di request timeout.

### DELETE

**Descrizione**: il metodo DELETE permette di eliminare una prenotazione esistente.

**Parametri**: `{id}` è un parametro di percorso che corrisponde all'identificativo univoco della prenotazione da eliminare.

**Body richiesta**: Non è previsto un body per questa richiesta.

**Risposta**: Non è prevista risposta al di fuori del codice di stato.

**Codici di stato restituiti**:

* 204 NO CONTENT
* 404 NOT FOUND: non esiste una prenotazione con l'ID specificato nel percorso.
