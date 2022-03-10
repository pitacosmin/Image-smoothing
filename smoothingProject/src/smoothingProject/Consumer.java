package smoothingProject;

public class Consumer extends ProducerConsumer {
	Consumer() { // constructor fara parametrii
		super();
	}
	
	public Consumer(Buffer buf) {
		super(false, buf);// constructorul clasei ProducerConsumer
		// producer = false
	}
}
