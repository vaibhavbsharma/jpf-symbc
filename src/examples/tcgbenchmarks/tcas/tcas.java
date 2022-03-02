package tcgbenchmarks.tcas;

import veritesting.Outputs;

public class tcas {
    public static int OLEV = 600;
    public static int MAXALTDIFF = 300;
    public static int MINSEP = 600;
    public static int NOZCROSS = 100;

    public static int Cur_Vertical_Sep;
    public static boolean High_Confidence;
    public static boolean Two_of_Three_Reports_Valid;

    public static int Own_Tracked_Alt;
    public static int Own_Tracked_Alt_Rate;
    public static int Other_Tracked_Alt;

    public static int Alt_Layer_Value;

    static int Positive_RA_Alt_Thresh_0;
    static int Positive_RA_Alt_Thresh_1;
    static int Positive_RA_Alt_Thresh_2;
    static int Positive_RA_Alt_Thresh_3;

    public static int Up_Separation;
    public static int Down_Separation;

    public static int Other_RAC;

    public static int NO_INTENT = 0;
    public static int DO_NOT_CLIMB = 1;
    public static int DO_NOT_DESCEND = 2;

    public static int Other_Capability;
    public static int TCAS_TA = 1;
    public static int OTHER = 2;

    public static int Climb_Inhibit;

    public static int UNRESOLVED = 0;
    public static int UPWARD_RA = 1;
    public static int DOWNWARD_RA = 2;

    private static int result_alt_sep_test = -1;
    private static int result_alim = -1;

    private static int b2I(boolean b) {
        return b ? 1 : 0;
    }

    public static void initialize() {
        Positive_RA_Alt_Thresh_0 = 400;
        Positive_RA_Alt_Thresh_1 = 500;
        Positive_RA_Alt_Thresh_2 = 640;
        Positive_RA_Alt_Thresh_3 = 740;
    }

    public static int ALIM() {
        if (Alt_Layer_Value == 0) {
            return Positive_RA_Alt_Thresh_0;
        } else if (Alt_Layer_Value == 1) {
            return Positive_RA_Alt_Thresh_1;
        } else if (Alt_Layer_Value == 2) {
            return Positive_RA_Alt_Thresh_2;
        } else {
            return Positive_RA_Alt_Thresh_3;
        }
    }

    public static int Inhibit_Biased_Climb() {
        if (Climb_Inhibit > 0) {
            int ret = Up_Separation + NOZCROSS;
            return ret;
        } else {
            return Up_Separation;
        }
    }

    public static boolean Non_Crossing_Biased_Climb() {
        int upward_preferred;
        int inhibit_biased_climb = Inhibit_Biased_Climb();
        if (inhibit_biased_climb > Down_Separation) {
            upward_preferred = 1;
        } else {
            upward_preferred = 0;
        }
        if (upward_preferred != 0) {
            int alim = ALIM();
            if (!(Down_Separation >= alim)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (!(Cur_Vertical_Sep >= MINSEP)) {
                return false;
            } else {
                int alim = ALIM();
                if (!(Up_Separation >= alim)) {
                    return false;
                } else {
                    boolean own_above_thread = Own_Above_Threat();
                    if (!own_above_thread) {
                        return false;
                    } else {
                        return true;
                    }
                }

            }
        }
    }

    public static boolean Non_Crossing_Biased_Descend() {
        int upward_preferred;
        int inhibit_biased_climb = Inhibit_Biased_Climb();
        if (inhibit_biased_climb > Down_Separation) {
            upward_preferred = 1;
        } else {
            upward_preferred = 0;
        }
        if (upward_preferred != 0) {
            int alim = ALIM();
            boolean own_below_threat = Own_Below_Threat();
            // reduction source
            if (!(Cur_Vertical_Sep >= MINSEP)) {
                return false;
            } else if (!(Down_Separation >= alim)) {
                return false;
            } else if (!own_below_threat) {
                return false;
            } else {
                return true;
            }
        } else {
            int alim = ALIM();
            boolean own_above_threat = Own_Above_Threat();
            // reduction source
            if (!(Up_Separation >= alim)) {
                return false;
            } else if (!own_above_threat) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static boolean Own_Below_Threat() {
        boolean ret = false;
        if (Own_Tracked_Alt < Other_Tracked_Alt) {
            ret = true;
        }
        return ret;
    }

    public static boolean Own_Above_Threat() {
        boolean ret = false;
        if (Other_Tracked_Alt < Own_Tracked_Alt) {
            ret = true;
        }
        return ret;
    }

    public static int alt_assign() {
        int alt_sep = UNRESOLVED;
        boolean need_upward_RA = false;
        boolean non_crossing_biased_climb = Non_Crossing_Biased_Climb();
        if (non_crossing_biased_climb) {
            boolean own_below_threat = Own_Below_Threat(); //return symbolic temp variable
            if (own_below_threat) {
                need_upward_RA = true; //is symbolic
            }
        }
        boolean need_downward_RA = false;
        boolean non_crossing_biased_descend = Non_Crossing_Biased_Descend();
        if (non_crossing_biased_descend) {
            boolean own_above_threat = Own_Above_Threat();
            if (own_above_threat) {
                need_downward_RA = true;
            }
        }
        if (need_upward_RA) {
            if (need_downward_RA) {
                alt_sep = UNRESOLVED;
            } else {
                alt_sep = UPWARD_RA;
            }
        } else {
            if (need_downward_RA) {
                alt_sep = DOWNWARD_RA;
            } else {
                alt_sep = UNRESOLVED;
            }
        }

		/*if(need_upward_RA && need_downward_RA) alt_sep = 0;
		if(need_upward_RA && !need_downward_RA) alt_sep = 1;
		if(!need_upward_RA && need_downward_RA) alt_sep = 2;
		if(!need_upward_RA && !need_downward_RA) alt_sep = 0;*/

        return alt_sep;
    }

    public static int alt_sep_test() {
        boolean enabled = false;
        boolean tcas_equipped = false;
        boolean intent_not_known = false;
        int alt_sep = UNRESOLVED;

        if (High_Confidence) {
            if (Own_Tracked_Alt_Rate <= OLEV) {
                if (Cur_Vertical_Sep > MAXALTDIFF) {
                    enabled = true;
                }
            }
        }

        if (enabled) {
            if (Other_Capability == TCAS_TA) {
                tcas_equipped = true;
            }
            if (tcas_equipped) {
                if (Two_of_Three_Reports_Valid) {
                    if (Other_RAC == NO_INTENT) {
                        intent_not_known = true;
                    }
                }
                if (intent_not_known) {
                    alt_sep = alt_assign();
                }
            } else {
                alt_sep = alt_assign();
            }
        }

        return alt_sep;
    }

    public static void mainProcess(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10, int a11, int a12) {//,
        //int a21, int a22, int a23, int a24, int a25, int a26, int a27, int a28, int a29, int a30, int a31, int a32) {
        initialize();
        Cur_Vertical_Sep = a1;
        if (a2 == 0) {
            High_Confidence = false;
        } else {
            High_Confidence = true;
        }
        if (a3 == 0) {
            Two_of_Three_Reports_Valid = false;
        } else {
            Two_of_Three_Reports_Valid = true;
        }

        Own_Tracked_Alt = a4;
        Own_Tracked_Alt_Rate = a5;
        Other_Tracked_Alt = a6;
        Alt_Layer_Value = a7;
        Up_Separation = a8;
        Down_Separation = a9;
        Other_RAC = a10;
        Other_Capability = a11;
        Climb_Inhibit = a12;

        result_alt_sep_test = alt_sep_test();
        result_alim = ALIM();


    }

    public static void sym1(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10, int a11, int a12) {
        mainProcess(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
    }

    public static void sym2(int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8, int b9, int b10, int b11, int b12) {
        mainProcess(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12);
    }

    public static void sym3(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12) {
        mainProcess(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12);
    }

    public static void sym4(int d1, int d2, int d3, int d4, int d5, int d6, int d7, int d8, int d9, int d10, int d11, int d12) {
        mainProcess(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    public static void sym5(int e1, int e2, int e3, int e4, int e5, int e6, int e7, int e8, int e9, int e10, int e11, int e12) {
        mainProcess(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12);
    }

    public static void sym6(int f1, int f2, int f3, int f4, int f5, int f6, int f7, int f8, int f9, int f10, int f11, int f12) {
        mainProcess(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
    }

    public static void sym7(int g1, int g2, int g3, int g4, int g5, int g6, int g7, int g8, int g9, int g10, int g11, int g12) {
        mainProcess(g1, g2, g3, g4, g5, g6, g7, g8, g9, g10, g11, g12);
    }

    public static void sym8(int h1, int h2, int h3, int h4, int h5, int h6, int h7, int h8, int h9, int h10, int h11, int h12) {
        mainProcess(h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12);
    }

    public static void sym9(int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12) {
        mainProcess(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12);
    }

    public static void sym10(int j1, int j2, int j3, int j4, int j5, int j6, int j7, int j8, int j9, int j10, int j11, int j12) {
        mainProcess(j1, j2, j3, j4, j5, j6, j7, j8, j9, j10, j11, j12);
    }

    public static void main(String[] argv) {
        int maxSteps = Integer.parseInt(System.getenv("MAX_STEPS"));
//		for (int i=0; i < maxSteps; i++)
//			mainProcess(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1); //,
        //601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym1(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym2(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym3(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym4(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym5(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym6(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym7(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym8(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym9(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
        if (maxSteps-- > 0) sym10(601, -1, 0, -1, 0, 0, 0, 301, 400, 0, 0, 1);
//        assert (result_alim >= -1);
    }

}
