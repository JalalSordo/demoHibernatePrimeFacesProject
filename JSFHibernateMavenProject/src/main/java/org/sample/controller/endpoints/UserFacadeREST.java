/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.endpoints;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.sample.controller.utils.TokenStore;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import org.sample.controller.utils.EncryptionException;
import org.sample.controller.utils.RESTUtils;
import org.sample.model.dao.UserDAO;
import org.sample.model.entities.User;

/**
 *
 */
@Path("user")
public class UserFacadeREST {

    private final UserDAO userDAO;

    public UserFacadeREST() {
        userDAO = new UserDAO();
    }

    @GET
    @Path("authenticate")
    public String authenticate(@Context HttpServletResponse servletResponse,
            @HeaderParam("userName") String userName,
            @HeaderParam("password") String password) {
        try {
            if (userDAO.getUserByUserNameAndPassword(userName, password) != null) {
                servletResponse.setHeader("token", TokenStore.createToken());
                return "OK";
            }
        } catch (EncryptionException ex) {
             return ex.getMessage();
        }
        return "Incorrect username or password";
    }

    @GET
    @Path("{id}")
    public String findById(@HeaderParam("token") String token, @PathParam("id") Integer id) {
        if (TokenStore.validateToken(token)) {
            return RESTUtils.JSONFactory.toJson(userDAO.getUserById(id));
        } else {
            return "Invalid Token";
        }
    }

    @PUT
    @Path("update")
    public String update(@HeaderParam("token") String token, String user) {
        if (TokenStore.validateToken(token)) {
            userDAO.saveUser(RESTUtils.JSONFactory.fromJson(user, User.class));
            return "OK";
        } else {
            return "Invalid Token";
        }
    }

}
