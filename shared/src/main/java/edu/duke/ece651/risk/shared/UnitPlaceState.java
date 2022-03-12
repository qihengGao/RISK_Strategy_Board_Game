package edu.duke.ece651.risk.shared;

import java.io.IOException;

public class UnitPlaceState extends State {

    @Override
    /**
     * Do placement action
     */
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
        System.out.println(mapTextView.displayMapInit());
        System.out.println(mapTextView.displayMap());
        System.out.println("You are: " + contex.getClientColor().getColorName());

        // intial amount limit is 30
        // would the Unit type be more in the future??
        // for (String type : contex.getTypes())
        int amountRest = 30;
        for (Territory territory : contex.getRiskMap().getTerritoriesByOwnerID(contex.getPlayerID())) {
            amountRest = placeUnitOnTerritory("Soldier", territory, amountRest);
        }
    }

    private int placeUnitOnTerritory(String unitType, Territory territory, int amountRest) {
        int amountToPut = readIntInputFromPlayer();
        // TO DO: check
        Unit unitToAdd = new BasicUnit(unitType, amountToPut);
        territory.tryAddUnit(unitToAdd);
        amountRest -= amountToPut;
        return amountRest;
    }

    // TO DO
    private int readIntInputFromPlayer() {
        return 0;
    }
}
