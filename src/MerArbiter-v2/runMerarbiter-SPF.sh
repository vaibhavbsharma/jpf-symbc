#!/bin/bash
JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-merarbiter-v2='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/merarbiter-v2/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '

shopt -s expand_aliases
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/merarbiter-v2/ && export TARGET_CLASSPATH_WALA 

for STEPS in {6..6}; do
  for MODE in {1..1}; do
    echo "running MerArbiter-v2.$((STEPS))step.mode$((MODE))";
    MAX_STEPS=$(($STEPS)) && export MAX_STEPS 
    timeout $(($TIMEOUT_MINS))m   java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar $JRDIR/src/MerArbiter-v2/merarbiterSPF.mode$((MODE)).jpf >& $JRDIR/logs/merarbiter.$((STEPS))step.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
      echo "running MerArbiter-v2.$((STEPS))step.mode$((MODE)) timed out" >> $JRDIR/logs/merarbiter.$((STEPS))step.mode$((MODE)).log
    fi
  done;
done

