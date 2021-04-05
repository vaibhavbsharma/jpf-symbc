#!/bin/bash

#takes as input mac or number of steps, for example ./runCoverageWBS.sh linux 1

if [ "$#" -ne 2 ]
then
     echo "Arguments are not equals to 2"
     exit 1
fi

if [ "$1" = "mac" ]; then
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/Users/sohahussein/git/rangerCopy/java-ranger/lib TARGET_CLASSPATH_WALA=/Users/sohahussein/git/rangerCopy/java-ranger/build/examples/ java -Djava.library.path=/Users/sohahussein/git/rangerCopy/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /Users/sohahussein/git/jpf-core/build/RunJPF.jar '
else
  alias runCoverage='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/soha/git/rangerCopy/java-ranger/lib TARGET_CLASSPATH_WALA=/home/soha/git/rangerCopy/java-ranger/build/examples/ java -Djava.library.path=/home/soha/git/rangerCopy/java-ranger/lib  -ea -Xmx2000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '
fi

#-Xmx1024m

shopt -s expand_aliases

if [ "$1" = "mac" ]; then
  COVERAGEDIR=/Users/sohahussein/git/rangerCopy/java-ranger
else
  COVERAGEDIR=/home/soha/git/rangerCopy/java-ranger
fi

MAX_STEPS=$2 && export MAX_STEPS

echo "maxsteps is $MAX_STEPS"


runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLCollect.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLCollect_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLCollect_Prune.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLCollect_Prune_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLCollect_Guide.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLCollect_Guide_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLCollect_Prune_Guide.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLCollect_Prune_Guide_steps$2.log

runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLJR_Collect.mode2.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLJRCollect.mode2_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLJR_Collect.mode3.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLJRCollect.mode3_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLJR_Collect.mode4.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLJRCollect.mode4_steps$2.log
runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLJR_Collect.mode5.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLJRCollect.mode5_steps$2.log
#runCoverage $COVERAGEDIR/src/examples/tcgbenchmarks/runconfig/nanoxml/NanoXMLJR_Collect_Guide.jpf >& $COVERAGEDIR/logs/nanoxml/NanoXMLJR_Collect_Guide_steps$2.log