/**
 * Branch class is used to store the employees of a branch and their information. Also it contains methods to perform operations on employees.
 * 
 * @author Mehmet Ali Özdemir
 * @since 23.11.2023
 */

import java.util.LinkedList;
import java.util.Queue;
import java.io.FileWriter;
import java.io.IOException;

public class Branch {
    private HashTable<String, Employee> employees;
    private Queue<String> cooksQueue;
    private FileWriter writer;
    private String district;
    private String manager;
    private String cashierToBePromoted;
    private String cashierToBeDismissed;
    private String cookToBeDismissed;
    private String courierToBeDismissed;
    private int monthlyBonus;
    private int totalBonus;
    private int numOfCooks, numOfCashiers, numOfCouriers;
    
    
    /**
     * Constructor for Branch class.
     * 
     * @param city City of the branch.
     * @param district District of the branch.
     * @param writer Writer object to write the output to the file.
     */
    public Branch(String city, String district, FileWriter writer) {
        this.employees = new HashTable<>();
        this.cooksQueue = new LinkedList<>();
        this.writer = writer;
        this.district = district;
        this.manager = null;
        this.cashierToBePromoted = null;
        this.cashierToBeDismissed = null;
        this.cookToBeDismissed = null;
        this.courierToBeDismissed = null;
        this.monthlyBonus = 0;
        this.totalBonus = 0;
        this.numOfCooks = 0;
        this.numOfCashiers = 0;
        this.numOfCouriers = 0;
    }

    /**
     * This methods adds a new employee to the branch. If the employee aldready exists, it prints an error message.
     * If the employee is a courier and there is a cashier to be dismissed, it adds the cashier and dismisses the courier to be dismissed.
     * If the employee is a cashier and there is a cashier to be promoted, it adds the cashier and promotes the cashier to be promoted and if there is a cashier to be dismissed, it dismisses the cashier to be dismissed.
     * If the employee is a cook and there is a manager to be dismissed, it adds the cook and dismisses the manager to be dismissed and if there is a cook to be dismissed, it dismisses the cook to be dismissed.
     * If the employee is a manager it adds the manager.
     * 
     * @param name Name of the employee.
     * @param position Position of the employee.
     * @throws IOException
     */
    public void add(String name, String position) throws IOException {
        Employee newEmployee = new Employee(name, position);
        
        if (employees.contains(name)) {
            writer.write("Existing employee cannot be added again.\n");
            return;
        }

        if (position.equals("COURIER")) {
            employees.insert(name, newEmployee);
            numOfCouriers++;

            if (courierToBeDismissed != null && numOfCouriers != 1) {
                if (employees.get(courierToBeDismissed).getPromotionPoints() <= -5)
                    dismissal(courierToBeDismissed);
            }
            courierToBeDismissed = null;
        }

        else if (position.equals("CASHIER")) {
            employees.insert(name, newEmployee);
            numOfCashiers++;

            if (cashierToBePromoted != null && numOfCashiers != 1) {
                if (employees.get(cashierToBePromoted).getPromotionPoints() >= 3)
                    promotionCheck(cashierToBePromoted);
            }
            cashierToBePromoted = null;
            
            if (cashierToBeDismissed != null && numOfCashiers != 1) {
                if (employees.get(cashierToBeDismissed).getPromotionPoints() <= -5)
                    dismissal(cashierToBeDismissed);
            }
            cashierToBeDismissed = null;
        }

        else if (position.equals("COOK")) {
            employees.insert(name, newEmployee);
            numOfCooks++;

            if (cooksQueue.size() > 0 && employees.get(manager).getPromotionPoints() <= -5)
                dismissal(manager);

            if (cookToBeDismissed != null && numOfCashiers != 1) {
                if (employees.get(cookToBeDismissed).getPromotionPoints() <= -5)
                    dismissal(cookToBeDismissed);
            }
            cookToBeDismissed = null;
        }

        else if (position.equals("MANAGER")) {
            employees.insert(name, newEmployee);
            manager = name;
        }
    }


    /**
     * This method removes an employee from the branch. If the employee does not exist, it prints an error message.
     * If the employee cannot leave the branch, then he/she gains $200 bonus.
     * If the manager leaves the branch, the cook to be manager becomes the manager.
     * 
     * @param name Name of the employee.
     * @throws IOException
     */
    public void leave(String name) throws IOException {
        if (!employees.contains(name)) {
            logNoSuchEmployee();
            return;
        }

        String position = employees.get(name).getPosition();

        if (position.equals("MANAGER") && cooksQueue.size() > 0 && numOfCooks > 1) {
            String cookToBeManager = cooksQueue.poll();
            employees.remove(manager);
            employees.get(cookToBeManager).updatePromotionPoints(-10);
            employees.get(cookToBeManager).setPosition("MANAGER");
            manager = cookToBeManager;
            numOfCooks--;
            logLeave(name);
            logPromote(manager, "Cook", "Manager");
        }

        else if (position.equals("COOK") && numOfCooks > 1) {
            employees.remove(name);
            cooksQueue.remove(name);
            numOfCooks--;
            logLeave(name);
        }

        else if (position.equals("CASHIER") && numOfCashiers > 1) {
            employees.remove(name);
            numOfCashiers--;
            logLeave(name);
        }

        else if (position.equals("COURIER") && numOfCouriers > 1) {
            employees.remove(name);
            numOfCouriers--;
            logLeave(name);
        }

        else if (employees.get(name).getPromotionPoints() > -5) {
            totalBonus += 200;
            monthlyBonus += 200;
        }
    }


    /**
     * This method dismisses an employee from the branch.
     * If manager is dismissed, the cook to be manager becomes the manager.
     * If other employees cannot be dismissed, they will be employee to be dismissed.
     * 
     * @param name Name of the employee.
     * @throws IOException
     */
    private void dismissal(String name) throws IOException {
        String position = employees.get(name).getPosition();

        if (position.equals("MANAGER") && cooksQueue.size() > 0 && numOfCooks > 1) {
            String cookToBeManager = cooksQueue.poll();
            employees.remove(manager);
            employees.get(cookToBeManager).updatePromotionPoints(-10);
            employees.get(cookToBeManager).setPosition("MANAGER");
            manager = cookToBeManager;
            numOfCooks--;
            logDismiss(name);
            logPromote(manager, "Cook", "Manager");
        }

        else if (position.equals("COOK")) {
            if (numOfCooks > 1) {
                employees.remove(name);
                numOfCooks--;
                logDismiss(name);
            }
            else
                cookToBeDismissed = name;
        }

        else if (position.equals("CASHIER")) {
            if (numOfCashiers > 1) {
                employees.remove(name);
                numOfCashiers--;
                logDismiss(name);
            }
            else
                cashierToBeDismissed = name;
        }

        else if (position.equals("COURIER")) {
            if (numOfCouriers > 1) {
                employees.remove(name);
                numOfCouriers--;
                logDismiss(name);
            }
            else
                courierToBeDismissed = name;
        }
    }

    
    /**
     * This method updates the performance of an employee. It updates the employee's promotion points and adds the bonus to the monthly bonus and total bonus.
     * If the employee does not exist, it prints an error message.
     * If the employee's promotion points are greater than or equal to 3, he/she is checked whether he/she can be promoted or not.
     * If the employee's promotion points are less than or equal to -5, he/she is checked whether he/she can be dismissed or not.
     * 
     * @param name Name of the employee.
     * @param score Monthly score of the employee.
     * @throws IOException
     */
    public void performanceUpdate(String name, int score) throws IOException {
        if (!employees.contains(name)) {
            logNoSuchEmployee();
            return;
        }
    
        int promotionPoints = score / 200;
        int bonus = score > 0 ? score % 200:0;

        employees.get(name).updatePromotionPoints(promotionPoints);
        monthlyBonus += bonus;
        totalBonus += bonus;

        if (employees.get(name).getPromotionPoints() >= 3)
            promotionCheck(name);

        if (employees.get(name).getPromotionPoints() <= -5)
            dismissal(name);
    }
    

    /**
     * This method checks whether an employee can be promoted or not.
     * If the employee is a cashier and cannot be promoted, he/she will be cashier to be promoted.
     * If the employee is a cook and cannot be promoted, he/she will be added to the cooks queue and manager's promotion points are checked to dismiss the manager or not.
     * If the employee is a cook and his/her promotion points are less than 10 and he/she is in the cooks queue, he/she will be removed from the cooks queue.
     * 
     * @param name Name of the employee.
     * @throws IOException
     */
    private void promotionCheck(String name) throws IOException {
        int promotionPoints = employees.get(name).getPromotionPoints();

        if (employees.get(name).getPosition().equals("CASHIER")) {
            if (numOfCashiers != 1) {
                employees.get(name).setPosition("COOK");
                employees.get(name).updatePromotionPoints(-3);
                numOfCooks++;
                numOfCashiers--;
                logPromote(name, "Cashier", "Cook");
            }
            else
                cashierToBePromoted = name;
        }

        if (employees.get(name).getPosition().equals("COOK")) {
            if (promotionPoints >= 10 && !cooksQueue.contains(name)) {
                cooksQueue.offer(name);

                if (employees.get(manager).getPromotionPoints() <= -5)
                    dismissal(manager);
            }

            else if (promotionPoints < 10 && cooksQueue.contains(name))
                cooksQueue.remove(name);
        }
    }

    public void setMonthlyBonus() {
        this.monthlyBonus = 0;
    }

    public void printMonthlyBonuses() throws IOException {
        writer.write(String.format("Total bonuses for the %s branch this month are: %d\n", district, monthlyBonus));
    }
    
    public void printOverallBonuses() throws IOException {
        writer.write(String.format("Total bonuses for the %s branch are: %d\n", district, totalBonus));
    }

    public void printManager() throws IOException {
        writer.write(String.format("Manager of the %s branch is %s.\n", district, manager));
    }

    private void logLeave(String name) throws IOException {
        writer.write(String.format("%s is leaving from branch: %s.\n", name, district));
    }
    private void logDismiss(String name) throws IOException {
        writer.write(String.format("%s is dismissed from branch: %s.\n", name, district));
    }

    private void logPromote(String name, String initialPosition, String newPosition) throws IOException {
        writer.write(String.format("%s is promoted from %s to %s.\n", name, initialPosition, newPosition));
    }

    private void logNoSuchEmployee() throws IOException {
        writer.write("There is no such employee.\n");
    }
}