/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas05_client;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
 * version 0.0.2e beta
 */
public class Tugas05_client {
    // Path File can modified 
    private final static String path_src = "C:\\Users\\FUJITSU\\Documents\\NetBeansProjects\\filesharing";
    private final static String path_dst = "C:\\Users\\FUJITSU\\Documents\\NetBeansProjects\\filesharing";
    private static Socket sock;
    private static InputStream is;
    private static OutputStream os;
    private static BufferedOutputStream bos;
    private static String terima = "";
    private static String pesan = "";
    private final static DecimalFormat df = new DecimalFormat("0.000");
    private static int x = 0;

    
    private static long flag = 0;
    private static long l;
    private volatile static int bytesReads = 0;
    
    private static int count = 0;
    private final static int time = 2;
    //private static DesktopDemo DeskDem;

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
            //DeskDem = new DesktopDemo();
//            System.out.println(System.currentTimeMillis() + " " + String.valueOf(Timestamp.valueOf(LocalDateTime.now())).replace(" ", "_").replace(".", ",").replace(":", "."));
//            System.out.println(String.valueOf(Date.valueOf(LocalDate.now())) + "_" + (String.valueOf(Time.valueOf(LocalTime.now()))).replace(":", "."));
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
        catch (IOException | InterruptedException ex) {
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
    
    private static void read() throws IOException, InterruptedException {
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
   
    /*public static class DesktopDemo extends JFrame implements ActionListener {
        
        private JButton open;
        private JButton edit;
        
        private Desktop desktop;
        
        private String path = "C:\\Users\\asus\\Documents\\Data";
        
        public DesktopDemo(){
            desktop = Desktop.getDesktop();
            open = new JButton("Open");
            edit = new JButton("Edit");
            open.addActionListener(this);
            edit.addActionListener(this);
            JPanel p = new JPanel();
            p.add(open);
            p.add(edit);
            add(p);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
                         
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
             Object source = e.getSource();
             if (source == open)
             {
                 try{
                     desktop.open(new File(path));
                 }
                 catch(IOException z){
                     System.out.println(z.getMessage());
                 }
             }
             else if (source == edit)
             {
                 try{
                     desktop.edit(new File(path));
                 }
                 catch(IOException z){
                     System.out.println(z.getMessage());
                 }
             }
        }
        
    }*/
    
    private static void readKey() throws IOException, InterruptedException {
        int buf;
        byte[] mybytearray = new byte[16384]; //16384
        while (System.in.available() > 0) {
            buf = System.in.read();
            
            terima = terima.concat(String.valueOf((char) buf));
            if (terima.contains("\r\n") || terima.contains("\n")) {
//                System.out.println(terima);
                if (!terima.contains("\r\n")) {
                    terima = terima.replace("\n", "\r\n");
                }
                if (terima.contains("take")) {
                    File myFile;
                    l = 0;
                    String name = terima.replace("take ", "").replace("\r\n", "");
                    myFile = new File(path_src, name);
                    if ((l = myFile.length()) > 0) {
                        bos.write(terima.getBytes());
                        bos.flush();
                        
                        terima = String.valueOf(l) + "\r\n";
                        System.out.println("file size: " + convert(l).replace("/s", ""));
                        bos.write(terima.getBytes());
                        bos.flush();
                    }
                    
                    try (BufferedInputStream fbis = new BufferedInputStream(new FileInputStream(myFile))) {
                        Thread task = new progressBar();
                        task.start();
                        int bytesRead;
                        do {
                            bytesRead = fbis.read(mybytearray, 0, 16384);
                            flag += bytesRead;
                            bytesReads += bytesRead;
//                            System.out.println(df.format(flag*100.0/l) + "% " + df.format(bytesRead/1024.0) + " KB/s");
                            bos.write(mybytearray, 0, bytesRead);
                        } while(!String.valueOf(flag).equals(String.valueOf(l)));
                        bos.flush();
                        task.join();
                        progressBarLast();
                        flag = 0;
                        System.out.println("\r\ntotal time: " + etaConvert(count/time));
                        count = 0;
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
    
    private static void readFile(String nameFile) throws IOException, InterruptedException {
        String name = nameFile.replace("\r\n", "");
        String current;
        String temp = "";
        int buf;
        do{
            buf = is.read();
            temp = temp.concat(String.valueOf((char) buf));
        } while (!temp.contains("\r\n"));
        temp = temp.replace("\r\n", "");
        l = Integer.parseInt(temp);
        System.out.println("file size: " + convert(l).replace("/s", ""));
//        System.out.println(temp);
        byte[] mybytearray = new byte[16384]; // 1024
//        System.out.println(name);
        File file;
        
        // if file doesnt exists, then create it
        do {
            current = String.valueOf(Timestamp.valueOf(LocalDateTime.now())).replace(" ", "_").replace(".", ",").replace(":", ".");
            file = new File(path_dst, current + "_" + name);
        } while (file.exists());
        file.createNewFile();
        System.out.println("save as: " + current + "_" + name);
        
        FileOutputStream fos = new FileOutputStream(file, false);
        
        try (BufferedOutputStream fbos = new BufferedOutputStream(fos)) {
            Thread task = new progressBar();
            task.start();
            int bytesRead;
            do {
                bytesRead = is.read(mybytearray, 0, 16384);
                flag += bytesRead;
                bytesReads += bytesRead;
//                System.out.println(df.format(flag*100.0/t_l) + "% " + df.format(bytesRead/1024.0) + " KB/s");
//                System.out.println(bytesRead + " " + flag + "/" + temp);
                fbos.write(mybytearray, 0, bytesRead);
            } while(!String.valueOf(flag).equals(temp) );
            task.join();
            progressBarLast();
            flag = 0;
            System.out.println("\r\ntotal time: " + etaConvert(count/time));
            count = 0;
        }
    }
    
    private static class progressBar extends Thread {
        
        progressBar() {
        }
        
        @Override
        public void run() {
            do {
                progressBarLast();
                try {
                    Thread.sleep(1000/time);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tugas05_client.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while(flag != l);
        }
    }
    
    private static void progressBarLast() {
        char[] posisi = {'/', '-', '\\', '|'};
        
        bytesReads *= time;
        String speed = convert(bytesReads);
                
        String temp_str = "";
        String persen = df.format(flag*100.0/l) + "% ";
        temp_str = temp_str.concat("\r[");
        
//        if (bytesReads == 0) bytesReads = 1;
        String eta = " eta " + etaConvert((l - flag)/(bytesReads + 1));
        
        int i;
        for (i=0; i<flag*100/(l*2)-1; i++) {
            temp_str = temp_str.concat("=");
        }
        temp_str = temp_str.concat(">");
        i++;
        for (; i<50; i++) {
            temp_str = temp_str.concat(" ");
        }
        temp_str = temp_str.concat("] " + posisi[x] + " " + persen + speed + eta);
        System.out.print(temp_str);
        System.out.flush();
        x++;
        x %= 4;
        bytesReads = 0;
        
        count++;
    }
    
    private static String convert(long l) {
        String speed;
        if (l >= 1073741824) {
            speed = df.format(l/1073741824.0) + " GB/s";
        }
        else if (l >= 1048576) {
            speed = df.format(l/1048576.0) + " MB/s";
        }
        else if (l >= 1024) {
            speed = df.format(l/1024.0) + " KB/s";
        }
        else {
            speed = df.format(l) + " B/s";
        }
        return speed;
    }
    
    private static String etaConvert(long l_d) {
        String eta = "";
        if (l_d >= 86400) {
            eta += String.valueOf(l_d/86400) + " d ";
            l_d %= 86400;
        }
        if (l_d >= 3600) {
            eta += String.valueOf(l_d/3600) + " h ";
            l_d %= 3600;
        }
        if (l_d >= 60) {
            eta += String.valueOf(l_d/60) + " m ";
            l_d %= 60;
        }
        eta += String.valueOf(l_d) + " s";
        return eta;
    }
    
}
