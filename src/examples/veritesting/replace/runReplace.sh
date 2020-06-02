#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx8192m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s expand_aliases
JRDIR=$JRDIR

#echo "running replace5.mode1"
#runSPF $JRDIR/src/examples/veritesting/replace/replace.mode1.jpf >& $JRDIR/logs/replace.mode1.log
#echo "running replace5.mode2"
#runSPF $JRDIR/src/examples/veritesting/replace/replace.mode2.jpf >& $JRDIR/logs/replace.mode2.log
#echo "running replace5.mode3"
#runSPF $JRDIR/src/examples/veritesting/replace/replace.mode3.jpf >& $JRDIR/logs/replace.mode3.log
#echo "running replace5.mode4"
#runSPF $JRDIR/src/examples/veritesting/replace/replace.mode4.jpf >& $JRDIR/logs/replace.mode4.log
#echo "running replace5.mode5"
#runSPF $JRDIR/src/examples/veritesting/replace/replace.mode5.jpf >& $JRDIR/logs/replace.mode5.log

echo "running replace11.mode1"
runSPF $JRDIR/src/examples/veritesting/replace/replace11.mode1.jpf >& $JRDIR/logs/replace11.mode1.log
echo "running replace11.mode2"
runSPF $JRDIR/src/examples/veritesting/replace/replace11.mode2.jpf >& $JRDIR/logs/replace11.mode2.log
echo "running replace11.mode3"
runSPF $JRDIR/src/examples/veritesting/replace/replace11.mode3.jpf >& $JRDIR/logs/replace11.mode3.log
echo "running replace11.mode4"
runSPF $JRDIR/src/examples/veritesting/replace/replace11.mode4.jpf >& $JRDIR/logs/replace11.mode4.log
echo "running replace11.mode5"
runSPF $JRDIR/src/examples/veritesting/replace/replace11.mode5.jpf >& $JRDIR/logs/replace11.mode5.log
