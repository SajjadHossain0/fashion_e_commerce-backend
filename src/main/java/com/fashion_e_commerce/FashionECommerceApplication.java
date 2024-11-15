package com.fashion_e_commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FashionECommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FashionECommerceApplication.class, args);
    }

}
/*
Shopping Cart Functionality:

Add to Cart: Create an API endpoint to add products to a shopping cart (by user ID or session).
Update Cart: Update quantities or remove items from the cart.
View Cart: Display all items in the cart, with subtotal and total price calculations.
Discount Calculation: Include discount prices if applicable.
User Checkout Process:

Address Management: Allow users to add/edit shipping addresses.
Order Summary: Display a review of cart items, total cost, discounts, and delivery details.
Payment Integration: Integrate with a payment gateway (even a basic mock-up at this stage).
Order Confirmation: Provide an order confirmation and generate an order ID.
Order Management:

Order History: Allow users to view past orders, including status, shipping details, and tracking.
Order Status Updates: Add order statuses like “Pending,” “Shipped,” and “Delivered.”
Admin Order Processing: Add admin endpoints for processing and updating order status.
User Profile Management:

Account Details: Allow users to view and update their account information.
Saved Addresses: Add functionality to save multiple addresses for faster checkout.
Product Reviews and Ratings:

Allow users to add ratings and reviews for products, and display them on the product detail page.
Include average ratings and review counts.
Product Search and Filtering:

Search Bar: Enable keyword-based product searches.
Filtering: Allow users to filter by categories, brands, sizes, or prices.
Wishlist Feature:

Add functionality for users to add products to a wishlist for future consideration.
Admin Dashboard (if needed):

A dashboard for product, order, and category management.
Monitor sales, user activity, and inventory.
*/