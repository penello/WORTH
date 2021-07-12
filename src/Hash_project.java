import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;


public class Hash_project implements Serializable {

    private ConcurrentHashMap<String, Project> progetti;

    public Hash_project(){
        progetti = new ConcurrentHashMap<>();
    }

    /**
     * @param projectname
     * @return il progetto di nome projectname
     */
    public Project get_project(String projectname){
        return progetti.get(projectname);
    }

    /**
     * metodo utilizzato per creare un nuovo progettp
     * @param projectname
     * @param username
     * @return il risultato dell'operazione
     */
    public String add_project(String projectname, String username){
        Project new_project = new Project(projectname);
        if(progetti.putIfAbsent(projectname, new_project) != null) return "Il progetto " + projectname + " è già stato creato";
        if(!new_project.add_member(username)) return "errore nell'aggiunta dell'utente come membro";
        Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
        User utente = obj.get_user(username);
        utente.add_project(projectname);
        return "SUCCESS";
    }

    /**
     * metodo usato per recuperare dati salvati dei progetti
     * @param projectname
     * @return il risultato dell'operazione
     */
    public boolean restore_project(String projectname){
        Project new_project = new Project(projectname);
        return progetti.putIfAbsent(projectname, new_project) == null;
    }

    /**
     * metodo per recuperare i dati salvati relativi ai membri del progetto
     * @param member
     * @param projectname
     */
    public void restore_member(LinkedList<String> member, String projectname){
        Project prj = progetti.get(projectname);
        prj.copy_member(member);
    }

    /**
     * metodo usato per recuperare i dati salvati relativi alle card del progetto
     * @param carta
     * @param projectname
     */
    public void restore_card(Card carta, String projectname){
        Project prj = progetti.get(projectname);
        prj.copy_card(carta);
    }

    /**
     * metodo usato per aggiungere un nuovo progetto alla strutura dati
     * @param projectname
     * @param username
     * @return il risultato dell'operazione
     */
    public String add_projectmember(String projectname, String username){
        Project proj = progetti.get(projectname);
        if(proj == null) return "Il progetto "+projectname+" non esiste";
        Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
        User utente = obj.get_user(username);
        if(utente == null) return "L'utente non esiste";
        else if(!proj.add_member(username)) return "l'utente "+username+" è già un membro del progetto";
        utente.add_project(projectname);
        return "SUCCESS";
    }

    /**
     * metodo usato per recuperare la lista dei membri del progetto
     * @param projectname
     * @param username
     * @return il risultato dell'operazione
     */
    public LinkedList<String> show_members(String projectname, String username){
        Project proj = progetti.get(projectname);
        if(proj == null) return null;
        else if(!proj.containsmember(username)) return null;
        return proj.getMembri();
    }

    /**
     * metodo usato per recuperare la lista delle carte del progetto
     * @param projectname
     * @param username
     * @return il risultato dell'operazione
     */
    public LinkedList<String> show_cards(String projectname,String username){
        Project proj = progetti.get(projectname);
        if(proj == null) return null;
        else if(!proj.containsmember(username)) return null;
        return proj.getAllcards();
    }

    /**
     * metodo usato per recuperare le informazioni relative a una card del progetto
     * @param projectname
     * @param cardname
     * @return il risultato dell'operazione
     */
    public String show_card(String projectname, String cardname){
        Project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        Card carta = proj.Getcard(cardname);
        if(carta == null) return "la carta "+cardname+" non esiste";
        return "SUCCESS La carta "+cardname+" si trova nella lista "+carta.getlist()+", la sua descrizione è: "+carta.getDescription();
    }

    /**
     * metodo usato per aggiungere una card al progetto
     * @param projectname
     * @param cardname
     * @param descrizione
     * @return il risultato dell'operazione
     */
    public String add_card(String projectname, String cardname, String descrizione){
        Project proj = progetti.get(projectname);
        if(proj == null) return"il progetto "+projectname+" non esiste";
        String ret = proj.addcard(cardname, descrizione);
        return ret;
    }

    /**
     * metodo usato per richiedere lo spostamento in un'altra lista di una card del progetto
     * @param projectname
     * @param cardname
     * @param listapartenza
     * @param listadestinazione
     * @return il risultato dell'operazione
     */
    public String move_card(String projectname, String cardname, String listapartenza, String listadestinazione){
        Project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        String ret = proj.movecard(cardname,listapartenza,listadestinazione);
        return ret;
    }

    /**
     * metodo usato per recuperare la lista dei movimenti di una card del progetto
     * @param projectname
     * @param cardname
     * @return il risultato dell'operazione
     */
    public String get_cardhistory(String projectname, String cardname){
        Project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        Card carta = proj.Getcard(cardname);
        if(carta == null) return "la carta "+cardname+" non esiste";
        String s = "SUCCESS ";

        Iterator<String> iterator = (carta.getHistory()).iterator();
        while(iterator.hasNext()){
            s = s+iterator.next()+"|";
        }
        return s;
    }

    /**
     * metodo usato per cancellare un progetto dalla struttura dati
     * @param projectname
     * @param username
     * @return il risultato dell'operazione
     */
    public String cancell_project(String projectname, String username){
        Project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        else if(!proj.allcarddone()) return "Alcune card non sono state completate, impossibile eliminare il progetto";
        LinkedList<String> membri = proj.getMembri();
        Iterator<String> iterator = membri.iterator();
        while(iterator.hasNext()){
            User ut = Singleton_db_utenti.getInstanceUtenti().get_user(iterator.next());
            if(!ut.cancel_project(projectname)) return "Impossibile eliminare il progetto";
        }
        progetti.remove(projectname);
        return "SUCCESS";
    }

}
