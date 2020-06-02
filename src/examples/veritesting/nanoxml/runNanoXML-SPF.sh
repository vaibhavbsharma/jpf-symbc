#!/bin/bash
JRDIR=$(pwd)/../../../../
JPF_CORE_DIR=$JRDIR/../jpf-core
alias runSPF-nanoxml='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JRDIR/lib TARGET_CLASSPATH_WALA=$JRDIR/build/examples/ java -Djava.library.path=$JRDIR/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar $JPF_CORE_DIR/build/RunJPF.jar '
shopt -s direxpand
shopt -s expand_aliases
 for i in {7..7}; do
   for j in {1..1}; do
     echo "running NanoXML.$((i))sym.mode$((j))";
     runSPF-nanoxml  $JRDIR/src/examples/veritesting/nanoxml/DumpXML.$((i))sym.mode$((j)).jpf >& $JRDIR/logs/NanoXML.$((i))sym.mode$((j)).log
   done;
 done

# for i in {9..9}; do
#   for j in {5..2}; do
#     echo "running NanoXML.$((i))sym.mode$((j))";
#     runSPF-nanoxml  $JRDIR/src/examples/veritesting/nanoxml/DumpXML.$((i))sym.mode$((j)).jpf >& $JRDIR/logs/NanoXML.$((i))sym.mode$((j)).log
#   done;
# done
