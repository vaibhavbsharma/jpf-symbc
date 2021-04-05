#!/bin/bash

#takes as input mac or number of steps, for example ./runCoverageWBS.sh linux 1

if [ "$#" -ne 2 ]
then
     echo "Arguments are not equals to 2"
     exit 1
fi

if [ "$1" = "mac" ]; then
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/Users/sohahussein/git/java-ranger/lib TARGET_CLASSPATH_WALA=/Users/sohahussein/git/java-ranger/build/examples/ java -Djava.library.path=/Users/sohahussein/git/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /Users/sohahussein/git/jpf-core/build/RunJPF.jar '
else
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/soha/git/java-ranger/lib TARGET_CLASSPATH_WALA=/home/soha/git/java-ranger/build/examples/ java -Djava.library.path=/home/soha/git/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '
fi

#-Xmx1024m

shopt -s expand_aliases

if [ "$1" = "mac" ]; then
  COVERAGEDIR=/Users/sohahussein/git/java-ranger
else
  COVERAGEDIR=/home/soha/git/java-ranger
fi

MAX_STEPS=$2 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"


runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/TCASCollect.jpf >& $COVERAGEDIR/logs/tcas/TCASCollect_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect_Prune.jpf >& $COVERAGEDIR/logs/tcas/TCASCollect_Prune_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect_Guide.jpf >& $COVERAGEDIR/logs/tcas/TCASCollect_Guide_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/tcas/TCASCollect_Prune_Guide_steps$2.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/TCASJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/tcas/TCAS_JR_Collect.mode2_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/TCASJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/tcas/TCAS_JR_Collect.mode3_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/TCASJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/tcas/TCAS_JR_Collect.mode4_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/TCASJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/tcas/TCAS_JR_Collect.mode5_steps$2.log
