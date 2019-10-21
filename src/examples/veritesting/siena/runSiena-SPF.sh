#!/bin/bash
alias runSPF-siena='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '

shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger
TIMEOUT_MINS=1440 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ && export TARGET_CLASSPATH_WALA 

for SYM in {6..7}; do 
  for MODE in {1..1}; do
    echo "running siena.$((SYM)).mode$((MODE))";
    timeout $(($TIMEOUT_MINS))m   java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar $VERIDIR/src/examples/veritesting/siena/SENPDriver.$((SYM)).mode$((MODE)).jpf >& $VERIDIR/logs/siena.$((SYM)).mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
      echo "running siena.$((SYM)).mode$((MODE)) timed out" >> $VERIDIR/logs/siena.$((SYM)).mode$((MODE)).log
    fi
  done;
done;
