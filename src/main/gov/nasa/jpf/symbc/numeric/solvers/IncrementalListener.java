/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.nasa.jpf.symbc.numeric.solvers;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.VM;

import java.util.Stack;

public class IncrementalListener extends PropertyListenerAdapter {

    public static IncrementalSolver solver;

    public static Stack<String> solverHashStack = new Stack<String>();

    public IncrementalListener(Config config, JPF jpf) {
        String stringDp = SymbolicInstructionFactory.dp[0];
        if (stringDp.equalsIgnoreCase("z3inc")) {
            solver = new ProblemZ3Incremental();
        } else if (stringDp.equalsIgnoreCase("z3bitvectorinc")) {
            solver = new ProblemZ3BitVectorIncremental();
        } else {
            System.err.println("Trying to use incremental listener, but solver " + stringDp + " does not support incremental solving (try z3inc or z3bitvectorinc)");
            jpf.removeListener(this);
        }

    }

    @Override
    public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
        if (currentCG instanceof PCChoiceGenerator) {
//            System.out.println("choiceGeneratorAdvanced: at " + currentCG.getInsn().getMethodInfo() + "#" + currentCG.getInsn().getPosition());
            if (IncrementalListener.solver != null) {
                if (solver instanceof ProblemZ3BitVectorIncremental) {
                    solverHashStack.push(ProblemZ3BitVectorIncremental.Z3Wrapper.getInstance().getSolver().toString());
                } else if (solver instanceof ProblemZ3Incremental) {
                    solverHashStack.push(ProblemZ3Incremental.Z3Wrapper.getInstance().getSolver().toString());
                } else assert false;

                solver.push();
            }
        }
    }

    /**
     * When the state backtracks we want to pop from the solver any constraints that it had on that path, and compare it with the last state we have pushed on the solverHashStack.
     * If that is successful we prepare the solverHashStack by restoring it previous state, through popping.
     * @param search
     */
    @Override
    public void stateBacktracked(Search search) {
        if (search.getVM().getSystemState().getChoiceGenerator() instanceof PCChoiceGenerator) {
//            System.out.println("stateBacktracked");
            if (IncrementalListener.solver != null) {
                solver.pop();
//                assert (solver.toString().hashCode() == solverHashStack.peek()) : "inconsistency detected in incremental solver state, every push must have a pop. Assumption Violated. Failing.";
                if (solver instanceof ProblemZ3BitVectorIncremental) {
                    String solverHashCode = ProblemZ3BitVectorIncremental.Z3Wrapper.getInstance().getSolver().toString();
                    assert ((solverHashCode.isEmpty() && solverHashStack.isEmpty()) || solverHashCode.equals(solverHashStack.peek())) : "inconsistency detected in incremental solver state, every push must have a pop. Assumption Violated. Failing.";
                } else if (solver instanceof ProblemZ3Incremental) {
                    String solverHashCode = ProblemZ3Incremental.Z3Wrapper.getInstance().getSolver().toString();
                    assert ((solverHashCode.isEmpty() && solverHashStack.isEmpty()) || solverHashCode.equals(solverHashStack.peek())) : "inconsistency detected in incremental solver state, every push must have a pop. Assumption Violated. Failing.";
                } else assert false;
                solverHashStack.pop();
            }
        }
    }
}
