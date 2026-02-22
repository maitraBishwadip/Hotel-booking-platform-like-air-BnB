# AirBnB Hotel Booking System

A comprehensive hotel booking platform built with Spring Boot that enables hotel management, room inventory tracking, dynamic pricing, and booking operations.

## 📋 Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [MVC Architecture](#mvc-architecture)
- [Business Logic](#business-logic)
- [API Documentation](#api-documentation)
- [Getting Started](#getting-started)
- [Database Schema](#database-schema)
- [Security](#security)

## 🎯 Overview

This is a Spring Boot-based hotel booking system inspired by AirBnB that provides:
- Hotel and room management for property owners
- Dynamic inventory management with date-based availability
- Real-time hotel search with availability checking
- Multi-step booking process with guest management
- JWT-based authentication and authorization
- Strategy pattern-based dynamic pricing
- Role-based access control (GUEST, HOTEL_MANAGER)

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security with JWT (jsonwebtoken 0.13.0)
- **Object Mapping**: ModelMapper 3.2.0
- **Build Tool**: Maven
- **Additional**: Lombok, BCrypt Password Encoding

## 🏗 MVC Architecture

The application follows a clean layered architecture with clear separation of concerns:

### 1. **Controller Layer** (`controllers/`)
Handles HTTP requests and responses, delegates business logic to services.

- **AuthController**: User registration and authentication
- **HotelController**: Admin operations for hotel CRUD (Hotel Manager only)
- **HotelBrowseController**: Public hotel search and information retrieval
- **RoomAdminController**: Room management within hotels (Hotel Manager only)
- **HotelBookingController**: Booking initialization and guest management

### 2. **Service Layer** (`service/`)
Contains business logic and orchestrates data operations.

- **AuthService**: User signup and login with JWT token generation
- **HotelService**: Hotel lifecycle management, activation, and information retrieval
- **RoomService**: Room CRUD operations
- **BookingService**: Complex booking workflow (initialization, reservation, guest addition)
- **InventoryService**: Inventory initialization, deletion, and hotel search with availability
- **UserService**: User retrieval and authentication
- **PricingService**: Dynamic price calculation using strategy pattern

### 3. **Repository Layer** (`repository/`)
Data access layer using Spring Data JPA.

- **UserRepository**: User data access
- **HotelRepository**: Hotel entity persistence
- **RoomRepository**: Room entity operations
- **BookingRepository**: Booking records management
- **InventoryRepository**: Complex queries for availability and locking
- **HotelMinPriceRepository**: Optimized hotel search with price aggregation
- **GuestRepository**: Guest information storage

### 4. **Entity Layer** (`entity/`)
JPA entities representing database tables.

- **User**: User account with roles (UserDetails implementation)
- **Hotel**: Hotel information with contact details and ownership
- **Room**: Room types with pricing and capacity
- **Booking**: Booking records with status tracking
- **Inventory**: Date-based room availability and pricing
- **Guest**: Guest information for bookings
- **Payment**: Payment transaction records
- **HotelMinPrice**: Materialized view for optimized search
- **HotelContactInfo**: Embedded contact information

### 5. **DTO Layer** (`dto/`)
Data Transfer Objects for API request/response.

- Request DTOs: SignUpRequestDto, LoginDto, BookingRequest, HotelSearchRequest
- Response DTOs: UserDto, HotelDto, RoomDto, BookingDto, HotelPriceDto, LoginResponseDto
- Info DTOs: HotelInfoDto with room details

### 6. **Security Layer** (`security/`)
Authentication and authorization implementation.

- **JWTService**: Token generation and validation
- **JWTAuthFilter**: Request filtering and authentication
- **AuthService**: Authentication business logic
- **WebSecurityConfig**: Security configuration with role-based access

### 7. **Strategy Pattern** (`strategy/`)
Dynamic pricing strategies for inventory.

- **PricingStrategy** (Interface): Contract for pricing calculation
- **BasePricingStrategy**: Base room price
- **HolidayPricingStrategy**: Holiday surge pricing
- **OccupancyPricingStrategy**: Occupancy-based pricing
- **UrgencyPricingStrategy**: Last-minute booking pricing
- **SurgePricingStrategy**: Demand-based pricing

### 8. **Exception Handling** (`exception/`)
Custom exceptions for error handling.

- **ResourceNotFoundException**: Entity not found exceptions
- **UnauthorisedException**: Authorization failures

## 📊 Business Logic

### Hotel Management Flow
1. **Hotel Creation**: Hotel manager creates a hotel (inactive by default)
2. **Room Addition**: Add multiple room types to the hotel
3. **Hotel Activation**: Activate hotel → automatically initializes 1 year of inventory for all rooms
4. **Inventory Management**: System creates daily inventory records with base pricing

### Booking Flow
1. **Search Hotels**: User searches by city, dates, and room count
   - System checks inventory availability across date range
   - Returns hotels with available rooms and minimum price
   
2. **Initialize Booking**: User selects hotel and room
   - System locks inventory (pessimistic locking)
   - Validates availability for entire date range
   - Increments reserved count in inventory
   - Creates booking with RESERVED status
   - Booking expires in 10 minutes if not completed
   
3. **Add Guests**: User adds guest information
   - Validates booking ownership
   - Checks booking hasn't expired
   - Updates status to GUESTS_ADDED
   
4. **Payment** (Pending implementation)
   - Process payment
   - Confirm booking
   - Convert reserved to booked in inventory

### Inventory System
- **Daily Inventory**: Each room has inventory records for each date
- **Capacity Tracking**: totalCount, bookedCount, reservedCount
- **Dynamic Pricing**: Base price × surge factor
- **Availability Check**: Ensures rooms available for entire stay duration
- **City-based Indexing**: Optimized search by city and date range

### Authentication & Authorization
- **JWT-based Authentication**: Access token + Refresh token pattern
- **Role-based Access Control**:
  - PUBLIC: Hotel search, hotel information
  - AUTHENTICATED: All booking operations
  - HOTEL_MANAGER: Hotel and room management (/admin/**)
- **Password Security**: BCrypt encoding

### Dynamic Pricing Strategy
The system uses Strategy Pattern for flexible pricing:
- Currently configured with `holidayPrice` strategy
- Can be swapped at runtime for different pricing models
- Surge factor applied to base price for final inventory price

## 🚀 API Documentation

Base URL: `http://localhost:8081/api/v1`

### Authentication APIs

#### 1. User Signup
```http
POST /api/v1/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}

Response: 201 Created
{
  "id": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "roles": ["GUEST"]
}
```

#### 2. User Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
Note: Refresh token set as HttpOnly cookie
```

### Hotel Browse APIs (Public)

#### 3. Search Hotels
```http
GET /api/v1/hotels/search?city=Mumbai&startDate=2026-03-01&endDate=2026-03-05&roomCount=2&page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "hotelId": 1,
      "hotelName": "Grand Plaza",
      "city": "Mumbai",
      "minPrice": 5000.00,
      "availableRooms": 5
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  "size": 10
}
```

#### 4. Get Hotel Info
```http
GET /api/v1/hotels/1/info

Response: 200 OK
{
  "id": 1,
  "name": "Grand Plaza",
  "city": "Mumbai",
  "photos": ["url1", "url2"],
  "amenities": ["WiFi", "Pool", "Gym"],
  "hotelContactinfo": {
    "phone": "+91-1234567890",
    "email": "contact@grandplaza.com"
  },
  "rooms": [
    {
      "id": 1,
      "type": "Deluxe",
      "basePrice": 5000.00,
      "capacity": 2,
      "amenities": ["AC", "TV"]
    }
  ]
}
```

### Booking APIs (Authenticated)

#### 5. Initialize Booking
```http
POST /api/v1/booking/init
Authorization: Bearer {token}
Content-Type: application/json

{
  "hotelId": 1,
  "roomId": 1,
  "checkInDate": "2026-03-01",
  "checkOutDate": "2026-03-05",
  "roomsCount": 2
}

Response: 200 OK
{
  "id": 101,
  "hotelId": 1,
  "roomId": 1,
  "roomsCount": 2,
  "checkInDate": "2026-03-01T00:00:00",
  "checkOutDate": "2026-03-05T00:00:00",
  "bookingStatus": "RESERVED",
  "amount": 10.00,
  "createdAt": "2026-02-22T10:30:00"
}
```

#### 6. Add Guests to Booking
```http
POST /api/v1/booking/101/addGuests
Authorization: Bearer {token}
Content-Type: application/json

[
  {
    "name": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "+91-9876543210",
    "gender": "MALE",
    "age": 30
  },
  {
    "name": "Jane Doe",
    "email": "jane@example.com",
    "phoneNumber": "+91-9876543211",
    "gender": "FEMALE",
    "age": 28
  }
]

Response: 200 OK
{
  "id": 101,
  "bookingStatus": "GUESTS_ADDED",
  "guests": [...]
}
```

### Hotel Admin APIs (HOTEL_MANAGER Role)

#### 7. Create Hotel
```http
POST /api/v1/admin/hotels
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Grand Plaza",
  "city": "Mumbai",
  "photos": ["url1", "url2"],
  "amenities": ["WiFi", "Pool"],
  "hotelContactinfo": {
    "phone": "+91-1234567890",
    "email": "contact@grandplaza.com"
  }
}

Response: 201 Created
{
  "id": 1,
  "name": "Grand Plaza",
  "city": "Mumbai",
  "active": false
}
```

#### 8. Get Hotel by ID
```http
GET /api/v1/admin/hotels/1
Authorization: Bearer {token}

Response: 200 OK
{
  "id": 1,
  "name": "Grand Plaza",
  "city": "Mumbai",
  "active": true
}
```

#### 9. Update Hotel
```http
PUT /api/v1/admin/hotels/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Grand Plaza Premium",
  "city": "Mumbai",
  "amenities": ["WiFi", "Pool", "Gym", "Spa"]
}

Response: 200 OK
```

#### 10. Delete Hotel
```http
DELETE /api/v1/admin/hotels/1
Authorization: Bearer {token}

Response: 200 OK
```

#### 11. Activate Hotel
```http
PATCH /api/v1/admin/hotels/1/activate
Authorization: Bearer {token}

Response: 204 No Content
```
*Note: This initializes 1 year of inventory for all hotel rooms*

### Room Admin APIs (HOTEL_MANAGER Role)

#### 12. Create Room
```http
POST /api/v1/admin/hotels/1/rooms
Authorization: Bearer {token}
Content-Type: application/json

{
  "type": "Deluxe Suite",
  "basePrice": 5000.00,
  "totalCount": 10,
  "capacity": 2,
  "photos": ["url1", "url2"],
  "amenities": ["AC", "TV", "Mini Bar"],
  "active": true
}

Response: 201 Created
```

#### 13. Get All Rooms of Hotel
```http
GET /api/v1/admin/hotels/1/rooms
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "type": "Deluxe Suite",
    "basePrice": 5000.00,
    "capacity": 2
  }
]
```

#### 14. Get Room by ID
```http
GET /api/v1/admin/hotels/1/rooms/1
Authorization: Bearer {token}

Response: 200 OK
```

#### 15. Delete Room
```http
DELETE /api/v1/admin/hotels/1/rooms/1
Authorization: Bearer {token}

Response: 204 No Content
```

## 🚦 Getting Started

### Prerequisites
- Java 21
- Maven
- PostgreSQL

### Configuration

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/airBnB
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update

# Server Configuration
server.port=8081
server.servlet.context-path=/api/v1

# JWT Secret
jwt.secretKey=your_secret_key_here
```

### Installation

1. Clone the repository
2. Create PostgreSQL database:
   ```sql
   CREATE DATABASE airBnB;
   ```
3. Update application.properties with your credentials
4. Build the project:
   ```bash
   mvn clean install
   ```
5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8081/api/v1`

## 🗄 Database Schema

### Key Entities and Relationships

```
User (app_user)
├── id (PK)
├── email (unique)
├── password (encrypted)
├── name
└── roles (ElementCollection)

Hotel
├── id (PK)
├── name
├── city
├── photos (array)
├── amenities (array)
├── active
├── owner_id (FK → User)
└── hotelContactinfo (Embedded)

Room
├── id (PK)
├── hotel_id (FK → Hotel)
├── type
├── basePrice
├── totalCount
├── capacity
├── photos (array)
├── amenities (array)
└── active

Inventory
├── id (PK)
├── hotel_id (FK → Hotel)
├── room_id (FK → Room)
├── date (unique with hotel+room)
├── bookedCount
├── reservedCount
├── totalCount
├── surgeFactor
├── price
├── city
└── closed

Booking
├── id (PK)
├── hotel_id (FK → Hotel)
├── room_id (FK → Room)
├── user_id (FK → User)
├── roomsCount
├── checkInDate
├── checkOutDate
├── bookingStatus
├── amount
└── guests (ManyToMany)

Guest
├── id (PK)
├── name
├── email
├── phoneNumber
├── gender
├── age
└── user_id (FK → User)
```

### Enums

- **Role**: GUEST, HOTEL_MANAGER
- **BookingStatus**: RESERVED, GUESTS_ADDED, PAYMENT_PENDING, CONFIRMED, CANCELLED, EXPIRED
- **PaymentStatus**: PENDING, COMPLETED, FAILED, REFUNDED
- **Gender**: MALE, FEMALE, OTHER

## 🔐 Security

### Security Features
1. **JWT Authentication**: 
   - Access tokens for API authentication
   - Refresh tokens (HttpOnly cookies) for token renewal
   
2. **Role-Based Access Control**:
   - `/admin/**` → HOTEL_MANAGER role required
   - `/bookings/**` → Authenticated users only
   - Public endpoints for search and browse

3. **Password Security**: BCrypt encoding with salt

4. **CSRF Protection**: Disabled (stateless JWT)

5. **Session Management**: Stateless (no server-side sessions)

### Security Configuration
- JWT filter runs before UsernamePasswordAuthenticationFilter
- Custom AccessDeniedHandler for authorization failures
- User ownership validation in booking operations

## 📝 Additional Features

### Scheduled Tasks
- `@EnableScheduling` configured for cron jobs
- Can be used for:
  - Booking expiry checks
  - Price updates
  - Inventory cleanup

### Transaction Management
- `@Transactional` on critical operations
- Pessimistic locking for inventory reservation
- Prevents race conditions in booking

### Logging
- SLF4J with Lombok `@Slf4j`
- Info and error level logging for debugging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is a demo/educational project for Spring Boot learning.

---

**Note**: This is a work in progress. Several features are under development. Check GITHUB_ISSUES.md for planned enhancements and known issues.

