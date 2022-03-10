package smoothingProject;

public class Buffer {
	private int[][] pixels;
	private boolean enter = false;
	
	public synchronized int[][] get() {
		while (!enter) {
			try {
				wait(); //Producer-ul asteapta sa puna o valoare	
			} catch (InterruptedException exp) {
				exp.printStackTrace();
			}
		}		
		enter = false;// dau accesul catre producator
		notifyAll();// anunt incheierea procedurii
		return this.pixels;// returnez matricea actualizata
	}
	
	public synchronized void put(int[][] pixels) {
		while (enter) {
			try {
				wait(); // Consumer-ul asteapta sa ia o valoare
				
			} catch (InterruptedException exp) {
				exp.printStackTrace();
			}
		}
		
		this.pixels = pixels; // matricea primita
		enter = true; // dau accesul catre consumator
		notifyAll(); // anunt incheierea procedurii
	}
}
