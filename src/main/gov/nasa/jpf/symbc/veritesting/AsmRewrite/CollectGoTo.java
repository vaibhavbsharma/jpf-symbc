package gov.nasa.jpf.symbc.veritesting.AsmRewrite;

import javafx.util.Pair;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CollectGoTo extends ClassVisitor {
    public static HashMap<String, ArrayList<InstructionCollector>> methodInstHashMap = new HashMap<>();

    VisitorPass visitorPass;

    //second pass related vairables
    HashMap<Integer, ModifiedGoTo> methodGoToInsHashMap = new HashMap<>();

    ArrayList<Label> methodNewLabelArr = new ArrayList<>();

    HashMap<String, Pair<ArrayList<Label>, HashMap<Integer, ModifiedGoTo>>> newGoToAndLabelsPairMap;


    public CollectGoTo(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
        this.visitorPass = VisitorPass.READINGPASS;
    }


    public CollectGoTo(ClassVisitor cv, HashMap<String, ArrayList<Pair<Integer, Label>>> collectedJumpInstructions,
                       HashMap<String, ArrayList<Label>> backEdgeTargetLabels) {
        super(Opcodes.ASM5, cv);
        this.visitorPass = VisitorPass.WRITINGPASS;
        newGoToAndLabelsPairMap = ModifiedGoTo.createAll(collectedJumpInstructions, backEdgeTargetLabels);
    }


    public static byte[] execute(byte[] b) {
        ClassReader classReader = new ClassReader(b);
        final ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        CollectGoTo firstPassClassVisitor = new CollectGoTo(cw);

        classReader.accept(firstPassClassVisitor, ClassReader.EXPAND_FRAMES);


        HashMap<String, ArrayList<Pair<Integer, Label>>> collectedJumpInstructions = new HashMap<>();
        HashMap<String, ArrayList<Label>> backEdgeTargetLabels = new HashMap<>();

        //System.out.println("visited jump instructions in method: " + name + signature);
        Iterator<Map.Entry<String, ArrayList<InstructionCollector>>> methodInstItr = methodInstHashMap.entrySet().iterator();
        while (methodInstItr.hasNext()) {
            Map.Entry<String, ArrayList<InstructionCollector>> entry = methodInstItr.next();
            String methodName = entry.getKey();
            for (InstructionCollector instructionCollector : entry.getValue()) {
                collectedJumpInstructions.put(methodName, instructionCollector.collectedJumpInstructions);
                backEdgeTargetLabels.put(methodName, instructionCollector.backEdgeTargetLabel);
            }
        }
        if (backEdgeTargetLabels.size() > 0) { // discovered backedge in the method, try to rewrite.

            ClassReader newClassReader = new ClassReader(b);
            final ClassWriter newcw = new ClassWriter(newClassReader, ClassWriter.COMPUTE_FRAMES | ClassWriter
                    .COMPUTE_MAXS);
            CollectGoTo secPassClassVisitor = new CollectGoTo(newcw, collectedJumpInstructions, backEdgeTargetLabels);
            newClassReader.accept(secPassClassVisitor, ClassReader.EXPAND_FRAMES);
            return newcw.toByteArray();

        } else
            return cw.toByteArray();
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
        if (visitorPass == VisitorPass.READINGPASS) {
            v = new InstructionCollector(v, access, name, desc, signature, exceptions);
            ArrayList<InstructionCollector> instructionCollectors = CollectGoTo.methodInstHashMap.get(name);
            if (instructionCollectors == null) {
                instructionCollectors = new ArrayList<>();
                instructionCollectors.add((InstructionCollector) v);
                methodInstHashMap.put(name, instructionCollectors);
            } else
                instructionCollectors.add((InstructionCollector) v);
        } else { //writing pass
            assert (visitorPass == VisitorPass.WRITINGPASS);
            Pair<ArrayList<Label>, HashMap<Integer, ModifiedGoTo>> newGoToAndLabelsPair = newGoToAndLabelsPairMap.get(name);
            methodNewLabelArr = newGoToAndLabelsPair.getKey();
            methodGoToInsHashMap = newGoToAndLabelsPair.getValue();
            v = new GoToWriter(v, access, name, desc, signature, exceptions);
        }
        return v;
    }


    class InstructionCollector extends GeneratorAdapter {
        public ArrayList<Pair<Integer, Label>> collectedJumpInstructions = new ArrayList<>();

        public ArrayList<Label> seenLabels = new ArrayList<>();

        // indicates the number of goto that we have encountered so far.
        public int goToPos = 0;

        public ArrayList<Label> backEdgeTargetLabel = new ArrayList<>();

        InstructionCollector(MethodVisitor delegate, int access, String name, String desc, String signature, String[] exceptions) {
            super(Opcodes.ASM5, delegate, access, name, desc);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            if (this.mv != null) {
                if (opcode == 167) { // is a goTo instruction.
                    ++goToPos;
                    if (isBackEdgeLabel(label)) {
                        if (!backEdgeTargetLabel.contains(label))
                            backEdgeTargetLabel.add(label);
                        collectedJumpInstructions.add(new Pair<>(goToPos, label));
                    }
                }
                this.mv.visitJumpInsn(opcode, label);
            }
        }

        private boolean isBackEdgeLabel(Label label) {
            return (seenLabels.contains(label));
        }

        public void visitLabel(Label label) {
            if (this.mv != null) { //collecting all encountered labels.
                seenLabels.add(label);
                this.mv.visitLabel(label);
            }
        }
    }


    class GoToWriter extends GeneratorAdapter {

        // indicates the number of goto that we have encountered so far.
        public int goToOrdNum = 0;
        private int currentLine;
        private ArrayList<Label> labelVisitLineNum = new ArrayList<>();

        GoToWriter(MethodVisitor delegate, int access, String name, String desc, String signature, String[]
                exceptions) {
            super(Opcodes.ASM5, delegate, access, name, desc);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            if (this.mv != null) {
                if (opcode == 167) { // is a goTo instruction.
                    ++goToOrdNum;
                    ModifiedGoTo modifiedGoTo = methodGoToInsHashMap.get(goToOrdNum);
                    if (modifiedGoTo != null) { // if it is in the modifiedGoToHashMap
                        if (modifiedGoTo.isLastGoTo) { //if it is the last goTo then we need to visit the newLabel first before we visit the instruction.
                            super.mv.visitLabel(modifiedGoTo.jumpLabel);
                            super.mv.visitJumpInsn(opcode, label);
                            return;
                        } else { // if it is not the last then lets visit the goTo with the new target created for it.
                            super.mv.visitJumpInsn(opcode, modifiedGoTo.jumpLabel);
                            /*if (!labelVisitLineNum.contains(modifiedGoTo.jumpLabel)) {
                                super.mv.visitLabel(modifiedGoTo.jumpLabel);
                                //super.mv.visitLineNumber(currentLine, modifiedGoTo.jumpLabel);
                                labelVisitLineNum.add(modifiedGoTo.jumpLabel);
                            }*/
                            return;
                        }
                    }
                }
                this.mv.visitJumpInsn(opcode, label);
            }
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            mv.visitLineNumber(line, start);
            currentLine = line;
        }
    }
}
