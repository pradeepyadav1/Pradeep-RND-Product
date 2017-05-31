package com.fstm.process.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fstm.bean.LoginData;
import com.fstm.data.access.interfaces.UserDetailDAO;
import com.fstm.data.model.UserDetails;
import com.fstm.exception.custom.AccountLockException;
import com.fstm.exception.custom.UserNotFoundException;

/**
 * The Class LoginProcess.
 */
@Service("loginProcess")
@Transactional(readOnly=true)
public class LoginProcessImpl implements LoginProcess {

    /** The user dao. */
    @Autowired
    private UserDetailDAO userDetailDAO;

    /* (non-Javadoc)
	 * @see com.fstm.process.user.LoginProcess#login(com.fstm.bean.LoginData)
	 */
    @Transactional
    @Override
    public boolean login(LoginData loginData) {
				
    			UserDetails user = userDetailDAO.findByEmailAndTenant(loginData);
    			
    			if(null == user)
    				throw new UserNotFoundException();
    			
    			if(user.getFailedLoginAttampt().intValue() > 2)
    				throw new AccountLockException();
    			
    			// check password stored into database matches with the password entered by the user
    			if(user.getPassword().equals(loginData.getPassword())){
    				// to avoid database hit if already failed login attempt is zero.
    				if(user.getFailedLoginAttampt().intValue() != 0 ){
    					updateFailedAttempt(user,0);
    				}
    				return Boolean.TRUE;
    			}else{
    				// if password does not match update failed login attempt by 1.
    				updateFailedAttempt(user, user.getFailedLoginAttampt() + 1);
    				return Boolean.FALSE;
    			}
    			
    }
    
    private void updateFailedAttempt(final UserDetails user,final int failedLoginAttempt){
			user.setFailedLoginAttampt(failedLoginAttempt);
			userDetailDAO.update(user);
    }
}
