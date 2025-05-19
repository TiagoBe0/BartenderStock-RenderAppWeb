package com.proyecto.demo.security; // Asegúrate de que este sea tu paquete correcto

import com.proyecto.demo.servicios.UsuarioServicio; // Tu servicio de usuario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Habilita la seguridad a nivel de método (ej. @PreAuthorize)
public class Security extends WebSecurityConfigurerAdapter {

    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    public void configuracionGlobal(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder())
         ;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("LLEGAMOS AL LOGIN SECURITY");
        http
               .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
               .antMatchers("/css/*", "/js/*", "/img/*",
                        "/**").permitAll()
                .and().
                formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/inicio")
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
               .logoutSuccessUrl("/login?logout")             
               .permitAll().
              and().csrf().disable();
   }
}