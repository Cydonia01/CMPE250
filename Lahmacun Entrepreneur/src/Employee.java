/**
 * This class represents an employee of the company.
 * 
 * @author Mehmet Ali Özdemir
 * @since 23.11.2023
 */
public class Employee {
    private String position;
    private int promotionPoints;

    public Employee(String name, String position) {
        this.position = position;
        this.promotionPoints = 0;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getPromotionPoints() {
        return promotionPoints;
    }

    public void updatePromotionPoints(int promotionPoints) {
        this.promotionPoints += promotionPoints;
    }
}