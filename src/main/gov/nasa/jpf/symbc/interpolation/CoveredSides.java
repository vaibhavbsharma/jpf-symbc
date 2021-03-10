package gov.nasa.jpf.symbc.interpolation;

/**
 * used to tell if we have which side was explored, and thus which side have we created the wp for.
 * to be able to use the wp, we must have explored both the then and the else side of an branching instruction.
 */
public enum CoveredSides {
    THEN,ELSE,BOTH
}
