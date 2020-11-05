#!/bin/bash

#takes as input mac or number of steps, for example ./runCoverageWBS.sh linux 1

if [ "$#" -ne 2 ]
then
     echo "Arguments are not equals to 2"
     exit 1
fi

if [ "$1" = "mac" ]; then
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/Users/sohahussein/git/rangerCopy/java-ranger/lib TARGET_CLASSPATH_WALA=/Users/sohahussein/git/rangerCopy/java-ranger/build/examples/ java -Djava.library.path=/Users/sohahussein/git/rangerCopy/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /Users/sohahussein/git/jpf-core/build/RunJPF.jar '
else
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/soha/git/rangerCopy/java-ranger/lib TARGET_CLASSPATH_WALA=/home/soha/git/rangerCopy/java-ranger/build/examples/ java -Djava.library.path=/home/soha/git/rangerCopy/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '
fi

#-Xmx1024m

shopt -s expand_aliases

if [ "$1" = "mac" ]; then
  COVERAGEDIR=/Users/sohahussein/git/rangerCopy/java-ranger
else
  COVERAGEDIR=/home/soha/git/rangerCopy/java-ranger
fi

MAX_STEPS=$2 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"


runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/siena/sienaCollect.jpf >& $COVERAGEDIR/logs/siena/sienaCollect_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/siena/sienaCollect_Prune.jpf >& $COVERAGEDIR/logs/siena/sienaCollect_Prune_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/siena/sienaCollect_Guide.jpf >& $COVERAGEDIR/logs/siena/sienaCollect_Guide_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/siena/sienaCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/siena/sienaCollect_Prune_Guide_steps$2.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/siena/sienaJR_Collect.jpf >& $COVERAGEDIR/logs/siena/sienaJRCollect_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/siena/sienaJR_Collect_Guide.jpf >& $COVERAGEDIR/logs/siena/sienaJR_Collect_Guide_steps$2.log