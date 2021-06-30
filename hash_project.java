import java.io.Serializable;
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
        //TODO: aggiungere utente che ha creato il progetto
        //TODO: serializzare e notificare la creazione di un nuovo utente
        return "Il progetto "+projectname+" è stato creato correttamente";
    }


    public String add_projectmember(String projectname, String username){
        project proj = progetti.get(projectname);
        if(proj == null) return "Il progetto "+projectname+" non esiste";
        else if(!proj.add_member(username)) return "l'utente "+username+" è già un membro del progetto";
        return "l'utente "+username+" è stato aggiunto correttamente come membro del progetto "+projectname;
    }

    public LinkedList<String> show_members(String projectname){
        project proj = progetti.get(projectname);
        if(proj == null) return null;
        return proj.getMembri();
    }

    public boolean member(String projectname){
        return progetti.containsKey(projectname);
    }

}