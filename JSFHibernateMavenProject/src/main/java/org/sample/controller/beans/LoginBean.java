/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.beans;

import org.sample.model.dao.UserDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.sample.controller.utils.EncryptionException;
import org.sample.controller.utils.SessionUtils;

import org.sample.model.entities.User;

/**
 *
 */
@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private String userName;
    private String password;

    private UserDAO userDAO;

    @PostConstruct
    public void init() {
       
            userDAO = new UserDAO();
  
    }

    public String doLogin() {

        try {
            User aUser = userDAO.getUserByUserNameAndPassword(userName, password);
            if (aUser != null) {
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("user", aUser);
                return "index?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Échec de la connexion", "Mauvais nom d'utilisateur ou mot de passe"));
            }
        } catch (EncryptionException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Échec de la connexion", "Problem de lors de l'encodage du mot de passe"));
        }
          return "login";
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
