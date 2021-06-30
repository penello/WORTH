import java.util.Collection;
import java.util.LinkedList;

public class project {

    private final String projectname;
    private final LinkedList<card> todo;
    private final LinkedList<card> inprogress;
    private final LinkedList<card> toberevised;
    private final LinkedList<card> done;
    private final LinkedList<String> membri;
    private final LinkedList<String> allcards;

    public project(String projectname){
        this.projectname = projectname;
        membri = new LinkedList<String>();
        todo = new LinkedList<card>();
        inprogress = new LinkedList<card>();
        toberevised = new LinkedList<card>();
        done = new LinkedList<card>();
        allcards = new LinkedList<String>();
    }

    public String getProjectname(){
        return projectname;
    }

    public LinkedList<card> getTodo(){
        return todo;
    }

    public LinkedList<card> getInprogress(){
        return inprogress;
    }

    public LinkedList<card> getToberevised(){
        return toberevised;
    }

    public LinkedList<card> getDone(){
        return done;
    }

    public LinkedList<String> getMembri() {return membri;}


    public boolean add_member(String username){
        if(membri.contains(username)) return false;
        membri.add(username);
        return true;
    }

    /**
    public boolean addmember(user utent){
        user utente = utent;
        String username = utente.getUsername();
        String password = utente.getPassword();
        if(membri.member(utente.getUsername())) return false;
        membri.add_user(username, password);
        return true;
    }*/

    public boolean containsmember(String username){
        return membri.contains(username);
    }


    public String addcard(String cardname, String description){
        if(allcards.contains(cardname)) return "la card "+cardname+" è già presente";
        card newcard = new card(cardname, description);
        allcards.add(cardname);
        todo.add(newcard);
        return "la card "+cardname+" è stata creata correttamente";
    }

    public card Getcard(String cardname){
        for(card carta : todo) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        for(card carta : inprogress) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        for(card carta : toberevised) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        for(card carta : done) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        return null;
    }

    public String movecard(String cardname, String lista_partenza, String lista_destinazione){

        String return_msg = "Errore nello spostamento della card";

        //vincoli che devono essere rispettati
        if(lista_partenza.equals(lista_destinazione)) return "Le due liste coincidono nessuna operazione disponibile";
        if(lista_partenza.equals("done")) return "La card è nella lista DONE, non può essere spostata";
        if(lista_partenza.equals("todo")){
            if(!lista_destinazione.equals("inprogress")) return"Dalla lista TODO la card può essere spostata solo nella lista INPROGRESS";
        }
        else if(lista_partenza.equals("inprogress")){
            if(lista_destinazione.equals("todo")) return"Dalla lista INPROGRESS la card può essere spostata solo nelle liste TOBEREVISED e DONE";
        }
        else if(lista_partenza.equals("toberevised")){
            if(lista_destinazione.equals("todo")) return "Dalla lista TOBERIVISED la card può essere spostata solo nelle liste INPROGRESS e DONE";
        }

        card carta = Getcard(cardname);
        if(carta.getlist().equals(lista_partenza)){
            if(lista_partenza.equals("todo")){
                todo.remove(carta);
                carta.addhistory("inprogress");
                inprogress.add(carta);
                return_msg = "card spostata con successo da TODO a INPROGRESS";
            }
            if(lista_partenza.equals("inprogress")){
                inprogress.remove(carta);
                if(lista_destinazione.equals("done")){
                    carta.addhistory("done");
                    done.add(carta);
                    return_msg = "card spostata con successo da INPROGRESS a DONE";
                }
                else if(lista_destinazione.equals("toberevised")){
                    carta.addhistory("toberevised");
                    toberevised.add(carta);
                    return_msg = "card spostata con successo da INPROGRESS a TOBEREVISED";
                }
            }
            if(lista_partenza.equals("toberevised")){
                toberevised.remove(carta);
                if(lista_destinazione.equals("done")){
                    carta.addhistory("done");
                    done.add(carta);
                    return_msg = "carta spostata con successo da TOBEREVISED a DONE";
                }
                else if(lista_destinazione.equals("inprogress")){
                    carta.addhistory("inprogress");
                    inprogress.add(carta);
                    return_msg = "carta spostata con successo da TOBEREVISED a INPROGRESS";
                }
            }
        }
        return return_msg;
    }

    public LinkedList<String> get_history(String cardname){
        card carta = Getcard(cardname);
        return carta.getHistory();
    }


}
