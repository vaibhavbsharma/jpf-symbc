package MerArbiter;

import java.io.Serializable;

public class UserMessage implements Serializable {
 public int uid;
 public int resource;
 public boolean request;
 public boolean cancel;

 public UserMessage (int uid, int res, boolean req, boolean can)  {
	 this.uid = uid;
	 resource = res;
	 request = req;
	 cancel = can;
 }

 public String toString() {
	 return "user "+uid+" resource "+resource+" request "+request+" cancel "+cancel;
 }
}
