package edu.duke.ece651.risk.shared.territory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Owner implements Serializable {
    private long ownerId;
    private int currTechlevel;
    private int ownedFoodResource;
    private int ownedTechResource;

    private int maxAllowedTechLevel;
    private HashMap<Integer, Integer> costToUpgradeTech;

    private HashSet<Long> alliance;

    /**
     * default constructor, used to set all values
     * @param ownerId
     * @param currTechlevel
     * @param ownedFoodResource
     * @param ownedTechResource
     */
    public Owner(long ownerId, int currTechlevel, int ownedFoodResource, int ownedTechResource){
        if(currTechlevel < 1 || currTechlevel > 6){
            throw new IllegalArgumentException("current technological level must be between 1 and 6");
        }
        if(ownedFoodResource < 0){
            throw new IllegalArgumentException("cannot set food resource < 0");
        }
        if(ownedTechResource < 0) {
            throw new IllegalArgumentException("cannot set technological resource < 0");
        }

        this.ownerId = ownerId;
        this.currTechlevel = currTechlevel;
        this.ownedFoodResource = ownedFoodResource;
        this.ownedTechResource = ownedTechResource;
        this.maxAllowedTechLevel = 6;
        this.alliance = new HashSet<>();

        this.costToUpgradeTech = new HashMap<>();
        this.costToUpgradeTech.put(2, 50);
        this.costToUpgradeTech.put(3, 75);
        this.costToUpgradeTech.put(4, 125);
        this.costToUpgradeTech.put(5, 200);
        this.costToUpgradeTech.put(6, 300);
    }

    /**
     * initializer constructor, used to create an minimum owner
     * @param ownerId
     */
    public Owner(long ownerId){
        this(ownerId, 1, 0, 0);
    }

    public int getCurrTechlevel() {
        return currTechlevel;
    }

    public int getOwnedFoodResource() {
        return ownedFoodResource;
    }

    public int getOwnedTechResource() {
        return ownedTechResource;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public HashSet<Long> getAlliance() {
        return alliance;
    }

    public void setAlliance(HashSet<Long> alliance) {
        this.alliance = alliance;
    }

    /**
     * alliance former and breaker
     * @param id
     */
    public boolean formAlliance(Long id) {
        if (id.equals(ownerId) || this.alliance.contains(id)){
            return false;
        }
        this.alliance.add(id);
        return true;
    }

    public boolean breakAlliance(Long id) {
        if (id.equals(ownerId) || !this.alliance.contains(id)){
            return false;
        }
        this.alliance.remove(id);
        return true;
    }

    /**
     * try add or remove food resources
     * @param offset
     * @return
     */
    public String tryAddOrRemoveFoodResource(int offset) {
        if(this.ownedFoodResource + offset < 0){
            return "You do not have sufficient food resources";
        }
        this.ownedFoodResource += offset;
        return null;
    }

    /**
     * y add or remove tech resources
     * @param offset
     * @return
     */
    public String tryAddOrRemoveTechResource(int offset) {
        if(this.ownedTechResource + offset < 0){
            return "You do not have sufficient tech resources";
        }
        this.ownedTechResource += offset;
        return null;
    }

    /**
     * attempt to update tech
     * @return null on success, otherwise the error message
     */
    public String tryUpgradeTechLevel(){
        // fail when already at max leve
        if(this.currTechlevel == this.maxAllowedTechLevel){
            return "you are already at maximum technology level, cannot upgrade";
        }
        // fail when does not have enough tech resources
        if(this.ownedTechResource < this.costToUpgradeTech.get(this.currTechlevel + 1)){
            return "you do not have enough technological resource to upgrade";
        }
        this.currTechlevel++;
        this.ownedTechResource -= this.costToUpgradeTech.get(currTechlevel);
        return null;
    }
}
