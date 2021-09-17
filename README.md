# pps-bunny

## Processo di sviluppo adottato
Il processo di sviluppo è stato deciso con l'obiettivo di adottare le metologie previste dalla programmazione Agile, 
in particolare alcuni aspetti del framework Scrum. 
Il Product Backlog, contente tutti i 

//Parlare di scrum, come lo abbiamo adottato e come abbiamo gestito il lavoro (Sprint di 2 settimane) e parlare del
risultato prodotto da ogni sprint:

Per gli Sprint si è scelta una durata di circa due settimane. Per ogni Sprint, durante lo Sprint Planning si prefissa 
un obiettivo in termini di prototitipo funzionante da produrre alla scadenza delle due setttimane. In funzione di tale obiettivo 
si selezionano i task da completare durante lo Sprint fra tutti quelli individuati nel Product Backlog.
Di seguito sono riportati gli obietti ed i risultati realmente prodotti in ogni Sprint.

1. Sprint 1: abbiamo prodotto una modellazione concettuale che copre il modello del problema
2. Sprint 2: abbiamo prodotto una prima versione dell'applicazione in cui è possibile osservare la riproduzione dei
   conigli
3. Sprint 3: abbiamo integrato alla prima versione dell'app la possibilità di introdurre mutazioni genetiche nei conigli
4. Sprint 4:

### Modalità di revisione dei task
Durante lo Sprint Planning viene assegnato a ogni membro l'insieme di task che esso ha il compito di portare a termine entro lo sprint successivo.
A ciascuno dei task che sono stati pianificati viene quindi definita una Issue su GitHub con la persona incaricata e
una scheda su Trello nella sezione "To Do".

La scheda su trello fornisce la possibilità di aggiungere dei commenti e dei punti elenco, permettendo ad ogni sviluppatore
di avere tutte le informazioni relative a quel task in un solo punto e visibili da tutti gli altri membri. Trello consente
inoltre di trascinare i task in sezioni dedicate per la fase di sviluppo in cui si trovano, per essere aggiornati sul lavoro che
gli altri componenti del gruppo stanno effettuando.

Il flusso di lavoro all'interno del repository che si è deciso di adottare si chiama `Git Flow`. Esso prevede che sul branch
`main` o `master` vengano fatti i nel momento del rilascio di versione, mentre il branch principale per lo sviluppo
è `develop`. A partire da `develop` gli sviluppatori creano un proprio branch, il cui nome segue un pattern da noi definito
`feature/<iniziale_nome><iniziale_cognome>-<nome_feature>`, al cui interno viene sviluppato il codice per risolvere ciascuna
delle issue. Di seguito è riportata una immagine che rappresenta il flusso di sviluppo: