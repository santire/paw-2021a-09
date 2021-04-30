package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class PawUserDetailsService implements UserDetailsService {


    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final User user = userService.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email + " not found"));
        if(!user.isActive()) {
            throw new UsernameNotFoundException(email + " not activated");
        }

        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if(userService.isRestaurantOwner(user.getId())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_RESTAURANTOWNER"));
        }


        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), authorities);
    }
}
