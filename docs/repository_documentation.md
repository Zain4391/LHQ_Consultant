## Repository Documentation

Repositories provide data access abstraction and support JPA-derived query methods, custom @Query methods, and bulk operations.

### Patterns & Best Practices

- **FETCH JOIN**: Used to eagerly load relationships in a single query, avoiding N+1 problems.
- **Custom Queries**: Explicit JPQL for complex filtering, ownership checks, and aggregations.
- **Modifying Queries**: @Modifying @Query for bulk updates/deletes with transaction management.
- **Pagination**: Page<T> return types with Pageable for large result sets.
- **Ownership Verification**: Queries to confirm resource ownership before allowing mutations.

### UserRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/user/repository/UserRepository.java`

**Key Methods:**

| Method                                    | Purpose                                           |
| ----------------------------------------- | ------------------------------------------------- |
| `findByEmail(String email)`               | Fetch user by email; used in login flow           |
| `existsByEmail(String email)`             | Check if email is already registered              |
| `findAllByRole(Role role, Pageable)`      | Filter users by role (CLIENT, LAWYER, ADMIN)      |
| `searchByFullName(String name, Pageable)` | Case-insensitive full-name search for admin panel |

**Usage:**

- Authentication: Lookup user by email during login
- Admin: Search and list users by role

### LawyerProfileRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/lawyer/repository/LawyerProfileRepository.java`

**Key Methods:**

| Method                                                   | Purpose                                                        |
| -------------------------------------------------------- | -------------------------------------------------------------- |
| `findByUserId(String userId)`                            | Get lawyer profile for a user (one-to-one)                     |
| `existsByUserId(String userId)`                          | Check if user is a registered lawyer                           |
| `existsByBarNumber(String barNumber)`                    | Verify bar number uniqueness                                   |
| `findByIdWithSpecialties(String id)`                     | Fetch lawyer WITH specialties in single query (N+1 prevention) |
| `findAllBySpecialtyName(String specialtyName, Pageable)` | Filter lawyers by specialty (case-insensitive)                 |
| `searchByFullName(String name, Pageable)`                | Search lawyers by first/last name (on joined User)             |

**Usage:**

- Profile Pages: Load lawyer details with specialties
- Search: Filter by specialty name or lawyer name
- Validation: Bar number uniqueness during profile creation

### SpecialtyRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/lawyer/repository/SpecialtyRepository.java`

**Key Methods:**

| Method                                | Purpose                                   |
| ------------------------------------- | ----------------------------------------- |
| `findByNameIgnoreCase(String name)`   | Lookup specialty by name                  |
| `existsByNameIgnoreCase(String name)` | Check specialty exists (case-insensitive) |
| `findAllByIdIn(Set<String> ids)`      | Bulk fetch specialties by IDs             |

**Usage:**

- Profile Creation: Resolve specialty IDs to Specialty entities
- Validation: Ensure specialty names are unique

### BookingRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/booking/repository/BookingRepository.java`

**Key Methods:**

| Method                                                                        | Purpose                                       |
| ----------------------------------------------------------------------------- | --------------------------------------------- |
| `findAllByUserId(String userId, Pageable)`                                    | User's bookings (paginated)                   |
| `findAllByUserIdAndStatus(String userId, BookingStatus status, Pageable)`     | User's bookings filtered by status            |
| `findAllByLawyerId(String lawyerId, Pageable)`                                | Lawyer's bookings (paginated)                 |
| `findAllByLawyerIdAndStatus(String lawyerId, BookingStatus status, Pageable)` | Lawyer's bookings filtered by status          |
| `findByIdAndUserId(String id, String userId)`                                 | Ownership check: booking belongs to user      |
| `findByIdAndLawyerId(String id, String lawyerId)`                             | Ownership check: booking belongs to lawyer    |
| `findCompletedBookingByIdAndUserId(String bookingId, String userId)`          | Fetch COMPLETED booking (for review creation) |
| `existsByTimeSlotId(String timeSlotId)`                                       | Prevent double-booking of a time slot         |

**Usage:**

- User Dashboard: Show user's bookings
- Lawyer Dashboard: Show lawyer's bookings
- Booking Confirmation: Check time slot availability
- Review Creation: Validate booking was completed before allowing review

### AvailabilityTemplateRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/booking/repository/AvailabilityTemplateRepository.java`

**Key Methods:**

| Method                                                     | Purpose                                                    |
| ---------------------------------------------------------- | ---------------------------------------------------------- |
| `findAllByLawyerId(String lawyerId, Pageable)`             | List all templates for a lawyer                            |
| `findAllByLawyerIdAndIsActiveTrue(String lawyerId)`        | List ACTIVE templates only (for slot generation)           |
| `findByIdAndLawyerId(String id, String lawyerId)`          | Ownership check                                            |
| `existsByLawyerIdAndDayOfWeek(String lawyerId, DayOfWeek)` | Check if template exists for day                           |
| `findAllActiveWithLawyer()`                                | Fetch ALL active templates WITH lawyer (for scheduler job) |
| `deactivateAllByLawyerId(String lawyerId)`                 | Bulk deactivate all templates (when lawyer is suspended)   |

**Usage:**

- Lawyer Profile: Display availability templates
- Slot Generation: Weekly scheduled job fetches active templates
- Admin Actions: Suspend lawyer → deactivate all templates

### TimeSlotRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/booking/repository/TimeSlotRepository.java`

**Key Methods:**

| Method                                                                                                  | Purpose                                                                     |
| ------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| `findAllByLawyerId(String lawyerId, Pageable)`                                                          | List all slots for a lawyer                                                 |
| `findAllByLawyerIdAndStatus(String lawyerId, TimeSlotStatus status, Pageable)`                          | Filter slots by status                                                      |
| `findByIdAndLawyerId(String id, String lawyerId)`                                                       | Ownership check                                                             |
| `existsOverlappingSlot(String lawyerId, Instant startTime, Instant endTime)`                            | Detect time conflicts when creating slot                                    |
| `existsOverlappingSlotExcluding(String lawyerId, Instant startTime, Instant endTime, String excludeId)` | Detect conflicts when updating slot (exclude self)                          |
| `existsByTemplateIdAndWindow(String templateId, Instant windowStart, Instant windowEnd)`                | Prevent duplicate slot generation                                           |
| `deleteAvailableSlotsByTemplateId(String templateId)`                                                   | Delete AVAILABLE slots when template is deleted (preserve BOOKED/CONFIRMED) |
| `findAvailableSlots(String lawyerId, Instant from, Instant to)`                                         | Fetch available slots for user booking flow                                 |
| `cancelSlotsByTemplateId(String templateId)`                                                            | Cancel all slots for a template                                             |

**Usage:**

- Booking Flow: Show available slots to user
- Slot Creation: Check for time conflicts
- Admin Actions: Delete template → cascade delete slots

### ReviewRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/review/repository/ReviewRepository.java`

**Key Methods:**

| Method                                                                          | Purpose                                                   |
| ------------------------------------------------------------------------------- | --------------------------------------------------------- |
| `findAllByLawyerId(String lawyerId, Pageable)`                                  | List all reviews for a lawyer                             |
| `findAllByUserId(String userId, Pageable)`                                      | List all reviews by a user                                |
| `findAllByLawyerIdAndSentiment(String lawyerId, Sentiment sentiment, Pageable)` | Filter reviews by sentiment (POSITIVE, NEGATIVE, NEUTRAL) |
| `findByBookingId(String bookingId)`                                             | 1:1 lookup: get review for a booking                      |
| `existsByBookingId(String bookingId)`                                           | Check if booking already has a review                     |
| `findRatingStatsByLawyerId(String lawyerId)`                                    | Aggregate: average rating + count for lawyer profile      |
| `findSentimentBreakdownByLawyerId(String lawyerId)`                             | Analytics: sentiment distribution [Sentiment, count]      |

**Usage:**

- Lawyer Profile: Display average rating and review count
- Review Display: Show reviews for a lawyer (with pagination)
- Analytics Dashboard: Sentiment breakdown for admin
- Review Creation: Check booking doesn't already have a review

### ClientLawyerRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/cases/repository/ClientLawyerRepository.java`

**Key Methods:**

| Method                                                                             | Purpose                                                               |
| ---------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `findAllByUserId(String userId, Pageable)`                                         | List all lawyers a client is connected with                           |
| `findAllByLawyerId(String lawyerId, Pageable)`                                     | List all clients a lawyer is connected with                           |
| `findAllByUserIdAndStatus(String userId, ClientLawyerStatus status, Pageable)`     | Filter by relationship status                                         |
| `findAllByLawyerIdAndStatus(String lawyerId, ClientLawyerStatus status, Pageable)` | Filter by relationship status                                         |
| `findByUserIdAndLawyerId(String userId, String lawyerId)`                          | Lookup existing relationship                                          |
| `existsByUserIdAndLawyerId(String userId, String lawyerId)`                        | Check relationship exists (prevent duplicate on booking confirmation) |
| `findByIdAndPrincipal(String id, String principalId)`                              | Ownership check: current user is client OR lawyer in relationship     |

**Usage:**

- Case Creation: Verify ClientLawyer relationship exists
- Dashboard: Show connected lawyers/clients
- Booking Confirmation: Auto-create ClientLawyer relationship if not exists
- Case Operations: Only client or lawyer can modify relationship

### CaseRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/cases/repository/CaseRepository.java`

**Key Methods:**

| Method                                                                                 | Purpose                                         |
| -------------------------------------------------------------------------------------- | ----------------------------------------------- |
| `findAllByClientLawyerId(String clientLawyerId, Pageable)`                             | List cases for a ClientLawyer relationship      |
| `findAllByClientLawyerIdAndStatus(String clientLawyerId, CaseStatus status, Pageable)` | Filter cases by status (OPEN, CLOSED, INACTIVE) |
| `findAllByUserId(String userId, Pageable)`                                             | All cases for a client (via ClientLawyer join)  |
| `findAllByLawyerId(String lawyerId, Pageable)`                                         | All cases for a lawyer (via ClientLawyer join)  |
| `findAllByUserIdAndStatus(String userId, CaseStatus status, Pageable)`                 | Client's cases filtered by status               |
| `findAllByLawyerIdAndStatus(String lawyerId, CaseStatus status, Pageable)`             | Lawyer's cases filtered by status               |

**Usage:**

- Client Dashboard: Show my cases
- Lawyer Dashboard: Show my client cases
- Case Management: List cases by status

## Query Performance Considerations

### N+1 Prevention

- **LawyerProfileRepository.findByIdWithSpecialties()**: Uses FETCH JOIN to load specialties in single query
- **AvailabilityTemplateRepository.findAllActiveWithLawyer()**: FETCH JOINs lawyer for scheduler
- **TimeSlotRepository.findAvailableSlots()**: Directly queries TimeSlot without loading related bookings unless needed

### Pagination

- Use `Pageable` for queries returning large result sets
- Countquery is explicitly specified for FETCH JOIN + Pageable combinations
- Page result includes totalElements, totalPages, hasNext for UI pagination controls

### Bulk Operations

- **@Modifying queries**: Use @Transactional and @Modifying for bulk updates/deletes
- Examples:
  - Deactivate all templates for a lawyer (account suspension)
  - Delete available slots when template is removed
  - Cancel all slots for a template

### Ownership & Security

- All mutations include ownership checks before allowing updates
- Examples:
  - `findByIdAndUserId()` — confirm booking belongs to user
  - `findByIdAndLawyerId()` — confirm booking belongs to lawyer
  - `findByIdAndPrincipal()` — confirm user is party in ClientLawyer relationship
