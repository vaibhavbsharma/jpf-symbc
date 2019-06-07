
public class Process {
	int pid;
	int priority;
	Process next;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int _pid) {
		pid = _pid;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int _priority) {
		priority = _priority;
	}
	public Process getNext() {
		return next;
	}
	public void setNext(Process _next) {
		next = _next;
	}
}
