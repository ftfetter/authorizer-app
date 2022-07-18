package com.github.ftfetter.challenge.authorizer.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorizerIOTest {

    @Test
    @DisplayName("When something is written in the stdin, then should be returned as string")
    void readTest() {
        String input = "TEST STRING INPUT";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        AuthorizerIO authorizerIO = new AuthorizerIO(new Scanner(System.in));

        String actual = authorizerIO.readInput().get();

        assertEquals(input, actual);
    }

    @Test
    @DisplayName("When a string is passed, then should print that in the stdout")
    void writeTest() {
        String output = "TEST STRING OUTPUT";
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        AuthorizerIO authorizerIO = new AuthorizerIO(new Scanner(System.in));

        authorizerIO.writeOutput().accept(output);
        String actual = out.toString();

        assertEquals(output+"\n", actual);
    }
}