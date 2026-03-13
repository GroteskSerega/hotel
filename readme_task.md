# Final Project: "Development with Spring Framework" Course
## About the Project

You are to develop the backend component of a hotel booking service, featuring content management capabilities through an administrative CMS panel.

The primary goal is to enable users to book their chosen hotel for a specific period.

In addition, you must implement a hotel search system based on criteria and ratings, along with an in-app scoring system (from 1 to 5).

The service must allow administrators to export operational statistics as a CSV file.

### Task 1. Environment Setup
- Create a project with the necessary dependencies. You will need Spring, Spring Web MVC, Spring Data, and MapStruct.
- Create a new database for the application.
- Define a docker-compose file containing your application container and a PostgreSQL container.

### Task 2. Hotel Management
- Define the "Hotel" Entity: Each object must include a name, advertisement title, city, address, distance from the city center, rating (1 to 5), and the total number of ratings used to calculate the average.
- Database Schema: Create a table in the database to store hotel information.
- ORM Mapping: Configure the mapping between the database table columns and Java entity fields.
- Repository Layer: Create a Spring Data JPA interface for the "Hotel" entity to support full CRUD operations.
- Service Layer: Implement a Hotel Service to handle business logic and CRUD operations.
- Data Transfer Objects (DTOs): Define DTOs for handling controller requests and responses:
- - Request DTO: For creating or editing a "Hotel." Note: The rating and total number of ratings must not be modifiable via HTTP requests.
- - Response DTO: For returning a single "Hotel" entity.
- - Paginated Response DTO: For returning a list of objects (other DTOs) with pagination support.
- Mapping with MapStruct: Create mapping services to convert between entities and DTOs:
- - From Create/Update DTO to "Hotel" entity.
- - From "Hotel" entity to Update DTO.
- - From "Hotel" entity to Response DTO (this DTO must include the rating and the number of ratings).
- REST API Controller: Implement a controller for Hotel CRUD operations. The controller must include the following endpoints:
- - Find a specific hotel by ID.
- - Create a new hotel.
- - Update an existing hotel.
- - Delete a hotel.
- - Retrieve a list of all available hotels.

### Task 3. Error Handling
- Error DTO: Define a Data Transfer Object (DTO) to be returned when service-level errors occur.
- Controller Advice: Implement a @ControllerAdvice to intercept exceptions and return a structured Error DTO with a descriptive message and the appropriate HTTP status code:
- - Entity Not Found: Return a 404 Not Found status if the entity is missing from the database.
- - Client Errors: Return a 400 Bad Request status for invalid input or incorrect data.
- - Unhandled Exceptions: Return a 500 Internal Server Error for any other unexpected errors.

### Task 4. Room Management
- Define the "Room" Entity: Each object must include a name, description, room number, price, maximum occupancy (capacity), and a list of unavailable dates. Every room must be associated with a specific hotel.
- Database Schema: Create a table in the database to store room information.
- ORM Mapping: Configure the mapping between the database table columns and the Java entity fields.
- Repository Layer: Create a Spring Data JPA interface for the "Room" entity to support full CRUD operations.
- Service Layer: Implement a Room Service to handle business logic and CRUD operations.
- Data Transfer Objects (DTOs): Define DTOs for handling controller requests and responses:
- - Request DTO: For creating or editing a "Room."
- - Response DTO: For returning a single "Room" entity.
- Mapping with MapStruct: Create mapping services to convert between entities and DTOs:
- - From Create/Update DTO to "Room" entity.
- - From "Room" entity to Update DTO.
- - From "Room" entity to Response DTO.
- REST API Controller: Implement a controller for Room CRUD operations. The controller must include the following endpoints:
- - Find a specific room by ID.
- - Create a new room.
- - Update an existing room.
- - Delete a room.

### Task 5. User Management
- Define the "User" Entity: Each user must have a unique username, password, email address, and a role (either USER or ADMIN).
- Database Schema: Create a table in the database to store user information.
- ORM Mapping: Configure the mapping between the database table columns and the Java entity fields.
- Repository Layer: Create a Spring Data JPA interface for the "User" entity. The interface must provide capabilities to:
- - Perform full CRUD operations.
- - Find a user by their username.
- - Check if a user already exists with a specific username and email address.
- Service Layer: Implement a User Service with the following capabilities:
- - Perform full CRUD operations.
- - Create a new user.
- - Find a user by their username.
- Data Transfer Objects (DTOs): Define DTOs for handling controller requests and responses:
- - Request DTO: For creating or editing a "User."
- - Response DTO: For returning a single "User" entity.
- Mapping with MapStruct: Create mapping services to convert between entities and DTOs:
- - From Create/Update DTO to "User" entity.
- - From "User" entity to Update DTO.
- - From "User" entity to Response DTO.
- REST API Controller: Implement a controller for User operations. The controller must include a method to create a new user with a specified role (the role is passed as a parameter during creation). Pre-registration check: Before creating a new user, verify that no account already exists with the same username or email.

### Task 6. Booking Management
- Define the "Booking" Entity: This entity must include check-in and check-out dates, as well as information about the booked room and the user performing the booking.
- Database Schema: Create a table in the database to store booking information.
- ORM Mapping: Configure the mapping between the database table columns and the Java entity fields.
- Repository Layer: Create a Spring Data JPA interface for the "Booking" entity to support full CRUD operations.
- Booking Service: Implement a service that provides the following capabilities:
- - Room Reservation: Allow users to book rooms for specified dates. Note: The booking is tied to a specific room. Different users can book the same room multiple times, but only for dates when the room is available.
- - Retrieve Bookings: Get a list of all existing reservations.
- Data Transfer Objects (DTOs): Define DTOs for handling controller requests and responses:
- - Request DTO: For creating a new booking request.
- - Response DTO: For returning the result of a booking operation.
- REST API Controller: Implement a controller for booking operations. The controller must include the following endpoints:
- - Book a room.
- - Retrieve a list of all processed bookings.

### Task 7. Spring Security Configuration
Implement all necessary services and configurations to secure your application using Spring Security. Use Basic Auth for development.
- Security Models and Services:
- Define the UserDetails object, including the required fields and roles.
- Implement the UserDetailsService to load user data from the database.
- Base Security Configuration: Configure Spring Security so that unauthorized access is permitted only for the registration endpoint.
- Controller Access Control: Update the existing controllers to enforce the following rules:
- - Public Access: User registration must be available without authentication.
- - Admin Only: Creating, editing, and deleting Hotels must be restricted to users with the ADMIN role.
- - Admin Only: Creating, editing, and deleting Rooms must be restricted to users with the ADMIN role.
- - Admin Only: Retrieving the list of all bookings must be restricted to the ADMIN role.
- - Authenticated Users: All other methods must be accessible to both USER and ADMIN roles, but only after successful authentication.

### Task 8. Hotel Rating Update Logic

Implement a method in the Hotel Controller to allow updating a hotel's rating (on a scale of 1 to 5). This endpoint must be accessible to both USER and ADMIN roles.

The rating update must follow this specific formula:

**Given:**

- rating: Current average rating.
- newMark: The new score provided by a user.
- totalRating: The sum of all scores.
- numberOfRatings: The total count of ratings provided so far.

**Calculation Steps:**

- Calculate the current total sum: Multiply the current rating by the numberOfRatings.
```
totalRating = rating × numberOfRating
```

- Adjust the total sum: Subtract the current rating from the totalRating and add the newMark.
```
totalRating = totalRating − rating + newMark
```

- Calculate the new average: Divide the updated totalRating by the numberOfRatings. The result must be rounded to one decimal place.
```
rating = totalRating / numberOfRating
(round to 1 decimal place)
```

- Update the count: Increment the numberOfRatings by one to account for the new entry.
```
numberOfRating = numberOfRating + 1
```

### Task 9. Paginated Hotel Search with Filtering
- Paginated List DTO: Define a DTO that returns a list of objects along with the total number of records available in the database. This DTO must be used to support response pagination.
- Filtering Implementation: Add a method to the Hotel Controller that allows filtering results by the following categories:
- - Hotel ID
- - Hotel name
- - Advertisement title
- - City
- - Address
- - Distance to the city center
- - Rating and number of ratings
- Pagination Parameters: Enable the use of pagination parameters (such as page number and page size) for hotel search requests.
- JPA Specification: Use the JPA Specification API to implement the dynamic filtering logic.

### Task 10. Paginated Room Search with Advanced Filtering
- Filtering Implementation: Add a method to the Room Controller that supports filtering by the following criteria:
- - Room ID
- - Room title
- - Minimum and maximum price
- - Number of guests (occupancy)
- - Check-in and check-out dates
- - Hotel ID
- Pagination: Implement support for pagination parameters (page number and page size) for room search requests using JPA Specification.
- Date Range Filtering Logic:
- - Filtering by dates must consider both check-in and check-out fields. If only one field is provided, the filter must be ignored.
- - When dates are specified, the search must return only rooms that are available within the entire selected range.
- Booking History: Update the existing method for retrieving processed bookings to support pagination.

**Example Scenario:**

A specific room is already booked for the following periods:

- Jan 01, 2023 – Jan 10, 2023
- Jan 20, 2023 – Jan 30, 2023

1. If a user searches for Jan 03 – Jan 11, this room must be excluded from the results (because it overlaps with the first booking).
2. If a user searches for Jan 11 – Jan 19, this room must be included in the results (as it is free during this gap).

### Task 11. Statistical Analysis Layer Implementation
Develop a statistics layer that utilizes Kafka for data ingestion and MongoDB for persistent storage. Implement a service within this layer to export collected statistical data into a CSV file.
The system must handle two specific event types: User Registration and Room Booking.

- Registration Event: Must store the newly created user's ID.
- Booking Event: Must store the ID of the user who made the reservation, along with the check-in and check-out dates.
- Storage: All event data must be stored in MongoDB in JSON format.

**Requirements:**
- Event Models: Define base events and create models for both sending (producing) and processing (consuming) these events.
- Kafka Configuration: Configure Kafka settings and add Kafka and Zookeeper services to the docker-compose.yml file.
- Persistence & Logic: Create the entity, repository, and service required to manage statistics within MongoDB.
- CSV Export Service: Implement a service to aggregate and export all statistical records into a CSV format.
- Event Triggers: Integrate event publishing logic into the existing User Registration and Room Booking flows.
- Statistics Controller: Implement a controller with an endpoint to download the generated CSV file. Access to this method must be restricted to the ADMIN role only.

## Implementation Guidelines
1. Core Service: Use Spring MVC and PostgreSQL for implementing the primary business logic.
2. Statistics Layer: Use MongoDB to store and manage the statistical data.
3. Entity Relationships: Refer to the database diagram below for the entity relationship mapping.
![](/images/relations.png)
4. Schema Adherence: Ensure the project implementation strictly follows the provided database schema.
5. Compliance: Fully adhere to the specific service requirements detailed in each task.