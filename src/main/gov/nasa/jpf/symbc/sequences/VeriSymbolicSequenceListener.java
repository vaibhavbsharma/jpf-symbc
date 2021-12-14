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

//
//Copyright (C) 2007 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
package gov.nasa.jpf.symbc.sequences;

// does not work well for static methods:summary not printed for errors
// SH: Code copied from SymbolicSequenceListener with some modification for branch coverage test case generation for Veritesting.

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.symbc.VeriBranchListener;
import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.vm.*;

import java.util.*;

public class VeriSymbolicSequenceListener extends ThreadSymbolicSequenceListener implements PublisherExtension {


    public VeriSymbolicSequenceListener(Config conf, JPF jpf) {
        super(conf, jpf);
    }

    @Override
    public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
        //this is always overriding ThreadSymbolicSequenceListener, since we do not want ThreadSymbolicSequenceListener
        // to generate test cases anymore. It is up to VeriSymbolicSequenceListener to either generate test cases
        // using the region, obligation way, or if no regions are along the path, then it can invoke the ordinary way
        // of ThreadSymbolicSequenceListener that we already use for SPF.

        /*if (VeriBranchListener.newCoveredOblg.size() > 0)
            return;
        else {
            super.threadTerminated(vm, terminatedThread);
            VeriBranchListener.updateCoverageEndOfPath(); // we still want to update the coverage done by SPF only.
        }*/

        super.threadTerminated(vm, terminatedThread);
    }

    public static void collectVeriTests(VM vm, Map<String, Object> veriSolutionMap) {

        SystemState ss = vm.getSystemState();
        ChoiceGenerator<?> cg = vm.getChoiceGenerator();

        if (!(cg instanceof PCChoiceGenerator)) {
            ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGenerator();
            while (!((prev_cg == null) || (prev_cg instanceof PCChoiceGenerator))) {
                prev_cg = prev_cg.getPreviousChoiceGenerator();
            }
            cg = prev_cg;
        }

        if ((cg instanceof PCChoiceGenerator) && ((PCChoiceGenerator) cg).getCurrentPC() != null) {

            ChoiceGenerator<?>[] cgs = ss.getChoiceGenerators();
            methodSequences.add(getMethodSequence(cgs, veriSolutionMap));
        }
    }
}
