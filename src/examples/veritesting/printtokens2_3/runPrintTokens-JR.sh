#!/bin/bash

JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-pt='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '

shopt -s expand_aliases
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA

for NSYM in {5..5}; do # 6 symbolic inputs timeout in 24 hours
  for MODE in {2..5}; do
    echo "running printtokens.$(($NSYM))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/printtokens2_3/printtokens.$(($NSYM))sym.mode$(($MODE)).jpf >& $JRDIR/logs/printtokens.$((NSYM))sym.mode$((MODE)).log
    if [ $? -eq 124 ]; then 
          echo "running printtokens.$(($NSYM))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/printtokens.$((NSYM))sym.mode$((MODE)).log
    fi
  done;
done

#for NSYM in {6..6}; do # 6 symbolic inputs timeout in 24 hours
#  for MODE in {5..5}; do
#    echo "running printtokens.$(($NSYM))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/printtokens2_3/printtokens.$(($NSYM))sym.mode$(($MODE)).jpf >& $JRDIR/logs/printtokens.$((NSYM))sym.mode$((MODE)).log
#    if [ $? -eq 124 ]; then
#          echo "running printtokens.$(($NSYM))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/printtokens.$((NSYM))sym.mode$((MODE)).log
#    fi
#  done;
#done
