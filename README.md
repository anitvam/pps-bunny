# pps-bunny
`pps-bunny` is a simulator of the bunny population. With a game-like interface it is possible to interfere with the normal reproduction of the population. This project is developed for academic purposes for the course `Paradigmi di Programmazione e Sviluppo` of `University of Bologna` under the academic year 2020/2021.

The project is inspired by the following simulator: https://phet.colorado.edu/en/simulation/natural-selection.

## Requirements
The following dependencies are required to play `pps-bunny`:
- SBT v1.5.5 - Build tool required to execute the source code or tests
- Scala v2.13
- JVM >= v1.11 - Java Virtual Machine on which is executed Scala

## Usage
You can find the latest `jar` of the application inside the [`GitHub Release` section](https://github.com/anitvam/pps-bunny/releases).
To execute the application, simply run:
```
$ java -jar `path-to-downloaded-jar`
```

Alternatively, you can clone the repository and execute the following commands to generate the `jar` executable file:
```
$ sbt compile
$ sbt assembly
```

## Test
You can execute tests with the command:
```
$ sbt test
```

## Authors
- Martina Baiardi ([anitvam](https://github.com/anitvam))
- Asia Lucchi ([aslucc](https://github.com/aslucc))
- Alessia Rocco ([alessiarocco98](https://github.com/alessiarocco98))
- Marta Spadoni ([martaSpadoni](https://github.com/martaSpadoni))


Some icons made by [Freepik](https://www.freepik.com") from [Flaticon](https://www.flaticon.com/).
