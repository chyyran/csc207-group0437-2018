package kitchen;

import events.EventEmitter;
import events.newevents.BillPrintEvent;
import events.newevents.OrderChangedEvent;
import services.OrderManagerService;
import services.serialization.PaymentService;
import services.framework.*;
import restaurant.*;

import java.util.*;

public class Server extends Service {

    /**
     * Name of the Server.
     */
    private String name;

    /**
     * The table the Server is responsible for.
     */
    private Table table;

    /**
     * Handles the events.
     */
    private EventEmitter emitter;

    /**
     * Inventory of all ingredients of this restaurant.
     */
    private Inventory inventory;

    /**
     * Manages the orders.
     */
    private OrderManagerService orderManager;

    /**
     * Manages the payment.
     */
    private PaymentService paymentManager;

    /**
     * Class constructor specifying the emitter, printer, name, table, inventory, and manager.
     *
     * @param emitter        main event handler
     * @param name           name of the Server
     * @param table          the table number the Server is responsible for
     * @param inventory      inventory of all ingredients
     * @param orderManager   manager of the orders
     * @param paymentManager manager of the payment
     */
    @ServiceConstructor
    public Server(EventEmitter emitter, String name, Table table, Inventory inventory,
                  OrderManagerService orderManager, PaymentService paymentManager) {
        this.emitter = emitter;
        this.name = name;
        this.table = table;
        this.inventory = inventory;
        this.orderManager = orderManager;
        this.paymentManager = paymentManager;
        emitter.registerEventHandler(this::updateIngredient, OrderChangedEvent.class);
        emitter.registerEventHandler(this::rejectOrderItem, OrderChangedEvent.class);
        paymentManager.registerTable(table);
        serve();
        checkout();
    }

    /**
     * Updates the ingredient when an OrderCompleteEvent is filed.
     *
     * @param event the event called after an order is completed by the Chef
     */
    private void updateIngredient(OrderChangedEvent event) {
        if (event.getNewStatus() != OrderStatus.FILLED) return;
        Order order = orderManager.getOrder(event.getOrderNumber());
        if (order == null) return;

        MenuItem mi = order.getMenuItem();
        Map<Ingredient, Integer> ingredients = mi.getIngredients();
        for (Ingredient i : ingredients.keySet()) {
            int deduct = ingredients.get(i);
            this.inventory.removeFromInventory(i, deduct);
        }
    }

    /**
     * Rejects the order when an OrderRejectEvent is filed.
     *
     * @param event the event called after an order is rejected by the Chef
     */
    private void rejectOrderItem(OrderChangedEvent event) {
        if (event.getNewStatus() == OrderStatus.REJECTED) {
            Order order = orderManager.getOrder(event.getOrderNumber());
        }
    }

    public void addOrder(OrderChangedEvent event) {
        if (event.getNewStatus() == OrderStatus.CREATED) {
          paymentManager.registerOrder(table, orderManager.getOrder(event.getOrderNumber()));
        }

    }

    private void serve() {
        // Mark as Billable
        // TODO: add the price onto the bill
        emitter.registerEventHandler(e -> {
            if (e.getNewStatus() == OrderStatus.DELIVERED) {
                orderManager.notifyOrderStatusChanged(e.getOrderNumber(), OrderStatus.BILLABLE, this.name);
            }
        }, OrderChangedEvent.class);
    }

    private void checkout() {
        // Print the bill
        // todo: do this properly with the table?
        emitter.registerEventHandler(e -> {
            if (e.getTableNumber() == this.table.getTableNumber()) {
                logger.info(paymentManager.printBill(table));
            }
        }, BillPrintEvent.class);
    }
}
