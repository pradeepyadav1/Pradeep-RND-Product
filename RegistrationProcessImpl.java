package com.fstm.process.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fstm.bean.LoginData;
import com.fstm.data.access.interfaces.UserDetailDAO;
import com.fstm.data.model.UserDetails;
import com.fstm.exception.custom.UserNotFoundException;

/**
 * The Class RegistrationProcess.
 */
@Service("registrationProcess")
@Transactional(readOnly=true)
public class RegistrationProcessImpl implements RegistrationProcess {

    /** The user dao. */
    @Autowired
    private UserDetailDAO userDetailDAO;

   
    @Transactional
    @Override
    public UserDetails registerUser(UserDetails userData) {
           return userDetailDAO.save(userData);
    }

    /**
     * Generate password.
     *
     * @return the string
     */
    private static final String generatePassword() {
    	return UUID.randomUUID().toString().substring(0, 6);

    }

    /* (non-Javadoc)
	 * @see com.fstm.process.user.RegistrationProcess#updatePassword(com.fstm.bean.LoginData)
	 */
    @Transactional
    @Override
    public String resetPassword(LoginData loginData) {
        UserDetails user = userDetailDAO.findByEmail(loginData.getEmail());
        if(null == user)
        	throw new UserNotFoundException();
        String newPassword = generatePassword();
        user.setPassword(newPassword);
        userDetailDAO.update(user);
        return newPassword;
    }
    
    /**
     * Change Password via UI
     */
    @Transactional
    @Override
    public String changePassword(LoginData loginData) {
    	UserDetails user = userDetailDAO.findByEmailAndPassword(loginData);
    	 if(null == user)
         	throw new UserNotFoundException();
        String newPassword = loginData.getNewPassword();
        user.setPassword(newPassword);
        userDetailDAO.update(user);
        return newPassword;
    }

}
