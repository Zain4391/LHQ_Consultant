## Repository Documentation

Repositories provide data access abstraction and support JPA-derived query methods, custom @Query methods, and bulk operations.

### Patterns & Best Practices

- **FETCH JOIN**: Used to eagerly load relationships in a single query, avoiding N+1 problems.
- **Full Graph Methods**: New `*WithFullGraph()` methods provide complete entity graphs for DTO mapping.
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

| Method                                                                | Purpose                                                        |
| --------------------------------------------------------------------- | -------------------------------------------------------------- |
| `findByUserId(String userId)`                                         | Get lawyer profile for a user (one-to-one)                     |
| `existsByUserId(String userId)`                                       | Check if user is a registered lawyer                           |
| `existsByBarNumber(String barNumber)`                                 | Verify bar number uniqueness                                   |
| `findByIdWithSpecialties(String id)`                                  | Fetch lawyer WITH specialties in single query (N+1 prevention) |
| `findAllBySpecialtyName(String specialtyName, Pageable)`              | Filter lawyers by specialty (case-insensitive)                 |
| `searchByLawyerFullName(String name, Pageable)`                       | Search lawyers by first/last name (on joined User)             |
| `findByIdWithFullGraph(String id)`                                    | Full graph fetch: lawyer → user, specialties                   |
| `findAllWithFullGraph(Pageable)`                                      | Paginated list with user + specialties (for browse all)        |
| `findAllBySpecialtyNameWithFullGraph(String specialtyName, Pageable)` | Filter by specialty with full graph                            |

**Usage:**

- Profile Pages: Load lawyer details with specialties using full graph methods
- Search: Filter by specialty name or lawyer name
- Validation: Bar number uniqueness during profile creation
- Browse: Display all lawyers with complete data (prevents N+1)

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

| Method                                                                        | Purpose                                                         |
| ----------------------------------------------------------------------------- | --------------------------------------------------------------- |
| `findAllByUserId(String userId, Pageable)`                                    | User's bookings (paginated)                                     |
| `findAllByUserIdAndStatus(String userId, BookingStatus status, Pageable)`     | User's bookings filtered by status                              |
| `findAllByLawyerId(String lawyerId, Pageable)`                                | Lawyer's bookings (paginated)                                   |
| `findAllByLawyerIdAndStatus(String lawyerId, BookingStatus status, Pageable)` | Lawyer's bookings filtered by status                            |
| `findByIdAndUserId(String id, String userId)`                                 | Ownership check: booking belongs to user                        |
| `findByIdAndLawyerId(String id, String lawyerId)`                             | Ownership check: booking belongs to lawyer                      |
| `findCompletedBookingByIdAndUserId(String bookingId, String userId)`          | Fetch COMPLETED booking (for review creation)                   |
| `existsActiveBookingForTimeSlot(String timeSlotId)`                           | Prevent double-booking of a time slot                           |
| `existsConfirmedBookingBetween(String userId, String lawyerId)`               | Check if confirmed booking exists between user and lawyer       |
| `findByIdWithFullGraph(String id)`                                            | Full graph fetch: booking → user, lawyer, lawyer.user, timeSlot |
| `findByIdAndUserIdWithFullGraph(String id, String userId)`                    | Full graph with ownership check (user-side)                     |
| `findByIdAndLawyerIdWithFullGraph(String id, String lawyerId)`                | Full graph with ownership check (lawyer-side)                   |
| `findAllByUserIdWithFullGraph(String userId, Pageable)`                       | Paginated list with full graph (user-side)                      |
| `findAllByLawyerIdWithFullGraph(String lawyerId, Pageable)`                   | Paginated list with full graph (lawyer-side)                    |

**Usage:**

- User Dashboard: Show user's bookings with full entity graph
- Lawyer Dashboard: Show lawyer's bookings with full entity graph
- Booking Confirmation: Check time slot availability
- Review Creation: Validate booking was completed before allowing review
- Full Graph Fetches: Used by services mapping to BookingResponse DTOs (prevents N+1 problems)

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

| Method                                                                          | Purpose                                                       |
| ------------------------------------------------------------------------------- | ------------------------------------------------------------- |
| `findAllByLawyerId(String lawyerId, Pageable)`                                  | List all reviews for a lawyer                                 |
| `findAllByUserId(String userId, Pageable)`                                      | List all reviews by a user                                    |
| `findAllByLawyerIdAndSentiment(String lawyerId, Sentiment sentiment, Pageable)` | Filter reviews by sentiment (POSITIVE, NEGATIVE, NEUTRAL)     |
| `findByBookingId(String bookingId)`                                             | 1:1 lookup: get review for a booking                          |
| `existsByBookingId(String bookingId)`                                           | Check if booking already has a review                         |
| `findRatingStatsByLawyerId(String lawyerId)`                                    | Aggregate: average rating + count for lawyer profile          |
| `findSentimentBreakdownByLawyerId(String lawyerId)`                             | Analytics: sentiment distribution [Sentiment, count]          |
| `findByIdWithFullGraph(String id)`                                              | Full graph fetch: review → user, lawyer, lawyer.user, booking |
| `findAllByLawyerIdWithFullGraph(String lawyerId, Pageable)`                     | Paginated list for a lawyer with full graph                   |
| `findAllByUserIdWithFullGraph(String userId, Pageable)`                         | Paginated list for a user with full graph                     |
| `findByBookingIdWithFullGraph(String bookingId)`                                | Full graph fetch by bookingId (for review creation)           |

**Usage:**

- Lawyer Profile: Display average rating and review count
- Review Display: Show reviews for a lawyer (with full entity graph)
- Analytics Dashboard: Sentiment breakdown for admin
- Review Creation: Check booking doesn't already have a review (prevents duplicates)
- Full Graph Fetches: Used by services mapping to ReviewResponse DTOs

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
| `findByIdWithFullGraph(String id)`                                                 | Full graph fetch: clientLawyer → user, lawyer, lawyer.user            |
| `findAllByUserIdWithFullGraph(String userId, Pageable)`                            | Paginated list for a user with full graph                             |
| `findAllByLawyerIdWithFullGraph(String lawyerId, Pageable)`                        | Paginated list for a lawyer with full graph                           |

**Usage:**

- Case Creation: Verify ClientLawyer relationship exists
- Dashboard: Show connected lawyers/clients with full entity graph
- Booking Confirmation: Auto-create ClientLawyer relationship if not exists
- Case Operations: Only client or lawyer can modify relationship

### CaseRepository

**File:** `src/main/java/com/LHQ_Backend/LHQ_Backend/cases/repository/CaseRepository.java`

**Key Methods:**

| Method                                                                                 | Purpose                                                           |
| -------------------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| `findAllByClientLawyerId(String clientLawyerId, Pageable)`                             | List cases for a ClientLawyer relationship                        |
| `findAllByClientLawyerIdAndStatus(String clientLawyerId, CaseStatus status, Pageable)` | Filter cases by status (OPEN, CLOSED, INACTIVE)                   |
| `findAllByUserId(String userId, Pageable)`                                             | All cases for a client (via ClientLawyer join)                    |
| `findAllByLawyerId(String lawyerId, Pageable)`                                         | All cases for a lawyer (via ClientLawyer join)                    |
| `findAllByUserIdAndStatus(String userId, CaseStatus status, Pageable)`                 | Client's cases filtered by status                                 |
| `findAllByLawyerIdAndStatus(String lawyerId, CaseStatus status, Pageable)`             | Lawyer's cases filtered by status                                 |
| `findByIdAndPrincipal(String caseId, String principalId)`                              | Ownership check: user is client or lawyer in case                 |
| `findByIdWithFullGraph(String id)`                                                     | Full graph fetch: case → clientLawyer → user, lawyer, lawyer.user |
| `findAllByClientLawyerIdWithFullGraph(String clientLawyerId, Pageable)`                | Paginated list by clientLawyerId with full graph                  |

**Usage:**

- Client Dashboard: Show my cases with full entity graph
- Lawyer Dashboard: Show my client cases with full entity graph
- Case Management: List cases by status
- Full Graph Fetches: Used by services mapping to CaseResponse DTOs

## Query Performance Considerations

### N+1 Prevention

- **LawyerProfileRepository.findByIdWithFullGraph()**: Uses FETCH JOIN to load user + specialties in single query
- **BookingRepository.findByIdWithFullGraph()**: Eagerly loads user, lawyer with lawyer's user, and timeSlot
- **ReviewRepository.findByIdWithFullGraph()**: Eagerly loads user, lawyer, lawyer's user, and booking
- **ClientLawyerRepository.findByIdWithFullGraph()**: Eagerly loads user and lawyer with lawyer's user
- **CaseRepository.findByIdWithFullGraph()**: Eagerly loads complete ClientLawyer graph with user and lawyer details

### Pagination

- Use `Pageable` for queries returning large result sets
- `countQuery` is explicitly specified for FETCH JOIN + Pageable combinations
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
