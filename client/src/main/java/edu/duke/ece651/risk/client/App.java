/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risk.client;

import edu.duke.ece651.risk.shared.MapTextView;
import edu.duke.ece651.risk.shared.RISKMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class App {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        RISKMap riskMap = null;

        Socket socket1;
        int portNumber = 1777;


        socket1 = new Socket("localhost", portNumber);
        System.out.println("Connection to " + socket1 + "!");

        //Construct the ObjectOutputStream before the ObjectInputStream in at least one end.
        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket1.getInputStream());


        riskMap = (RISKMap) ois.readObject();
        System.out.println("Receiving map from server");

        MapTextView mapTextView = new MapTextView(riskMap);
        System.out.println(mapTextView.displayMapInit());

        System.out.println("Closing socket and terminating program.");
        ois.close();
        oos.close();
        socket1.close();

    }
}
