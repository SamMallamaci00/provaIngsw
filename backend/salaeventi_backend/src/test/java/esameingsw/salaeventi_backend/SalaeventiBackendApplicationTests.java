package esameingsw.salaeventi_backend;

import esameingsw.salaeventi_backend.persistenza.DBManager;
import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SalaeventiBackendApplicationTests {

    @Test
    public void nuouoUtenteDaFrontend() {
        Cliente c = new Cliente();

        c.setId(null);
        c.setUsername("cliente00");

        c.setEmail("emailprova@gmail.com");
        c.setPassword("password00");
        c.setPermessi("cliente");
        DBManager.getInstance().getClienteDao().saveOrUpdate(c);
    }

}
