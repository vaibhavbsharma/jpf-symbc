#!/bin/bash

## takes as input mac or number of steps, for example ./runCoverageWBS.sh linux 1

if [ "$1" = "mac" ]; then
  #export DYLD_LIBRARY_PATH=/Users/sohahussein/git/java-ranger/lib
  export TARGET_CLASSPATH_WALA=/Users/sohahussein/git/java-ranger/build/examples/
  alias runCoverage='java -Djava.library.path="/Users/sohahussein/git/java-ranger/lib" -jar /Users/sohahussein/git/jpf-core/build/RunJPF.jar '
else
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/soha/git/java-ranger/lib TARGET_CLASSPATH_WALA=/home/soha/git/java-ranger/build/examples/ java -Djava.library.path=/home/soha/git/java-ranger/lib  -ea -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '
fi

#-Xmx1024m

shopt -s expand_aliases

if [ "$1" = "mac" ]; then
  COVERAGEDIR=/Users/sohahussein/git/java-ranger
else
  COVERAGEDIR=/home/soha/git/java-ranger
fi

MAXSTEPS=$2 && export MAX_STEPS

echo "maxsteps is $MAXSTEPS"

#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Steps_$MAXSTEPS.log
runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollectPrune.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Prune_Steps_$MAXSTEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollectGuide.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Guide_Steps_$MAXSTEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollectPrune_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Prune_Guide_Steps_$MAXSTEPS.log
