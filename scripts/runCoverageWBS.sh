#!/bin/bash

#takes as input mac or number of steps, for example ./runCoverageWBS.sh linux 1

if [ "$#" -ne 2 ]
then
     echo "Arguments are not equals to 2"
     exit 1
fi

if [ "$1" = "mac" ]; then
  exit 1
  #export DYLD_LIBRARY_PATH=/Users/sohahussein/git/java-ranger/lib
  export TARGET_CLASSPATH_WALA=/Users/sohahussein/git/java-ranger/build/examples/
  alias runCoverage='java -Djava.library.path="/Users/sohahussein/git/java-ranger/lib" -jar /Users/sohahussein/git/jpf-core/build/RunJPF.jar '
else
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/soha/git/java-ranger/lib TARGET_CLASSPATH_WALA=/home/soha/git/java-ranger/build/examples/ java -Djava.library.path=/home/soha/git/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '
fi


shopt -s expand_aliases

if [ "$1" = "mac" ]; then
  COVERAGEDIR=/Users/sohahussein/git/java-ranger
else
  COVERAGEDIR=/home/soha/git/java-ranger
fi

MAX_STEPS=$2 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSCollect.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSCollect_Prune.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Prune_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSCollect_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Guide_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Prune_Guide_steps$2.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.jpf >& $COVERAGEDIR/logs/wbs/WBSJR_Collect_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect_Prune.jpf >& $COVERAGEDIR/logs/wbs/WBSJR_Collect_Prune_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSJR_Collect_Guide_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect_Prune_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSJR_Collect_Prune_Guide_steps$2.log