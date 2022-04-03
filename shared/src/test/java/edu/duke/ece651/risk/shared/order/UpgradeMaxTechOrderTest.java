package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class UpgradeMaxTechOrderTest {
    @Test
    public void test_executeOrder(){
        UpgradeMaxTechOrder order = new UpgradeMaxTechOrder(0L, "Test1", "Test2", "Soldier", 10);

        HashSet<Territory> territories = new HashSet<>();
        Territory territory1 = new BasicTerritory("test1", 10, 100, 100);
        territory1.tryChangeOwnerTo(0L);
        territories.add(territory1);
        RISKMap riskMap = new RISKMap(territories);

    }
}
