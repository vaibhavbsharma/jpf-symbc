#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-wbs='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
VERIDIR=$JRDIR
runSPF-wbs $VERIDIR/src/examples/veritesting/wbs/WBSEqCheck.jpf >& $VERIDIR/logs/WBSEqCheck.log
