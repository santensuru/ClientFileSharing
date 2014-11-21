/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas05_client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;

/**
 *
 * @author
 * 1. Djuned Fernando Djusdek   5112100071
 * 2. M. Arief Ridwan           5112100097
 * 3. I Gede Arya Putra Perdana 5112100151
 * 
 * https://github.com/santensuru/ClientFileSharing
 * email: djuned.ong@gmail.com
 * 
 * version 0.0.1h beta
 */
public class Tugas05_client {
    // Path File can modified 
    private final static String path_src = "C:\\cygwin64\\home\\user\\coba\\FTP\\lala\\haha\\coba buat\\haha-coba\\1-coba";
    private final static String path_dst = "C:\\cygwin64\\home\\user\\coba\\FTP\\lala\\haha\\coba buat\\haha-coba\\2-coba";
    private static Socket sock;
    private static InputStream is;
    private static OutputStream os;
    private static BufferedOutputStream bos;
    static String terima = "";
    static String pesan = "";
    static DecimalFormat df = new DecimalFormat("0.000");
    static int x = 0;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        try {
            sock = new Socket(args[0], 6060);
            is = sock.getInputStream();
            os = sock.getOutputStream();
            bos = new BufferedOutputStream(os);
            while (true) {
                read();
                if (!pesan.isEmpty()) {
                    System.out.print(pesan);
                    if (pesan.contains("bye")) {
                        break;
                    }
                    pesan = "";
                }
                //System.out.println("READ");
                readKey();
                //System.out.println("WRITE");
            }
        }
        catch (Exception ex) {
            System.err.println("Error: " + ex.toString());
        }
        //while(true) {
//            Socket sock = new Socket("127.0.0.1", 6060);
//            byte[] mybytearray = new byte[1024];
//            InputStream is = sock.getInputStream();
//            FileOutputStream fos = new FileOutputStream("s.pdf");
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            int bytesRead;
//            do {
//                bytesRead = is.read(mybytearray, 0, mybytearray.length);
//                System.out.println(bytesRead);
//                bos.write(mybytearray, 0, bytesRead);
//            } while(bytesRead == 1024);
            //bos.close();
            //sock.close();
        //}
    }
    
    private static void read() throws IOException {
        int buf;
        while (is.available() > 0) {
            buf = is.read();
            pesan = pesan.concat(String.valueOf((char) buf));
            //System.out.println((char) buf);
            if (pesan.contains("\r\n") || pesan.contains("\n")) {
                if (pesan.contains("file")) {
                    System.out.print(pesan);
                    readFile(pesan.replace("file ", ""));
                    pesan = "";
                }
            }
        }
    }
    
    private static void readKey() throws IOException {
        int buf;
        byte[] mybytearray = new byte[16384];
        while (System.in.available() > 0) {
            buf = System.in.read();
            
            terima = terima.concat(String.valueOf((char) buf));
            if (terima.contains("\r\n") || terima.contains("\n")) {
//                System.out.println(terima);
                if (!terima.contains("\r\n")) {
                    terima = terima.replace("\n", "\r\n");
                }
                if (terima.contains("take")) {
                    File myFile = null;
                    long l = 0;
                    String name = terima.replace("take ", "").replace("\r\n", "");
                    myFile = new File(path_src, name);
                    if ((l = myFile.length()) > 0) {
                        bos.write(terima.getBytes());
                        bos.flush();
                        
                        terima = String.valueOf(l) + "\r\n";
                        System.out.print("file size: " + terima);
                        bos.write(terima.getBytes());
                        bos.flush();
                    }
                    
                    long flag = 0;
                    try (BufferedInputStream fbis = new BufferedInputStream(new FileInputStream(myFile))) {
                        int bytesRead;
                        do {
                            bytesRead = fbis.read(mybytearray, 0, 16384);
                            flag += bytesRead;
                            progressBar(flag, l, bytesRead);
//                            System.out.println(df.format(flag*100.0/l) + "% " + df.format(bytesRead/1024.0) + " KB/s");
                            bos.write(mybytearray, 0, bytesRead);
                        } while(bytesRead == 16384 || !String.valueOf(flag).equals(String.valueOf(l)));
                        bos.flush();
                    }
                    catch(IOException e) {
                        System.out.println("3 file not found, please try again (You cannot cancel it.)");
                    }
                }
                else {
                    bos.write(terima.getBytes());
                    bos.flush();
                }
                terima = "";
            }
        }
    }
    
    private static void readFile(String nameFile) throws IOException {
        String name = nameFile.replace("\r\n", "");
        String temp = "";
        int buf;
        do{
            buf = is.read();
            temp = temp.concat(String.valueOf((char) buf));
        } while (!temp.contains("\r\n"));
        System.out.print("file size: " + temp);
        temp = temp.replace("\r\n", "");
        long t_l = Integer.parseInt(temp);
//        System.out.println(temp);
        byte[] mybytearray = new byte[1024];
////        System.out.println(name);
        File file = new File(path_dst, name);
        // if file doesnt exists, then create it
        if (!file.exists()) {
                file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file, false);
        
        long flag = 0;
        try (BufferedOutputStream fbos = new BufferedOutputStream(fos)) {
            int bytesRead;
            do {
                bytesRead = is.read(mybytearray, 0, 1024);
                flag += bytesRead;
                progressBar(flag, t_l, bytesRead);
//                System.out.println(df.format(flag*100.0/t_l) + "% " + df.format(bytesRead/1024.0) + " KB/s");
//                System.out.println(bytesRead + " " + flag + "/" + temp);
                fbos.write(mybytearray, 0, bytesRead);
            } while(bytesRead == 1024 || !String.valueOf(flag).equals(temp) );
        }
    }
    
    private static void progressBar(long flag, long l, long bytesRead) {
        char[] posisi = {'/', '-', '\\', '|'};
        String temp_str = "";
        String persen = df.format(flag*100.0/l) + "% ";
        String speed = df.format(bytesRead/1024.0) + " KB/s";
        temp_str = temp_str.concat("[");
        int i;
        for (i=0; i<flag*100/(l*5)-1; i++) {
            temp_str = temp_str.concat("=");
        }
        temp_str = temp_str.concat(">");
        int j = i++;
        for (i=i; i<20; i++) {
            temp_str = temp_str.concat(" ");
        }
        temp_str = temp_str.concat("] " + posisi[x%4] + " " + persen + speed + "\r\n");
        System.out.print(temp_str);
        System.out.flush();
        x++;
        x %= 100;
    }
    
}
