package smoothingProject;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Smoothing implements Interface{
	
	BufferedImage bufferedImage; // fereastra pentru poza procesata     
    ImageIcon icon;
    ImageIcon icon1;
    JLabel imgNeprelucrata=new JLabel();
    JLabel imgPrelucrata=new JLabel();
	
	public static BufferedImage imagineInit = null; // imaginea originala, citita din fisier
	public static BufferedImage imaginePentruProcesare = null; // partea de imagine ce urmeaza a fi trimisa
	
	public static BufferedImage imagineProcesata = null; // imaginea finala, procesata
	
	private long timpCitire; // timp citire
	private long timpProcesare; // timp procesare
	private long timpScriere; // timp scriere
	
	private String inFile; // locatia imaginii initiale
	private String outFile; // locatia imaginii procesate
	
	Smoothing(String in, String out) {
		inFile = in;   //locatia imaginii inainte de prelucrare
		outFile = out; //locatia imaginii dupa prelucrare
	}
	
	
	public void startSmoothing() {
		File file = null;// declaram un fisier
		
		JFrame jf = new JFrame();
	       JPanel jp = new JPanel();        
	       jf.add(jp);
	       jp.add(imgPrelucrata);   //pannel pentru imaginea prelucrata
	       jp.add(imgNeprelucrata); //pannel pentru imaginea neprelucrata
	   
	    // proprietatile ferestrei in care afisam imaginea prelucrata
	       
	    jf.setVisible(true);
	    jf.setSize(500,500);
	    jf.setLocation(400,300);
	    jf.setTitle("Image smoothing");
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        // citirea imaginii
		timpCitire = System.currentTimeMillis(); //variabila folosita pentru a prelua timpul curent
		
		try {
			file = new File(inFile); //deschid fisierul de input
			imagineInit = ImageIO.read(file); // citesc imaginea initiala
			
		} catch(IOException exp) {
			System.out.println("Error: " + exp); // eroare
		}
		
		timpCitire = System.currentTimeMillis() - timpCitire; 
		
		imaginePentruProcesare = new BufferedImage(imagineInit.getWidth(), imagineInit.getHeight(), imagineInit.getType());
		
		// declar obiecte de tip Buffer, Producer si Consumer
		
		Buffer buffer = new Buffer(); 
		Producer producer = new Producer(buffer); 
		Consumer consumer = new Consumer(buffer); 
		
		producer.start(); // start thread pentru Producer
		consumer.start(); // start thread pentru Consumer
		
		try {
			consumer.join(); // unesc thread-ul curent cu cel al Consumer-ului 
			// procesarea imaginii asteapta pana cand Consumer-ul isi termina executia
		} catch (InterruptedException exp) {
			exp.printStackTrace(); // eroare la unire
		}
		
	
		// procesarea imaginii
		timpProcesare = System.currentTimeMillis();
		
		float [] boxblur = new float[]{   1f / 9f, 1f / 9f, 1f / 9f,	//matrice de tip box blur
										  1f / 9f, 1f / 9f, 1f / 9f,
										  1f / 9f, 1f / 9f, 1f / 9f };
		//creare Kernel
		Kernel kernel = new Kernel(3, 3, boxblur);
		        
		// creare matrice de convolutie prin ConvolveOp
		BufferedImageOp op = new ConvolveOp(kernel);
		        
		// modificare imagine prin realizarea convolutiei dintre matricea de convolutie si BufferedImage
		imagineProcesata = op.filter(imaginePentruProcesare, null);   
		        
		timpProcesare = System.currentTimeMillis() - timpProcesare;
		System.out.println("\n S-a terminat etapa de procesare a imaginii \n");

        // Scrierea imagini prelucrate in fisier
		timpScriere = System.currentTimeMillis();
		        
		try {
			file = new File(outFile); // locatia imaginii de output
			ImageIO.write(imagineProcesata, "bmp", file); // scriu imaginea obtinuta la adresa de output
	
			icon1 = new ImageIcon(imagineInit); 
			icon = new ImageIcon(imagineProcesata);   			
			imgPrelucrata.setIcon(icon1);
	        imgNeprelucrata.setIcon(icon);		        	
		} catch(IOException exp) {
			System.out.println("Error: " + exp); // eroare
		}
		timpScriere = System.currentTimeMillis() - timpScriere; // calculeaza timpul de scriere
		
		System.out.println("Performante: ");
		System.out.println("Citirea a durat: " + timpCitire/1000.0f + " secunde"); // afisare timp citire
		System.out.println("Procesarea a durat: " + timpProcesare/1000.0f + " secunde"); // afisare timp procesare
		System.out.println("Scrierea a durat: " + timpScriere/1000.0f + " secunde"); // afisare timp scriere
	}
	
}
