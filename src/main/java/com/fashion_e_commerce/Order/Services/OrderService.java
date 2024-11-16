package com.fashion_e_commerce.Order.Services;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.Cart.Services.CartService;
import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Repositories.OrderRepository;
import com.fashion_e_commerce.Order.Repositories.ShippingChargeRepository;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService; // Assuming a cart service exists
    @Autowired
    private ShippingChargeRepository shippingChargeRepository;
    @Autowired
    private ShippingChargeService shippingChargeService;

    public Order placeOrder(Long userId, String contactInfo, String shippingAddress, String paymentMethod,boolean isDhaka) {
        // Fetch cart items for the user
        List<CartItem> cartItems = cartService.getCartItems(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        // Calculate total price
        double totalPrice = cartItems.stream()
                .mapToDouble(CartItem::getTotalprice)
                .sum();

        // Get shipping charge
        double shippingCharge = shippingChargeService.getShippingCharge(isDhaka);

        // Add shipping charge to the total price
        double finalTotalPrice = totalPrice + shippingCharge;

        // Create an order
        Order order = new Order();
        order.setUser(cartItems.get(0).getUser()); // Assuming all cart items belong to the same user
        order.setItems(cartItems);
        order.setShippingCharge(shippingCharge);
        order.setTotalPrice(finalTotalPrice);
        order.setContactInfo(contactInfo);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart
        cartService.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // Admin feature to view all orders
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);  // Fetch order or return null if not found
    }


    public byte[] generateInvoice(Order order) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add Title
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.beginText();
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Invoice");
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText("Order ID: " + order.getId());
            contentStream.newLine();

            // Add User Details
            contentStream.showText("Customer: " + order.getUser().getFullname());
            contentStream.newLine();
            contentStream.showText("Email: " + order.getUser().getEmail());
            contentStream.newLine();

            // Add Order Items
            contentStream.showText("Order Details:");
            contentStream.newLine();
            for (CartItem item : order.getItems()) {
                contentStream.showText("- " + item.getProduct().getTitle() + " | Quantity: " + item.getQuantity() + " | Price: " + item.getProduct().getDiscountedPrice());
                contentStream.newLine();
            }

            // Add Total Price
            contentStream.showText("Shipping Charge: " + order.getShippingCharge());
            contentStream.newLine();
            contentStream.showText("Total Price: " + order.getTotalPrice());
            contentStream.newLine();

            // Add Shipping Info
            contentStream.showText("Shipping Address: " + order.getShippingAddress());
            contentStream.newLine();
            contentStream.showText("Payment Method: " + order.getPaymentMethod());
            contentStream.endText();

            contentStream.close();

            // Write the document to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
