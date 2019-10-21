#!/bin/bash
alias runSPF-cli='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '

shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ && export TARGET_CLASSPATH_WALA

# MODE=1
# NSTEP=7
# echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/apachecli/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log &
# 
# echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/apachecli/ApacheCLI.$(($NSTEP))sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log 
# if [ $? -eq 124 ]; then 
#     echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
# fi

# NSTEP=7, MODE=1 already times out in 24 hours, but I will only know this with certainty at 11:04 PM on May 1, 2019
# NSTEP=8
# echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/apachecli/ApacheCLI.$(($NSTEP))sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log 
# if [ $? -eq 124 ]; then 
#     echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
# fi

for NSTEP in {4..6}; do
  for MODE in {1..1}; do
    if [ $NSTEP -lt 8 ]
      then
      echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log 
      if [ $? -eq 124 ]; then 
            echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
      fi
    fi
  done;

  for MODE in {1..1}; do
    echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
        echo "running ApacheCLI.$(($NSTEP))sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/ApacheCLI.$((NSTEP))sym.mode$((MODE)).log
    fi
  done;
  
done
