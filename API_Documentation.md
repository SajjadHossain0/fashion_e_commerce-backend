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