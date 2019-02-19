package lu.plezy.timesheet.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.repository.UsersRepository;

/*
 * UserDetailsServiceImpl implements UserDetailsService and overrides loadUserByUsername() method.
 * 
 * loadUserByUsername method finds a record from users database tables to build a UserDetails object for authentication.
 * 
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with -> username : " + username));

        return UserInfo.build(user);
    }

}