#!/bin/bash

alias runSPF-tcas='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '
shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger

# run tcas with 1 step for 5 modes
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.log
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode2.jpf >& $VERIDIR/logs/tcas.mode2.log
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode3.jpf >& $VERIDIR/logs/tcas.mode3.log
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode4.jpf >& $VERIDIR/logs/tcas.mode4.log
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode4.jpf >& $VERIDIR/logs/tcas.mode4.log
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.log


TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/tcas/ && export TARGET_CLASSPATH_WALA


# run tcas with up to 10 steps in mode 5
#echo "Running 1 step - mode 5"
#MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 2 step - mode 5"
#MAX_STEPS=2 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 3 step - mode 5"
#MAX_STEPS=3 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 4 step - mode 5"
#MAX_STEPS=4 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 5 step - mode 5"
#MAX_STEPS=5 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 6 step - mode 5"
#MAX_STEPS=6 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 7 step - mode 5"
#MAX_STEPS=7 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 8 step - mode 5"
#MAX_STEPS=8 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
#echo "Running 9 step - mode 5"
#MAX_STEPS=9 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 5"
MAX_STEPS=10 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode5.jpf >& $VERIDIR/logs/tcas.mode5.$(($MAX_STEPS))step.log


echo "Running 10 step - mode 2"
MAX_STEPS=10 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode2.jpf >& $VERIDIR/logs/tcas.mode2.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 3"
MAX_STEPS=10 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode3.jpf >& $VERIDIR/logs/tcas.mode3.$(($MAX_STEPS))step.log
echo "Running 10 step - mode 4"
MAX_STEPS=10 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/examples/veritesting/tcas/tcas.mode4.jpf >& $VERIDIR/logs/tcas.mode4.$(($MAX_STEPS))step.log
# run tcas with up to 10 steps in mode 1
# echo "Running 1 step - mode 1"
# MAX_STEPS=1 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 2 step - mode 1"
# MAX_STEPS=2 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 3 step - mode 1"
# MAX_STEPS=3 && export MAX_STEPS 
#    timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 4 step - mode 1"
# MAX_STEPS=4 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 5 step - mode 1"
# MAX_STEPS=5 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 6 step - mode 1"
# MAX_STEPS=6 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 7 step - mode 1"
# MAX_STEPS=7 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 8 step - mode 1"
# MAX_STEPS=8 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 9 step - mode 1"
# MAX_STEPS=9 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log
# echo "Running 10 step - mode 1"
# MAX_STEPS=10 && export MAX_STEPS && runSPF-tcas $VERIDIR/src/tcas/tcas.mode1.jpf >& $VERIDIR/logs/tcas.mode1.$(($MAX_STEPS))step.log

