# PPS-BUNNY

[Baiardi Martina](mailto:martina.baiardi4@studio.unibo.it),
[Lucchi Asia](mailto:asia.lucchi@studio.unibo.it),
[Spadoni Marta](mailto:marta.spadoni2@studio.unibo.it),
[Rocco Alessia](mailto:alessia.rocco@studio.unibo.it)

## Descrizione
`pps-bunny` è un simulatore per osservare l'evoluzione naturale di una famiglia di conigli nel corso delle generazioni.
Per far interagire l'utente con la simulazione è prevista una interfaccia grafica game-like che permetta di inserire 
delle mutazioni sia dei conigli che dell'ambiente circostante. 

La proposta si ispira al seguente gioco: https://phet.colorado.edu/en/simulation/natural-selection.

### Genetica
Il dominio dell'applicazione riguarda la genetica, perciò si è pensato di fornire una breve introduzione su tale argomento e sulla terminologia utilizzata nella relazione. 

La genetica studia il genoma, formato da cromosomi e geni dai quali dipende il patrimonio genetico della prole che è composto dagli attributi trasmessi dai genitori e determinerà l'aspetto dei figli.  <br /> Ogni gene riguarda una caratteristica, che nell'ambito della riproduzione dei conigli può essere ad esempio il *colore della pelliccia* o la *forma delle orecchie*. Ogni gene è formato da due alleli ed ogni allele che può comparire in due forme, ad esempio per il *colore della pelliccia* possono essere la *pelliccia bianca* o la *pelliccia bruna*. Una delle due forme è dominante e l'altra recessiva, ciò significa che se compaiono entrambe per un certo gene quella dominante prevarrà, sarà visibile sul figlio e di conseguenza farà parte del suo *fenotipo*. <br /> Ogni figlio eredita dai genitori una coppia di alleli, uno dal padre ed uno dalla madre. Tale coppia sarà una delle combinazioni degli alleli dei genitori rappresentata nel  [Quadrato di Punnett](https://it.wikipedia.org/wiki/Quadrato_di_Punnett), uno schema che per una qualsiasi coppia di genitori con certi alleli mostra le quattro combinazioni di alleli possibili per formare il gene del figlio. 

#### Glossario
* **Gene** - Una caratteristica dell'organismo, è composto da due alleli.
* **Allele** - Componente del gene che può comparire in due forme, una dominante e un recessiva.
* **Dominante** - Tipo di allele che, se presente, viene mostrato all'esterno.
* **Recessivo** - Tipo di allele che viene mostrato solamente se non si trova insieme ad un allele di tipo dominante.
* **Genotipo** - L'intero patrimonio genetico di un organismo.
* **Fenotipo** - Le caratteristiche effettivamente mostrate dall'organismo.
* **Omozigote** - Un gene che contiene due alleli dello stesso tipo, siano essi entrambi dominanti o entrambi recessivi.
* **Eterozigote** - Un gene che contiene alleli di tipo diverso, uno dominante ed uno recessivo.

## Processo di sviluppo adottato
Il processo di sviluppo adottato mette in pratica le metodologie previste dalla programmazione Agile, in particolare alcuni aspetti del framework **Scrum**:
* prevedendo delle riunioni periodiche per organizzare il lavoro, lo Sprint Planning e la Sprint Review,
* producendo artefatti come il Product Backlog e il resoconto di ogni riunione,
* tenendo traccia dello svolgimento di ogni task da parte dei singoli componenti del team.

Il **Product Backlog** contiene le funzionalità fondamentali da sviluppare per la realizzazione dell'applicativo e viene aggiornato durante ogni Sprint Planning in base alle esigenze che emergono man mano che il progetto avanza. Per ogni feature sono individuati i task che ne permettono il completamento, a ognuno di essi è assegnato un punteggio che indica la difficoltà prevista per portarlo a termine.  <br /> Durante la **Sprint Review** si aggiorna il punteggio per i task dello Sprint appena concluso, per valutare la differenza fra la complessità prevista e quella reale. Inoltre durante la revisione si chiudono le eventuali Pull Request lasciate in sospeso e si rilascia un eseguibile dello stato attuale dell'applicazione.

### Sprint
Per gli **Sprint** si è scelta una durata di circa due settimane, con la preparazione dello Sprint successivo prevista il lunedì.

Per ogni Sprint, durante lo **Sprint Planning** si prefissa un obiettivo in termini di prototipo funzionante da produrre alla scadenza usando un approccio di sviluppo incrementale. In funzione di tale obiettivo si selezionano i task da completare durante lo Sprint fra tutti quelli individuati nel Product Backlog. Ogni task viene assegnato a un membro del team in modo che ogni sviluppatore nel corso della realizzazione del progetto si cimenti in vari aspetti (GUI, Model, Design, Documentazione) e che per ogni Sprint il carico sia adeguatamente bilanciato fra i componenti. <br /> Infine durante ogni Sprint Planning si discutono gli aspetti inerenti la modellazione del dominio in modo che tutti i membri del gruppo abbiano chiaro come lo si intende rappresentare, al fine di evitare fraintendimenti. <br /> Per lo Sprint Planning è prevista una durata di circa due ore, in realtà alcune pianificazioni hanno richiesto a malapena una ora mentre altre, in particolare quelle iniziali, sono arrivate a più di tre ore.

### Obiettivi
Di seguito sono riportati gli obiettivi fissati ed i risultati realmente prodotti in ogni Sprint.

| Sprint       | Obiettivo    | Risultato     |
| :------------- | :---------- | :----------- |
| 1 | Produrre una modellazione corretta e possibilmente completa del sistema.  <br /> Questo è l'unico Sprint che ha avuto una durata inferiore alle due settimane (circa 10 giorni) e il cui obbiettivo non è un prototipo tangibile per un eventuale utente. Si è scelto di dedicare un intero Sprint alla modellazione in modo che tutti i componenti del team avessero tempo di studiare il dominio e per dare centralità al design dell'applicativo. | Obiettivo portato a termine. |
| 2 | Implementare una prima versione dell'applicazione, in cui è possibile osservare visivamente la riproduzione dei coniglietti. Nella pratica, si tratta di: <ul><li>Realizzare il model di base per la rappresentazione del coniglietto ed il suo patrimonio genetico.</li><li>Creare la struttura MVC dell'applicativo ed il loop che permette di avanzare nelle generazioni.</li><li> Visualizzare il pannello principale con i coniglietti che saltano.</li></ul>  | Obiettivo portato a termine. |
| 3 | Integrare alcune feature alla prima versione prodotta, in particolare: <ul><li>Estendere la GUI con tutti i pannelli necessari, in particolare quelli per la scelta delle mutazioni e del grafico.</li><li>Aggiungere  le mutazioni ai conigli.</li><li>Dare la possibilità di scegliere la dominanza delle mutazioni introdotte.</li><li>Dare la possibilità di cambiare clima.</li><li>Visualizzare l'albero genealogico di un qualsiasi coniglio.</li><li>Visualizzare il grafico con la cardinalità della popolazione.</li><li>Visualizzare il grafico con le proporzioni.</li><li>Iniziare la stesura del report.</li></ul> | Obiettivo portato a termine.   |
| 4 | Integrare nel prototipo prodotto nel precedente sprint le seguenti funzionalità: <ul><li>Realizzare il model dei fattori disturbanti</li><li>Estendere la GUI con il pannello per la scelta dei fattori disturbanti</li><li>Visualizzazione dei predatori durante la simulazione</li><li>Aggiungere al grafico delle proporzioni la funzionalità _history_</li><li>Aggiungere il _restart_ al termine della simulazione</li> <li>Dare la possibilità di modificare la velocità della simulazione</li><li>Aggiungere alla GUI un orologio per visualizzare il trascorrere del tempo della simulazione</li>| Obiettivo portato parzialmente a termine.|
| 5 | Rifinire il prototipo realizzato nello sprint precedente ed aggiungere la feature mancante: la realizzazione dell'orologio, per poter rilasciare la release definitiva del sistema. Completare la stesura del report. Questo Sprint ha avuto una durata di 10 giorni. | Obiettivo portato a termine. |

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

### Scelta degli strumenti
- sbt
- ScalaTest
- Trello
- ScalaFMT
- ScalaFX

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

### Requisiti Business
L'obiettivo del progetto è quello di sviluppare un simulatore per l'evoluzione di una popolazione di conigli. Il simulatore sarà corredato da un'interfaccia grafica che permetterà all'utente di interferire con la naturale evoluzione della specie.
Il sistema permetterà di modificare il patrimonio genetico dei coniglietti in modo tale da poter individuare, durante la simulazione, quali tratti favoriscono lo sviluppo della specie in diverse tipologie di ambiente.
Sarà inoltre possibile studiare come le varie caratteristiche genetiche vengono ereditate dalle generazioni future.

L'interfaccia grafica permetterà all'utente di visualizzare ogni singolo coniglietto con l'insieme dei tratti ereditati dai genitori e l'ambiente di simulazione in cui la popolazione si trova, caratterizzato da 3 principali componenti:
* Il clima: estivo o invernale 
* La presenza o meno dei predatori
* La tipologia di risorse alimentari presenti

Il sistema permetterà inoltre all'utente di avere un aggiornamento real-time dell'andamento della simulazione in termini di cardinalità della popolazione e della distribuzione delle diverse caratteristiche genetiche.
Tali informazioni saranno inoltre rappresentate da opportuni grafici.

### Requisiti Utente
Di seguito sono riportati i requisiti visti nell'ottica di cosa può fare l'utente con l'applicativo.
* L'utente potrà visualizzare lo stato attuale della simulazione nel pannello principale dell'interfaccia: quanti conigli sono presenti e quali caratteristiche possiedono, le condizioni ambientali e l'avanzamento temporale delle generazioni;
* Tale pannello sarà realizzato con un'interfaccia grafica 2D accattivante ed intuitiva.
* L'utente potrà modificare il clima in cui si riproducono i coniglietti.
* L'utente potrà scegliere quali mutazioni introdurre, specificando quali di esse sono dominanti e quali recessive.
* L'utente potrà gestire i vari fattori che condizioneranno l'evoluzione.
* L'utente potrà visualizzare il patrimonio genetico di qualsiasi coniglietto. Ogni gene è rappresentato da una lettera dell'alfabeto, se maiscuola corrisponde all'allele dominante mentre se minuscola all'allele recessivo. Ogni coniglietto perciò mantiene una coppia di lettere per ogni gene, corrispondenti agli alleli ereditati dai genitori. Ad esempio, se il gene *Orecchie* corrisponde alla lettera <tt>e</tt> e il coniglietto è eterozigote per quel gene, allora sarà rappresentato dalla coppia di lettere <tt>eE</tt>.
* L'utente potrà decidere di visualizzare vari grafici:
  * Un grafico a linee che mostrerà la cardinalità della popolazione di conigli con l'avanzare delle generazioni.
  * L' albero genealogico di uno specifico coniglio a sua scelta, per evidenziare quali caratteristiche sono state ereditate dai loro antenati.
  * Dei grafici a torta rappresentanti la proporzione fra coniglietti con i due fenotipi disponibili per ogni gene, all'inizio e al termine di ciascuna generazione.
* L'utente potrà incrementare la velocità della simulazione

### Requisiti Funzionali 
Di seguito sono riportati i requisiti individuati durante lo studio del dominio e le regole scelte per la sua rappresentazione.
* Il numero massimo di generazioni è 1000, considerato come numero limite difficilmente raggiungibile senza che i conigli muoiano tutti o superino il loro numero massimo.
* Il numero massimo di coniglietti è 1000, il raggiungimento di tale numero indicherà la fine della simulazione.
* Ogni coniglietto vive per quattro generazioni.

* Ogni coniglietto sarà caratterizzato da un genere, un'età e un patrimonio genetico.
* Il patrimonio genetico di ogni coniglietto sarà inizialmente costituito solo da geni con alleli di tipo base, per i quali la dominanza non è stata ancora scelta. Nel momento in cui viene introdotta la mutazione verrà anche specificato se essa è dominante o recessiva. Finchè l'utente non compie questa scelta, i coniglietti procederanno nella riproduzione con i caratteri stabiliti inizialmente come base.
* Ogni generazione di conigli avrà alcuni tratti specifici ereditati dai suoi antenati e altri scaturiti da mutazioni genetiche, grazie a tali caratteristiche essi potranno avere più o meno probabilità di sopravvivenza nello specifico ambiente in cui vengono inseriti. Di seguito sono riportati i geni presi in considerazione nell'applicativo con le possibili mutazioni genetiche e la lettera che vi è associata.

| Gene | Base | Mutazione | Lettera |
| ----- | ----- | ----- | ----- |
| Colore della pelliccia | Bianco | Marrone | F |
| Lunghezza del pelo| Corto | Lungo | L |
| Denti | Corti | Lunghi | T |
| Orecchie | Alte | Basse | E |
| Salto | Normale | Alto | J |

* Per quanto riguarda la riproduzione, dalla totalità dei coniglietti si formano casualmente delle coppie con il vincolo che ciascuna sia composta da un coniglietto maschio e uno femmina. Ogni coppia genera quattro figli in modo da avere tutte le combinazioni degli alleli rappresentate dal [Quadrato di Punnett](https://it.wikipedia.org/wiki/Quadrato_di_Punnett). </br> Ad esempio, avendo 21 coniglietti si formano 10 coppie, ognuna delle quali fa 4 figli per un totale di 40 figli, quindi al termine della riproduzione avremo 21 + 40 = 61 coniglietti. </br> Di seguito è riportato un esempio di [Quadrato di Punnett](https://it.wikipedia.org/wiki/Quadrato_di_Punnett) per il gene riguardante il colore della pelliccia (lettera <tt>f</tt>), con i figli dati dalla coppia d'esempio <tt>ff + fF</tt>.

| x | F  | f | 
| --- | --- | --- |
| f | fF  | ff | 
| f | fF  | ff | 

* Quando viene introdotta una mutazione, questa si presenterà solo su uno dei quattro figli del 50%+1 delle coppie. Il coniglietto mutato mostrerà per il relativo gene una coppia di alleli che prescinde da quelli dei genitori e dalle combinazioni previste dal [Quadrato di Punnett](https://it.wikipedia.org/wiki/Quadrato_di_Punnett). La mutazione viene introdotta come **omozigote**, ovvero con due alleli di tipo mutato, a prescindere che essi siano dominanti o recessivi. </br> Ad esempio, l'utente sceglie di inserire la mutazione dominante per il gene riguardante il colore della pelliccia (lettera <tt>f</tt>), perciò tutti i coniglietti con fenotipo di base presenti fino a quel momento devono essere formati da due alleli recessivi (<tt>ff</tt>), mentre il coniglietto mutato sarà formato da due allei dominanti (<tt>FF</tt>). </br> Di seguito è riportato un esempio dei quattro figli generati da un coppia in cui viene introdotta questa mutazione.

| x | f  | f | 
| --- | --- | --- |
| f | ff  | *FF* | 
| f | ff  | ff | 

* Nel caso in cui siano definite più mutazioni (su vari geni) durante la medesima generazione, si cercherà di mantenere un'unica mutazione per coniglio, le mutazioni si andranno ad accumulare sullo stesso figlio solo nel caso in cui non si disponga di abbastanza conigli.

* Sarà possibile influenzare l'evoluzione della specie attraverso vari fattori disturbanti:
  * La variazione delle condizioni ambientali, in particolare la presenza di temperature ostili.
  * L'aggiunta o la rimozione dall'ambiente dei predatori, in particolare dei lupi.
  * La gestione delle risorse alimentari, che possono essere scarse, difficilmente raggiungibili o difficilmente masticabili.
* L’ambiente può avere un clima caldo o freddo, che andrà a influenzare l'efficacia dei fattori sopracitati.
* Ogni fattore disturbante elimina una certa percentuale di coniglietti dall’ambiente, in ogni generazione i fattori agiscono uno alla volta sulla popolazione di coniglietti rimasta rispetto all'azione di un eventuale fattore precedente.
* Ogni generazione, come impostazione di partenza, ha una durata di 12 secondi, dei quali 3 sono dedicati alla riproduzione e 9 all'eliminazione dei coniglieti dovuta alla presenza dei fattori disturbanti (3 secondi per ogni tipologia). I fattori sono applicati nel seguente ordine: predatori, risorse alimentari, temperature ostili.
* Sarà possibile incrementare la velocità dello svolgimento della simulazione, andando a diminuire il tempo dedicato a ciascuna fase di ogni generazione.

### Requisiti non Funzionali
Di seguito sono descritti i requisiti non funzionali dell'applicativo:
* _Usabilità_: L'interfaccia grafica dovrà essere semplice ed intuitiva così da permettere ad un utente non esperto del dominio di comprendere quali sono le principali operazioni che può eseguire
* _User Experience_: L'interfaccia grafica sarà implementata in modo da rendere l'esperienza dell'utente con la simulazione ludica
* _Cross Platform_: Sarà possibile eseguire il sistema sui 3 principali sistemi operativi: Linux, Windows, MacOs.

### Requisiti di Implementazione 
Di seguito vengono riportati i requisiti relativi all'implementazione del sistema:
* Il sistema sarà sviluppato in Scala 2.13.6 e per eventuali feature sarà possibile integrare delle teorie Prolog
* Il sistema farà riferimento al JDK 11, eventuali librerie esterne utilizzate dovranno supportare almeno tale versione
* Il testing del sistema sarà effettuato utilizzando ScalaTest, in questo modo sarà minimizzata la presenza di errori e facilitato l'aggiornamento di eventuali funzionalità.
* Il codice sorgente sarà verificato mediante l'utilizzo del linter ScalaFMT


## Design architetturale
A seguito dell'analisi dei requisiti e data la natura del progetto si è scelto di adottare il pattern architetturale MVC (Model-View-Controller), in questo modo è possibile incapsulare le responsabilità dei singoli componenti e avere un'architettura modulare e flessibile.

Inoltre, dato che la simulazione si basa fortemente sulla possibilità dell'utente di interagire con essa, risulta fondamentale l'opportunità offerta dal pattern di poter realizzare interfacce grafiche differenti senza dover modificare il model, ad esempio con Framework più moderni.

### Utilizzo del Pattern MVC
In figura viene riportato un diagramma ad alto livello di come è stato strutturato il sistema per implementare il pattern architetturale scelto.
![](images/mvc_architecture.png)
Nello specifico è possibile osservare come l'applicativo si sviluppi su 4 componenti fondamentali:
* __Model__: modulo che incapsula tutte le classi relative alle entità e ai concetti che compongono il dominio della simulazione
* __Engine__: modulo che incapsula il loop su cui si basa la simulazione e le entità che gestiscono la storia della simulazione
* __View__: modulo che incapsula le entità che gestiscono l'interfaccia grafica del sistema e le classi che permettono di visualizzare i grafici
* __Controller__: componente che gestisce le interazioni tra Model e View e si occupa di avviare e gestire il loop.

## Design di dettaglio
### Model
#### Bunny
// mutabilità di age e alive solo dentro bunny, immutabilità di history bunny
#### Genoma
// Completed Genotype VS Partial Genotype
#### Reproduction e Mutation
#### Generazioni e Ambiente
#### Fattori

### Engine
Il modulo di `Engine` racchiude tutte le strutture dati necessarie per definire il motore della simulazione e l'aggiornamento della stessa. 
In questo package, al fine di adottare uno stile di programmazione funzionale, si è deciso di utilizzare la libreria Cats Effect per definire il `SimulationEngine` e il `GenerationTimer`, 
nello specifico si utilizza la Monade IO, un tipo di dato lazy che consente di codificare computazioni, sincrone o asincrone, contenenti side-effect come un valore puro.

#### SimulationEngine e GenerationTimer
Data la struttura sequenziale e periodica della simulazione è stato naturale modellare il suo andamento come una successione temporizzata delle varie fasi che la compongono.
Si è quindi voluto dapprima modellare in modo funzionale il timer che scandisce il tempo all'interno di ciascuna generazione, il `GenerationTimer`. 
Quest'ultimo fornisce un metodo per ritardare l'esecuzione di un task di uno specifico intervallo di tempo, attraverso una monade IO che può essere eseguita anche in modo asincrono.

Il vero motore della simulazione è però `SimulationEngine`, il quale fornisce una descrizione monadica del loop della simulazione.
Nello specifico, dato che ciascuna generazione è caratterizzata dalle medesime fasi si è deciso di modellare il singolo ciclo di una generazione, il `generationLoop`, come il susseguirsi delle sue fasi.
Ogni `generationPhase` è caratterizzata da un'azione che va a modificare il contesto della simulazione e un istante temporale in cui tale azione deve essere eseguita. Ad esempio, per quanto riguarda la fase dei lupi, questa ha come azione associata il "pasto" dei predatori, che comporterà la morte di alcuni coniglietti, e come istante assegnato il terzo secondo dall'inizio della simulazione.
Il loop generale della simulazione si ottiene mediante ricorsione, infatti se al termine del `generationLoop` non si è verificata una delle tre condizioni che portano alla terminazione della simulazione, si esegue un nuovo `generationLoop`.

Il `SimulationEngine` è inoltre caratterizzato da una `simulationSpeed`, tale valore permette di diminuire gli intervalli di tempo di attesa tra le varie fasi, così da velocizzare ciascuna generazione.

L'avvio della simulazione viene scatenato dal `Controller` a seguito dello start da parte dell'utente, nello specifico si attiva l'esecuzione asincrona del `simulationLoop`, il quale aggiorna l'interfaccia grafica per gestire la prima riproduzione relativa alla creazione dei `Bunny` capostipiti e in seguito avvia il loop della generazione zero.

#### Simulation History e Simulation
`SimulationHistory` è il Singleton che consente di gestire la storia della simulazione, modellata come una lista di `Generation`, di interagire con la generazione attualmente in esecuzione e di coordinare il passaggio da una generazione all'altra. 
Viene in particolar modo utilizzato dal `Controller`, per introdurre lato Model le modifiche dell'ambiente di simulazione attivate dall'utente attraverso l'interfaccia grafica e viene utilizzato indirettamente dal `SimulationEngine` all'interno del `generationLoop`.

`Simulation` è l'object di utility che consente di creare le monadi da utilizzare all'interno del `generationLoop`, ad esempio si occupa di monadizzare l'azione associata alla fase dei lupi descritta in precedenza. 
Nello specifico, `Simulation` permette di incapsulare attraverso delle monadi di tipo `IO[Unit]` le interazioni che il `SimulationEngine` ha con il `Controller` e `SimulationHistory`.

### View
// parlare di scalafx e scala-fxml 
// parlare del fatto che scalafx è un dsl che parte da scalafx e quindi prevede una implementazione orientata alla object orientation
#### Controllers
#### AnimalViews

###Grafici

Di seguito viene preposta una descrizione del design dei tre grafici introdotti nel sistema per avere in tempo reale delle informazioni strutturate riguardanti l'andamento della simulazine.
####Pedigree Chart

####Population Chart
L'obiettivo di questo grafico è quello di fornire all'utente informazioni real-time circa l'andamento della cardinalità della popolazione durante le fasi delle varie generazioni, evidenziando quindi quanti `Bunny` sono morti a causa dei vari fattori introdotti nell'ambiente e quanti sono i nuovi nati a seguito della riproduzione.
Per implementare questo tipo di grafico si è utilizzato come base il `LineChart` offerto dalla libreria ScalaFX trasformandolo però in uno _Step Chart_.

Per creare e personalizzare i vari componenti del grafico: gli assi, le serie di dati, i singoli dati e il grafico stesso, si è fatto uso del pattern Factory, in questo modo ad esempio, i dati (`XYChart.Data`)
possono essere creati facendo riuso di codice e incapsulando la logica che trasforma il LineChart in uno StepChart.  

L'utilizzo del paradigma funzionale puro per quanto riguarda la gestione dei dati da graficare è stato limitato dal fatto che ScalaFX è in realtà il wrapper scala di JavaFX quindi una libreria OOP, dunque
i tipi di dato creati non sono toltalmente esenti da side-effect ma ne limitano il più possibile.

Si è inoltre, utilizzato il pattern _Pimp my Library_ al fine di aggiungere metodi alle classi relative ai grafici di ScalaFX, in questo modo si è ad esempio facilitato l'accesso alla legenda dei grafici o il modo con cui aggiungere dati al grafico stesso.

####Proportions Chart
L'obiettivo del `Proportions Chart` è quello di riassumere come durante una generazione vari la distribuzione dell'allele base e quello mutato di un determinato gene sulla popolazione, permettendo quindi all'utente di determinare quale delle due caratteristiche associate al gene sopravviva meglio a determinati fattori.

Il grafico si sviluppa su due sotto-grafici a torta, il primo mostra la distribuzione degli alleli all'inizio della generazione mentre il secondo mostra la variazione della distribuzione durante le varie fasi.
Si è inoltre, inserita la modalità _history_ che consente di navigare tra le varie generazione per poter visualizzare l'andamento della distribuzione del gene durante il corso di tutta la simulazione.

Come per il `PopulationChart` si è fatto uso del pattern _Factory_, per generare i singoli grafici a torta, del pattern _Pimp my Library_ per aggiungere dei metodi alla classe `PieChart` di ScalaFX e del pattern _Adapter_, implementato tramite gli impliciti, per convertire i dati del Model in quelli adatti al grafico.

### Pattern di progettazione
Si è cercato di utilizzare il più possibile pattern di progettazione al fine di implementare soluzioni ottimali per problemi di design ricorrenti.
#### Pimp My Library
_Pimp My Library_ è sicuramente uno dei pattern più utilizzati all'interno del progetto, questo consente di arricchire classi già definite con dei nuovi metodi realizzati ad-hoc. 
Si è utilizzato infatti il pattern per estendere gli Option, le Tuple e le collection di Scala ma anche le strutture dati relative ai grafici della libreria JavaFX.
#### Factory
Si è utilizzato il pattern _Factory_ in due accezioni, sia quella propria di Scala che prevede l'utilizzo dei companion object per la costruzione delle strutture dati mantenendo quindi private le loro implementazioni, come fatto in `Environment` e `Generation`, sia nella versione più generale adottata per costruire e personalizzare le componenti dei grafici `PopulationChart` e `ProportionsChart`.
#### Builder
Il pattern _Builder_ è stato utilizzato nella versione implementata da Scala stesso, cioè mediante l'utilizzo di parametri di default nei costruttori. Un esempio è visibile nella creazione dei `Bunny`.
#### Strategy
Il pattern _Strategy_ è nativamente supportato dal linguaggio attraverso la possibilità di utilizzare funzioni higher-order. 
Un esempio di utilizzo è visibile nel `BaseAppController` per il caricamento dei componenti dei vari pannelli.
#### Adapter
Anche il pattern _Adapter_ è fornito nativamente dal linguaggio grazie alla possibilità di definire dei metodi impliciti per la conversione dei tipi di dato. 
Un esempio di uso di tale pattern è visibile nel componente grafico `PopulationChart` in cui si usano per trasformare le tuple Scala in tipi di dati propri del sistema come il `ChartPoint`
#### Template Method
Il pattern `Template Method` permette di definire la struttura di un comportamento utilizzando dei metodi astratti che verranno poi implementati dalle specifiche estensioni di tale classe. 
Questo pattern è stato utilizzato all'interno del package `it.unibo.pss.bunny.world.disturbingFactor` per avere un maggiore riuso di codice e rendere di conseguenza più semplici e comprensibili le specifiche implementazioni.
Un esempio concreto è visibile anche nella classe astratta `ClimateFactor` nel metodo `applyDamage`, il quale utilizza due metodi astratti: `summerAction` e `winterAction`. Questi metodi vengono poi implementati dai fattori che estendono tale classe, 
distinguendo il comportamento che verrà adottato dal fattore rispettivamente in presenza del clima estivo o del clima invernale.
#### Singleton
Il pattern _Singleton_ è facilmente implementabile in Scala ed è stato fondamentale per implementare, in particolare, il `Controller` e `SimulationHistory`.
### Organizzazione del codice
Nella figura riportata si evidenzia l'organizzazione in package dei sorgenti del sistema, ognuno dei quali raggruppa le classi relative a specifiche feature.
Per facilitare la lettura del diagramma sono stati omessi alcuni package minori, in particolare tutti i package relativi all'implementazione in scalaFX dell'applicazione.

![](images/package_architecture.png)  

## Implementazione
Nei seguenti paragrafi ciascun componente descriverà per quali parti è responsabile dell'implementazione.
### Baiardi

### Lucchi

Mi sono occupata dello sviluppo di parte del Model, in particolare i package `it.unibo.pps.bunny.model.bunny` (tranne il file `Mutation`), `it.unibo.pps.bunny.model.genome`, l'oggetto `Reproduction` del package `it.unibo.pps.bunny.model.world` e tutti i test che li riguardano. </br>
Per quanto riguarda la visualizzazione, ho sviluppato il Pedigree, ovvero il package `it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree` e la risorsa in prolog *pedigree_dim.pl*. Questa parte mi ha portato via molto più tempo del previsto e ha subito molte modifiche dovute alla continua scoperta di nuovi bug.</br>
Nel package `it.unibo.pps.bunny.util` mi sono occupata di `Scala2P` e ho aggiunto alcuni pimp in `PimpScala`. </br>
Ho lavorato marginalmente con l'fxml e gli elementi del package `it.unibo.pps.bunny.view.scalaFX.FXControllers`, ad esempio per l'introduzione del Reset dopo la conclusione della simulazione, l'aggiunta della legenda dei Geni e e l'aiuto nella risoluzione di alcuni bug.

#### TDD
Per l'implementazione dei concetti fondamentali del modello, ovvero il coniglietto, il genoma e la riproduzione ho utilizzato la tecnica del **TDD** al fine di produrre di volta in volta solamente il codice necessario per raggiungere obiettivi minimi, mettendo quindi al centro il *cosa* fare prima del *come*. Successivamente il codice è stato manipolato più volte per aumentare la qualità e grazie ai test a disposizione si è potuto facilmente controllare che il comportamento restasse invariato anche a fronte dei cambiamenti nella forma.

#### Ricorsione
Ho utilizzato la **ricorsione** nella costruzione dell'albero genealogico del coniglietto. </br>
Ho tentato di trasformare tale ricorsione in una di tipo *tail* senza successo e sono giunta alla conclusione che avendo due chiamate alla funzione ricorsiva dentro la funzione stessa, una per ogni genitore del coniglietto, non fosse possibile lasciare come ultima istruzione della funzione una soal chiamata ricorsiva perchè i risultati delle due chiamate devono essere combinati insieme in un unico nodo. </br>
Non è stato possibile usare un accumulatore perchè ognuna delle due chiamate si espande autonomamente e tentando di strutturare questo procedimento ci si ritrova a specificare manualmente l'intera composizione dell'albero perdendo totalmente l'utilità della ricorsione.

#### Prolog
Ho usato il **Prolog** per compiere operazioni matematiche al fine di ottenere le misure migliori possibili con cui parametrizzare l'albero genealogico in modo che fosse visualizzabile senza uscire dai limiti del suo pannello. In particolare, data una serie di misure, il gruppo di *clausole* permette di ottenere la dimensione del coniglietto e il numero ottimale di generazioni da rappresentare. Le misure inserite nel *goal* nello specifico sono:
* le dimensioni del pannello,
* la quantità di generazioni che si vorrebbero visualizzare per l'albero, 
* il limite di dimensione del singolo coniglietto in modo che la rappresentazione grafica abbia un minimo di qualità,
* il rapporto fra la dimensione del coniglietto ed altri elementi dell'albero come il simbolo dell'addizione, le immagini informative sullo stato del coniglio e la misura del font. 

L'idea della *teoria* sviluppata è la seguente:
* calcolare la dimensione massima che può avere il coniglietto per l'altezza e la larghezza del pannello,
* controllare che si trovi nel range delle misure possibili, in caso contrario sostituire la dimensione calcolata con l'upper bound o il lower bound,
* nel caso in cui la dimensione del coniglietto sia quella minima, ricalcolare il numero di generazioni in modo da diminuirle se la quantità originale non riesce a stare nel pannello usando per il coniglietto a dimensione minima invece di quella necessaria per visualizzare tutte le generazioni richieste.

Per quanto riguarda l'integrazione con il Prolog, ho creato un *engine* decisamente semplificato per risolvere solamente goal con un unico risultato in quanto sufficiente per l'uso che è stato fatto della programmazione logica. L'*engine* restituisce un `Option` contenente il risultato in caso di successo oppure vuoto in caso di fallimento.</br>
L'uso del Prolog inizialmente non era previsto, è stato inserito perchè eseguendo il calcolo delle dimensioni mi sono ritrovata a risolvere manualmente delle equazioni e mi è sembrato essere un adeguato ambito di applicazione della programmazione logica.

#### Funzioni Higher-Order
Ho ampio uso di **funzioni higher-order**, quindi del pattern Strategy, in particolare di quelle messe a disposizione dal linguaggio su costrutti come `List` o `Seq`. A tal fine ho creato funzioni usabili come parametro per evitare ripetizione di codice e la creazione di un metodo apposito, un esempio si può trovare nella funzione `generateChildren` dell'oggetto `Reproduction` in cui è utile crearsi una funzione `createBunny` che genera un coniglietto a partire dal genotipo e il genere e viene usata due volte in funzioni higher-order per ottenere i conigli di sesso femminile e maschile. </br> 
Ho creato svariate funzioni di questo tipo, ad esempio *generator* (ex. `baseBunnyGenerator`, `randomBunnyGenerator`, `spacingGenerator`), *chooser* (ex. `randomGenderChooser`, `randomAlleleKindChooser`) o *viewer* (ex. `infoViewer`, `allelesViewer`) registrate in delle variabili. Sebbene solitamente siano chiamate direttamente, si è preferito usare delle variabili invece che dei metodi statici in modo che queste funzionalità siano eventualmente usabili come parametro di funzione higher-order dato che essendo dei "generatori", "decisori" o "visualizzatori" si prestano a questo tipo di utilizzo.

#### Altro
Ho usato la **for comprehension** per generare le quattro combinazioni di alleli per i figli a partire da quelli dei genitori e creare i genotipi per i conigllietti aggiungendo man mano tutti i geni.

Ho fatto ampio uso di **`Object`** per la fruizione di metodi statici, in particolare sono presenti sia dei Companion Object (ex. `Bunny`) che degli oggetti non legati ad una specifica classe ma contenenti tutti i metodi per realizzare una funzionalità (ex. `Reproduction`) o per supportare altri oggetti (ex. `KindsUtils`).

Per controllare la validità delle azioni compiute sui conigli ho utilizzato svariate **eccezioni**, spesso attivate nel momento in cui si inizializza in maniera illecita un elemento dei package `it.unibo.pps.bunny.model.genome` e `it.unibo.pps.bunny.model.bunny`.

Gli **impliciti** sono stati usati per la conversione automatica di svariati elementi al fine di alleggerire il codice ed evitare l'uso ripetuto di `asInstanceOf[A]`.

Ho utilizzato delle **`Enumeration`** per esprimere le tipologie di Geni e di Alleli disponibili, questo costrutto mi è stato comodo per poter ciclare su tutti i Geni o gli Alleli, operazione molto frequente sia nei metodi che nei test del Model. Le enumerazioni contengono delle classi protette con le proprietà a cui è associato ogni elemento dell'enumerazione e gli elementi vengono implicitamente trasformati in un oggetto della classe ogni volta che se ne vuole conoscere una proprietà. 

Ho fatto uso di `Type` per creare degli **alias** e per rendere più leggibili alcune istruzioni, ad esempio esplicitando il significato che assume la sequenza di *bunny* nelle varie occasioni, in particolare nell'oggetto `Bunny`.

Nei trait `Genotype` e `Phenotype` ho usato le funzioni `values` e `apply` per accedere in maniera più comoda e veloce ai valori delle mappe a cui i trait citati fanno da wrapper.

### Spadoni 

La parte di progetto da me interamente sviluppata è quella contenuta nel package `it.unibo.pps.bunny.engine`. 
Dopo aver consultato alcuni dei progetti consigliati dal docente, nello specifico _evo-sim_ e _Primer_, ho deciso di volermi cimentare nell'implementare il loop della simulazione utilizzando la libreria Cats Effect, 
che consente di utilizzare il costrutto delle monadi senza la necessità di implementarle da zero.

Inoltre, ho implementato i grafici PopulationChart e Proportions Chart (quest'ultimo implementato in `it.unibo.pps.bunny.view.scalaFX.FXControllers.ProportionsChartController.scala`), i quali sono stati descritti precedentemente nella parte di Design di Dettaglio e che mi hanno portato a definire il file `PimpScalaFXChartLibrary` che racchiude tutte le classi implementate per attuare il pattern _Pimp My Library_.

Mi sono anche occupata di definire i concetti del Model di `Generation` e `Environment` insieme a Baiardi e ho partecipato, come tutti i componenti del gruppo, all'implementazione del `BaseAppController`. 
In particolare, di quest'ultimo ho anche realizzato il refactoring necessario al termine del progetto per migliorarne la qualità del codice.

Come elementi generali implementati ho sviluppato la classe di utility `PimpScala` che contiene l'implementazione di metodi aggiuntivi per alcune strutture dati di base di Scala, anche Lucchi ha aggiunto alcuni metodi.
Infine, ho aiutato Rocco nello sviluppo di `Mutation` e di `WolvesView`.

Per quanto riguarda la parte di test ho realizzato quelli riguardanti `SimulationHistory`, che trasversalmente testa anche `Generation`, e `Environment`, come metodologia di testing ho preferito adottare quella tradizionale piuttosto che il TDD.
### Rocco

### Testing
Per verificare la correttezza dell'implementazione delle principali strutture dati realizzate sono stati sviluppati dei test con il framework `ScalaTest`, in particolare con l'ausilio di `FunSpec` per rendere la loro descrizione più naturale.
Tali test consentono sia una verifica immediata del comportamento delle entità sviluppate sia una verifica continua per le modifiche apportate successivamente, che se effettuate nel modo sbagliato potrebbero comportare dei bug nel codice precedentemente implementato.

Per la metodologia di implementazione dei test si è lasciata la libera scelta ad ogni componente del gruppo: alcuni hanno preferito l'approccio TDD (Test Driven Development) per porzioni di codice importanti e complesse da sviluppare, mentre altri hanno scelto un approccio più tradizionale, cioè implementare prima una certa funzionalità e verificare in seguito che il comportamento sia quello atteso.  
Sono stati inoltre introdotti test dopo lo sviluppo di un insieme di funzionalità per verificare che la loro integrazione funzioni correttamente.

Si è cercato di mantenere più coverage possibile, per la parte di model dell'applicazione, riuscendo a raggiungere quasi il 100% nei package più critici e importanti del sistema come `it.unibo.pps.bunny.model.bunny` che contiene l'implementazione dei coniglietti, `it.unibo.pps.bunny.model.genome` che contiene della gestione del patrimonio genetico e `it.unibo.pps.bunny.model.world` che contiene la modellazione dell'ambiente di simulazione e della riproduzione.

È fornito di seguito uno screenshot che evidenzia le informazioni sopra riportate, calcolate con l'ausilio del plugin `sbt-scoverage`:

![](images/scoverage_report_2.png)

L'uso della CI su GitHub unito a tale livello di coverage garantisce che sul branch `develop` non sia mai stato inserito del codice non correttamente funzionante, infatti se i test non passano, la pull request effettuata dallo sviluppatore non viene mergiata.

Per quanto riguarda invece il package `it.unibo.pps.bunny.view` non sono stati implementati test di `ScalaTest` per verificarne il funzionamento, in quanto si è deciso di effettuare prevalentemente del beta-testing, cioè di eseguire direttamente l'applicativo per verificare ad esempio il movimento dei coniglietti e la gestione dei pannelli per l'inserimento delle mutazioni e dei fattori, oltre che per avere una controprova visiva del corretto funzionamento delle classi di model e dell'engine.

## Retrospettiva
