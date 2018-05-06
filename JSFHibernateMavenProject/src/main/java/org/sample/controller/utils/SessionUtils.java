/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.utils;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.sample.model.entities.User;

/**
 *
 */
public class SessionUtils implements Serializable {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.
                getCurrentInstance().
                getExternalContext().
                getSession(false);
    }

    public static User getConnectedUser() {
        return (User) getSession().getAttribute("user");
    }

    public static void setConnectedUser(User connectedUser) {
        getSession().setAttribute("user", connectedUser);
    }
}
