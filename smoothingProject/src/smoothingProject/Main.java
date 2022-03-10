package smoothingProject;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	 static void display(String... values){  //functie display
		 for(String s:values){  
			   System.out.println(s);  
		 }  
	 }
	
	public static void main(String[] args) throws IOException{
		//path de unde luam imaginea neprocesata
		display("Path-ul fisierului de unde se citeste poza (fisier BMP):");
		Scanner scannerin = new Scanner(System.in);
		String in = scannerin.nextLine();
		
		//path pentru imaginea procesata
		display("Path-ul fisierului unde se va scrie (fisier BMP):");
		Scanner scannerout = new Scanner(System.in);
		String out = scannerout.nextLine();
		
		// declar un obiect de tip Smoothing
		Smoothing smoothing = new Smoothing(in, out);
		smoothing.startSmoothing(); // start aplicatie
		
		scannerin.close();
		scannerout.close();
	}

}