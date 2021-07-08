import java.io.*;


public class Persistent_data {
    private final String absolute_path = "./";
    private final String project_folder = absolute_path+"Projects_folder/";
    private final String user_folder = absolute_path+"User_folder/";

    private volatile static Persistent_data istanza = null;


    private Persistent_data() {
        File file = new File(project_folder);
        File file2 = new File(user_folder);
        if(!file.exists()){
            try{
                file.mkdir();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(!file2.exists()){
            try{
                file2.mkdir();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Persistent_data getInstance(){
        if(istanza == null){
            synchronized (Persistent_data.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){
                    istanza = new Persistent_data();
                }
            }
        }
        return istanza;
    }

    public String getUser_folder(){ return user_folder;}

    public String getProject_folder(){ return project_folder;}

    public boolean save(String path, Object obj, Class<?> type){
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path));) {

            output.writeObject(type.cast(obj)); // salvo in modo persistente le informazioni del progetto
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean create_dir(String projectname){
        if(! new File(project_folder).exists()) return false;
        String dir = projectname+"/";
        File file = null;
        try {
            file = new File(project_folder + dir);
            file.mkdir();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
