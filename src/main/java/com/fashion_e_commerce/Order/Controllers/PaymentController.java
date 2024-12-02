package com.fashion_e_commerce.Order.Controllers;

import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {

    private final OrderService orderService;
    @Value("${sslcommerz.storeId}")
    private String storeId;

    @Value("${sslcommerz.storePassword}")
    private String storePassword;

    @Value("${sslcommerz.baseUrl}")
    private String baseUrl;

    @Value("${sslcommerz.successUrl}")
    private String successUrl;

    @Value("${sslcommerz.failUrl}")
    private String failUrl;

    @Value("${sslcommerz.cancelUrl}")
    private String cancelUrl;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order ID");
        }

        Order order = orderService.getOrderById(orderId);

        // Prepare data for SSLCommerz
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("store_id", storeId);
        paymentData.put("store_passwd", storePassword);
        paymentData.put("total_amount", String.valueOf(order.getTotalPrice()));
        paymentData.put("currency", "BDT");
        paymentData.put("tran_id", "ORDER_" + order.getId());
        paymentData.put("success_url", "http://your-domain.com/api/payment/success");
        paymentData.put("fail_url", "http://your-domain.com/api/payment/fail");
        paymentData.put("cancel_url", "http://your-domain.com/api/payment/cancel");

        paymentData.put("cus_name", order.getUser().getFullname());
        paymentData.put("cus_email", order.getUser().getEmail());
        paymentData.put("cus_phone", order.getContactInfo());
        paymentData.put("cus_add1", order.getShippingAddress());

        paymentData.put("shipping_method", "Courier");
        paymentData.put("product_name", "E-Commerce Items");
        paymentData.put("product_category", "General");
        paymentData.put("product_profile", "general");

        // Send request to SSLCommerz
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://securepay.sslcommerz.com/gwprocess/v3/api.php",
                paymentData, Map.class
        );

        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        String gatewayPageURL = responseBody.get("GatewayPageURL");

        if (gatewayPageURL != null) {
            return ResponseEntity.ok(Collections.singletonMap("GatewayPageURL", gatewayPageURL));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to initiate payment."));
        }
    }

    @PostMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestBody Map<String, String> request) {
        String tranId = request.get("tran_id");
        Order order = orderService.getOrderById(Long.parseLong(tranId.replace("ORDER_", "")));

        // Update order status to Paid
        order.setStatus("Paid");
        orderService.updateOrderStatus(order.getId(), "Paid");

        return ResponseEntity.ok("Payment successful!");
    }

    @PostMapping("/fail")
    public ResponseEntity<String> handlePaymentFailure(@RequestBody Map<String, String> request) {
        String tranId = request.get("tran_id");
        Order order = orderService.getOrderById(Long.parseLong(tranId.replace("ORDER_", "")));

        // Update order status to Payment Failed
        order.setStatus("Payment Failed");
        orderService.updateOrderStatus(order.getId(), "Payment Failed");

        return ResponseEntity.ok("Payment failed.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> handlePaymentCancel(@RequestBody Map<String, String> request) {
        String tranId = request.get("tran_id");
        Order order = orderService.getOrderById(Long.parseLong(tranId.replace("ORDER_", "")));

        // Update order status to Cancelled
        order.setStatus("Cancelled");
        orderService.updateOrderStatus(order.getId(), "Cancelled");

        return ResponseEntity.ok("Payment cancelled.");
    }
}
/*
#sslcommerze application.properties
sslcommerz.storeId=id
sslcommerz.storePassword=id@ssl
sslcommerz.baseUrl=https://sandbox.sslcommerz.com
sslcommerz.successUrl = http://localhost:3000/payment-status?status=VALID&tran_id={TRAN_ID}
sslcommerz.failUrl = http://localhost:3000/payment-status?status=FAILED&tran_id={TRAN_ID}
sslcommerz.cancelUrl = http://localhost:3000/payment-status?status=CANCELLED&tran_id={TRAN_ID}
*/
