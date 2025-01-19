package esameingsw.salaeventi_backend.controller;

import esameingsw.salaeventi_backend.persistenza.DBManager;
import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
public class Auth {
    private class AuthToken {
        String token;
        Cliente cliente;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }
    }


    @PostMapping("/login")
    public AuthToken login(@RequestBody Cliente cliente, HttpServletRequest req) throws Exception {
        String email = cliente.getEmail();
        String rawPassword = cliente.getPassword();

        // Instanzia l'encoder per comparare le password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Cerca l'utente nel database usando l'email
        Cliente clienteDB = DBManager.getInstance().getClienteDao().findByEmail(email);
        if (clienteDB != null && passwordEncoder.matches(rawPassword, clienteDB.getPassword())) {
            // Genera un token semplice concatenando email e una stringa codificata
            String encodedToken = codificaBase64(email + ":" + clienteDB.getPassword());

            // Crea la sessione utente
            HttpSession session = req.getSession();
            session.setAttribute("user", clienteDB);

            // Crea l'oggetto AuthToken da restituire
            AuthToken auth = new AuthToken();
            auth.setToken(encodedToken);
            auth.setCliente(clienteDB);
            return auth;
        } else {
            // Caso in cui le credenziali sono errate
            System.out.println("Utente non trovato o password errata");
            throw new Exception("Invalid credentials");
        }
    }



    @PostMapping("/logout")
    public boolean logout(@RequestBody Cliente cliente, HttpServletRequest req) throws Exception{
        return true;
    }



    @PostMapping("/isAuthenticated")
    public boolean isAuthenticated(@RequestBody Cliente cliente, HttpServletRequest req) throws Exception{
        String auth = req.getHeader("Authorization");
        if (auth != null){
            String token = auth.substring("Basic".length());
            return getClienteByToken(token) != null;
        }else {
            return false;
        }
    }

    public static Cliente getClienteByToken(String token) {
        if (token != null) {
            // Decodifica il token
            String decoded = decodificaBase64(token);
            String[] tokenParts = decoded.split(":");
            if (tokenParts.length != 2) {
                return null; // Token malformato
            }

            String email = tokenParts[0];
            String passwordHash = tokenParts[1];
            System.out.println("Dentro getUserByToken, email: " + email + " | password hash: " + passwordHash);

            // Trova l'utente dal database usando l'email
            Cliente clienteDB = DBManager.getInstance().getClienteDao().findByEmail(email);
            if (clienteDB != null && clienteDB.getPassword().equals(passwordHash)) {
                return clienteDB;
            } else {
                System.out.println("Utente non trovato o token non valido");
            }
        }
        return null;
    }


    private static String codificaBase64(String value){
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    private static String decodificaBase64(String value){
        return new String(Base64.getDecoder().decode(value.getBytes()));
    }
}