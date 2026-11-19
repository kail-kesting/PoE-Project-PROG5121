# PROG5121 — Programming 1A POE (Part 3)

**Student:** Kail Kesting  
**Module:** PROG5121 (Programming 1A)  
**Branch:** `Part3`

## Overview

This branch contains the **Part 3** implementation for the PROG5121 Portfolio of Evidence (PoE) messaging application in Java, building on user authentication and Part 2 QuickChat messaging functionality.

## Part 3 Features

- **Stored Messages Menu (`4) Stored Messages`):** Added to the QuickChat console menu in `PoEPart1.java`, opening the stored messages sub-menu (`storedMessagesMenu()`) with safe numeric input handling.
- **Array & JSON Operations (`Message.java`):** Loads and manages stored messages from `messages.json` and supports:
  1. Displaying the sender and recipient of all stored messages.
  2. Finding and displaying the longest stored message.
  3. Searching by Message ID to display the corresponding recipient and message.
  4. Searching for all stored messages for a specific recipient.
  5. Deleting a stored message by Message Hash and updating `messages.json`.
  6. Generating a full report listing the details of all stored messages.
- **Unit Tests (`MessageNGTest.java`):** TestNG tests verifying Part 2 and Part 3 messaging, array search/reporting, and deletion behavior alongside existing project tests.

## Project Structure

- `src/poe/part1/` — Application entry point (`PoEPart1.java`), messaging and array logic (`Message.java`), and login/registration classes.
- `test/poe/part1/` — TestNG unit tests (`MessageNGTest.java` and related test suites).
- `messages.json` — Stored message records used by the Part 3 stored messages features.
- `nbproject/` & `build.xml` — Apache NetBeans project configuration and Ant build script.
