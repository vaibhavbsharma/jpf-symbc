#!/bin/bash
JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-siena='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '

shopt -s expand_aliases
VERIDIR=$JRDIR
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA 

for SYM in {6..6}; do
  for MODE in {5..5}; do
    echo "running siena.$((SYM)).mode$((MODE))";
    timeout $(($TIMEOUT_MINS))m   java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar $VERIDIR/src/examples/veritesting/siena/SENPDriver.$((SYM)).mode$((MODE)).jpf >& $VERIDIR/logs/siena.$((SYM)).mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
      echo "running siena.$((SYM)).mode$((MODE)) timed out" >> $VERIDIR/logs/siena.$((SYM)).mode$((MODE)).log
    fi
  done;
done;
