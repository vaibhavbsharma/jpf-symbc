#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-wbs='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA

echo "Running 5 step - mode 1"
MAX_STEPS=5 && export MAX_STEPS
timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar  $JRDIR/src/examples/veritesting/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/wbs.$(($MAX_STEPS))step.mode1.log

#echo "Running 7 step - mode 1"
#MAX_STEPS=7 && export MAX_STEPS
#timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar  $JRDIR/src/examples/veritesting/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
