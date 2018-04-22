/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.model.dao;

import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.sample.controller.utils.EncryptionException;
import org.sample.controller.utils.EncryptionUtils;
import org.sample.model.entities.User;

/**
 *
 */
public class UserDAO implements Serializable {

    private final SessionFactory sessionFactory;

    public UserDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public User getUserByUserNameAndPassword(String userName, String password) throws EncryptionException {
        Session session = sessionFactory.openSession();

        String encryptPassword = (new EncryptionUtils()).encrypt(password);

        User aUser = (User) session.getNamedQuery("findUserByUserNameAndPassword")
                .setString("userName", userName)
                .setString("password", encryptPassword)
                .uniqueResult();
        session.close();
        return aUser;
    }

    public void saveUser(User user) {

        Session session = sessionFactory.openSession();
        System.out.println("----------------->" + user.getPictureRef());
        session.merge(user);
        session.flush();
        session.close();
    }

    public User getUserById(Integer id) {
        Session session = sessionFactory.openSession();
        User foundUser = (User) session.createQuery("from User where id=" + id).uniqueResult();

        session.close();
        return foundUser;
    }

}
