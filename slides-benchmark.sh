IT=20
WIT=10

sbt clean compile
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter2.IterationBenchmark_Large.compute[01].*" | tee -a chapter2-iteration.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter3.CollectionStringIterationBenchmark_(Small|Large)" | tee -a chapter3-iteration.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter3.AppendBenchmark_String_Large" | tee -a chapter3-append.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter3.SetBenchmark_.*Invoice_HalfHit_(VerySmall|Small|Large).*" | tee -a chapter3-sets.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter3.MapBenchmark_Invoice_HalfHit_(Small|Large).*" | tee -a chapter3-maps.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter4.StatsBenchmark_(Small|Large).f(astF)?oreach.*" | tee -a chapter4-stats.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter4.GameBenchmark(Small|Large).*" | tee -a chapter4-game.report.txt
sbt "Jmh/run -i $IT -wi $WIT -f1 -t1 .*chapter5.*(Small|Large).*gaston[134]" | tee -a chapter5-gaston.report.txt
