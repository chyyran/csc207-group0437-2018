package services;

import payment.Bill;
import restaurant.OrderItem;
import services.framework.Service;
import services.framework.ServiceConstructor;

import java.util.ArrayList;

/**
 * A service that prints bills according to specifications.
 */
public class BillPrinterService extends Service {

    /**
     * Constructs a BillPrinterService.
     */
    @ServiceConstructor
    public BillPrinterService() {
    }

    /**
     * Returns a string printing of a bill.
     * @param bill the bill to be printed.
     * @return a string representation of the specified bill.
     */
    public String printBill(Bill bill) {
        StringBuilder accumulator = new StringBuilder();
        // Format: ITEM:PRICE
        for (OrderItem orderItem : bill.getOrderItems()) {
            accumulator.append('\t');
            accumulator.append(orderItem.toString());
            accumulator.append(':');
            // TODO: Change to getPrice() - discount should always be applied
            accumulator.append(orderItem.getMenuItem().getOriginalPrice());
            accumulator.append(System.lineSeparator());
        }
        return accumulator.toString();
    }

    /**
     * Returns a string printing of a collection of bills.
     * @param bills the collection of bills to be printed.
     * @return a string representation of the specified bills.
     */
    public String printBills(ArrayList<Bill> bills) {
        StringBuilder accumulator = new StringBuilder();
        for (Bill bill : bills) {
            accumulator.append(printBill(bill));
        }

        return accumulator.toString();
    }

    /**
     * Returns a string printing of a collection of bills.
     * @param bills the collection of bills to be printed.
     * @return a string representation of the specified bills.
     */
    public String printBills(Bill[] bills) {
        StringBuilder accumulator = new StringBuilder();
        for (Bill bill : bills) {
            accumulator.append(printBill(bill));
        }

        return accumulator.toString();
    }

}
