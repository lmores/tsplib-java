# TSPLIB Java package

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.lmores.tsplib/tsplib?style=for-the-badge)](https://central.sonatype.com/artifact/io.github.lmores.tsplib/tsplib)

This library allows to read files in [TSPLIB](http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95) format.
All types of problems in TSPLIB format are supported, except those with extension `.problems`.

This package includes a copy of each file contained in the official TSPLIB archive.

The documentation is available [here]( https://javadoc.io/doc/io.github.lmores.tsplib/tsplib).


## How to

```java
import io.github.lmores.tsplib.TspInstance;
import io.github.lmores.tsplib.TsplibArchive;

String[] filenames = TsplibArchive.extractTspFilenames();
System.out.println(Arrays.toString(filenames));    // ["a280.opt.tour", "a280.tsp", "ali535.tsp", ...]

TspInstance instance = TsplibArchive.loadTspInstance("a280.tsp");
System.out.println(instance.name());               // "a280"
System.out.println(instance.comment());            // "drilling problem (Ludwig)"
System.out.println(instance.dimension());          // 280
System.out.println(instance.edgeWeightType());     // EUC_2D
System.out.println(instance.getEdgeWeight(0, 1));  // 20

Solutions solutions = TsplibArchive.loadTspTour("a280.opt.tour");
System.out.println(solutions.name());                       // "./TSPLIB/a280.tsp.optbc.tour"
System.out.println(solutions.comment());                    // ""
System.out.println(solutions.dimension());                  // 280
System.out.println(solutions.tours().length);               // 1
System.out.println(Arrays.toString(solutions.tours()[0]));  // [0, 1, 241, 242, 243, ...]

// Analogous methods are available also for ATSP, HCP, SOP and VRP instances (see the javadoc).
```
