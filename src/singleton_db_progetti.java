public class singleton_db_progetti {
    private volatile static singleton_db_progetti istanza = null;

    private hash_project progetti;

    private singleton_db_progetti() {
        progetti = new hash_project();
    }

    public static hash_project getInstanceProgetti(){
        if(istanza == null){
            synchronized (singleton_db_progetti.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){
                    istanza = new singleton_db_progetti();
                }
            }
        }
        return istanza.progetti;
    }
}
