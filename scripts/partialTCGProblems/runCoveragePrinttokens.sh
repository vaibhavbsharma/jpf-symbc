#!/bin/bash

#no input is provided, the number o steps are hardcoded


#alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xms28000m -Xmx28000m -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+UseCompressedOops -XX:PermSize=1024m -XX:MaxPermSize=1024m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '

alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx12000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG

MAX_STEPS=1 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"

mkdir $COVERAGEDIR/logs/log_printtokens2
mkdir $COVERAGEDIR/logs/log_printtokens2/partialproblem


#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode2_steps$MAX_STEPS.log \
#&& echo "JR mode2 onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode2PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode2PlainJR_steps$MAX_STEPS.log \
#&& echo "JR mode2 PlainJR finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode3_steps$MAX_STEPS.log \
#&& echo "JR mode3 onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode3PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode3PlainJR_steps$MAX_STEPS.log \
#&& echo "JR mode3 PlainJR finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode4_steps$MAX_STEPS.log \
#&& echo "JR mode4 onTheGoON finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode4PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode4PlainJR_steps$MAX_STEPS.log \
#&& echo "JR mode4 PlainJR finished" \

for i in {1..3} ; do
  sleep 10
  date
  runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensCollect.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensCollect_steps$MAX_STEPS.log
  echo "SPF finished"
done

date

for i in {1..3} ; do
  sleep 10
  date
  runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5_steps$MAX_STEPS.log
  echo "JR mode5 onTheGoON finished"
done

date

for i in {1..3} ; do
  sleep 10
  date
  runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5PlainJR_steps$MAX_STEPS.log
  echo "JR mode5 PlainJR finished"
done

date

#
#sleep 10  \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensCollect.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensCollect_steps$MAX_STEPS.log \
#; date \
#; echo "SPF finished" \
#; sleep 10  \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensCollect.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensCollect_steps$MAX_STEPS.log \
#; date \
#; echo "SPF finished" \
#; sleep 10  \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensCollect.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensCollect_steps$MAX_STEPS.log \
#; date \
#; echo "SPF finished" \
#; sleep 10 \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5_steps$MAX_STEPS.log \
#; date \
#; echo "JR mode5 onTheGoON finished" \
#; sleep 10 \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5_steps$MAX_STEPS.log \
#; date \
#; echo "JR mode5 onTheGoON finished" \
#; sleep 10 \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5_steps$MAX_STEPS.log \
#; date \
#; echo "JR mode5 onTheGoON finished" \
#; date \
#; sleep 10 \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5PlainJR_steps$MAX_STEPS.log \
#; echo "JR mode5 PlainJR finished" \
#; date  \
#; sleep 10 \
#; date \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5PlainJR_steps$MAX_STEPS.log \
#; echo "JR mode5 PlainJR finished" \
#; date\
#; sleep 10 \
#; runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5PlainJR.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5PlainJR_steps$MAX_STEPS.log \
#; echo "JR mode5 PlainJR finished" \
#; date


#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode2_onTheGoOFF.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode2_onTheGoOFF_steps$MAX_STEPS.log \
#&& echo "JR mode2 onTheGoOFF finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode4_onTheGoOFF.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode4_onTheGoOFF_steps$MAX_STEPS.log \
#&& echo "JR mode4 onTheGoOFF finished" \
#&& runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/printtokens/partialproblem/printtokensJR_Collect.mode5_onTheGoOFF.jpf >& $COVERAGEDIR/logs/log_printtokens2/partialproblem/printtokensJRCollect.mode5_onTheGoOFF_steps$MAX_STEPS.log \
#&& echo "JR mode5 onTheGoOFF finished" \
