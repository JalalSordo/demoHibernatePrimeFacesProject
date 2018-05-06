/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.filters;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class ImagesServlet extends HttpServlet {

    public static final String IMAGE_STORE = System.getProperty("user.home") + "/app_files/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageId = String.valueOf(request.getPathInfo().substring(1)); // Gets string that goes after "/images/".
        File file = new File(IMAGE_STORE + imageId);
        RandomAccessFile ra = new RandomAccessFile(file, "rw");
        byte[] imageData = new byte[(int) file.length()];
        try {
            ra.read(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setContentLength(imageData.length);
        response.getOutputStream().write(imageData);
    }

}
