#!/bin/bash


#no input is provided, the number o steps are hardcoded


alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ timeout 180m java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx5000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=1 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"



runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleCollect.jpf>& $COVERAGEDIR/logs/schedule/scheduleCollect_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleCollect_Prune.jpf >& $COVERAGEDIR/logs/schedule/scheduleCollect_Prune_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleCollect_Guide.jpf >& $COVERAGEDIR/logs/schedule/scheduleCollect_Guide_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/schedule/scheduleCollect_Prune_Guide_steps$MAX_STEPS.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/schedule/scheduleJRCollect.mode2_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/schedule/scheduleJRCollect.mode3_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/schedule/scheduleJRCollect.mode4_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/schedule/scheduleJRCollect.mode5_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/schedule/scheduleJR_Collect_Guide.jpf >& $COVERAGEDIR/logs/schedule/scheduleJR_Collect_Guide_steps$MAX_STEPS.log