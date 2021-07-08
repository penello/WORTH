public class Singleton_db_progetti {
    private volatile static Singleton_db_progetti istanza = null;

    private Hash_project progetti;

    private Singleton_db_progetti() {
        progetti = new Hash_project();
    }

    public static Hash_project getInstanceProgetti(){
        if(istanza == null){
            synchronized (Singleton_db_progetti.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){
                    istanza = new Singleton_db_progetti();
                }
            }
        }
        return istanza.progetti;
    }
}
