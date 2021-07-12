import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class Chat extends Thread {

    private MulticastSocket socket;

    private InetAddress group;

    private int port;

    private final JTextArea chatBox;

    private final String user;

    //address = "250.250.20.1:5842"
    public Chat(String address, JTextArea box, String username)throws IOException{
        super();
        String[] params = address.split(":");
        String multicastGroupAddress = params[0];
        int port = Integer.parseInt(params[1]);
        chatBox=box;
        user=username;
        this.port=port;
        socket = new MulticastSocket(port);
        group = InetAddress.getByName(multicastGroupAddress);
        socket.joinGroup(group);
    }

    /**
     * metodo eseguito da un thread, usato per la chat multicast
     */
    public void run() {
        String welcome="Si è unito alla chat del progetto";
        try{
            sendMessage(welcome); //send welcome message
        }catch (IOException e){
            chatBox.append("errore nella chat multicast\n");
            interrupt();
        }
        while(!Thread.interrupted()){
            try{
                String msg = readMessage();
                chatBox.append(msg+"\n");
            }
            catch (SocketException e){
                break;
            }
            catch (IOException e){
                e.printStackTrace();
                chatBox.append("Errore nella chat multicast\n");
                close();
            }
            //set Caret position to the  bottom of the JTextArea
            chatBox.setCaretPosition(chatBox.getText().length());
        }

    }

    /**
     * metodo usato per inviare un messaggio nella chat
     * @param msg
     * @throws IOException
     */
   public void sendMessage(String msg) throws IOException{
        msg = this.user+": "+msg;
        byte[] buffer = msg.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,group,port);
        socket.send(packet);
   }

    /**
     * metodo usato per leggere un messaggio arrivato alla chat
     * @return
     * @throws IOException
     */
   public String readMessage() throws IOException{
        byte[] buffer = new byte[socket.getReceiveBufferSize()];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String msg = new String(packet.getData(),packet.getOffset(),packet.getLength());
        return msg;
   }

    /**
     * metodo usato per uscire dalla chat multicast
     */
    public void close(){
        this.interrupt();
        try{
            socket.leaveGroup(group);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        socket.close();
    }
}