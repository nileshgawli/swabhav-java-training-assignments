package com.aurionpro.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.aurionpro.model.Cart;
import com.aurionpro.model.Customer;
import com.aurionpro.model.DeliveryPartner;
import com.aurionpro.model.FoodItem;
import com.aurionpro.model.LineItem;
import com.aurionpro.model.Order;
import com.aurionpro.util.SerializationUtil;

public class OrderService {
	private static OrderService instance;
	private static final String ORDERS_FILE = "orders.txt";

	private List<Order> allOrders;
	private int orderIdCounter;

	private AdminService adminService;
	private CustomerService customerService;
	private DeliveryPartnerService deliveryPartnerService;

	private OrderService() {
		allOrders = loadAllOrders();
		orderIdCounter = allOrders.stream().mapToInt(Order::getOrderId).max().orElse(0) + 1;
		adminService = AdminService.getInstance();
		customerService = CustomerService.getInstance();
		deliveryPartnerService = DeliveryPartnerService.getInstance();
	}

	public static OrderService getInstance() {
		if (instance == null) {
			instance = new OrderService();
		}
		return instance;
	}

	private List<Order> loadAllOrders() {
		List<Order> loadedOrders = SerializationUtil.deserialize(ORDERS_FILE);
		return loadedOrders != null ? loadedOrders : new ArrayList<>();
	}

	private void saveAllOrders() {
		SerializationUtil.serialize(allOrders, ORDERS_FILE);
	}

	// orderplacement
	public Order placeOrder(Customer customer, Cart cart, String paymentMode) {
		if (customer == null || cart == null || cart.isEmpty()) {
			System.out.println("Error: Customer or cart is invalid/empty. Cannot place order.");
			return null;
		}

		List<LineItem> cartItems = cart.getLineItems();
		if (cartItems.isEmpty()) {
			System.out.println("Error: Cart is empty. Cannot place order.");
			return null;
		}

		// assign delivery partner
		DeliveryPartner assignedPartner = deliveryPartnerService.assignRandomDeliveryPartner();
		if (assignedPartner == null) {
			System.out.println("Error: No delivery partners available at the moment. Please try again later.");
			return null;
		}

		// create order object
		int newOrderId = orderIdCounter++;
		Order newOrder = new Order(newOrderId, customer.getId(), cartItems, paymentMode, assignedPartner);

		//per order discount
		newOrder.calculateSubtotal();

		// overall discount
		double globalDiscountPercent = adminService.getGlobalDiscountPercent();
		double globalDiscountThreshold = adminService.getGlobalDiscountThreshold();
		newOrder.applyOverallDiscount(globalDiscountPercent, globalDiscountThreshold);

		customer.addOrderToHistory(newOrder);
		customerService.saveCustomer(customer);

		allOrders.add(newOrder);
		saveAllOrders(); 

		cart.clearCart();

		System.out.println("\nOrder placed successfully! Order ID: " + newOrder.getOrderId());
		generateInvoice(newOrder, customer);
		return newOrder;
	}

	public void generateInvoice(Order order, Customer customer) {
		System.out.println("\n-------------------------------------------------");
		System.out.println("                 INVOICE                         ");
		System.out.println("-------------------------------------------------");
		System.out.println("Customer Name: " + customer.getName());
		System.out.println("Customer ID  : " + customer.getId());
		System.out.println("Order ID     : " + order.getOrderId());
		System.out.println(
				"Order Date   : " + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.out.println("-------------------------------------------------");
		System.out.println("Items:");
		System.out.printf("%-25s %-5s %-10s %-10s\n", "Item Name", "Qty", "Price", "Subtotal");
		System.out.println("-------------------------------------------------");
		order.getItems().forEach(lineItem -> {
			FoodItem fi = lineItem.getFoodItem();
			System.out.printf("%-25s %-5d %-10.2f %-10.2f\n", fi.getName(), lineItem.getQuantity(),
					fi.calculateFinalPrice(), lineItem.calculateLineItemCost());
		});
		System.out.println("-------------------------------------------------");
		System.out.printf("Subtotal (before overall discount): ₹%.2f\n", order.getSubtotalAmount());
		if (order.getOverallDiscountApplied() > 0) {
			// Recalculate percentage for display if needed, or store it in Order model
			double discountPercentage = (order.getOverallDiscountApplied() / order.getSubtotalAmount()) * 100;
			System.out.printf("Overall Discount Applied          : ₹%.2f (%.2f%%)\n", order.getOverallDiscountApplied(),
					discountPercentage);
		}
		System.out.println("-------------------------------------------------");
		System.out.printf("FINAL TOTAL AMOUNT                : ₹%.2f\n", order.getFinalTotalAmount());
		System.out.println("Payment Mode                      : " + order.getPaymentMode());
		System.out.println("Delivery Partner                  : "
				+ (order.getDeliveryPartner() != null ? order.getDeliveryPartner().getName() : "N/A"));
		System.out.println("Order Status                      : " + order.getStatus());
		System.out.println("-------------------------------------------------");
	}

	public List<Order> getAllOrders() {
		return new ArrayList<>(allOrders);
	}
}