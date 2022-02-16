#!/bin/bash


#no input is provided, the number o steps are hardcoded


alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx12000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=1 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

mkdir $COVERAGEDIR/logs/log_tcas
mkdir $COVERAGEDIR/logs/log_tcas/partialproblem

#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASCollect_Path.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASCollect_Path_steps$MAX_STEPS.log \
#&& echo "SPF path finished" \


runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASCollect.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASCollect_steps$MAX_STEPS.log \
&& echo "SPF branch onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASJRCollect.mode2_steps$MAX_STEPS.log \
&& echo "JR mode2 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASJRCollect.mode4_steps$MAX_STEPS.log \
&& echo "JR mode4 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASJRCollect.mode5_steps$MAX_STEPS.log \
&& echo "JR mode5 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASJR_Collect.mode3_onTheGoOFF.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASJRCollect.mode3_onTheGoOFF_steps$MAX_STEPS.log \
&& echo "JR mode3 onTheGoOFF finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASJRCollect.mode3_steps$MAX_STEPS.log \
&& echo "JR mode3 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/tcas/partialproblem/TCASJR_Collect.mode3PlainJR.jpf >& $COVERAGEDIR/logs/log_tcas/partialproblem/TCASJRCollect.mode3PlainJR_steps$MAX_STEPS.log \
&& echo "JR mode3 PlainJR finished"

