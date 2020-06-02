#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-wbs='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
echo $JRDIR
# runSPF-wbs $JRDIR/src/examples/veritesting/wbs/WBS.mode1.jpf >& $JRDIR/logs/WBS.mode1.log
# runSPF-wbs $JRDIR/src/examples/veritesting/wbs/WBS.mode2.jpf >& $JRDIR/logs/WBS.mode2.log
# runSPF-wbs $JRDIR/src/examples/veritesting/wbs/WBS.mode3.jpf >& $JRDIR/logs/WBS.mode3.log
# runSPF-wbs $JRDIR/src/examples/veritesting/wbs/WBS.mode4.jpf >& $JRDIR/logs/WBS.mode4.log
# runSPF-wbs $JRDIR/src/examples/veritesting/wbs/WBS.mode5.jpf >& $JRDIR/logs/WBS.mode5.log

TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/wbs/ && export TARGET_CLASSPATH_WALA

#echo "Running 1 step - mode 5"
#MAX_STEPS=1 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 2 step - mode 5"
#MAX_STEPS=2 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 3 step - mode 5"
#MAX_STEPS=3 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 4 step - mode 5"
#MAX_STEPS=4 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 5 step - mode 5"
#MAX_STEPS=5 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 6 step - mode 5"
#MAX_STEPS=6 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 7 step - mode 5"
#MAX_STEPS=7 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 8 step - mode 5"
#MAX_STEPS=8 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
#echo "Running 9 step - mode 5"
#MAX_STEPS=9 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 2"
MAX_STEPS=10 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode2.jpf >& $JRDIR/logs/RunWBS.mode2.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 3"
MAX_STEPS=10 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode3.jpf >& $JRDIR/logs/RunWBS.mode3.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 4"
MAX_STEPS=10 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode4.jpf >& $JRDIR/logs/RunWBS.mode4.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 5"
MAX_STEPS=10 && export MAX_STEPS && runSPF-wbs $JRDIR/src/examples/veritesting/wbs/RunWBS.mode5.jpf >& $JRDIR/logs/RunWBS.mode5.$(($MAX_STEPS))step.log



# echo "Running 1 step - mode 1"
# MAX_STEPS=1 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 2 step - mode 1"
# MAX_STEPS=2 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 3 step - mode 1"
# MAX_STEPS=3 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 4 step - mode 1"
# MAX_STEPS=4 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 5 step - mode 1"
# MAX_STEPS=5 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 6 step - mode 1"
# MAX_STEPS=6 && export MAX_STEPS
# timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar  $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 7 step - mode 1"
# MAX_STEPS=7 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 8 step - mode 1"
# MAX_STEPS=8 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 9 step - mode 1"
# MAX_STEPS=9 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
# echo "Running 10 step - mode 1"
# MAX_STEPS=10 && export MAX_STEPS && runSPF-wbs $JRDIR/src/wbs/RunWBS.mode1.jpf >& $JRDIR/logs/RunWBS.mode1.$(($MAX_STEPS))step.log
