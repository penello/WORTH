import java.io.Serializable;
import java.util.LinkedList;

public class Card implements Serializable {

    private String name;
    private String description;
    private LinkedList<String> history;

    public Card(String nome, String descrizione){
        name = nome;
        description = descrizione;
        history = new LinkedList<String>();
        history.add("todo");
    }

    public Card(String nome, String descrizione, LinkedList<String> history){
        this.name = nome;
        this.description = descrizione;
        this.history = history;
        this.history.add("todo");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LinkedList<String> getHistory() {
        return history;
    }

    public String getlist(){
        return history.getLast();
    }

    public void addhistory(String listname){
        history.add(listname);
    }
}
