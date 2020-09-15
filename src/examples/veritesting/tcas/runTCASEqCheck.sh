#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-tcas='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
VERIDIR=$JRDIR
# TCASEqCheck should not be using a specific number of steps, it should always perform equivalence-checking over TCAS using only one step
unset MAX_STEPS
runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/TCASEqCheck.jpf >& $VERIDIR/logs/TCASEqCheck.log
