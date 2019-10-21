#!/bin/bash
alias runSPF-cli='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '

shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger
TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ && export TARGET_CLASSPATH_WALA


for NSTEP in {4..7}; do
  for MODE in {5..2}; do
    echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
          echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
    fi
  done;
done

for NSTEP in {8..8}; do
  for MODE in {5..5}; do
    echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar   $VERIDIR/src/examples/veritesting/apachecli/config_files/ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)).jpf >& $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
          echo "running ApacheCLI.$(($NSTEP))_1sym.mode$(($MODE)) timed out" >> $VERIDIR/logs/ApacheCLI.$((NSTEP))_1sym.mode$((MODE)).log
    fi
  done;
done
