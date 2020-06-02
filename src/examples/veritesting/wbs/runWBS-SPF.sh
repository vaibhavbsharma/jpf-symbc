#!/bin/bash

VERIDIR=$(pwd)/../../../../
JPF_CORE_DIR=$VERIDIR/../jpf-core
alias runSPF-wbs='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$VERIDIR/lib TARGET_CLASSPATH_WALA=$VERIDIR/build/examples/ java -Djava.library.path=$VERIDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$VERIDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$VERIDIR/build/examples/ && export TARGET_CLASSPATH_WALA

echo "Running 5 step - mode 1"
MAX_STEPS=5 && export MAX_STEPS
timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$VERIDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar  $VERIDIR/src/examples/veritesting/wbs/RunWBS.mode1.jpf >& $VERIDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log

#echo "Running 7 step - mode 1"
#MAX_STEPS=7 && export MAX_STEPS
#timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$VERIDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar  $VERIDIR/src/examples/veritesting/wbs/RunWBS.mode1.jpf >& $VERIDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
