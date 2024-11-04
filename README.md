All commands are run inside sbt.

Run with:

    jmh:run -i 10 -wi 10 -f1 -t1

Quick feedback:

    jmh:run -i 2 -wi 2 -f1 -t1

Specific benchmark (using a regex):

    jmh:run -i 10 -wi 10 -f1 -t1 co.datadome.pub.scalaio2024.benchmarks.chapter2..*
