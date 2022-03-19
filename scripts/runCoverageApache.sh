#!/bin/bash


#no input is provided, the number o steps are hardcoded

alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx27000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=1 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

mkdir $COVERAGEDIR/logs/log_cli



for i in {1..3} ; do
  sleep 10
  date
  runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheCollect.jpf >& $COVERAGEDIR/logs/log_cli/apacheCollect_steps$MAX_STEPS.log
  echo "SPF branch onTheGoON finished"
done

date

for i in {1..3} ; do
  sleep 10
  date
  runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/log_cli/apacheJRCollect.mode2_steps$MAX_STEPS.log
  echo "JR mode2 onTheGoON finished"
done

date

for i in {1..3} ; do
  sleep 10
  date
  runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_cli/apacheJRCollect.mode5_steps$MAX_STEPS.log
  echo "JR mode5 onTheGoON finished"
done

date

#
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheCollect.jpf >& $COVERAGEDIR/logs/log_cli/apacheCollect_steps$MAX_STEPS.log \
#&& echo "SPF branch onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/log_cli/apacheJRCollect.mode2_steps$MAX_STEPS.log \
#&& echo "JR mode2 onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/log_cli/apacheJRCollect.mode4_steps$MAX_STEPS.log \
#&& echo "JR mode4 onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_cli/apacheJRCollect.mode5_steps$MAX_STEPS.log \
#&& echo "JR mode5 onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/log_cli/apacheJRCollect.mode3_steps$MAX_STEPS.log \
#&& echo "JR mode3 onTheGoON finished" \

#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/apachecli/apacheJR_Collect.mode3NoBatch.jpf >& $COVERAGEDIR/logs/log_cli/apacheJR_Collect.mode3NoBatch_steps$MAX_STEPS.log \
#&& echo "JR mode3 NoBatch finished"