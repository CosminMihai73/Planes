import java.net.*;
import java.util.Random;
import java.io.IOException;

public class UdpServer {
	
	static int[][] arrAvioane = { 
			{0,0,1,0,0,0,0,0,0,0},
			{0,1,1,1,0,0,0,0,0,0},
			{0,0,1,0,0,0,1,0,1,0},
			{0,1,1,1,0,1,1,1,1,0},
			{0,0,0,0,0,0,1,0,1,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{1,0,1,0,0,0,0,0,0,0},
			{1,1,1,1,0,0,0,0,0,0},
			{1,0,1,0,0,0,0,0,0,0}
		};

		static int[][] arrVarfAvioane = {
			{0,2},
			{3,5},
			{8,3}
		};

  private static final java.text.DateFormat DF = java.text.DateFormat.getDateTimeInstance();

  private static String getTime() {
    return DF.format(new java.util.Date());
  }
  
  
  public static void main(String[] args) {
	  System.out.println("Server Start ..");
	  System.out.println("Am incarcat matricea de joc si astept date de la clienti");
	  
  	try {
      DatagramSocket ds = new DatagramSocket(55000);
      DatagramPacket dp = new DatagramPacket(new byte[120], 120);
      String resp;
      
      // afisez matricea de joc pe consola?
      
      while (true) {
        ds.receive(dp);
        String strPrimit = new String(dp.getData()).trim();
        // string are forma 00, .., 12, 13, ..,99, xy
        System.out.println("Am primit de la client " + "   " + strPrimit + " pe portul " + dp.getPort());
        // extrag iLinie (x) si iColoana (y) ca numere din strPrimit="75"
        int nr 			= Integer.parseInt(strPrimit); 
        int iLinie 		= nr/10; // 75 / 10 = 7
        int iColoana 	= nr%10; // 75 % 10 = 5
        System.out.println("Atacatorul tinteste pozitia (" + iLinie + "," + iColoana + ")" );
        
        int raspunsLovitura = 0; // nu a lovit niciun avion
        for(int i=0;i<3;i++){
        	if(arrVarfAvioane[i][0] == iLinie && arrVarfAvioane[i][1] == iColoana){
        		raspunsLovitura = 2; // avion lovit in varf
        	}
        }
        
        //  daca matricea de joc contine 1 pe pozitia (x,y) raspuns este 1 -> lovit
        if (arrAvioane[iLinie][iColoana]==1 && raspunsLovitura != 2) {
        	raspunsLovitura = 1;
        }
        
        resp = " " + raspunsLovitura;
       
        System.out.println("Timp trimitere: " +  getTime() + " raspuns:" + resp); 
        
        dp.setData(resp.getBytes());
        ds.send(dp);        
      }

      
    } catch (SocketException e) { e.printStackTrace();}
	  catch (IOException e) { e.printStackTrace();}
  }
}
