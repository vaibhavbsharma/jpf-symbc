#!/bin/bash

if [ "$1" = "mac" ]; then
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/Users/sohahussein/git/java-ranger/lib TARGET_CLASSPATH_WALA=/Users/sohahussein/git/java-ranger/build/examples/ java -Djava.library.path=/Users/sohahussein/git/java-ranger/lib  -ea -Dfile.encoding=UTF-8 -jar /Users/sohahussein/git/jpf-core/build/RunJPF.jar '
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

MAX_STEPS=2 && export MAX_STEPS

runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect.jpf >& $COVERAGEDIR/logs/coverage/tcas/TCASCollect.log
runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect_Prune.jpf >& $COVERAGEDIR/logs/coverage/tcas/TCASCollect_Prune.log
runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect_Guide.jpf >& $COVERAGEDIR/logs/coverage/tcas/TCASCollect_Guide.log
runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/tcas/TCASCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/coverage/tcas/TCASCollect_Prune_Guide.log
