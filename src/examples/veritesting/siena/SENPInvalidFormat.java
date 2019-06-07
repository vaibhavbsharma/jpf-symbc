
public class SENPInvalidFormat extends Exception {
    int		expected_type;
    String	expected_value;
    int		line_number;

    public SENPInvalidFormat() {
    	super();
    }
    public SENPInvalidFormat(String v) {
    	super("expecting: `" + v + "'");
    	expected_value = v;
    }
    public SENPInvalidFormat(int t, String v) {
    	this(v);
    	expected_type = t;
    }
    public SENPInvalidFormat(int t) {
    	this();
    	expected_type = t;
    }
    
    public int getExpectedType() {
    	return expected_type;
    }

    public String getExpectedValue() {
    	return expected_value;
    }

    public int getLineNumber() {
    	return expected_type;
    }
}
