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
Il processo di sviluppo adottato mette in pratica le metodologie previste dalla programmazione Agile, in particolare alcuni aspetti del framework **Scrum**:
* prevedendo delle riunioni periodiche per organizzare il lavoro, lo Sprint Planning e la Sprint Review,
* producendo artefatti come il Product Backlog e il resoconto di ogni riunione,
* tenendo traccia dello svolgimento di ogni task da parte dei singoli componenti del team.

Il **Product Backlog** contiene le funzionalità fondamentali da sviluppare per la realizzazione dell'applicativo e viene aggiornato durante ogni Sprint Planning in base alle esigenze che emergono man mano che il progetto avanza. Per ogni feature sono individuati i task che ne permettono il completamento, a ognuno di essi è assegnato un punteggio che indica la difficoltà prevista per portarlo a termine.  <br /> Durante la **Sprint Review** si aggiorna il punteggio per i task dello Sprint appena concluso, per valutare la differenza fra la complessità prevista e quella reale. Inoltre durante la revisione si chiudono le eventuali Pull Request lasciate in sospeso e si rilascia un eseguibile dello stato attuale dell'applicazione.

Per gli **Sprint** si è scelta una durata di circa due settimane, con la preparazione dello Sprint successivo prevista il lunedì.

Per ogni Sprint, durante lo **Sprint Planning** si prefissa un obiettivo in termini di prototipo funzionante da produrre alla scadenza. In funzione di tale obiettivo si selezionano i task da completare durante lo Sprint fra tutti quelli individuati nel Product Backlog. Ogni task viene assegnato a un membro del team in modo che ogni sviluppatore nel corso della realizzazione del progetto si cimenti in vari aspetti (GUI, Model, Design, Documentazione) e che per ogni Sprint il carico sia adeguatamente bilanciato fra i componenti. <br /> Infine durante ogni Sprint Planning si discutono gli aspetti inerenti la modellazione del dominio in modo che tutti i membri del gruppo abbiano chiaro come lo si intende rappresentare, al fine di evitare fraintendimenti. <br /> Per lo Sprint Planning è prevista una durata di circa due ore, in realtà alcune pianificazioni hanno richiesto a malapena una ora mentre altre, in particolare quelle iniziali, sono arrivate a più di tre ore.

Di seguito sono riportati gli obiettivi fissati ed i risultati realmente prodotti in ogni Sprint.

| Sprint       | Obiettivo    | Risultato     |
| :------------- | :---------- | :----------- |
| 1 |  Produrre una modellazione corretta e possibilmente completa del sistema.  <br /> Questo è l'unico Sprint che ha avuto una durata inferiore alle due settimane (circa 10 giorni) e il cui obbiettivo non è un prototipo tangibile per un eventuale utente. Si è scelto di dedicare un intero Sprint alla modellazione in modo che tutti i componenti del team avessero tempo di studiare il dominio e per dare centralità al design dell'applicativo. | Obiettivo portato a termine. |
| 2 | Implementare una prima versione dell'applicazione, in cui è possibile osservare visivamente la riproduzione dei coniglietti. Nella pratica, si tratta di: <ul><li>Realizzare il model di base per la rappresentazione del coniglietto ed il suo patrimonio genetico.</li><li>Creare la struttura MVC dell'applicativo ed il loop che permette di avanzare nelle generazioni.</li><li> Visualizzare il pannello principale con i coniglietti che saltano.</li></ul>  | Obiettivo portato a termine. |
| 3 | Integrare alcune feature alla prima versione prodotta, in particolare: <ul><li>Estendere la GUI con tutti i pannelli necessari, in particolare quelli per la scelta delle mutazioni e del grafico.</li><li>Aggiungere  le mutazioni ai conigli.</li><li>Dare la possibilità di scegliere la dominanza delle mutazioni introdotte.</li><li>Dare la possibilità di cambiare clima.</li><li>Visualizzare l'albero genealogico di un qualsiasi coniglio.</li><li>Visualizzare il grafico con i cambiamenti nella popolazione.</li><li>Visualizzare il grafico con le proporzioni.</li><li>Iniziare la stesura del report.</li></ul> | ??    |
| 4 | Introdurre i fattori disturbanti. | ?? |

### Modalità di revisione dei task

Durante lo Sprint Planning viene assegnato a ogni membro l'insieme di task che esso ha il compito di portare a termine entro lo sprint successivo.
Per ciascun task pianificato vengono definite una Issue su GitHub e una scheda Trello nella sezione "To Do" entrambe assegnate alla persona incaricata di implementare il task.

La scheda su Trello fornisce la possibilità di aggiungere dei commenti e dei punti elenco, permettendo ad ogni sviluppatore
di avere tutte le informazioni relative a quel task in un solo punto e visibili da tutti gli altri membri. Trello consente
inoltre di trascinare i task in sezioni dedicate per la fase di sviluppo in cui si trovano, per essere aggiornati sul lavoro che
gli altri componenti del gruppo stanno effettuando.

Il flusso di lavoro all'interno del repository che si è deciso di adottare si chiama `Git Flow`. Esso prevede che sul branch
`main` o `master` vengano fatti i commit nel momento del rilascio di versione, mentre il branch principale per lo sviluppo
è `develop`. A partire da `develop` gli sviluppatori creano un proprio branch, il cui nome segue un pattern da noi definito
`feature/<iniziale_nome><iniziale_cognome>-<nome_feature>`, al cui interno viene sviluppato il codice per risolvere ciascuna
delle issue. Di seguito è riportata una immagine che rappresenta il flusso di sviluppo:

![Git-Flow Workflow](./images/git_flow.svg)

Quando un membro ha completato il suo task, effettua una `pull request` che sottometterà alla revisione da parte
degli altri membri del team. Nel momento in cui viene fatta questa richiesta viene anche associata la `issue` relativa alla
risoluzione del task, questa operazione facilita l'individuazione di task che non sono stati svolti.

### Strumenti di test, build e Continuous Integration
Il processo di sviluppo adottato richiede che le feature sviluppate dai vari membri del team siano a ogni pull request
integrate con quelle già presenti nel branch develop, dunque per rendere il processo di integrazione più fluido,
evitando collisioni, si è deciso di introdurre la `Continuous Integration`. Con il file `.github/workflows/ci.yml` viene definita una sequenza di operazioni
che vogliamo che siano svolte dalle `GitHub Actions`, le quali sono un insieme di macchine virtuali che `GitHub` mette a disposizione
per poterle eseguire.

All'interno della nostra pipeline il progetto viene `compilato` e `testato` su macchine di diversi sistemi operativi: `MacOS`,
`Windows` e `Ubuntu`, con due versioni della JVM, ossia la `1.11` e la `1.16`.

La `Continuous Integration` definita nel nostro progetto prevede che i test vengano eseguiti tutte le volte che si
effettua una `push` e una `pull request` sui branch `develop` e `master`. Grazie a questa configurazione, al momento
di ogni `pull request` è possibile visualizzare se i test passano o meno, mantenendo il codice in develop sempre funzionante.

Come libreria di testing è stata utilizzata [ScalaTest](https://www.scalatest.org/).

## Requisiti

// mini-introduzione sulle regole della genetica utilizzate? (cos'è genotipo, fenotipo, dominante, recessivo, omozigote etc...)

### Requisiti Business 
Di seguito sono riportati i requisiti individuati durante lo studio del dominio e le regole scelte per la sua rappresentazione.
* Il numero massimo di generazioni è 1000, considerato come numero limite difficilmente raggiungibile senza che i conigli muoiano tutti o superino i loro numero massimo.
* Il numero massimo di coniglietti è 1000, il raggiungimento di tale numero indicherà la fine della simulazione.
* Ogni coniglietto vive per quattro generazioni.

* Ogni coniglietto avrà inizialmente solamente geni con alleli di tipo base, per i quali la dominanza non è stata ancora scelta. Nel momento in cui viene introdotta la mutazione verrà anche specificato se è essa è dominante o recessiva. Finchè l'utente non compie questa scelta, i coniglietti procederanno nella riproduzione con i caratteri stabiliti inizialmente come base.
* Ogni generazione di conigli avrà alcuni tratti specifici ereditati dai suoi antenati e altri scaturiti da mutazioni genetiche, grazie a tali caratteristiche essi potranno avere più o meno probabilità di sopravvivenza nello specifico ambiente in cui vengono inseriti. Di seguito sono riportate i geni presi in considerazione nell'applicativo con le possibili mutazioni genetiche e la lettere che vi è associata nell'applicativo.

| Gene | Base | Mutazione | Lettera |
| :-------: | :-----: | :-----: | :--: |
| Colore della pelliccia | Bianco | Bruno | F |
| Lunghezza del pelo| Corto | Lungo | L |
| Denti | Corti | Lunghi | T |
| Orecchie | Alte | Basse | E |
| Salto | Normale | Alto | J |

* Sarà possibile influenzare l'evoluzione della specie attraverso vari fattori disturbanti:
    * La variazione delle condizioni ambientali, in particolare la presenza di temperature ostili.
    * La presenza di predatori.
    * La gestione delle risorse alimentari, che possono essere scarse, difficilmente raggiungibili o difficilmente masticabili.
* L’ambiente può avere un clima caldo o freddo, che andrà a influenzare l'efficacia dei fattori sopracitati.
* Ogni fattore disturbante elimina una certa percentuale di coniglietti dall’ambiente, in ogni generazione i fattori agiscono uno alla volta sulla popolazione di coniglietti rimasti rispetto all'azione di un eventuale fattore precedente.
* Ogni generazione dura 12 secondi, dei quali tre a cavallo fra le due generazioni sono dedicati alla riproduzione e nove per l'eliminazione dei coniglieti dovuta alla presenza dei fattori disturbanti, tre per ognuno di essi, nel seguente ordine: predatori, temperature ostili, risorse alimentari.

* Per quanto riguarda la riproduzione, dalla totalità dei coniglietti si formano casualmente delle coppie, ognuna delle quali genera quattro figli in modo da avere tutte le combinazioni degli alleli rappresentate dal Quadrato di Punnett. </br> Ad esempio, avendo 21 coniglietti si formano 10 coppie, ognuna delle quali fa 4 figli per un totale di 40 figli, quindi al termine della riproduzione avremo 21 + 40 = 61 coniglietti. </br> Di seguito è riportato un esempio di Quadrato di Punnett per il gene riguardante il colore della pelliccia (lettera <tt>f</tt>), con i figli dati dalla coppia d'esempio <tt>ff + fF</tt>.

| x | F  | f | 
| :--: | :--: | :--: |
| f | fF  | ff | 
| f | fF  | ff | 

* Quando viene introdotta una mutazione nel (50%+1) delle coppie formate un figlio su quattro la presenterà, verrà quindi mostrato per quel gene una coppia di alleli dovuta alla mutazione e che prescinde dagli alleli dei genitori e dalle combinazioni previste dal Quadrato di Punnett. La mutazione viene introdotta come omozigote, ovvero con due alleli di tipo mutato, a prescindere che essi siano dominanti o recessivi. </br> Ad esempio, l'utente sceglie di inserire la mutazione dominante per il gene riguardante il colore della pelliccia (lettera <tt>f</tt>), perciò tutti i coniglietti con fenotipo base presenti fino a quel momento devono essere formati da un alleli recessivi (<tt>ff</tt>), mentre il coniglietto mutato sarà formato da due allei dominanti (<tt>FF</tt>). </br> Di seguito è riportato un esempio dei quattro figli generati da un coppia in cui viene introdotta questa mutazione.

| x | f  | f | 
| :--: | :--: | :--: |
| f | ff  | *FF* | 
| f | ff  | ff | 

* Nel caso in cui siano definite più mutazioni (su vari geni) durante la medesima generazione si cercherà di mantenere un'unica mutazione per coniglio, le mutazioni si andranno ad accumulare sullo stesso figlio solo nel caso in cui non si disponga di abbastanza conigli.

### Requisiti Utente
Di seguito sono riportati i requisiti visti nell'ottica di cosa può fare l'utente con l'applicativo.
* L'utente visualizzerà lo stato attuale della simulazione nel pannello principale: quanti conigli sono presenti e quali caratteristiche possiedono, le condizioni ambientali e l'avanzamento temporale delle generazioni;
* Tale pannello sarà realizzato con un'interfaccia grafica 2D accattivante ed intuitiva.
* L'utente potrà modificare il clima in cui si riproducono i coniglietti.
* L'utente potrà scegliere quali mutazioni introdurre, specificando quali di esse sono dominanti e quali recessive.
* L'utente potrà aggiungere vari fattori che condizioneranno l'evoluzione.
* L'utente potrà visualizzare il patrimonio genetico di ogni coniglietto, avendo ogni gene rappresentato da una lettere che compare maiscuola se ne rappresenta l'allele dominante e minuscola se ne rappresenta l'allele recessivo.
* L'utente potrà decidere di visualizzare vari grafici:
  * Un grafico a linee che rappresenta la popolazione di conigli con l'avanzare delle generazioni.
  * L' albero genealogico di uno specifico coniglio a sua scelta, per evidenziare quali caratteristiche sono state ereditate dai loro antenati.
  * Per ogni generazione, un grafico contenente la proporzione fra coniglietti con i due fenotipi disponibili per ogni gene.

### Requisiti Funzionali 
### Requisiti non Funzionali 
### Requisiti di Implementazione 
* La JVM >= v1.11 è richiesta per la nostra implementazione in ScalaFX

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
## Retrospettiva
