package veritesting.wbs;
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


import gov.nasa.jpf.symbc.Debug;

public class RunWBS_vt {

    public static void symbolicMethod() {}

    public static void main(String[] args) {
        WBS_vt wbs_vt = new WBS_vt();
        int maxSteps = Integer.parseInt(System.getenv("MAX_STEPS"));
//        for (int i = 0; i < maxSteps; i++) {
//            int pedal = 0; //Debug.makeSymbolicInteger("pedal" + i);
//            boolean auto = false; //Debug.makeSymbolicBoolean("auto" + i);
//            boolean skid = false; //Debug.makeSymbolicBoolean("skid" + i);
//            wbs.update(pedal, auto, skid);
//        }
        if (maxSteps-- > 0) wbs_vt.sym1(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym2(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym3(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym4(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym5(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym6(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym7(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym8(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym9(0, false, false);
        if (maxSteps-- > 0) wbs_vt.sym10(0, false, false);
        assert wbs_vt.checkProp1();
    }
}
