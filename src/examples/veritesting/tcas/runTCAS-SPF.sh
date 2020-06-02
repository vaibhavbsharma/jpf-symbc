#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-tcas='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
VERIDIR=$JRDIR

TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA


echo "Running 2 step - mode 1"
MAX_STEPS=2 && export MAX_STEPS
timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar $VERIDIR/src/examples/veritesting/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# tcas mode1 3 steps already times out in 24 hours
# echo "Running 4 step - mode 1"
# MAX_STEPS=4 && export MAX_STEPS 
# timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar $VERIDIR/src/examples/veritesting/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
