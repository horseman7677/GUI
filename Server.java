import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Server extends JFrame {

    JLabel heading = new JLabel("Server");
    JTextArea msgArea = new JTextArea();
    JTextField msgInput = new JTextField();
    Font font = new Font("Roboto", Font.PLAIN, 20);

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Server() {

        try {
            createGUI();
            server = new ServerSocket(7777);
            System.out.println("Server is ready...searching connection...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    private void handleEvents() {
        msgInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String send = msgInput.getText();
                    msgArea.append("Server :\s"+send+"\n");
                    out.println(send);
                    out.flush();
                    msgInput.setText("");
                    msgInput.requestFocus(); 
                }
            }

        });
    }

    private void createGUI() {
        this.setTitle("Main-Server");
        this.setSize(800,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        msgArea.setFont(font);
        msgInput.setFont(font);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        this.add(msgArea,BorderLayout.CENTER);
        this.add(msgInput,BorderLayout.SOUTH);

        setVisible(true);

    }

    private void startReading() {
        
        Runnable r1 = ()-> {
            System.out.println("reader typing...");

            while(true){
                try {
                    String msg = br.readLine();
                
                if(msg.equals("exit")){
                    System.out.println("user offline");
                    break;
                }
                msgArea.append("Client :\s"+msg+"\n");
                } 
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
        };
        new Thread(r1).start();
    
    }

    // this is only for console based(multithreading)
    private void startWriting() {
        
        Runnable r2 = ()->{
            System.out.println("writer typing...");

            while (true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String send = br1.readLine();
                    out.println(send);
                    out.flush();    
                } 
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }; 
        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("Server is online...");
        new Server();
    }
}