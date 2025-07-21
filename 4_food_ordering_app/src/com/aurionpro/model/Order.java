package com.aurionpro.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
 private static final long serialVersionUID = 1L;

 private int orderId;
 private int customerId;
 private LocalDateTime orderDate;
 private List<LineItem> items;
 private double subtotalAmount; 
 private double overallDiscountApplied; 
 private double finalTotalAmount; 
 private String paymentMode;
 private DeliveryPartner deliveryPartner;
 private String status; //pending, cancelled, deliveredd

 public Order(int orderId, int customerId, List<LineItem> items, String paymentMode, DeliveryPartner deliveryPartner) {
     this.orderId = orderId;
     this.customerId = customerId;
     this.orderDate = LocalDateTime.now(); 
     this.items = new ArrayList<>(items); 
     this.paymentMode = paymentMode;
     this.deliveryPartner = deliveryPartner;
     this.status = "Pending"; 
     this.subtotalAmount = 0.0;
     this.overallDiscountApplied = 0.0;
     this.finalTotalAmount = 0.0;
 }

 // Getters
 public int getOrderId() {
     return orderId;
 }

 public int getCustomerId() {
     return customerId;
 }

 public LocalDateTime getOrderDate() {
     return orderDate;
 }

 public List<LineItem> getItems() {
     return new ArrayList<>(items); 
 }

 public double getSubtotalAmount() {
     return subtotalAmount;
 }

 public double getOverallDiscountApplied() {
     return overallDiscountApplied;
 }

 public double getFinalTotalAmount() {
     return finalTotalAmount;
 }

 public String getPaymentMode() {
     return paymentMode;
 }

 public DeliveryPartner getDeliveryPartner() {
     return deliveryPartner;
 }

 public String getStatus() {
     return status;
 }

 // Setters
 public void setOrderId(int orderId) {
     this.orderId = orderId;
 }

 public void setCustomerId(int customerId) {
     this.customerId = customerId;
 }

 public void setOrderDate(LocalDateTime orderDate) {
     this.orderDate = orderDate;
 }

 public void setItems(List<LineItem> items) {
     this.items = new ArrayList<>(items);
 }

 public void setSubtotalAmount(double subtotalAmount) {
     this.subtotalAmount = subtotalAmount;
 }

 public void setOverallDiscountApplied(double overallDiscountApplied) {
     this.overallDiscountApplied = overallDiscountApplied;
 }

 public void setFinalTotalAmount(double finalTotalAmount) {
     this.finalTotalAmount = finalTotalAmount;
 }

 public void setPaymentMode(String paymentMode) {
     this.paymentMode = paymentMode;
 }

 public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
     this.deliveryPartner = deliveryPartner;
 }

 public void setStatus(String status) {
     this.status = status;
 }

 public double calculateSubtotal() {
     this.subtotalAmount = items.stream()
                               .mapToDouble(LineItem::calculateLineItemCost)
                               .sum();
     return this.subtotalAmount;
 }


 public double applyOverallDiscount(double discountPercentage, double discountThreshold) {
     calculateSubtotal();

     if (subtotalAmount >= discountThreshold && discountPercentage > 0) {
         this.overallDiscountApplied = subtotalAmount * (discountPercentage / 100.0);
         this.finalTotalAmount = subtotalAmount - this.overallDiscountApplied;
     } else {
         this.overallDiscountApplied = 0.0;
         this.finalTotalAmount = subtotalAmount;
     }
     return this.finalTotalAmount;
 }

 @Override
 public boolean equals(Object o) {
     if (this == o) return true;
     if (o == null || getClass() != o.getClass()) return false;
     Order order = (Order) o;
     return orderId == order.orderId;
 }

 @Override
 public int hashCode() {
     return Objects.hash(orderId);
 }

 @Override
 public String toString() {
     return "Order{" +
            "orderId=" + orderId +
            ", customerId=" + customerId +
            ", orderDate=" + orderDate +
            ", items=" + items.size() + " items" + 
            ", subtotalAmount=" + String.format("%.2f", subtotalAmount) +
            ", overallDiscountApplied=" + String.format("%.2f", overallDiscountApplied) +
            ", finalTotalAmount=" + String.format("%.2f", finalTotalAmount) +
            ", paymentMode='" + paymentMode + '\'' +
            ", deliveryPartner=" + (deliveryPartner != null ? deliveryPartner.getName() : "N/A") +
            ", status='" + status + '\'' +
            '}';
 }
}