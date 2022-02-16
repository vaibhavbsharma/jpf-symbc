#!/bin/bash


#no input is provided, the number o steps are hardcoded

alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx12000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=1 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

mkdir $COVERAGEDIR/logs/paper_example

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/testcaseperf/TCG_PaperExampleSPF.jpf >& $COVERAGEDIR/logs/paper_example/TCG_PaperExampleSPF.mode3.log \
&& echo "SPF onTheGoON finished"
