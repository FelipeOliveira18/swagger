package swagger.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

// Centraliza as configurações de segurança da API
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	// Obtém a senha do usuário comum através de uma variável de ambiente.
	@Value("${app.security.user-password}")
	private String userPassword;

	// Obtém a senha do administrador através de uma variável de ambiente.
	@Value("${app.security.admin-password}")
	private String adminPassword;


	// Define as regras de autenticação e autorização das requisições HTTP
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


	    http
			// Habilita autenticação HTTP Basic
	        .httpBasic(withDefaults())

			// Define quais usuarios podem acessar cada rota
	        .authorizeHttpRequests(authz -> authz

					// Permite acesso a console do H2 sem autenticação
	        	.requestMatchers("/h2-console").permitAll()

					// As rotas de clientes exigem a função USER
	            .requestMatchers("/api/clientes/**").hasRole("USER")
					// As demais rotas exigem uma das funções permitidas
	            .anyRequest().hasAnyRole("ADMIN", "USER")
	        )
				// Desabilita CSRF para facilitar o uso da API REST
	        .csrf(csrf -> csrf.disable());
	    return http.build();
	}
	// Cria os usuários utilizados pela aplicação
	// Neste projeto eles ficam armazenados apenas em memória
	 @Bean
	    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
	        UserDetails user1 = User.withUsername("user1")
	            .password(passwordEncoder.encode(userPassword))
	            .roles("USER")
	            .build();

	        UserDetails admin = User.withUsername("admin")
	            .password(passwordEncoder.encode(adminPassword))
	            .roles("ADMIN")
	            .build();

	        return new InMemoryUserDetailsManager(user1, admin);
	    }
		// Define o algoritmo utilizado para criptografar as senhas
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
		// Compara uma senha informada com uma senha criptografada
	    public boolean verificarSenha(String senhaRaw, String senhaHash) {
		    PasswordEncoder passwordEncoder = passwordEncoder();
	        return passwordEncoder.matches(senhaRaw, senhaHash);
	    }

	
}
