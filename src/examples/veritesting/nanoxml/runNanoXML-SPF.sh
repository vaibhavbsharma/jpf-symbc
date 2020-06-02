#!/bin/bash
alias runSPF-nanoxml='LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/export/scratch2/vaibhav/java-ranger/lib TARGET_CLASSPATH_WALA=/export/scratch2/vaibhav/java-ranger/build/examples/ java -Djava.library.path=/export/scratch2/vaibhav/java-ranger/lib -Xmx12288m -ea -Dfile.encoding=UTF-8 -jar /export/scratch/vaibhav/jpf-core-veritesting/build/RunJPF.jar '
shopt -s direxpand
shopt -s expand_aliases
VERIDIR=/export/scratch2/vaibhav/java-ranger
 for i in {7..7}; do
   for j in {5..2}; do
     echo "running DumpXML.$((i))sym.mode$((j))";
     runSPF-nanoxml  $VERIDIR/src/examples/veritesting/nanoxml/DumpXML.$((i))sym.mode$((j)).jpf >& $VERIDIR/logs/DumpXML.$((i))sym.mode$((j)).log
   done;
 done

# for i in {9..9}; do
#   for j in {5..2}; do
#     echo "running DumpXML.$((i))sym.mode$((j))";
#     runSPF-nanoxml  $VERIDIR/src/examples/veritesting/nanoxml/DumpXML.$((i))sym.mode$((j)).jpf >& $VERIDIR/logs/DumpXML.$((i))sym.mode$((j)).log
#   done;
# done
