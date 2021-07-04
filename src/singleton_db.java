import java.util.Collection;

public class singleton_db {

    private volatile static singleton_db istanza = null;

    private hash_users utenti;
    private hash_project progetti;

    private singleton_db() {
        utenti = new hash_users();
        progetti = new hash_project();
    }

    public static hash_users getInstanceUtenti(){
        if(istanza == null){
            synchronized (singleton_db.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){
                    istanza = new singleton_db();
                }
            }
        }
        return istanza.utenti;
    }

    public static hash_project getInstanceProgetti(){
        if(istanza == null){
            synchronized (singleton_db.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){
                    istanza = new singleton_db();
                }
            }
        }
        return istanza.progetti;
    }

//    public boolean member(String username){
//        return utenti.member((username));
//    }
//
//
//    public String login(String username, String password){
//
//        user utente = utenti.get_user(username);
//
//        if(utente == null || (!utente.verify_password(password))){
//            return "username o password non corretti";
//        }
//
//        utente.vai_on();
//        //notificare client con callback rmi grazie, danilo per questo sforzo che non ti va di fare adesso
//
//        return "login effettuato con successo";
//    }
//
//    public String logout(String username, String password){
//
//        user utente = utenti.get_user(username);
//
//        if(utente == null) return "Username errato";
//        utente.vai_off();
//        //notificare client con callback rmi grazie, danilo per questo sforzo che non ti va di fare adesso
//
//        return "logout effettuato con successo";
//    }



}