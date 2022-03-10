package smoothingProject;

public abstract class ProducerConsumer extends Thread {
	private Boolean producer;
	private Buffer buffer;
	
	ProducerConsumer() {
		producer = true;
	}
	ProducerConsumer(Boolean bool, Buffer buf) { // retin buffer-ul primit si starea producer-ului
		producer = bool;
		buffer = buf;
	}
	public void run() {
		int width = Smoothing.imagineInit.getWidth(); // latimea imaginii
		int height = Smoothing.imagineInit.getHeight(); // inaltimea imaginii
		
		if(producer) { // Producer-ul este activ
			for (int i = 0; i < 4; i++) { // impart imaginea in 4  
				int[][] pixels = new int[width][height];// declar o matrice prin care trimit cate
				// un sfert din imagine
				
				for (int j = height/4 * i; j < height/4 * (i + 1); j++) { // se alege sfertul curent
					// (imaginea este impartita pe inaltime)
					
					for (int k = 0; k < width; k++) {
						pixels[k][j] = Smoothing.imagineInit.getRGB(k, j);
						// retin pixelul curent din imaginea originala
					}
				}
				
				buffer.put(pixels);// se pune sfertul curent in Buffer
				
				System.out.println("Producatorul a pus sfertul cu numarul " + (i + 1) + " al imaginii.");
				
				try {
					sleep(1000); //sleep inainte de a trimite urmatorul sfert
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}
		else {
			// Consumer-ul este activ
			for (int i = 0; i < 4; i++) {
				
				int[][] pixels = new int[width][height/4]; // declar o matrice in care voi stoca sfertul de imagine primit
				pixels = buffer.get();// se ia sfertul curent din Buffer
				
				for (int j = height/4 * i; j < height/4 * (i + 1); j++) {
					for (int k = 0; k < width; k++) {
						Smoothing.imaginePentruProcesare.setRGB(k, j, pixels[k][j]);
						// setez pixelul curent in imaginea care va fi trimisa spre procesare
					}
				}
				
				System.out.println("Consumer-ul preia sfertul " + (i + 1) + " din imagine.");
				try {
					sleep(1000);//sleep pana sa fie trimis urmatorul sfert
				} catch (InterruptedException exp) {
					System.out.println(exp);
				}
			}
		}
	}
}

