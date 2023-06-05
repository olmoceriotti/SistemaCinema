# Progetto Sistemi Distribuiti 2022-2023 - TCP

Documentazione del protoccolo socket su TCP. Si suggerisce di seguire il protocollo di Redis (https://redis.io/docs/reference/protocol-spec/), perché è molto semplice sia da comprendere sia da implementare. Non è necessario prendere tutti i punti, basta quello necessario per l'invio della richiesta e della risposta.

La documentazione può variare molto in base al tipo di protocollo che si vuole costruire. Se come Redis, è un protocollo di richieste e risposte, per cui è necessario indicare come inviare la richiesta (comando e dati) e la risposta. Si può anche utilizzare JSON al posto delle semplici stringhe, in tal caso andranno specificati bene la struttura degli oggetti scambiati tra client e server.

Solo un tipo di dato: json

ogni oggetto json ha una classe:

1. Film
2. Proiezione
3. Prenotazione
4. Sala

Ognuna di queste classi possiede un prefisso prima dell'UUIDv4.hex che li identifica che permette di distiguengere il tipo di dato:

1. F:
2. Pro:
3. Pre:
4. S:

Ognuna di queste classi ha una struttra predefinita che corrisponde alla struttura degli attributi  delle classi con lo stesso nome all'interno del server-web.

Il protocollo è strutturato come segue:

I messaggi da parte del server nei confronti del database iniziano con un simbolo che specifica l'operazione:

1 Sta per create
2 Sta per read
3 Sta per update
4 sta per delete
5 Sta per exists
6 Sta per len
7 Sta per all

A seconda del tipo di operazione i messaggi successivi saranno differenti:

Nel caso dell'operazione create, il database riceverà una stringa JSON che conterrà il body della richiesta. Conterrà informazioni rigurado il tipo di dato da creare, le info che lo compongono. Si assume che la stringa sia ben formata e rispetti i requsiti di integrità. Il database ritornerà quindi al server un messaggio di conferma o di errore  che sarà strutturato seconda una convenzione successivamente specificata.

Nel caso dell'operazione read, il database ricevera l'id univoco della risorsa che si sta tentando di leggere. Nel caso in cui la richiesta vada a buon fine il database ritorna  un messaggio di successo e un messaggio con il dato richiesto. L'operazione può essere accoppiata con il comando all che ritorna tutti i possibili evalori  di un determinato tipo e la cardinalità dell'insieme.

Nel caso dell'operazione update, il database riceverà l'id univoco della risorsa che si intende modificare insime al corpo della  richiesta. 

Nel caso dell'operatore delete, il database ricevrà l'identificatore univoco della risorsa e la rimuoverà dal database. Invierà poi la risposta oppurtuna in caso di successo o insuccesso.

L'operatore exists, insieme a un id univco, permette  di controllare l'effettiva esistenza di  una risorsa.

L'operatore len non lo so ancora

Le risposte avranno il seguente formato:  

$ Sta per successo
\% Sta per risorsa inesistente
\# sta per richiesta inadeguata
~ sta per tutti gli altri possibili errori

DECISAMENTE DA RIVEDERE


