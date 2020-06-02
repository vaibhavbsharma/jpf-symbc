#!/bin/bash

alias runSPF='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx8192m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '
shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger

#echo "running replace5.mode1"
#runSPF $VERIDIR/src/examples/veritesting/replace/replace.mode1.jpf >& $VERIDIR/logs/replace.mode1.log
#echo "running replace5.mode2"
#runSPF $VERIDIR/src/examples/veritesting/replace/replace.mode2.jpf >& $VERIDIR/logs/replace.mode2.log
#echo "running replace5.mode3"
#runSPF $VERIDIR/src/examples/veritesting/replace/replace.mode3.jpf >& $VERIDIR/logs/replace.mode3.log
#echo "running replace5.mode4"
#runSPF $VERIDIR/src/examples/veritesting/replace/replace.mode4.jpf >& $VERIDIR/logs/replace.mode4.log
#echo "running replace5.mode5"
#runSPF $VERIDIR/src/examples/veritesting/replace/replace.mode5.jpf >& $VERIDIR/logs/replace.mode5.log

echo "running replace11.mode1"
runSPF $VERIDIR/src/examples/veritesting/replace/replace11.mode1.jpf >& $VERIDIR/logs/replace11.mode1.log
echo "running replace11.mode2"
runSPF $VERIDIR/src/examples/veritesting/replace/replace11.mode2.jpf >& $VERIDIR/logs/replace11.mode2.log
echo "running replace11.mode3"
runSPF $VERIDIR/src/examples/veritesting/replace/replace11.mode3.jpf >& $VERIDIR/logs/replace11.mode3.log
echo "running replace11.mode4"
runSPF $VERIDIR/src/examples/veritesting/replace/replace11.mode4.jpf >& $VERIDIR/logs/replace11.mode4.log
echo "running replace11.mode5"
runSPF $VERIDIR/src/examples/veritesting/replace/replace11.mode5.jpf >& $VERIDIR/logs/replace11.mode5.log
