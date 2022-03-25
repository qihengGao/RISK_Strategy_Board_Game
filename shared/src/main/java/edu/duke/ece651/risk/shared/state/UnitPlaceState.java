package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.*;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class UnitPlaceState extends State {

    @Override
    /**
     * Do placement action
     */
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
        contex.getOut().println(mapTextView.displayMap());
        contex.getOut().println("You are: " + contex.getClientColor().getColorName());

        // intial amount limit is 30
        // would the Unit type be more in the future??
        // for (String type : contex.getTypes())
        int amountRest = 30;
        Iterable<Territory> allTerritories = contex.getRiskMap().getTerritoriesByOwnerID(contex.getPlayerID());
        while (amountRest > 0) {
            for(Territory territory: allTerritories){
                amountRest = placeUnitOnTerritory("Soldier", territory, amountRest, contex.getOut(),
                        contex.getBufferedReader());
            }
        }

        //send back to server
        ArrayList<Territory> toSend = new ArrayList<>();
        for (Territory territory: allTerritories){
            toSend.add(territory);
        }
        contex.getOos().writeObject(toSend);

        //wait server for next state
        contex.setGameState(new WaitingState());
        contex.getGameState().doAction(contex);

    }

    private int placeUnitOnTerritory(String unitType, Territory territory, int amountRest, PrintStream out,
            BufferedReader userInput) throws IOException {
        
        if(amountRest == 0){
            out.println("You have no more available units to put in territory " + territory.getName() + 
                ", adding 0 units");
            territory.tryAddUnit(new BasicUnit(unitType, 0));
            return 0;
        }

        out.println("You have " + amountRest + " to put");
        out.println("How many " + unitType + "s you want to put in " + territory.getName());

        String amountToPutStr = userInput.readLine();
        int amountToPutInt;
        try {
            amountToPutInt = Integer.parseInt(amountToPutStr);
            if (amountToPutInt < 0){
                throw new IllegalArgumentException("Your input unit number is negative, please try again!");
            }
            if (amountToPutInt > amountRest) {
                throw new IllegalArgumentException("Amount to put must not exceed " + amountRest);
            }
        } catch (NumberFormatException e) {
            out.println(amountToPutStr + " is an Invalid input! Try again!");
            return placeUnitOnTerritory(unitType, territory, amountRest, out, userInput);
        } catch (IllegalArgumentException e) {
            int offset = e.toString().indexOf(":") + 2;
            out.println(e.toString().substring(offset));
            return placeUnitOnTerritory(unitType, territory, amountRest, out, userInput);
        }

        Unit unitToAdd = new BasicUnit(unitType, amountToPutInt);
        Unit unitInCurrTerritory = territory.getUnitByType(unitType);
        if (unitInCurrTerritory == null) {
            territory.tryAddUnit(unitToAdd);
        } else {
            unitInCurrTerritory.tryIncreaseAmount(unitToAdd.getAmount());
        }
        amountRest -= amountToPutInt;
        return amountRest;
    }
}