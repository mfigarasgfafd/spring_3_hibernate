package org.example.authenticate;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.configuration.HibernateUtil;
import org.example.dao.IUserRepository;
import org.example.dao.hibernate.UserDAO;
import org.example.model.User;

public class Authenticator {
    public static User login(String login, String password){
        IUserRepository jur = UserDAO.getInstance(HibernateUtil.getSessionFactory());
        User userFromDb = jur.getUser(login);
        if ( userFromDb!= null && hashPassword(password).equals(userFromDb.getPassword())) {
            return userFromDb;
        }
        return null;
    }

    public static String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }

}
