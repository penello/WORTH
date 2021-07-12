import java.io.Serializable;
import java.util.LinkedList;

public class Card implements Serializable {

    private String name;
    private String description;
    private LinkedList<String> history;

    /**
     *
     * @param nome della carta
     * @param descrizione della carta
     *
     */
    public Card(String nome, String descrizione){
        name = nome;
        description = descrizione;
        history = new LinkedList<String>();
        history.add("todo");
    }

    /**
     * Questo costruttore viene usato quando il server recupera i dati persistenti
     * @param nome della carta
     * @param descrizione della carta
     * @param history, lista dei movimenti della carta
     */
    public Card(String nome, String descrizione, LinkedList<String> history){
        this.name = nome;
        this.description = descrizione;
        this.history = history;
        this.history.add("todo");
    }

    /**
     *
     * @return il nome della carta
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return la descrizione della carta
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return la lista dei movimenti della carta
     */
    public LinkedList<String> getHistory() {
        return history;
    }

    /**
     *
     * @return il nome della lista in cui si trova la carta
     */
    public String getlist(){
        return history.getLast();
    }

    /**
     * metodo per aggiornare la lista dei movimenti della carta
     * @param listname
     */
    public void addhistory(String listname){
        history.add(listname);
    }
}
