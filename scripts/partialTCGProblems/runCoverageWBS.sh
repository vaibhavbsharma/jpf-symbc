#!/bin/bash

#no input is provided, the number o steps are hardcoded


alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx12000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

##maximum steps that we spf can do within an hour is 3
MAX_STEPS=3 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

mkdir $COVERAGEDIR/logs/log_wbs
mkdir $COVERAGEDIR/logs/log_wbs/partialproblem

#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSCollect_Path.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSCollect_Path_steps$MAX_STEPS.log \
#&& echo "SPF path finished" \


runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSCollect.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSCollect_steps$MAX_STEPS.log \
&& echo "SPF branch onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSJRCollect.mode2_steps$MAX_STEPS.log \
&& echo "JR mode2 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSJRCollect.mode4_steps$MAX_STEPS.log \
&& echo "JR mode4 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSJRCollect.mode5_steps$MAX_STEPS.log \
&& echo "JR mode5 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSJRCollect.mode3_steps$MAX_STEPS.log \
&& echo "JR mode3 onTheGoON finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSJR_Collect.mode3_onTheGoOFF.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSJRCollect.mode3_onTheGoOFF_steps$MAX_STEPS.log \
&& echo "JR mode3 onTheGoOFF finished" \
&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/partialproblem/WBSJR_Collect.mode3PlainJR.jpf >& $COVERAGEDIR/logs/log_wbs/partialproblem/WBSJRCollect.mode3PlainJR_steps$MAX_STEPS.log \
&& echo "JR mode3 PlainJR finished"

