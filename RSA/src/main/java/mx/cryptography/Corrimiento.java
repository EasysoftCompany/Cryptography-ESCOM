package mx.cryptography;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Corrimiento {
	
	private static final String archivoEntrada = "original.txt";
	private static final String archivoCifrado = "cifrado.txt";
	private static final String archivoDescifrado = "descifrado.txt";
	
	private static int alpha = 7;
	private static int beta = 3;
	private static int n = 26; 
	private static int inversoAditivo = 0;
	private static int inversoMultiplicativo = 1;
	
	private static FileReader fileReader;
	private static FileReader fl2;
	
	private static FileWriter fileWriter;
	private static FileWriter f2;
	
	public Corrimiento() throws IOException {
		fileReader = new FileReader(archivoEntrada);
	}
	
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	Corrimiento corrimiento = new Corrimiento();
    	int [] gcd = gcd(alpha,n);
    	
    	inversoAditivo = obtenerInversoAditivo(beta);
    	inversoMultiplicativo = gcd[1] ;
    	if(gcd[1]<0) {
    		System.out.println("aplicando inverso aditivo");
    		inversoMultiplicativo = obtenerInversoAditivo(gcd[1]);
    	}
    	
    	System.out.println("d: "+gcd[0]+" a: "+gcd[1]+" p:"+alpha+" q:"+n+" b:"+gcd[2]);
    	
        try {
        	int caracter;
            fileWriter = new FileWriter(new File(archivoCifrado), false);
            f2 = new FileWriter(new File(archivoDescifrado), false);
            
            //cifrado
            while ((caracter = fileReader.read()) != -1) {
            	int numero = corrimiento.cifrar(caracter-97);
                fileWriter.write((char)numero);
            }
            fileWriter.close();
            
            System.out.println("espere....");
            Integer a=sc.nextInt();
            
            fl2 = new FileReader(archivoCifrado);
            //descifrado
            while ((caracter = fl2.read()) != -1) {
            	f2.write((char) corrimiento.descifrar(caracter-65));
            }
            
            
            System.out.println("Listo");
        } catch (Exception e) {
        	System.err.println("Exception");
        } finally {
            cerrarArchivos();
        }
    }
    
    private int cifrar(int caracterDuro) {
    	int aux = ((alpha*caracterDuro + beta)%n);
        return aux+65;
    }
    
    private int descifrar(int caracterCifrado) {
    	int aux = (inversoMultiplicativo*(caracterCifrado + inversoAditivo)%n); 
    	return aux+97;
    }
    
    //return array[d,a,b] such that d = gcd(p,q), ap + bq = d
    private static int[] gcd(int p, int q) {
    	if(q==0) {
    		return new int[] {p,1,0};
    	}
    	int [] vals = gcd(q, p%q);
    	int d = vals[0];
    	int a = vals[2];
    	int b = vals[1] - (p/q) * vals[2];
    	return new int []{d,a,b};
    }
    
    private static int obtenerInversoAditivo(int numero) {
    	System.out.println("numero: "+numero);
    	int inverso = 0;
    	//Inverso aditivo
    	if(numero > 0) {
    		inverso = n - numero;
    	}else {
    		inverso = n + numero;
    	}
    	
    	
    	System.out.println("inverso: "+inverso);
    	return inverso;
    }
    
    private static void cerrarArchivos() throws IOException {
    	if (fileReader != null)
            fileReader.close();
        if (fileWriter != null)
            fileWriter.close();
        if(f2 != null)
        	f2.close();
    }
}
