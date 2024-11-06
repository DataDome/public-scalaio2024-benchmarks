# How to use

All commands are run inside sbt.

Run with:

    Jmh/run -i 10 -wi 10 -f1 -t1

Quick feedback:

    Jmh/run -i 2 -wi 2 -f1 -t1

Specific benchmark (using a regex):

    Jmh/run -i 10 -wi 10 -f1 -t1 co.datadome.pub.scalaio2024.benchmarks.chapter2..*


# Learn more

[Understanding JIT compilation in Java](https://medium.com/@sakshee_agrawal/understanding-just-in-time-jit-compilation-in-java-ae2a6b9fa931):
A quick read, which will give you a few keywords to search for regarding JIT optimization.

[What's new in CPUs since the 80s?](https://danluu.com/new-cpu-features/):
Long and extremely detailed read about CPU optimization. Just skimming it will let you learn the basics about what modern CPUs do.

[C-style for loops in Scala 3](https://august.nagro.us/scala-for-loop.html):
Some more details about why for-loops are problematic in Scala.
