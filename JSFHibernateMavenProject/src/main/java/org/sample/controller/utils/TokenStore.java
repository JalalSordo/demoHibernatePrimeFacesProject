/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.utils;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 */
public class TokenStore {

    public static ArrayList<String> currentToken = new ArrayList<>();

    public static String createToken() {
        String token = UUID.randomUUID().toString();
        registerToken(token);
        return token;
    }

    public static void registerToken(String token) {
        currentToken.add(token);
    }

    public static boolean validateToken(String token) {
        return currentToken.contains(token);
    }

}
