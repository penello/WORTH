import java.util.LinkedList;
import java.util.Random;

public class Project {

    private final String projectname;
    private final LinkedList<Card> todo;
    private final LinkedList<Card> inprogress;
    private final LinkedList<Card> toberevised;
    private final LinkedList<Card> done;
    private LinkedList<String> membri;
    private final LinkedList<String> allcards;
    private String ip_multicast;

    public Project(String projectname){
        this.projectname = projectname;
        membri = new LinkedList<String>();
        todo = new LinkedList<Card>();
        inprogress = new LinkedList<Card>();
        toberevised = new LinkedList<Card>();
        done = new LinkedList<Card>();
        allcards = new LinkedList<String>();
        ip_multicast = setip();
    }

    public String getProjectname(){
        return projectname;
    }

    public LinkedList<Card> getTodo(){
        return todo;
    }

    public LinkedList<Card> getInprogress(){
        return inprogress;
    }

    public LinkedList<Card> getToberevised(){
        return toberevised;
    }

    public LinkedList<Card> getDone(){
        return done;
    }

    public LinkedList<String> getMembri() {return membri;}

    public LinkedList<String> getAllcards() {return allcards;}

    public String getIp_multicast(){ return ip_multicast;}



    public void copy_member(LinkedList<String> member){
        this.membri = member;
    }

    public boolean add_member(String username){
        if(membri.contains(username)) return false;
        membri.add(username);
        return true;
    }


    public boolean containsmember(String username){
        return membri.contains(username);
    }

    //non controllo se ho già un ip uguale perchè probabilità è 1/16M
    public String setip(){
        int i;
        String r = "239";
        Random rand = new Random();
        for(i = 0; i<3; i++){
            Integer x = rand.nextInt(256);
            r = "."+ x.toString();
        }
        return r;
    }

    public void copy_card(Card carta){
        Card newcard = new Card(carta.getName(),carta.getDescription(),carta.getHistory());
        allcards.add(carta.getName());
        getlist(carta.getlist()).add(newcard);
    }

    public String addcard(String cardname, String description){
        if(allcards.contains(cardname)) return "la card "+cardname+" è già presente";
        Card newcard = new Card(cardname, description);
        allcards.add(cardname);
        todo.add(newcard);
        return "SUCCESS ";
    }

    public Card Getcard(String cardname){
        for(Card carta : todo) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        for(Card carta : inprogress) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        for(Card carta : toberevised) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        for(Card carta : done) {
            if (carta.getName().equals(cardname))
                return carta;
        }
        return null;
    }

    public LinkedList<Card> getlist(String listName){
        if(listName.equals("todo"))
        return todo;
        if(listName.equals("inprogress"))
            return inprogress;
        if(listName.equals("toberevised"))
            return toberevised;
        if(listName.equals("done"))
            return done;
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

        Card carta = Getcard(cardname);
        if(carta.getlist().equals(lista_partenza)){
            if(lista_partenza.equals("todo")){
                todo.remove(carta);
                carta.addhistory("inprogress");
                inprogress.add(carta);
                return_msg = "SUCCESS ";
            }
            if(lista_partenza.equals("inprogress")){
                inprogress.remove(carta);
                if(lista_destinazione.equals("done")){
                    carta.addhistory("done");
                    done.add(carta);
                    return_msg = "SUCCESS ";
                }
                else if(lista_destinazione.equals("toberevised")){
                    carta.addhistory("toberevised");
                    toberevised.add(carta);
                    return_msg = "SUCCESS ";
                }
            }
            if(lista_partenza.equals("toberevised")){
                toberevised.remove(carta);
                if(lista_destinazione.equals("done")){
                    carta.addhistory("done");
                    done.add(carta);
                    return_msg = "SUCCESS ";
                }
                else if(lista_destinazione.equals("inprogress")){
                    carta.addhistory("inprogress");
                    inprogress.add(carta);
                    return_msg = "SUCESS ";
                }
            }
        }
        return return_msg;
    }

    public LinkedList<String> get_history(String cardname){
        Card carta = Getcard(cardname);
        return carta.getHistory();
    }

    public boolean allcarddone(){
        if(!todo.isEmpty()) return false;
        if(!inprogress.isEmpty()) return false;
        return toberevised.isEmpty();
    }


}
