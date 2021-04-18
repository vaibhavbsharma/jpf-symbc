#!/bin/bash

#no input is provided, the number o steps are hardcoded


# Leverage the path of scripts to locate project path automatically
if [ $0 = "-bash" -o $0 = "-zsh" -o $0 = "zsh"  ]; then
    FILEDIR=`pwd`
else
    FILEDIR="$( cd "$(dirname $(readlink -f "$0"))" ; pwd -P )"
fi

source $FILEDIR/functools.sh


alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JR_DIR/lib TARGET_CLASSPATH_WALA=$JR_DIR/build/examples/ java -Djava.library.path=$JR_DIR/lib  -ea -Xmx5000m -Dfile.encoding=UTF-8 -jar $JPF_DIR/build/RunJPF.jar '


shopt -s expand_aliases

export MAX_STEPS=5

echo "maxsteps is $MAX_STEPS"

mkdir -p $JR_DIR/logs/log_wbs


runCoverage $JR_DIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSCollect.jpf >& $JR_DIR/logs/log_wbs/WBSCollect_steps$MAX_STEPS.log
#runCoverage $JR_DIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect_Prune.jpf >& $JR_DIR/logs/log_wbs/WBSCollect_Prune_steps$MAX_STEPS.log
#runCoverage $JR_DIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect_Guide.jpf >& $JR_DIR/logs/log_wbs/WBSCollect_Guide_steps$MAX_STEPS.log
#runCoverage $JR_DIR/src/examples/veritesting/test_case_gen/wbs/WBSCollect_Prune_Guide.jpf >& $JR_DIR/logs/log_wbs/WBSCollect_Prune_Guide_steps$MAX_STEPS.log

runCoverage $JR_DIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode2.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect.mode2_steps$MAX_STEPS.log
runCoverage $JR_DIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode3.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect.mode3_steps$MAX_STEPS.log
runCoverage $JR_DIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode4.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect.mode4_steps$MAX_STEPS.log
runCoverage $JR_DIR/src/examples/tcgbenchmarks/runconfig/wbs/WBSJR_Collect.mode5.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect.mode5_steps$MAX_STEPS.log
#runCoverage $JR_DIR/src/examples/veritesting/test_case_gen/wbs/WBS_JR_Collect_Prune.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect_Prune_steps$MAX_STEPS.log
#runCoverage $JR_DIR/src/examples/veritesting/test_case_gen/wbs/WBS_JR_Collect_Guide.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect_Guide_steps$MAX_STEPS.log
#runCoverage $JR_DIR/src/examples/veritesting/test_case_gen/wbs/WBS_JR_Collect_Prune_Guide.jpf >& $JR_DIR/logs/log_wbs/WBS_JR_Collect_Prune_Guide_steps$MAX_STEPS.log
