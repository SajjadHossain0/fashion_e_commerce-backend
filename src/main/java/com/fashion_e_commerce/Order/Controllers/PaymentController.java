package com.fashion_e_commerce.Order.Controllers;

import com.fashion_e_commerce.Order.Entities.Order;
import com.fashion_e_commerce.Order.Services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final OrderService orderService;
    private final WebClient webClient;

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

    public PaymentController(OrderService orderService, WebClient.Builder webClientBuilder) {
        this.orderService = orderService;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestParam Long orderId) {
        log.info("Initiating payment for orderId: {}", orderId);

        // Fetch order details
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getTotalPrice() <= 0) {
            log.error("Invalid order details for orderId: {}", orderId);
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Invalid order or missing payment details."
            );
        }

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

        log.info("Payment request payload: {}", paymentRequest);

        try {
            // Make HTTP request to SSLCommerz
            Map<String, Object> response = webClient
                    .post()
                    .uri("/gwprocess/v4/api.php")
                    .bodyValue(paymentRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Payment response: {}", response);

            // Validate the response from SSLCommerz
            if (response != null && "SUCCESS".equalsIgnoreCase((String) response.get("status"))) {
                return ResponseEntity.ok(response);
            } else {
                log.error("Payment initiation failed: {}", response);
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to initiate payment."));
            }
        } catch (Exception e) {
            log.error("Error initiating payment: {}", e.getMessage());
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment initiation failed."));
        }
    }

    @PostMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam Map<String, String> params) {
        log.info("Payment success callback received with params: {}", params);

        String transactionId = params.get("tran_id");
        String status = params.get("status");

        if ("VALID".equals(status)) {
            try {
                Long orderId = Long.parseLong(transactionId.split("_")[1]);
                orderService.updateOrderStatus(orderId, "Paid");
                log.info("Order {} marked as Paid.", orderId);
                return ResponseEntity.ok("Payment Successful!");
            } catch (Exception e) {
                log.error("Error updating payment status: {}", e.getMessage());
                return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Payment validation failed!");
            }
        }
        return ResponseEntity.badRequest().body("Invalid Payment");
    }

    @PostMapping("/fail")
    public ResponseEntity<String> handleFail(@RequestParam Map<String, String> params) {
        log.warn("Payment failed with params: {}", params);
        return ResponseEntity.badRequest().body("Payment Failed");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> handleCancel(@RequestParam Map<String, String> params) {
        log.warn("Payment cancelled with params: {}", params);
        return ResponseEntity.ok("Payment Cancelled");
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validatePayment(@RequestParam String transactionId) {
        log.info("Validating payment for transactionId: {}", transactionId);

        String validationUrl = baseUrl + "/validator/api/validationserverAPI.php";
        Map<String, String> params = Map.of(
                "store_id", storeId,
                "store_passwd", storePassword,
                "tran_id", transactionId
        );

        try {
            Map<String, Object> response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/validator/api/validationserverAPI.php")
                            .queryParam("store_id", storeId)
                            .queryParam("store_passwd", storePassword)
                            .queryParam("tran_id", transactionId)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Validation response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error validating payment: {}", e.getMessage());
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment validation failed."));
        }
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
