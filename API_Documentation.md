# Fashion E-Commerce API Documentation

## Base URL http://localhost:8080/api

---

## Endpoints

---

### AuthenticationController

#### Register a New User

- **URL**: `/auth/register`
- **Method**: `POST`
- **Request Body**:
    - `user` (User, required): User object with registration details.
- **Response**: `200 OK`  
  Returns a success message.

#### User Login

- **URL**: `/auth/login`
- **Method**: `POST`
- **Request Body**:
    - `email` (String, required): User email.
    - `password` (String, required): User password.
- **Response**: `200 OK`  
  Returns JWT token and user ID if login is successful.

---

### CategoryController

#### Add a New Category

- **URL**: `/categories`
- **Method**: `POST`
- **Parameters**:
    - `name` (String, required): The name of the category.
    - `image` (MultipartFile, required): Image file for the category.
- **Response**: `200 OK`  
  Returns the newly created category.

#### Add a New Subcategory

- **URL**: `/categories/{categoryId}/subcategories`
- **Method**: `POST`
- **Path Parameters**:
    - `categoryId` (Long, required): The ID of the parent category.
- **Parameters**:
    - `name` (String, required): The name of the subcategory.
    - `image` (MultipartFile, required): Image file for the subcategory.
- **Response**: `200 OK`  
  Returns the newly created subcategory.

#### Get All Categories

- **URL**: `/categories`
- **Method**: `GET`
- **Response**: `200 OK`  
  Returns a list of all categories with their subcategories.

#### Get Category by ID

- **URL**: `/categories/{categoryId}`
- **Method**: `GET`
- **Path Parameters**:
    - `categoryId` (Long, required): The ID of the category.
- **Response**: `200 OK`  
  Returns the category details for the specified ID.

#### Get Subcategories by Category ID

- **URL**: `/categories/{categoryId}/subcategories`
- **Method**: `GET`
- **Path Parameters**:
    - `categoryId` (Long, required): The ID of the category.
- **Response**: `200 OK`  
  Returns a list of subcategories under the specified category.

---

### ProductController

#### Add a New Product

- **URL**: `/products`
- **Method**: `POST`
- **Parameters**:
    - `title` (String, required): Title of the product.
    - `price` (double, required): Price of the product.
    - `discount` (double, optional): Discount percentage (e.g., 10 for 10% off).
    - `description` (String, required): Short description of the product.
    - `detailedDescription` (String, optional): Detailed description of the product.
    - `brand` (String, required): Brand name of the product.
    - `categoryId` (Long, required): ID of the product's category.
    - `subCategoryId` (Long, optional): ID of the product's subcategory.
    - `sizes` (List<String>, optional): Available sizes (e.g., ["M", "L", "XL"]).
    - `material` (String, optional): Material of the product (e.g., "Cotton").
    - `stock` (int, required): Stock count for the product.
    - `available` (boolean, required): Availability status.
    - `image` (MultipartFile, optional): Product image file.
- **Response**: `200 OK`  
  Returns the newly created product.

#### Get All Products

- **URL**: `/products`
- **Method**: `GET`
- **Response**: `200 OK`  
  Returns a list of all products.

#### Get a Single Product by ID

- **URL**: `/products/{productId}`
- **Method**: `GET`
- **Path Parameters**:
    - `productId` (Long, required): ID of the product to retrieve.
- **Response**: `200 OK`  
  Returns details of the specified product.

#### Update an Existing Product

- **URL**: `/products/{productId}`
- **Method**: `PUT`
- **Path Parameters**:
    - `productId` (Long, required): ID of the product to update.
- **Parameters**:
    - `title` (String, optional): Updated title of the product.
    - `price` (double, optional): Updated price.
    - `discount` (double, optional): Updated discount percentage.
    - `description` (String, optional): Updated short description.
    - `detailedDescription` (String, optional): Updated detailed description.
    - `brand` (String, optional): Updated brand.
    - `categoryId` (Long, optional): Updated category ID.
    - `subCategoryId` (Long, optional): Updated subcategory ID.
    - `sizes` (List<String>, optional): Updated sizes.
    - `material` (String, optional): Updated material.
    - `stock` (int, optional): Updated stock count.
    - `available` (boolean, optional): Updated availability status.
    - `image` (MultipartFile, optional): Updated image file.
- **Response**: `200 OK`  
  Returns the updated product.

#### Delete a Product by ID

- **URL**: `/products/{productId}`
- **Method**: `DELETE`
- **Path Parameters**:
    - `productId` (Long, required): ID of the product to delete.
- **Response**: `200 OK`  
  Returns a success message confirming deletion.

---

### Cart Controller

### Add Item to Cart

- **URL**: `/add`
- **Method**: `POST`
- **Description**: Adds a product to the user's cart.
- **Parameters**:
    - `userId` (Long, required): The ID of the user adding the item.
    - `productId` (Long, required): The ID of the product to add.
    - `quantity` (int, required): Quantity of the product to add.
- **Response**: `200 OK`
    - Returns the `CartItem` object that was added to the cart.

### Get All Cart Items for a User

- **URL**: `/{userId}`
- **Method**: `GET`
- **Description**: Retrieves all items in the cart for a specified user.
- **Path Parameters**:
    - `userId` (Long, required): The ID of the user whose cart items are being retrieved.
- **Response**: `200 OK`
    - Returns a list of `CartItem` objects in the user's cart.

### Remove Item from Cart

- **URL**: `/remove/{cartItemId}`
- **Method**: `DELETE`
- **Description**: Removes a specific item from the cart by its ID.
- **Path Parameters**:
    - `cartItemId` (Long, required): The ID of the cart item to remove.
- **Response**: `200 OK`
    - Returns a success message confirming the item was removed.

### Clear All Items from User's Cart

- **URL**: `/clear/{userId}`
- **Method**: `DELETE`
- **Description**: Clears all items from a user's cart.
- **Path Parameters**:
    - `userId` (Long, required): The ID of the user whose cart is to be cleared.
- **Response**: `200 OK`
    - Returns a success message confirming the cart was cleared.

## Data Models

### CartItem

Represents an item in the user's cart.

- **Fields**:
    - `id` (Long): The unique identifier of the cart item.
    - `user` (User): Reference to the `User` who owns the cart item.
    - `product` (Product): Reference to the `Product` added to the cart.
    - `quantity` (int): Quantity of the product in the cart.
    - `totalPrice` (double): Total price of the cart item based on quantity and product price.

## Notes

- Ensure that `userId` and `productId` are valid and exist in the database when using the `addToCart` endpoint.
- Clearing the cart with `/clear/{userId}` will remove all items associated with the specified user.

---

### Order Controller


### 1. **Place Order**
- **POST** `/api/orders/place`

**Description**:
- This endpoint allows users to place an order with the required details.

**Request Parameters**:
- `userId` (Long): The ID of the user placing the order.
- `contactInfo` (String): Contact information for delivery.
- `shippingAddress` (String): The address where the order should be shipped.
- `paymentMethod` (String): The payment method for the order (e.g., "COD", "Bkash").

**Response**:
- Status: `200 OK`
- Body: Returns the created order object, including its details such as `orderId`, `userId`, `contactInfo`, `shippingAddress`, and `paymentMethod`.

**Example Request**:
   ```http
   POST /api/orders/place?userId=1&contactInfo=1234567890&shippingAddress=123+Main+Street&paymentMethod=COD
```
---
