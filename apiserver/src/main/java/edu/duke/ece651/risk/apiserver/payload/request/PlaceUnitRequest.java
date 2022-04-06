package edu.duke.ece651.risk.apiserver.payload.request;

import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.Map;

public class PlaceUnitRequest {
    private Long roomID;
    private Map<String, Integer> unitPlaceOrders;

    /**
     * get room id
     * @return room id
     */
    public Long getRoomID() {
        return roomID;
    }

    /**
     * get unit place orders
     * @return unit place orders
     */
    public Map<String, Integer> getUnitPlaceOrders() {
        return unitPlaceOrders;
    }
}
