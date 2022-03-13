package edu.duke.ece651.risk.shared;

import java.io.*;

public abstract class State implements Serializable {

    public abstract void doAction(ClientContext contex) throws IOException, ClassNotFoundException;

    /**
     * Read server port number from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Port number read from input source.
     * @throws IOException           if read an empty line or the input source in null.
     * @throws NumberFormatException if the line we read from input source is not an integer.
     */
    public static int readServerPort(String prompt, BufferedReader inputReader, PrintStream out) throws IOException, NumberFormatException {
        out.print(prompt + "\n");
        String s =inputReader.readLine();
        if (s == null) {
            throw new EOFException("Invalid input: Empty line\n");
        }
        return Integer.parseInt(s);

    }

    /**
     * Read server address from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Server address read from input source.
     * @throws IOException           if read an empty line or the input source in null.
     */
    public static String readServerAddress(String prompt, BufferedReader inputReader, PrintStream out) throws IOException {
        out.print(prompt + "\n");
        String s =inputReader.readLine();
        if (s == null) {
            throw new EOFException("Invalid input: Empty line\n");
        }
        return s;

    }

}
