# PoE Project - PROG5121

## Part 2 - QuickChat Message Module & Unit Testing

### Overview
Part 2 introduces the `Message` class and integrates a full messaging workflow into the application. It includes recipient cell phone validation, unique ID/Hash generation, message status handling, and local JSON-formatted storage.

### Key Features Implemented
* **Message Class (`Message.java`)**:
  * Auto-generates a unique **Message ID** (<= 10 characters).
  * Validates international recipient cell phone numbers (e.g., `+27...`).
  * Generates a unique **Message Hash** combining the ID, recipient number, and text payload.
* **QuickChat Menu Integration**:
  * Interactive loop allowing users to send messages, view message history/hashes, or exit.
* **Unit Testing (`MessageNGTest.java`)**:
  * Automated tests verifying Message ID validation, cell phone format checks, and Hash generation logic.

### Branch Structure
* **Part 2 Branch**: `Part2`
