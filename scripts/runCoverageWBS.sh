#!/bin/bash

#no input is provided, the number o steps are hardcoded


alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ timeout 180m java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx5000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=5 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"


runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSCollect.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect_Prune.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Prune_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Guide_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBSCollect_Prune_Guide_steps$MAX_STEPS.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect.mode2_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect.mode3_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect.mode4_steps$MAX_STEPS.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect.mode5_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBS_JR_Collect_Prune.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect_Prune_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBS_JR_Collect_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect_Guide_steps$MAX_STEPS.log
#runCoverage $COVERAGEDIR/src/examples/veritesting/test_case_gen/wbs/WBS_JR_Collect_Prune_Guide.jpf >& $COVERAGEDIR/logs/wbs/WBS_JR_Collect_Prune_Guide_steps$MAX_STEPS.log
