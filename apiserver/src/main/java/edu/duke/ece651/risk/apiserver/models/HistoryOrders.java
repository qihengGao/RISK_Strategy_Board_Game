package edu.duke.ece651.risk.apiserver.models;


import edu.duke.ece651.risk.shared.order.Order;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HistoryOrders")
public class HistoryOrders {
    Long roomID;
    Long roundNumber;
    Order order;

}
