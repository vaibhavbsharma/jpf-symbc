#!/bin/bash

#takes nothing as an input --
# (((((runs from "scripts" directory))))

rm -rf ../logs/printtokens2_3
mkdir ../logs/printtokens2_3

alias runEquivelanceCheck='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/media/soha/DATA/git/jrTCG/lib TARGET_CLASSPATH_WALA=/media/soha/DATA/git/jrTCG/build/examples/ java -Djava.library.path=/media/soha/DATA/git/jrTCG/lib  -ea -Xmx5000m -Dfile.encoding=UTF-8 -jar /home/soha/git/jpf-core/build/RunJPF.jar '


shopt -s expand_aliases


COVERAGEDIR=/media/soha/DATA/git/jrTCG



runEquivelanceCheck $COVERAGEDIR/src/examples/veritesting/printtokens2_3/PrintTokensEqCheck.jpf>& $COVERAGEDIR/logs/printtokens2_3/PrintTokensEqCheck.log

runEquivelanceCheck $COVERAGEDIR/src/examples/veritesting/printtokens2_3/PrintTokensEqCheckTCG.jpf>& $COVERAGEDIR/logs/printtokens2_3/PrintTokensEqCheckTCG.log

