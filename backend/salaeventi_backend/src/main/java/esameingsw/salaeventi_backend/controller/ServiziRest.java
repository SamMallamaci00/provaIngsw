package esameingsw.salaeventi_backend.controller;

import esameingsw.salaeventi_backend.persistenza.DBManager;
import esameingsw.salaeventi_backend.persistenza.dao.ClienteDao;
import esameingsw.salaeventi_backend.persistenza.dao.PrenotazioneDao;
import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import esameingsw.salaeventi_backend.persistenza.model.Prenotazione;
import esameingsw.salaeventi_backend.persistenza.model.SalaEvento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
public class ServiziRest {

    @PostMapping("addCliente")
    public ResponseEntity<String> addCliente(@RequestBody Cliente cliente){

        DBManager.getInstance().getClienteDao().saveOrUpdate(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creato con successo!");

    }

    @GetMapping("getCliente")
    public Cliente getCliente(@RequestParam ("email") String email){

        Cliente c = DBManager.getInstance().getClienteDao().findByEmail(email);



        System.out.println("dentro getcliente");
        System.out.println("idcliente: " + c.getId());
        System.out.println("username: " + c.getUsername());

        return c;

    }

    @GetMapping("getPermessi")
    public String getPermessi(@RequestParam ("email") String email){

        String r = DBManager.getInstance().getClienteDao().findByEmail(email).getPermessi();

        System.out.println("dentro getcliente");
        return r;

    }



    @PostMapping("addSala")
    public ResponseEntity<String> addSala(@RequestBody SalaEvento salaEvento){

        System.out.println("Dentro addSala");

        DBManager.getInstance().getSalaEventoDao().saveOrUpdate(salaEvento);

        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creato con successo!");

    }

    @GetMapping("getSale")
    public List<SalaEvento> getSale(){
        return DBManager.getInstance().getSalaEventoDao().findAll();

    }

  @PostMapping("registrazione")
  public ResponseEntity<String> prenotaSala(@RequestParam ("username") String username, @RequestParam ("email") String email,
                                            @RequestParam ("password") String password){


    if (DBManager.getInstance().getClienteDao().findByEmail(email) != null){
      System.out.println("Esiste già un utente che utilizza l'email selezionata.");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Esiste già un utente che utilizza l'email selezionata.");
    }
    else if(DBManager.getInstance().getClienteDao().findByUsername(username)!= null){
      System.out.println("Esiste già un utente con l'username inserito.");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Esiste già un utente con l'username inserito.");
    }
    else{
      Cliente cliente = new Cliente();

      cliente.setId(null);
      cliente.setUsername(username);
      cliente.setPermessi("cliente");
      cliente.setEmail(email);
      cliente.setPassword(password);

      DBManager.getInstance().getClienteDao().saveOrUpdate(cliente);
      return ResponseEntity.status(HttpStatus.CREATED).body("Sala prenotata creato con successo!");

    }

  }


    @PostMapping("prenotaSala")
    public ResponseEntity<String> prenotaSala(@RequestParam ("sala") Long idSala, @RequestParam ("cliente") String email,
                                             @RequestParam ("data") Date data){

        SalaEvento sala = DBManager.getInstance().getSalaEventoDao().findById(idSala);
        Cliente cliente = DBManager.getInstance().getClienteDao().findByEmail(email);


        if (DBManager.getInstance().getPrenotazioneDao().DispSalaByDate(sala, data)){

            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setSala(sala);
            prenotazione.setData(data);
            prenotazione.setCliente(cliente);
            prenotazione.setCosto(sala.getPrezzo());

            prenotazione.setId(0L);

            DBManager.getInstance().getPrenotazioneDao().saveOrUpdate(prenotazione);

            return ResponseEntity.status(HttpStatus.CREATED).body("Sala prenotata creato con successo!");

        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La sala non è più disponibile per la data selezionata.");

        }



    }

    @PostMapping("updatePren")
    public ResponseEntity<String> updatePren(@RequestParam("prenotazione") Long id, @RequestParam("data") Date data) {

        Prenotazione prenotazione = DBManager.getInstance().getPrenotazioneDao().findById(id);

        if (DBManager.getInstance().getPrenotazioneDao().DispSalaByDate(prenotazione.getSala(), data)) {
            prenotazione.setData(data);

            DBManager.getInstance().getPrenotazioneDao().saveOrUpdate(prenotazione);

            return ResponseEntity.status(HttpStatus.CREATED).body("Prenotazione aggiornata con successo!");
        } else {
            // Ritorna un errore di conflitto con un messaggio appropriato
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La sala non è disponibile per la data selezionata.");
        }
    }

    @GetMapping("verificaDisp")
    public boolean verificaDisp(@RequestParam ("sala") Long idSala, @RequestParam ("data") Date data){

        SalaEvento sala = DBManager.getInstance().getSalaEventoDao().findById(idSala);

        boolean risultato = DBManager.getInstance().getPrenotazioneDao().DispSalaByDate(sala, data);

        System.out.println(risultato);

        return risultato;

    }

    @GetMapping("getPrenCliente")
    public List<Prenotazione> getPrenCliente(@RequestParam ("cliente") String email){

        Cliente cliente = DBManager.getInstance().getClienteDao().findByEmail(email);

        List<Prenotazione> prenotazioni = DBManager.getInstance().getPrenotazioneDao().findByCliente(cliente);

        for(Prenotazione p : prenotazioni){
            System.out.println(p.getCosto());
        }

        return DBManager.getInstance().getPrenotazioneDao().findByCliente(cliente);



    }

    @GetMapping("getAllPren")
    public List<Prenotazione> getAllPren(){
        System.out.println("dentro getAllPren");

        List<Prenotazione> prenotazioni = DBManager.getInstance().getPrenotazioneDao().findAll();

        for(Prenotazione p : prenotazioni){
            System.out.println(p.getId());
        }


        return  DBManager.getInstance().getPrenotazioneDao().findAll();

    }


    @PostMapping("deletePren")
    public ResponseEntity<String> deletePren(@RequestParam("idPren") long idPren){


        Prenotazione p = DBManager.getInstance().getPrenotazioneDao().findById(idPren);
        DBManager.getInstance().getPrenotazioneDao().delete(p);



        return ResponseEntity.status(HttpStatus.CREATED).body("Prenotazione creata con successo!");

    }

}
