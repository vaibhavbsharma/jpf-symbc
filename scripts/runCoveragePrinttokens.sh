#!/bin/bash

#no input is provided, the number o steps are hardcoded


alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx5000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=1 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

mkdir $COVERAGEDIR/logs/log_printtokens2

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensCollect.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensCollect_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensCollect_Prune.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensCollect_Prune_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensCollect_Guide.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensCollect_Guide_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensCollect_Prune_Guide_steps$MAX_STEPS.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJRCollect.mode2_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJRCollect.mode3_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJRCollect.mode4_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJRCollect.mode5_steps$MAX_STEPS.log

#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect_Guide.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJR_Collect_Guide_steps$MAX_STEPS.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect.mode3NoBatch.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJR_Collect.mode3NoBatch_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/printtokensJR_Collect.mode3PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/printtokensJR_Collect.mode3PlainJR_steps$MAX_STEPS.log
