package com.fashion_e_commerce.Order.Services;

import com.fashion_e_commerce.Cart.Entities.CartItem;
import com.fashion_e_commerce.Cart.Repositories.CartRepository;
import com.fashion_e_commerce.Cart.Services.CartService;
import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Repositories.OrderRepository;
import com.fashion_e_commerce.User.Entities.User;
import com.fashion_e_commerce.User.Repositories.UserRepository;
import com.fashion_e_commerce.User.Services.UserService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ShippingChargeService shippingChargeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public Order placeOrder(Long userId, String contactInfo, String shippingAddress, String paymentMethod, boolean isDhaka) {
        // Fetch cart items for the user
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        // Calculate total price
        double totalPrice = cartItems.stream()
                .mapToDouble(CartItem::getTotalprice)
                .sum();

        // Add shipping charge
        double shippingCharge = shippingChargeService.getShippingCharge(isDhaka);
        double finalTotalPrice = totalPrice + shippingCharge;

        // Create an order
        Order order = new Order();
        order.setUser(cartItems.get(0).getUser()); // Assuming all cart items belong to the same user
        order.setShippingCharge(shippingCharge);
        order.setTotalPrice(finalTotalPrice);
        order.setContactInfo(contactInfo);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(cartItems);

        // Associate order with cart items
        cartItems.forEach(item -> item.setOrder(order));

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after order is saved
        cartService.clearCart(userId);

        User user = userRepository.findById(userId).get();
        user.setNumber(order.getContactInfo());
        user.setAddress(order.getShippingAddress());
        userRepository.save(user);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    @Transactional
    public void removeCartItem(Long orderId, Long cartItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Remove the cart item and update the order
        order.getItems().remove(cartItem);
        orderRepository.save(order);
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
                contentStream.showText("- " + item.getProduct().getTitle() + " | Quantity: " +
                        item.getQuantity() + " | Price: " + item.getProduct().getDiscountedPrice());
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
