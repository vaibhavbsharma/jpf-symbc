package MerArbiter;

import java.io.Serializable;

public class ArbiterMessage implements Serializable {
 public boolean grant;
 public boolean deny;
 public boolean rescind;

 public ArbiterMessage (boolean gr, boolean den, boolean res) {
	 grant = gr;
	 deny = den;
	 rescind = res;
 }

 public String toString() {
	 return " grant "+grant+" deny "+deny+" rescind "+rescind;
 }
}
