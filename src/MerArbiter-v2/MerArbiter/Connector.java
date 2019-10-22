package MerArbiter;

import java.util.LinkedList;

import osl.manager.Actor;
// import osl.manager.ActorName;
// import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;
import osl.util.constraints.Disable;

public class Connector extends Actor {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// connection between arbiter and user1
	LinkedList<ArbiterMessage> atou1queue = new LinkedList<ArbiterMessage> ();
	LinkedList<UserMessage> u1toaqueue = new LinkedList<UserMessage> ();

	// connection between arbiter and user2
	LinkedList<ArbiterMessage> atou2queue = new LinkedList<ArbiterMessage> ();
	LinkedList<UserMessage> u2toaqueue = new LinkedList<UserMessage> ();

	public Connector () { }

	@message
	public void User1sendsToArbiter(UserMessage arg)throws InterruptedException {
		// Send does not block
		u1toaqueue.add(arg);
	}


	@message
	public ArbiterMessage User1recvFromArbiter() throws InterruptedException {
		// Recv will block if queue is empty (see below)
		ArbiterMessage res;
		res = atou1queue.remove();
		return res;
	}

	@Disable(messageName = "User1recvFromArbiter")
	public Boolean disableUser1recvFromArbiter(){
		// Synch constraint: recv is blocked when queue is empty
		return atou1queue.isEmpty();
	}

	@message
	public void User2sendsToArbiter(UserMessage arg)throws InterruptedException {
		// Send does not block
		u2toaqueue.add(arg);
	}


	@message
	public ArbiterMessage User2recvFromArbiter() throws InterruptedException {
		// Recv will block if queue is empty (see below)
		ArbiterMessage res;
		res = atou2queue.remove();
		return res;
	}

	@Disable(messageName = "User2recvFromArbiter")
	public Boolean disableUser2recvFromArbiter(){
		// Synch constraint: recv is blocked when queue is empty
		return atou2queue.isEmpty();
	}


	@message
	public void ArbitersendsToUser1(ArbiterMessage arg) throws InterruptedException {
		// Send does not block
		atou1queue.add(arg);
	}

	@message
	public UserMessage ArbiterrecvFromUser1()throws InterruptedException {
		// Synch constraints: recv is blocked when queue is empty
		UserMessage res;
		res = u1toaqueue.remove();
		return res;
	}

	@Disable(messageName = "ArbiterrecvFromUser1")
	public Boolean disableArbiterRecvFromUser1(){
		// Synch constraint: recv is blocked when queue is empty
		return u1toaqueue.isEmpty();
	}

	@message
	public void ArbitersendsToUser2(ArbiterMessage arg) throws InterruptedException {
		// Send does not block
		atou2queue.add(arg);
	}

	@message
	public UserMessage ArbiterrecvFromUser2()throws InterruptedException {
		// Synch constraints: recv is blocked when queue is empty
		UserMessage res;
		res = u2toaqueue.remove();
		return res;
	}

	@Disable(messageName = "ArbiterrecvFromUser2")
	public Boolean disableArbiterRecvFromUser2(){
		// Synch constraint: recv is blocked when queue is empty
		return u2toaqueue.isEmpty();
	}

  @message
	  public void shutdown() {
	    destroy("Connector is done.");
	  }

}
