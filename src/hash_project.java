import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class hash_project implements Serializable {

    private ConcurrentHashMap<String, project> progetti;

    public hash_project(){
        //TODO: add initialCapacity and concurrencyLevel
        //utenti = new ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel);
        progetti = new ConcurrentHashMap<>();
    }

    public project get_project(String projectname){
        return progetti.get(projectname);
    }

    public String add_project(String projectname, String username){
        project new_project = new project(projectname);
        if(progetti.putIfAbsent(projectname, new_project) != null) return "Il progetto " + projectname + " è già stato creato";
        if(!new_project.add_member(username)) return "errore nell'aggiunta dell'utente come membro";
        hash_users obj =singleton_db.getInstanceUtenti();
        user utente = obj.get_user(username);
        utente.add_project(projectname);
        //TODO: serializzare e notificare la creazione di un nuovo utente
        return "Il progetto "+projectname+" è stato creato correttamente";
    }


    public String add_projectmember(String projectname, String username){
        project proj = progetti.get(projectname);
        if(proj == null) return "Il progetto "+projectname+" non esiste";
        else if(!proj.add_member(username)) return "l'utente "+username+" è già un membro del progetto";
        hash_users obj = singleton_db.getInstanceUtenti();
        user utente = obj.get_user(username);
        utente.add_project(projectname);
        return "l'utente "+username+" è stato aggiunto correttamente come membro del progetto "+projectname;
    }

    public LinkedList<String> show_members(String projectname){
        project proj = progetti.get(projectname);
        if(proj == null) return null;
        return proj.getMembri();
    }

    public LinkedList<String> show_cards(String projectname){
        project proj = progetti.get(projectname);
        if(proj == null) return null;
        return proj.getAllcards();
    }

    public String show_card(String projectname, String cardname){
        project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        card carta = proj.Getcard(cardname);
        if(carta == null) return "la carta "+cardname+" non esiste";
        return "La carta "+cardname+" si trova nella lista "+carta.getlist()+", la sua descrizione è: "+carta.getDescription();
    }

    public String add_card(String projectname, String cardname, String descrizione){
        project proj = progetti.get(projectname);
        if(proj == null) return"il progetto "+projectname+" non esiste";
        String ret = proj.addcard(cardname, descrizione);
        return ret;
    }

    public String move_card(String projectname, String cardname, String listapartenza, String listadestinazione){
        project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        String ret = proj.movecard(cardname,listapartenza,listadestinazione);
        return ret;
    }

    public String get_cardhistory(String projectname, String cardname){
        project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        card carta = proj.Getcard(cardname);
        if(carta == null) return "la carta "+cardname+" non esiste";
        String s = " ";

        Iterator<String> iterator = (carta.getHistory()).iterator();
        while(iterator.hasNext()){
            s = s+iterator.next()+" ";
        }
        return s;
    }

    public String cancell_project(String projectname){
        project proj = progetti.get(projectname);
        if(proj == null) return "il progetto "+projectname+" non esiste";
        if(!proj.allcarddone()) return "Alcune card non sono state completate, impossibile eliminare il progetto";
        progetti.remove(projectname);
        return "progetto eliminato con successo";
    }

    public boolean member(String projectname){
        return progetti.containsKey(projectname);
    }

}
