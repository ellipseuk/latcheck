package com.latcheck.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    @Test
    void testValidInput() {
        MainController controller = new MainController();

        // valid URL and valid count
        assertTrue(controller.isValidInput("https://example.com", "10"));

        // valid URL and valid count with leading/trailing spaces
        assertFalse(controller.isValidInput("https://example.com", "0"));
        assertFalse(controller.isValidInput("https://example.com", "1001"));
        assertFalse(controller.isValidInput("https://example.com", "abc"));

        // invalid URL
        assertFalse(controller.isValidInput("", "10"));
        assertFalse(controller.isValidInput(null, "10"));
        assertFalse(controller.isValidInput("   ", "10"));
    }
}