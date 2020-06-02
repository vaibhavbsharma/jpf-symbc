#!/bin/bash
JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-nanoxml='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s direxpand
shopt -s expand_aliases

TIMEOUT_MINS=720 && export TIMEOUT_MINS
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib && export LD_LIBRARY_PATH
TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ && export TARGET_CLASSPATH_WALA

for NSYM in {7..7}; do
  for MODE in {5..2}; do
    echo "running DumpXML.$(($NSYM))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/nanoxml/DumpXML.$(($NSYM))sym.mode$(($MODE)).jpf >& $JRDIR/logs/DumpXML.$((NSYM))sym.mode$((MODE)).log 
    if [ $? -eq 124 ]; then 
          echo "running DumpXML.$(($NSYM))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/DumpXML.$((NSYM))sym.mode$((MODE)).log
    fi
  done;
done


#for NSYM in {9..9}; do
#  for MODE in {5..5}; do
#    echo "running DumpXML.$(($NSYM))sym.mode$(($MODE))" && timeout $(($TIMEOUT_MINS))m  java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar   $JRDIR/src/examples/veritesting/nanoxml/DumpXML.$(($NSYM))sym.mode$(($MODE)).jpf >& $JRDIR/logs/DumpXML.$((NSYM))sym.mode$((MODE)).log
#    if [ $? -eq 124 ]; then
#          echo "running DumpXML.$(($NSYM))sym.mode$(($MODE)) timed out" >> $JRDIR/logs/DumpXML.$((NSYM))sym.mode$((MODE)).log
#    fi
#  done;
#done

