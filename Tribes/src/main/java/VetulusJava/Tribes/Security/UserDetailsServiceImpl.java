package VetulusJava.Tribes.Security;

import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Services.UserService.IUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("useDB")
public class UserDetailsServiceImpl implements UserDetailsService {

    private IUserService userService;
    public UserDetailsServiceImpl(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            ApplicationUser applicationUser = userService.getUser(username);
        if(applicationUser == null){
            throw new UsernameNotFoundException( "name not found");
        }
        return buildUserForAuthentication(applicationUser, new ArrayList<>());
    }

    private User buildUserForAuthentication(ApplicationUser user, List<GrantedAuthority> authorities) {
        String username = user.getName();
        String password = user.getPassword();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        UUID id = user.getId();
        CurrentUser currentUser = new CurrentUser(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
        currentUser.setId(id);
        return currentUser;
    }
}
