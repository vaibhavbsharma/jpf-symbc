#!/bin/bash
JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-cli='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '

shopt -s expand_aliases
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA


for NSTEP in {5..5}; do
  for MODE in {5..2}; do
    echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
          echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)) timed out" >> $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
    fi
  done;
done

#for NSTEP in {8..8}; do
#  for MODE in {5..5}; do
#    echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
#    if [ $? -eq 124 ]; then
#          echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)) timed out" >> $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
#    fi
#  done;
#done
