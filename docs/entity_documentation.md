# Entity Documentation

This document describes all JPA entities currently defined in LHQ_Backend.

## Conventions

- IDs are String values generated with UuidGenerator.
- Most relationships use FetchType.LAZY.
- created_at fields use CreationTimestamp where present.

## User

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/user/entity/User.java
- Table: users
- Fields:
  - id (PK)
  - first_name (required)
  - last_name (required)
  - email (required, unique)
  - password (required)
  - age (optional)
  - role (enum, required)
  - created_at

## LawyerProfile

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/lawyer/entity/LawyerProfile.java
- Table: lawyer_profiles
- Fields:
  - id (PK)
  - user_id (one-to-one, required, unique)
  - bio
  - about
  - rate (precision 10, scale 2)
  - bar_number (unique)
  - years_of_experience
  - created_at
- Relationships:
  - Many-to-many with Specialty through lawyer_specialties
  - specialties defaults to empty Set using Builder.Default

## Specialty

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/lawyer/entity/Specialty.java
- Table: specialties
- Fields:
  - id (PK)
  - name (required, unique)

## AvailabilityTemplate

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/booking/entity/AvailabilityTemplate.java
- Table: availability_templates
- Fields:
  - id (PK)
  - lawyer_id (many-to-one, required)
  - day_of_week (enum, required)
  - start_time (required)
  - end_time (required)
  - slot_duration_minutes (required)
  - is_active (required, default true)
  - created_at

## TimeSlot

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/booking/entity/TimeSlot.java
- Table: time_slots
- Fields:
  - id (PK)
  - lawyer_id (many-to-one, required)
  - template_id (many-to-one, required)
  - start_time (required)
  - end_time (required)
  - status (enum, required, default AVAILABLE)
  - created_at

## Booking

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/booking/entity/Booking.java
- Table: bookings
- Fields:
  - id (PK)
  - user_id (many-to-one, required)
  - lawyer_id (many-to-one, required)
  - time_slot_id (one-to-one, required, unique)
  - charges (precision 10, scale 2)
  - notes
  - created_at

## Review

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/review/entity/Review.java
- Table: reviews
- Fields:
  - id (PK)
  - user_id (many-to-one, required)
  - lawyer_id (many-to-one, required)
  - booking_id (one-to-one, required, unique)
  - rating (required)
  - comment
  - sentiment (enum)
  - created_at

## ClientLawyer

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/cases/entity/ClientLawyer.java
- Table: client_lawyer
- Fields:
  - id (PK)
  - user_id (many-to-one, required)
  - lawyer_id (many-to-one, required)
  - status (enum, required, default ACTIVE)
  - created_at
- Constraints:
  - Composite unique constraint on (user_id, lawyer_id)
  - Prevents duplicate client-lawyer pairs

## Case

- File: src/main/java/com/LHQ_Backend/LHQ_Backend/cases/entity/Case.java
- Table: cases
- Fields:
  - id (PK)
  - client_lawyer_id (many-to-one, required)
  - title (required)
  - description
  - case_type
  - status (enum, required, default OPEN)
  - opened_at
  - closed_at

## Relationship Summary

- User -> Booking: one-to-many (via Booking.user)
- LawyerProfile -> Booking: one-to-many (via Booking.lawyer)
- Booking -> Review: one-to-one
- LawyerProfile -> Review: one-to-many
- User -> Review: one-to-many
- LawyerProfile <-> Specialty: many-to-many via lawyer_specialties
- User <-> LawyerProfile link for representation: modeled by ClientLawyer with unique (user_id, lawyer_id)
- ClientLawyer -> Case: one-to-many (via Case.clientLawyer)
