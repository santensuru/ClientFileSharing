/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas05_client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author user
 */
public class Tugas05_client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //while(true) {
            Socket sock = new Socket("127.0.0.1", 6060);
            byte[] mybytearray = new byte[1024];
            InputStream is = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream("s.pdf");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead;
            do {
                bytesRead = is.read(mybytearray, 0, mybytearray.length);
                System.out.println(bytesRead);
                bos.write(mybytearray, 0, bytesRead);
            } while(bytesRead == 1024);
            //bos.close();
            //sock.close();
        //}
    }
    
}