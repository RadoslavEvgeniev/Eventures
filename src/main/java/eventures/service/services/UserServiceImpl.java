package eventures.service.services;

import eventures.domain.entities.User;
import eventures.domain.entities.UserRole;
import eventures.domain.models.service.UserServiceModel;
import eventures.service.repositories.RoleRepository;
import eventures.service.repositories.UserRepository;
import eventures.utils.MappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = this.userRepository.findByUsername(username).orElse(null);
        if (!username.contains("@")) {
            userDetails = this.userRepository.findByUsername(username).orElse(null);
        } else {
            userDetails = this.userRepository.findByEmail(username).orElse(null);
            if (userDetails == null) {
                throw new UsernameNotFoundException("Invalid user.");
            }

            ((User) userDetails).setUsername(((User) userDetails).getEmail());
        }

        if (userDetails == null) {
            throw new UsernameNotFoundException("Invalid user.");
        }

        return userDetails;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        User user = (User) MappingUtil.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        if (this.userRepository.count() == 0) {
            this.seedRolesInDb();
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ADMIN"));
        }

        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));

        this.userRepository.save(user);
    }

    private void seedRolesInDb() {
        if (this.roleRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setAuthority("ROLE_USER");
            UserRole adminRole = new UserRole();
            adminRole.setAuthority("ROLE_ADMIN");
            this.roleRepository.save(userRole);
            this.roleRepository.save(adminRole);
        }
    }
}
