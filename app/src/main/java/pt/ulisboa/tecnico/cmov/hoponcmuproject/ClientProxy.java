package pt.ulisboa.tecnico.cmov.hoponcmuproject;

import android.os.AsyncTask;

import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import static android.widget.Toast.LENGTH_SHORT;

public class ClientProxy implements Runnable {


    private static int S_PORT = 9999;
    private static String S_IP = "10.0.2.2";
    private UserRequest _userRequest;
    private NetworkMsg _networkMsg;
    private JSONObject _message;
    private Handler _handler;


    public ClientProxy(UserRequest userRequest, Handler handler, NetworkMsg networkMsg, JSONObject message) {
        super();
        _userRequest = userRequest;
        _networkMsg = networkMsg;
        _handler = handler;
        _message = message;
    }

    @Override
    public void run() {

        try {
            //Send Request to server
            byte[] request = _message.toString().getBytes();

            Socket socket = new Socket(S_IP, S_PORT);

            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            oos.writeInt(request.length);
            oos.write(request);


            //Receive Reply from server
            DataInputStream ois = new DataInputStream(socket.getInputStream());
            int lengh = ois.readInt();
            byte[] message = new byte[lengh];
            ois.readFully(message, 0, lengh);


            JSONObject reply = new JSONObject(new String(message));
            System.out.println("Received: " + reply);

            int replyType;
            replyType= reply.getInt(NetworkKey.REPLY_TYPE.toString());
            ServerReply serverReply = ServerReply.values()[replyType];

            _handler.obtainMessage(serverReply.ordinal(), reply).sendToTarget();


            oos.close();
            ois.close();

        } catch(UnknownHostException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}




