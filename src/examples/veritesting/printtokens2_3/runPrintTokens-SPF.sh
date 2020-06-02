#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-pt='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '

shopt -s expand_aliases
VERIDIR=$JRDIR
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA

for NSYM in {5..5}; do # 6 symbolic inputs timeout in 24 hours
  for MODE in {1..1}; do
    echo "running printtokens.$(($NSYM))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/printtokens2_3/printtokens.$(($NSYM))sym.mode$(($MODE)).jpf >& $VERIDIR/logs/printtokens.$((NSYM))sym.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
          echo "running printtokens.$(($NSYM))sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/printtokens.$((NSYM))sym.mode$((MODE)).log
    fi
  done;
done
