#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-schedule='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
runSPF-schedule $JRDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode1.jpf >& $JRDIR/logs/schedule.mode1.log
runSPF-schedule $JRDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode2.jpf >& $JRDIR/logs/schedule.mode2.log
runSPF-schedule $JRDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode3.jpf >& $JRDIR/logs/schedule.mode3.log
runSPF-schedule $JRDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode4.jpf >& $JRDIR/logs/schedule.mode4.log
runSPF-schedule $JRDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode5.jpf >& $JRDIR/logs/schedule.mode5.log
