package veritesting.tcas;

public class TCASRegion {
    public int OLEV = 600;
    public int MAXALTDIFF = 300;
    public int MINSEP = 600;
    public int NOZCROSS = 100;

    public int Cur_Vertical_Sep;
    public boolean High_Confidence;
    public boolean Two_of_Three_Reports_Valid;

    public int Own_Tracked_Alt;
    public int Own_Tracked_Alt_Rate;
    public int Other_Tracked_Alt;

    public int Alt_Layer_Value;

    int Positive_RA_Alt_Thresh_0;
    int Positive_RA_Alt_Thresh_1;
    int Positive_RA_Alt_Thresh_2;
    int Positive_RA_Alt_Thresh_3;

    public int Up_Separation;
    public int Down_Separation;

    public int Other_RAC;

    public int NO_INTENT = 0;
    public int DO_NOT_CLIMB = 1;
    public int DO_NOT_DESCEND = 2;

    public int Other_Capability;
    public int TCAS_TA = 1;
    public int OTHER = 2;

    public int Climb_Inhibit;

    public int UNRESOLVED = 0;
    public int UPWARD_RA = 1;
    public int DOWNWARD_RA = 2;

    private int result_alt_sep_test = -1;
    private int result_alim = -1;

    private int b2I(boolean b) {
        return b ? 1 : 0;
    }

    public void initialize() {
        Positive_RA_Alt_Thresh_0 = 400;
        Positive_RA_Alt_Thresh_1 = 500;
        Positive_RA_Alt_Thresh_2 = 640;
        Positive_RA_Alt_Thresh_3 = 740;
    }

    public int ALIM() {
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

    public int alt_sep_test() {
        int alt_sep = UNRESOLVED;
        if (High_Confidence) {
            alt_sep = ALIM();
        }
        return alt_sep;
    }

    public void mainProcess(int a1, int a2, int a7) {//,

        initialize();
        Cur_Vertical_Sep = a1;
        if (a2 == 0) {
            High_Confidence = false;
        } else {
            High_Confidence = true;
        }

        Alt_Layer_Value = a7;

        result_alt_sep_test = alt_sep_test();

    }

    public void sym1(int a1, int a2, int a7) {
        mainProcess(a1, a2, a7);
    }

    public static void main(String[] argv) {
        int maxSteps = Integer.parseInt(System.getenv("MAX_STEPS"));
        if (maxSteps-- > 0) (new TCASRegion()).sym1(601, -1, 0);

    }

}
