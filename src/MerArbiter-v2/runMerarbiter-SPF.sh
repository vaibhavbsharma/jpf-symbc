#!/bin/bash
alias runSPF-merarbiter-v2='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/merarbiter-v2/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '

shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/merarbiter-v2/ && export TARGET_CLASSPATH_WALA 

for STEPS in {6..8}; do
  for MODE in {1..1}; do
    echo "running MerArbiter-v2.$((STEPS))step.mode$((MODE))";
    MAX_STEPS=$(($STEPS)) && export MAX_STEPS 
    timeout $(($TIMEOUT_MINS))m   java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar $VERIDIR/src/MerArbiter-v2/merarbiterSPF.mode$((MODE)).jpf >& $VERIDIR/logs/merarbiter.$((STEPS))step.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
      echo "running MerArbiter-v2.$((STEPS))step.mode$((MODE)) timed out" >> $VERIDIR/logs/merarbiter.$((STEPS))step.mode$((MODE)).log
    fi
  done;
done

