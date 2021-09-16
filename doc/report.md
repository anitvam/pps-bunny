# Bunny Survival

[Baiardi Martina](mailto:martina.baiardi4@studio.unibo.it),
[Lucchi Asia](mailto:asia.lucchi@studio.unibo.it),
[Spadoni Marta](mailto:alessia.rocco@studio.unibo.it),
[Rocco Alessia](mailto:marta.spadoni2@studio.unibo.it)

## Descrizione
`pps-bunny` è un simulatore per osservare l'evoluzione naturale di una famiglia di conigli nel corso delle generazioni.
Per far interagire l'utente con la simulazione è prevista una interfaccia grafica game-like che permetta di inserire 
delle mutazioni sia dei conigli che dell'ambiente circostante. 

La proposta si ispira al seguente gioco: https://phet.colorado.edu/en/simulation/natural-selection.

## Processo di sviluppo adottato
//Parlare di scrum, come lo abbiamo adottato e come abbiamo gestito il lavoro (Sprint di 2 settimane) e parlare del 
risultato prodotto da ogni sprint:
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

![Git-Flow Workflow](./images/git_flow.svg)

Quando un membro ha completato il suo task allora egli effettua una `pull request` che sottometterà alla revisione da parte 
degli altri membri del team. Nel momento in cui viene fatta questa richiesta viene anche associata la `issue` relativa alla 
risoluzione del task, questa operazione facilita l'individuazione di task che non sono stati svolti.


### Strumenti di test, build e Continuous Integration
Per avere sicurezza che tutte le integrazioni da parte dei membri del team non vadano a collidere con quelle presenti nel branch `develop` 
è stata introdotta la `Continuous Integration`. Con il file `.github/workflows/ci.yml` viene definita una sequenza di operazioni
che vogliamo che siano svolte dalle `GitHub Actions`, le quali sono un insieme di macchine virtuali che `GitHub` mette a disposizione
per poterle eseguire.

All'interno della nostra pipeline il progetto viene `compilato` e `testato` su macchine di diversi sistemi operativi: `MacOS`, 
`Windows` e `Ubuntu`, con due versioni della JVM, ossia la `1.11` e la `1.16`. 

La `Continuous Integration` definita nel nostro progetto prevede che i test vengano eseguiti tutte le volte che si 
effettua una `push` e una `pull request` sui branch `develop` e `master`. Grazie a questa configurazione, al momento 
di ogni `pull request` è possibile visualizzare se i test passano o meno, mantenendo il codice in develop sempre funzionante.

Come libreria di testing è stata utilizzata [ScalaTest](https://www.scalatest.org/).

## Requisiti
// Requisiti Business
// Requisiti Utente
// Requisiti Funzionali
// Requisiti non Funzionali
// Requisiti di Implementazione
// La JVM >= v1.11 è richiesta per la nostra implementazione in ScalaFX

## Design architetturale
// Architettura complessiva
// Descrizione di pattern architetturali usati
// Scelte tecnologiche cruciali a fini architetturali

## Design di dettaglio

## Implementazione
### Baiardi
### Lucchi
### Spadoni 
### Rocco

## Restrospettiva