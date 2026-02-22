# GitHub Issues - AirBnB Booking System

This document outlines proposed issues/enhancements for the project. Create these as GitHub issues to track development progress.

---

## 🔴 Critical Priority

### Issue #1: Implement Comprehensive Unit and Integration Tests
**Labels**: `testing`, `critical`, `good-first-issue`

**Description**:
The application currently lacks test coverage. Need to implement comprehensive testing strategy.

**Tasks**:
- [ ] Add unit tests for service layer (target 80%+ coverage)
  - AuthService tests (signup, login, token generation)
  - BookingService tests (booking flow, validation, expiry)
  - HotelService tests (CRUD operations, activation)
  - InventoryService tests (availability checks, locking)
  - PricingService tests (strategy pattern validation)
- [ ] Add unit tests for repository layer (custom queries)
- [ ] Add integration tests for controller layer
  - Test all REST endpoints with MockMvc
  - Test authentication and authorization flows
  - Test error handling
- [ ] Add integration tests for database operations
  - Test pessimistic locking in inventory
  - Test transaction rollbacks
  - Test concurrent booking scenarios
- [ ] Set up test database configuration (H2 or Testcontainers)
- [ ] Configure JaCoCo for code coverage reporting
- [ ] Add GitHub Actions for running tests on PR

**Acceptance Criteria**:
- Minimum 80% code coverage
- All critical paths tested
- Integration tests pass with real database scenarios
- CI/CD pipeline runs tests automatically

---

### Issue #2: Implement Role-Based Authorization on All Endpoints
**Labels**: `security`, `critical`, `enhancement`

**Description**:
Currently, JWT authentication is implemented but fine-grained authorization is incomplete. Need to secure all endpoints properly.

**Current Issues**:
- AuthController login method has compilation errors (Session.Cookie issue)
- No authorization checks on many endpoints
- Missing validation for hotel ownership in admin operations

**Tasks**:
- [ ] Fix compilation error in AuthController (line 40)
- [ ] Implement proper role-based authorization for all endpoints:
  - `/admin/hotels/**` → Only HOTEL_MANAGER who owns the hotel
  - `/admin/hotels/{id}/rooms/**` → Only owner of the hotel
  - `/booking/**` → Authenticated users only
- [ ] Add ownership validation:
  - Hotel managers can only edit their own hotels
  - Users can only access their own bookings
- [ ] Add `@PreAuthorize` annotations where needed
- [ ] Implement method-level security for sensitive operations
- [ ] Add authorization unit tests
- [ ] Document all endpoint security requirements

**Acceptance Criteria**:
- All endpoints have proper authorization
- Ownership validation prevents unauthorized access
- Security tests verify authorization rules
- 403 Forbidden returned for unauthorized access

---

### Issue #3: Standardize API Response Format
**Labels**: `api`, `enhancement`, `critical`

**Description**:
API responses are inconsistent. Some return DTOs directly, others return ResponseEntity with different status codes. Need to standardize.

**Current Issues**:
- Inconsistent HTTP status codes (200 vs 201 for creation)
- No standard error response format
- Missing metadata (timestamp, request ID)
- No pagination metadata standardization

**Tasks**:
- [ ] Create standard API response wrapper:
  ```java
  ApiResponse<T> {
    boolean success;
    String message;
    T data;
    String timestamp;
    String requestId;
    List<String> errors;
  }
  ```
- [ ] Create standard pagination response:
  ```java
  PagedResponse<T> {
    List<T> content;
    PageMetadata page;
    boolean success;
    String message;
  }
  ```
- [ ] Create standard error response:
  ```java
  ErrorResponse {
    String error;
    String message;
    int status;
    String timestamp;
    String path;
    List<ValidationError> validationErrors;
  }
  ```
- [ ] Implement `@ControllerAdvice` for global exception handling
- [ ] Update all controllers to use standard responses
- [ ] Add request ID tracking (via filter/interceptor)
- [ ] Update API documentation with new response formats
- [ ] Add response DTOs to OpenAPI/Swagger

**Acceptance Criteria**:
- All endpoints return standardized responses
- Error responses include helpful information
- Consistent HTTP status codes across all endpoints
- Documentation reflects standard formats

---

### Issue #4: Fix Compilation Errors and Code Issues
**Labels**: `bug`, `critical`

**Description**:
Several compilation and code quality issues exist in the codebase.

**Known Issues**:
- AuthController line 40-42: `Session.Cookie` doesn't exist (should use `Cookie` from javax.servlet.http)
- HotelServiceImpl line 35: Incorrect User import (using SecurityProperties.User instead of entity.User)
- BookingServiceImpl: `guestRepository` field not declared
- Missing error handling in several service methods
- Unused imports in multiple files

**Tasks**:
- [ ] Fix AuthController cookie implementation
- [ ] Fix HotelServiceImpl user import
- [ ] Add guestRepository to BookingServiceImpl
- [ ] Remove unused imports across the codebase
- [ ] Add null checks where needed
- [ ] Fix all compiler warnings
- [ ] Run static code analysis (SonarLint/SpotBugs)
- [ ] Set up code quality checks in CI/CD

**Acceptance Criteria**:
- Project compiles without errors
- No compiler warnings
- Code quality tools show no critical issues
- All services properly autowired

---

## 🟡 High Priority

### Issue #5: Integrate Stripe Payment Gateway
**Labels**: `feature`, `payment`, `high-priority`

**Description**:
Payment functionality is currently not implemented. Need to integrate Stripe for handling payments.

**Tasks**:
- [ ] Add Stripe Java SDK dependency
- [ ] Create PaymentService with Stripe integration
- [ ] Implement payment methods:
  - Create payment intent
  - Process payment
  - Handle payment confirmation
  - Process refunds
  - Handle webhooks for payment events
- [ ] Add payment endpoints to BookingController:
  - `POST /booking/{id}/payment/init` - Initialize payment
  - `POST /booking/{id}/payment/confirm` - Confirm payment
  - `POST /payment/webhook` - Stripe webhook handler
- [ ] Update Booking entity Payment relationship
- [ ] Implement payment status tracking
- [ ] Add payment failure handling
- [ ] Complete booking workflow:
  - GUESTS_ADDED → PAYMENT_PENDING → CONFIRMED
  - Update inventory from reserved to booked
  - Send confirmation email
- [ ] Add payment security (idempotency keys)
- [ ] Handle payment timeouts
- [ ] Add payment logging and auditing
- [ ] Create Payment DTOs (PaymentRequest, PaymentResponse)
- [ ] Add payment tests (mock Stripe API)
- [ ] Document payment flow in README

**Acceptance Criteria**:
- Users can complete payment via Stripe
- Payment status properly tracked
- Webhooks handle async events correctly
- Failed payments release inventory
- Complete booking creates confirmed reservation
- Tests cover payment scenarios

---

### Issue #6: Implement Complete Booking Workflow
**Labels**: `feature`, `booking`, `high-priority`

**Description**:
The booking workflow is incomplete. Need to implement the full lifecycle.

**Tasks**:
- [ ] Implement booking expiry mechanism:
  - Scheduled job to check expired reservations
  - Release reserved inventory after 10 minutes
  - Mark booking as EXPIRED
  - Send expiry notification
- [ ] Implement booking cancellation:
  - `POST /booking/{id}/cancel` endpoint
  - Calculate refund based on cancellation policy
  - Release booked inventory back to available
  - Update booking status to CANCELLED
- [ ] Implement booking confirmation flow:
  - After successful payment
  - Send confirmation email
  - Generate booking reference
- [ ] Add booking history endpoint:
  - `GET /booking/my-bookings`
  - Filter by status, dates
  - Pagination support
- [ ] Add booking details endpoint:
  - `GET /booking/{id}`
  - Include hotel, room, guest details
- [ ] Implement dynamic price calculation:
  - Calculate actual amount based on dates
  - Apply surge pricing
  - Calculate taxes and fees
  - Update booking amount
- [ ] Add booking validation:
  - Check-in date not in past
  - Check-out after check-in
  - Maximum guests per room
  - Minimum booking duration
- [ ] Implement booking modification:
  - Date changes (if available)
  - Guest count changes
  - Room upgrade options

**Acceptance Criteria**:
- Complete booking lifecycle implemented
- Expired bookings auto-release inventory
- Users can cancel and get appropriate refunds
- Dynamic pricing calculates correct amounts
- Validation prevents invalid bookings
- Users can view booking history

---

### Issue #7: Implement Notification Service
**Labels**: `feature`, `notification`, `high-priority`

**Description**:
No notification system exists. Users need to receive emails/SMS for booking events.

**Tasks**:
- [ ] Add Spring Mail dependency
- [ ] Create NotificationService interface
- [ ] Implement EmailService:
  - Configure SMTP (e.g., SendGrid, AWS SES)
  - Create email templates (Thymeleaf)
  - Booking confirmation email
  - Booking cancellation email
  - Payment receipt email
  - Booking reminder (24h before check-in)
- [ ] Implement async notification processing:
  - Use @Async for email sending
  - Configure thread pool
  - Add retry mechanism for failures
- [ ] Create notification events:
  - BookingConfirmedEvent
  - BookingCancelledEvent
  - PaymentReceivedEvent
  - BookingReminderEvent
- [ ] Implement event listeners for notifications
- [ ] Add notification preferences to User entity
- [ ] Create notification logging/audit trail
- [ ] Add email templates:
  - Welcome email (signup)
  - Booking confirmation
  - Cancellation receipt
  - Payment receipt
- [ ] (Optional) Add SMS integration (Twilio)
- [ ] Add notification tests (mock email service)

**Acceptance Criteria**:
- Users receive emails for all major events
- Emails are professionally formatted
- Async processing doesn't block API calls
- Failed emails are logged and retried
- Users can opt-out of notifications

---

## 🟢 Medium Priority

### Issue #8: Implement Application Profiling and Performance Optimization
**Labels**: `performance`, `optimization`, `medium-priority`

**Description**:
Need to profile the application and optimize performance bottlenecks.

**Tasks**:
- [ ] Set up Spring Boot Actuator:
  - Add actuator dependency
  - Enable health, metrics, info endpoints
  - Configure custom metrics
- [ ] Add database query profiling:
  - Enable Hibernate statistics
  - Log slow queries
  - Identify N+1 query problems
- [ ] Implement query optimization:
  - Add appropriate indexes (city, date on Inventory)
  - Use JOIN FETCH for lazy loading
  - Optimize HotelMinPrice queries
  - Add query hints for performance
- [ ] Implement caching:
  - Add Spring Cache (Redis/Caffeine)
  - Cache hotel details
  - Cache user details
  - Implement cache eviction strategy
- [ ] Add database connection pooling:
  - Configure HikariCP properly
  - Set optimal pool size
  - Add connection timeout
- [ ] Profile API endpoints:
  - Use Spring Boot Actuator + Micrometer
  - Integrate with Prometheus/Grafana
  - Identify slow endpoints
- [ ] Add pagination limits:
  - Max page size restrictions
  - Default page sizes
  - Cursor-based pagination for large datasets
- [ ] Implement API rate limiting:
  - Use Bucket4j or similar
  - Per-user rate limits
  - Per-IP rate limits
- [ ] Load testing:
  - Use JMeter/Gatling
  - Test concurrent booking scenarios
  - Test search performance
- [ ] Add monitoring and alerting

**Acceptance Criteria**:
- All endpoints respond in < 200ms (P95)
- Database queries optimized
- Caching reduces DB load by 60%+
- No N+1 query issues
- Application handles 1000+ concurrent users
- Monitoring dashboard available

---

### Issue #9: Add API Documentation with OpenAPI/Swagger
**Labels**: `documentation`, `api`, `medium-priority`

**Description**:
API documentation is only in README. Need interactive API documentation.

**Tasks**:
- [ ] Add SpringDoc OpenAPI dependency
- [ ] Configure Swagger UI
- [ ] Add OpenAPI annotations to controllers:
  - @Operation for endpoint descriptions
  - @ApiResponse for response codes
  - @Parameter for parameter descriptions
  - @Schema for DTO documentation
- [ ] Add authentication to Swagger UI:
  - JWT token input
  - Test authenticated endpoints
- [ ] Add examples for request/response
- [ ] Group endpoints by tags
- [ ] Add API versioning information
- [ ] Configure Swagger UI customization:
  - Logo
  - Title
  - Description
  - Contact info
- [ ] Export OpenAPI spec (JSON/YAML)
- [ ] Host API documentation online
- [ ] Add Try It Out feature

**Acceptance Criteria**:
- Swagger UI accessible at /swagger-ui.html
- All endpoints documented
- Interactive testing works
- Authentication flow documented
- OpenAPI spec available for export

---

### Issue #10: Implement Search Filters and Sorting
**Labels**: `feature`, `search`, `medium-priority`

**Description**:
Hotel search is basic. Need advanced filters and sorting options.

**Tasks**:
- [ ] Add search filters:
  - Price range (min/max)
  - Hotel amenities (multi-select)
  - Room amenities
  - Star rating
  - Guest rating/reviews
  - Distance from location
- [ ] Add sorting options:
  - Price (low to high, high to low)
  - Popularity
  - Rating
  - Distance
  - Newest first
- [ ] Implement filter combinations (AND/OR logic)
- [ ] Add search result count
- [ ] Implement search result caching
- [ ] Add "Save Search" feature
- [ ] Add search history for users
- [ ] Optimize filter queries
- [ ] Add faceted search (filter counts)

**Acceptance Criteria**:
- Users can apply multiple filters
- Sorting works correctly
- Filter combinations return accurate results
- Search performance < 500ms
- Filter counts show accurate numbers

---

### Issue #11: Implement Hotel Reviews and Ratings
**Labels**: `feature`, `reviews`, `medium-priority`

**Description**:
Hotels need a review and rating system.

**Tasks**:
- [ ] Create Review entity:
  - User, Hotel, Booking reference
  - Rating (1-5 stars)
  - Review text
  - Review date
  - Helpful count
- [ ] Add review endpoints:
  - `POST /booking/{id}/review` - Add review after stay
  - `GET /hotels/{id}/reviews` - Get hotel reviews
  - `PUT /reviews/{id}` - Update own review
  - `DELETE /reviews/{id}` - Delete own review
  - `POST /reviews/{id}/helpful` - Mark review helpful
- [ ] Implement review validation:
  - Only after completed stay
  - One review per booking
  - Text length limits
  - Profanity filter
- [ ] Calculate hotel ratings:
  - Average rating
  - Rating distribution
  - Total review count
  - Recent rating trend
- [ ] Add review moderation:
  - Flag inappropriate reviews
  - Admin approval workflow
- [ ] Add review sorting:
  - Most recent
  - Highest rated
  - Most helpful
- [ ] Display reviews on hotel page
- [ ] Add review responses (hotel owners)

**Acceptance Criteria**:
- Users can leave reviews after checkout
- Reviews display on hotel pages
- Ratings calculate correctly
- Moderation prevents abuse
- Helpful voting works

---

### Issue #12: Add Image Upload and Storage
**Labels**: `feature`, `media`, `medium-priority`

**Description**:
Currently, photos are stored as URL strings. Need proper image upload.

**Tasks**:
- [ ] Choose storage solution:
  - AWS S3
  - Cloudinary
  - Local storage + CDN
- [ ] Add file upload dependency (AWS SDK or Cloudinary SDK)
- [ ] Create ImageService:
  - Upload image
  - Delete image
  - Resize/optimize images
  - Generate thumbnails
- [ ] Add image upload endpoints:
  - `POST /admin/hotels/{id}/images` - Upload hotel image
  - `DELETE /admin/hotels/{id}/images/{imageId}` - Delete image
  - `POST /admin/rooms/{id}/images` - Upload room image
- [ ] Implement image validation:
  - File type (JPEG, PNG, WebP)
  - File size limits (max 5MB)
  - Image dimensions
  - Virus scanning
- [ ] Add image metadata:
  - Caption
  - Alt text
  - Display order
- [ ] Optimize images:
  - Compress on upload
  - Multiple sizes (thumbnail, medium, full)
  - WebP conversion
  - Lazy loading
- [ ] Add signed URLs for secure access
- [ ] Implement image CDN

**Acceptance Criteria**:
- Hotel managers can upload images
- Images stored securely in cloud
- Multiple sizes generated automatically
- Fast image loading with CDN
- Validation prevents inappropriate uploads

---

## 🔵 Low Priority / Nice to Have

### Issue #13: Implement Admin Dashboard
**Labels**: `feature`, `admin`, `enhancement`

**Description**:
Create an admin dashboard for hotel managers and system admins.

**Tasks**:
- [ ] Create admin statistics endpoints:
  - Total bookings
  - Revenue metrics
  - Occupancy rates
  - Popular hotels/rooms
  - User growth
- [ ] Add dashboard for hotel managers:
  - Own hotel stats
  - Booking calendar
  - Revenue reports
  - Guest reviews
- [ ] Add system admin dashboard:
  - Platform-wide statistics
  - User management
  - Hotel approval workflow
  - Revenue reporting
- [ ] Create data visualization endpoints
- [ ] Add export functionality (CSV, PDF)
- [ ] Implement date range filtering
- [ ] Add real-time updates (WebSocket)

**Acceptance Criteria**:
- Hotel managers see their hotel stats
- Admins see platform-wide metrics
- Data is accurate and real-time
- Reports can be exported

---

### Issue #14: Implement Wishlist/Favorites Feature
**Labels**: `feature`, `user-experience`, `enhancement`

**Description**:
Users should be able to save favorite hotels.

**Tasks**:
- [ ] Create Wishlist entity
- [ ] Add wishlist endpoints:
  - `POST /wishlist/hotels/{id}` - Add to wishlist
  - `DELETE /wishlist/hotels/{id}` - Remove from wishlist
  - `GET /wishlist` - Get user's wishlist
- [ ] Add wishlist indicator in search results
- [ ] Send notifications for price drops
- [ ] Add wishlist sharing feature

**Acceptance Criteria**:
- Users can save favorite hotels
- Wishlist persists across sessions
- Price alerts work correctly

---

### Issue #15: Implement Multi-language Support (i18n)
**Labels**: `feature`, `internationalization`, `enhancement`

**Description**:
Add support for multiple languages.

**Tasks**:
- [ ] Add Spring i18n support
- [ ] Externalize all messages
- [ ] Create resource bundles for languages:
  - English
  - Hindi
  - Spanish
- [ ] Add language selection API
- [ ] Translate error messages
- [ ] Translate email templates
- [ ] Add language preference to User entity

**Acceptance Criteria**:
- API supports multiple languages
- User can set preferred language
- All messages translated

---

### Issue #16: Implement Loyalty Program
**Labels**: `feature`, `gamification`, `enhancement`

**Description**:
Create a loyalty/rewards program for frequent users.

**Tasks**:
- [ ] Create Points entity
- [ ] Award points for bookings
- [ ] Implement point redemption
- [ ] Add membership tiers (Silver, Gold, Platinum)
- [ ] Provide tier benefits:
  - Discount percentages
  - Early check-in
  - Late checkout
  - Priority support
- [ ] Add points history endpoint
- [ ] Display tier progress

**Acceptance Criteria**:
- Users earn points on bookings
- Points can be redeemed for discounts
- Tier benefits apply automatically

---

### Issue #17: Add Social Login (OAuth2)
**Labels**: `feature`, `authentication`, `enhancement`

**Description**:
Allow users to login with Google, Facebook, etc.

**Tasks**:
- [ ] Add Spring OAuth2 Client dependency
- [ ] Configure OAuth2 providers:
  - Google
  - Facebook
  - GitHub
- [ ] Implement OAuth2 login flow
- [ ] Link social accounts to existing users
- [ ] Add social profile data sync

**Acceptance Criteria**:
- Users can login with social accounts
- Profile data syncs correctly
- Existing users can link accounts

---

### Issue #18: Implement Real-time Chat Support
**Labels**: `feature`, `support`, `enhancement`

**Description**:
Add live chat for customer support.

**Tasks**:
- [ ] Choose WebSocket implementation (STOMP)
- [ ] Create chat entities (Message, Conversation)
- [ ] Implement WebSocket endpoints
- [ ] Create chat UI integration points
- [ ] Add support agent queue
- [ ] Store chat history
- [ ] Add file sharing in chat

**Acceptance Criteria**:
- Users can chat with support
- Real-time message delivery
- Chat history persists

---

### Issue #19: Implement Mobile Push Notifications
**Labels**: `feature`, `mobile`, `enhancement`

**Description**:
Add push notification support for mobile apps.

**Tasks**:
- [ ] Integrate Firebase Cloud Messaging
- [ ] Create device token registration
- [ ] Send push for booking events
- [ ] Add notification preferences
- [ ] Implement notification scheduling

**Acceptance Criteria**:
- Mobile users receive push notifications
- Notifications are timely and relevant
- Users can control notification types

---

### Issue #20: Add Automated Backup and Disaster Recovery
**Labels**: `infrastructure`, `devops`, `enhancement`

**Description**:
Implement backup strategy for data protection.

**Tasks**:
- [ ] Configure automated database backups
- [ ] Set up backup retention policy
- [ ] Implement point-in-time recovery
- [ ] Create disaster recovery plan
- [ ] Test backup restoration
- [ ] Monitor backup success
- [ ] Document recovery procedures

**Acceptance Criteria**:
- Daily automated backups
- 30-day retention period
- Tested restore procedures
- < 1 hour RTO (Recovery Time Objective)

---

## 📋 Technical Debt & Code Quality

### Issue #21: Refactor Service Layer Dependencies
**Labels**: `refactoring`, `technical-debt`

**Description**:
Some services have circular dependencies or tight coupling.

**Tasks**:
- [ ] Review and fix circular dependencies
- [ ] Implement proper dependency injection
- [ ] Reduce coupling between services
- [ ] Extract common functionality to utility classes
- [ ] Implement service interfaces consistently

---

### Issue #22: Add Input Validation
**Labels**: `validation`, `security`, `technical-debt`

**Description**:
Need comprehensive input validation across all endpoints.

**Tasks**:
- [ ] Add @Valid annotations to controller methods
- [ ] Add Bean Validation annotations to DTOs:
  - @NotNull, @NotBlank
  - @Email, @Pattern
  - @Min, @Max for numeric fields
  - @Size for strings and collections
  - @Future for dates
- [ ] Create custom validators where needed
- [ ] Implement global validation exception handler
- [ ] Add validation error messages to response
- [ ] Document validation rules in API docs

---

### Issue #23: Implement Comprehensive Logging Strategy
**Labels**: `logging`, `observability`, `technical-debt`

**Description**:
Improve logging for better debugging and monitoring.

**Tasks**:
- [ ] Standardize log levels (INFO, WARN, ERROR)
- [ ] Add structured logging (JSON format)
- [ ] Add correlation IDs for request tracing
- [ ] Log sensitive operations (audit trail)
- [ ] Configure log aggregation (ELK/Splunk)
- [ ] Add log retention policy
- [ ] Implement log security (no sensitive data)

---

### Issue #24: Database Migration with Flyway/Liquibase
**Labels**: `database`, `devops`, `technical-debt`

**Description**:
Replace hibernate ddl-auto with proper migration tool.

**Tasks**:
- [ ] Add Flyway dependency
- [ ] Create initial migration scripts
- [ ] Change ddl-auto to validate
- [ ] Document migration process
- [ ] Add migration testing
- [ ] Set up migration CI/CD

---

## 🏷️ Issue Labels Guide

- **critical**: Must be fixed/implemented soon
- **high-priority**: Important features
- **medium-priority**: Nice to have features
- **enhancement**: New features
- **bug**: Something isn't working
- **security**: Security-related issues
- **performance**: Performance improvements
- **technical-debt**: Code refactoring needed
- **documentation**: Documentation improvements
- **testing**: Test coverage
- **good-first-issue**: Good for newcomers

---

## 📊 Recommended Implementation Order

1. **Phase 1 - Stabilization** (Issues #1, #2, #4)
   - Fix compilation errors
   - Add tests
   - Implement proper authorization

2. **Phase 2 - Core Features** (Issues #3, #5, #6, #7)
   - Standardize APIs
   - Payment integration
   - Complete booking workflow
   - Notifications

3. **Phase 3 - Performance & Quality** (Issues #8, #9, #22, #23)
   - Profiling and optimization
   - API documentation
   - Validation
   - Logging

4. **Phase 4 - Enhancement** (Issues #10-#12)
   - Advanced search
   - Reviews
   - Image uploads

5. **Phase 5 - Growth Features** (Issues #13-#20)
   - Admin dashboard
   - Social features
   - Mobile support

---

**Note**: Create these issues in your GitHub repository with appropriate labels and milestones. Adjust priorities based on your project goals and timeline.

