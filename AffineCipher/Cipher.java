package cipher;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Cipher {
	
	private final String archivoEntrada = "original.txt";
	private final String archivoCifrado = "cifrado.txt";
	private final String archivoDescifrado = "descifrado.txt";
	
	private int alpha;
	private int beta;
	private int n = 256; 
	private int inversoAditivo;
	private int inversoMultiplicativo;
	
	private FileReader fileReader;
	private FileReader fl2;
	
	private FileWriter fileWriter;
	private FileWriter f2;
	
        private String procedimiento;
        
	public Cipher() throws IOException {
            fileReader = new FileReader(archivoEntrada);
	}
	
    public String iniciarAlgotimo(int b, int a) throws IOException {
        procedimiento = "";
        alpha = a;
        beta = b;
    	Cipher corrimiento = new Cipher();
        System.out.println("alpha: "+alpha+" beta: "+beta);
        System.out.println("inverso aditivo: "+inversoAditivo);
    	
        int [] gcd = gcd(alpha,n);
    	inversoAditivo = obtenerInversoAditivo(beta);
    	inversoMultiplicativo = gcd[1] ;
        
    	if(gcd[1]<0) {
            System.out.println("aplicando inverso aditivo al resusltado de euclides");
            inversoMultiplicativo = obtenerInversoAditivo(gcd[1]);
    	}
        System.out.println("inverso multiplicativo: "+inversoMultiplicativo);
        try {
            int caracter;
            fileWriter = new FileWriter(new File(archivoCifrado), false);
            f2 = new FileWriter(new File(archivoDescifrado), false);
            //cifrado
            while ((caracter = fileReader.read()) != -1) {
            	int numero = corrimiento.cifrar(caracter-97,alpha, beta);
                fileWriter.write((char)numero);
            }
            fileWriter.close();
            fl2 = new FileReader(archivoCifrado);
            //descifrado
            while ((caracter = fl2.read()) != -1) {
            	f2.write((char) corrimiento.descifrar(caracter-65, inversoMultiplicativo, inversoAditivo));
            }            
        } catch (Exception e) {
        	System.err.println("Exception");
        } finally {
            cerrarArchivos();
        }
        return procedimiento;
    }
    
    private int cifrar(int caracterDuro, int alpha, int beta) {
    	int aux = ((alpha*caracterDuro + beta)%n);
        return aux+65;
    }
    
    private int descifrar(int caracterCifrado, int inversoMultiplicativo, int inversoAditivo) {
    	int aux = (inversoMultiplicativo*(caracterCifrado + inversoAditivo)%n); 
    	return aux+97;
    }
    
    //return array[d,a,b] such that d = gcd(p,q), ap + bq = d
    private int[] gcd(int p, int q) {
    	if(q==0) {
            return new int[] {p,1,0};
    	}else{
            procedimiento+=("p: "+p+" q: "+q+" \n");
        }
    	int [] vals = gcd(q, p%q);
    	int d = vals[0];
    	int a = vals[2];
    	int b = vals[1] - (p/q) * vals[2];
        procedimiento+=("d: "+d+" p:"+p+" a: "+a+" q: "+q+" b: "+b +"\n");
    	return new int []{d,a,b};
    }
    
    private int obtenerInversoAditivo(int numero) {
    	int inverso = 0;
    	if(numero > 0) {
            inverso = n - numero;
    	}else {
            inverso = n + numero;
    	}
    	return inverso;
    }
    
    private void cerrarArchivos() throws IOException {
    	if (fileReader != null)
            fileReader.close();
        if (fileWriter != null)
            fileWriter.close();
        if(f2 != null)
        	f2.close();
    }
}
