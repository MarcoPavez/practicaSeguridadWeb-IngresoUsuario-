package seguridad.seguridadingresousuario.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import org.springframework.security.core.userdetails.User;

import seguridad.seguridadingresousuario.model.Authority;
import seguridad.seguridadingresousuario.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    UserRepository userRepository;

    // Throws son expeciones, si tira error devuelve esto sino lo otro
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario con el repository, si no existe se lanza(throw) una
        // exepcion (generada por nosotros) SE CREA UN ERROR
        seguridad.seguridadingresousuario.model.User appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("no existe el usuario"));

        // Mappear lista de authority con propiedades de spring security

        List grantList = new ArrayList();

        for (Authority authority : appUser.getAuthority()) {
            // Aca devuelve el rol del usuario, si es user, admin, cliente etc, y lo guarda
            // en grantedAuthority
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
            grantList.add(grantedAuthority);
        }

        // Creacion Userdetails, esto va a retornar finalmente los datos del usuario (se
        // le entrega user, password y rol de usuario -grantlist-)
        UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), grantList);

        return user;
    }
}
