package gov.nasa.jpf.symbc.branchcoverage;

public enum CoverageMode {
    SPF, //this is a coverage collection mode for SPF
    COLLECT_PRUNE,
    COLLECT_GUIDE,
    COLLECT_PRUNE_GUIDE,
    JR_COLLECT, //this is a coverage collection mode for SPF
    JRCOLLECT_GUIDE,
    JRCOLLECT_PRUNE,
    JRCOLLECT_PRUNE_GUIDE, JR_PLAIN,
}
