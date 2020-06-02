#!/bin/bash

alias runSPF-schedule='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '
shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger
runSPF-schedule $VERIDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode1.jpf >& $VERIDIR/logs/schedule.mode1.log
runSPF-schedule $VERIDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode2.jpf >& $VERIDIR/logs/schedule.mode2.log
runSPF-schedule $VERIDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode3.jpf >& $VERIDIR/logs/schedule.mode3.log
runSPF-schedule $VERIDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode4.jpf >& $VERIDIR/logs/schedule.mode4.log
runSPF-schedule $VERIDIR/src/examples/veritesting/schedule2_3/schedule2_3.mode5.jpf >& $VERIDIR/logs/schedule.mode5.log
