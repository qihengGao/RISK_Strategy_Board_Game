package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.*;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.AttackOrder;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class ReEnterOrderState extends State {
    private Order illegalOrder;

    public ReEnterOrderState(Order illegalOrder){
        this.illegalOrder = illegalOrder;
    }

    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        String check_message = illegalOrder.executeOrder(contex.getRiskMap());
        contex.println(check_message);
        contex.println("If you want to abandon this order, just press Enter");
        Order newOrder = readOrderFromUser(contex.getRiskMap(), contex.getBufferedReader(), contex.getOut(),
                contex.getPlayerID(), illegalOrder.getOrderType());

        contex.writeObject(newOrder);

        //wait server for next state
        contex.setGameState(new WaitingState());
        contex.getGameState().doAction(contex);
    }

    public Order readOrderFromUser(RISKMap riskMap, BufferedReader input, PrintStream output, Long ID, String chosenOrder) throws IOException {
        while (true) {
            String userInput = input.readLine();
            if (userInput.equals("")){
                return null;
            }
            try {
                String[] inputs = checkFormatAndSplit(userInput);
                int amountUnderOrder = readOrderUnitAmount(inputs);

                Order order = getOrder(ID, inputs, amountUnderOrder);
                if (chosenOrder.equals("Attack")) {
                    order = new AttackOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
                }
                String check_message = excuteOrder(riskMap,order);
                if (check_message != null) {
                    throw new IllegalArgumentException(check_message);
                }
                return order;
            } catch (IllegalArgumentException e) {
                int offset = e.toString().indexOf(":") + 2;
                output.println(e.toString().substring(offset));
            }
        }
    }

    protected MoveOrder getOrder(Long ID, String[] inputs, Integer amountUnderOrder) {
        return new MoveOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
    }

    protected String excuteOrder(RISKMap riskMap,Order order){
        return order.executeOrder(riskMap);
    }

    protected int readOrderUnitAmount (String[] inputs){
        int ans;
        try{
            ans = Math.abs(Integer.parseInt(inputs[3]));
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Unit Amount must be an integer!");
        }
        return ans;
    }

    protected String[] checkFormatAndSplit(String userInput) throws IllegalArgumentException{
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
