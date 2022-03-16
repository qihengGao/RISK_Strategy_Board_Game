package edu.duke.ece651.risk.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;

public class ReEnterOrderState extends State{
    private Order illegalOrder;

    public ReEnterOrderState(Order illegalOrder){
        this.illegalOrder = illegalOrder;
    }

    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        String check_message = illegalOrder.executeOrder(contex.getRiskMap());
        contex.getOut().println(check_message);
        Order newOrder = readOrderFromUser(contex.getRiskMap(), contex.getBufferedReader(), contex.getOut(),
                contex.getPlayerID(), illegalOrder.getOrderType());

        contex.getOos().writeObject(newOrder);

        //wait server for next state
        contex.setGameState(new WaitingState());
        contex.getGameState().doAction(contex);
    }

    public Order readOrderFromUser(RISKMap riskMap, BufferedReader input, PrintStream output, long ID, String chosenOrder) throws IOException {
        while (true) {
            String userInput = input.readLine();
            if (userInput.equals("")){
                return null;
            }
            try {
                String[] inputs = checkFormatAndSplit(userInput);
                int amountUnderOrder = readOrderUnitAmount(inputs);

                Order order = new MoveOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
                if (chosenOrder.equals("Attack")) {
                    order = new AttackOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
                }
                String check_message = order.executeOrder(riskMap);
                if (check_message != null) {
                    throw new IllegalArgumentException(check_message);
                }
                return order;
            } catch (IllegalArgumentException e) {
                int offset = e.toString().indexOf(":") + 2;
                output.println(e.toString().substring(offset));
                continue;
            }
        }
    }

    private int readOrderUnitAmount (String[] inputs){
        int ans;
        try{
            ans = Integer.parseInt(inputs[3]);
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Unit Amount must be an integer!");
        }
        return ans;
    }

    private String[] checkFormatAndSplit(String userInput) throws IllegalArgumentException{
        String[] ans = userInput.split(",");
        if (ans.length != 4) {
            throw new IllegalArgumentException("Your input " + userInput + " is not following the format!");
        }
        return ans;
    }
    /**
     private void displayMapAndOptions(RISKMap riskMap, PrintStream output, long ID, TreeMap<Long, Color> idToColor) {
     MapTextView mapTextView = new MapTextView(riskMap, idToColor);
     output.println(mapTextView.displayMap());
     output.println("You are the " + idToColor.get(ID).getColorName() + " Player, what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
     }*/
}
