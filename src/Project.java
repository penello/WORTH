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

    /**
     *
     * @return la lista dei membri del progetto
     */
    public LinkedList<String> getMembri() {return membri;}

    /**
     *
     * @return la lista di tutte le carte del progetto
     */
    public LinkedList<String> getAllcards() {return allcards;}

    /**
     *
     * @return l'ip della chat multicast relativa al progetto
     */
    public String getIp_multicast(){ return ip_multicast;}


    /**
     * metodo chiamato per recuperare i dati salvati
     * @param member
     */
    public void copy_member(LinkedList<String> member){
        this.membri = member;
    }

    /**
     * metodo usato per aggiungere un nuovo membro al progetto
     * @param username
     * @return il risultato dell'operazione
     */
    public boolean add_member(String username){
        if(membri.contains(username)) return false;
        membri.add(username);
        return true;
    }

    /**
     * metodo usato per verificare se un utente è membro del progetto
     * @param username
     * @return
     */
    public boolean containsmember(String username){
        return membri.contains(username);
    }

    /**
     * metodo per creare l'ip della chat relativa al progetto
     * non controllo se ho già un ip uguale perchè probabilità è 1/16M
     * @return il risultato dell'operazione
     */
    public String setip(){
        int i;
        String r = "239";
        Random rand = new Random();
        for(i = 0; i<3; i++){
            Integer x = rand.nextInt(256);
            r = r+"."+ x.toString();
        }
        return r;
    }

    /**
     * metodo usato per recuperare i dati salvati relativi alle card del progetto
     * @param carta
     */
    public void copy_card(Card carta){
        Card newcard = new Card(carta.getName(),carta.getDescription(),carta.getHistory());
        allcards.add(carta.getName());
        getlist(carta.getlist()).add(newcard);
    }

    /**
     * metodo usato per aggiungere una nuova carta al progetto
     * @param cardname
     * @param description
     * @return il risultato dell'operazione
     */
    public String addcard(String cardname, String description){
        if(allcards.contains(cardname)) return "la card "+cardname+" è già presente";
        Card newcard = new Card(cardname, description);
        allcards.add(cardname);
        todo.add(newcard);
        return "SUCCESS";
    }

    /**
     * metodo usato per recuperare una specifica carta del progetto
     * @param cardname
     * @return
     */
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

    /**
     * metodo usato per recuperare la lista in cui si trova la carta
     * @param listName
     * @return il risultato dell'operazione
     */
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

    /**
     * metodo usato per spostare una card tra le varie liste del progetto
     * @param cardname
     * @param lista_partenza
     * @param lista_destinazione
     * @return il risultato dell'operazione
     */
    public String movecard(String cardname, String lista_partenza, String lista_destinazione){

        String return_msg = "Errore nello spostamento della card";

        //vincoli che devono essere rispettati
        if(lista_partenza.equals("todo") || lista_partenza.equals("inprogress") || lista_partenza.equals("toberevised") ||  lista_partenza.equals("done")) {
            if (lista_destinazione.equals("todo") || lista_destinazione.equals("inprogress") || lista_destinazione.equals("toberevised") || lista_destinazione.equals("done")) {
                if (lista_partenza.equals(lista_destinazione))
                    return "Le due liste coincidono nessuna operazione disponibile";
                else if (lista_partenza.equals("done")) return "La card è nella lista DONE, non può essere spostata";
                else if (lista_partenza.equals("todo")) {
                    if (!lista_destinazione.equals("inprogress"))
                        return "Dalla lista TODO la card può essere spostata solo nella lista INPROGRESS";
                } else if (lista_partenza.equals("inprogress")) {
                    if (lista_destinazione.equals("todo"))
                        return "Dalla lista INPROGRESS la card può essere spostata solo nelle liste TOBEREVISED e DONE";
                } else if (lista_partenza.equals("toberevised")) {
                    if (lista_destinazione.equals("todo"))
                        return "Dalla lista TOBERIVISED la card può essere spostata solo nelle liste INPROGRESS e DONE";
                }

                Card carta = Getcard(cardname);
                if (carta.getlist().equals(lista_partenza)) {
                    if (lista_partenza.equals("todo")) {
                        todo.remove(carta);
                        carta.addhistory("inprogress");
                        inprogress.add(carta);
                        return_msg = "SUCCESS";
                    }
                    if (lista_partenza.equals("inprogress")) {
                        inprogress.remove(carta);
                        if (lista_destinazione.equals("done")) {
                            carta.addhistory("done");
                            done.add(carta);
                            return_msg = "SUCCESS";
                        } else if (lista_destinazione.equals("toberevised")) {
                            carta.addhistory("toberevised");
                            toberevised.add(carta);
                            return_msg = "SUCCESS";
                        }
                    }
                    if (lista_partenza.equals("toberevised")) {
                        toberevised.remove(carta);
                        if (lista_destinazione.equals("done")) {
                            carta.addhistory("done");
                            done.add(carta);
                            return_msg = "SUCCESS";
                        } else if (lista_destinazione.equals("inprogress")) {
                            carta.addhistory("inprogress");
                            inprogress.add(carta);
                            return_msg = "SUCCESS";
                        }
                    }
                }
            }
            else return "Nome della lista di destinazione errato";
        }
        else return "Nome della lista di partenza errato";
        return return_msg;
    }

    /**
     * metodo per verificare se tutte le carte sono nella lista done
     * @return il risultato dell'operazione
     */
    public boolean allcarddone(){
        if(!todo.isEmpty()) return false;
        if(!inprogress.isEmpty()) return false;
        return toberevised.isEmpty();
    }
}
