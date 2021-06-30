import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia per registration
 * Permette a un Utente di registrarsi al servizio con Username e Password
 */

public interface registration_int extends Remote {
    /**
     * @overview: metodo che permette all'utente la registrazione al servizio
     * @return: ritorna una stringa che descrive l'esito dell'operazione
     */

    public String register(String Username, String Password) throws RemoteException;
}
