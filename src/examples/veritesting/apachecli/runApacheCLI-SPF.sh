#!/bin/bash
JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-cli='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '

shopt -s expand_aliases
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA

# MODE=1
# NSTEP=7
# echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/apachecli/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log &
# 
# echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/apachecli/ApacheCLI.$(($NSTEP))sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log 
# if [ $? -eq 124 ]; then 
#     echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
# fi

# NSTEP=7, MODE=1 already times out in 24 hours, but I will only know this with certainty at 11:04 PM on May 1, 2019
# NSTEP=8
# echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/apachecli/ApacheCLI.$(($NSTEP))sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log 
# if [ $? -eq 124 ]; then 
#     echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
# fi

for NSTEP in {5..5}; do
  for MODE in {1..1}; do
    if [ $NSTEP -lt 8 ]
      then
      echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log 
      if [ $? -eq 124 ]; then 
            echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)) timed out" >> $JRDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
      fi
    fi
  done;

#  for MODE in {1..1}; do
#    echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))sym.mode$(($MODE)).jpf >& $JRDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
#    if [ $? -eq 124 ]; then
#        echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
#    fi
#  done;
  
done
