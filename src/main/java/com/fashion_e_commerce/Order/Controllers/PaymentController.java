package com.fashion_e_commerce.Order.Controllers;

import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Services.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
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
        // Fetch the order details
        Order order = orderService.getOrderById(orderId);

        // Prepare payment request payload
        Map<String, String> paymentRequest = new HashMap<>();
        paymentRequest.put("store_id", storeId);
        paymentRequest.put("store_passwd", storePassword);
        paymentRequest.put("total_amount", String.valueOf(order.getTotalPrice()));
        paymentRequest.put("currency", "BDT");
        paymentRequest.put("tran_id", "ORDER_" + orderId);
        paymentRequest.put("success_url", successUrl);
        paymentRequest.put("fail_url", failUrl);
        paymentRequest.put("cancel_url", cancelUrl);
        paymentRequest.put("cus_name", order.getUser().getFullname());
        paymentRequest.put("cus_email", order.getUser().getEmail());
        paymentRequest.put("cus_add1", order.getShippingAddress());
        paymentRequest.put("cus_phone", order.getContactInfo());
        paymentRequest.put("product_name", "Order_" + orderId);

        // Make HTTP request to SSLCommerz
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/gwprocess/v4/api.php",
                paymentRequest,
                Map.class
        );

        // Return the payment gateway redirect URL
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam Map<String, String> params) {
        String transactionId = params.get("tran_id");
        String status = params.get("status");

        if ("VALID".equals(status)) {
            // Update the order status in the database
            Long orderId = Long.parseLong(transactionId.split("_")[1]);
            orderService.updateOrderStatus(orderId, "Paid");
            return ResponseEntity.ok("Payment Successful!");
        }
        return ResponseEntity.badRequest().body("Invalid Payment");
    }

    @PostMapping("/fail")
    public ResponseEntity<String> handleFail(@RequestParam Map<String, String> params) {
        return ResponseEntity.badRequest().body("Payment Failed");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> handleCancel(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok("Payment Cancelled");
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validatePayment(@RequestParam String transactionId) {
        String validationUrl = baseUrl + "/validator/api/validationserverAPI.php";
        Map<String, String> params = Map.of(
                "store_id", storeId,
                "store_passwd", storePassword,
                "tran_id", transactionId
        );

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(validationUrl, Map.class, params);

        return ResponseEntity.ok(response.getBody());
    }


}
