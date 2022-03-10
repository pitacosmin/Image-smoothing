package smoothingProject;

public class Producer extends ProducerConsumer {
	Producer() { // constructor fara parametrii
		super();
	}
	
	public Producer(Buffer buf) {
		super(true, buf);// constructorul clasei ProducerConsumer
		// am setat producer = true (deoarece producer incepe)
	}
}