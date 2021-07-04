import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;


public class registration extends RemoteServer implements registration_int {

    //private hash_users utenti;
    private int porta_rmi = 2048;

    public registration() {}

    public void start(){
        try {
            registration_int stub = (registration_int) UnicastRemoteObject.exportObject(this, 0);
            LocateRegistry.createRegistry(porta_rmi);
            Registry r = LocateRegistry.getRegistry(porta_rmi);
            r.rebind("RegisterUser", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String register(String nickutente, String Password) throws RemoteException{

        hash_users obj = singleton_db.getInstanceUtenti();
        String ret;

        if(!nickutente.isEmpty() && !Password.isEmpty()){
            ret = obj.add_user(nickutente,Password);
        }
        else return "nickutente e password non validi";
        return ret;

    }
}
