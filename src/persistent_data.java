

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class persistent_data {
    private final String absolute_path = "~/IdeaProjects/WORTH/";
    private final String project_folder = absolute_path+"Projects_folder/";
    private final String user_folder = absolute_path+"User_folder/";

    private volatile static persistent_data istanza = null;


    private persistent_data() {
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

    public static persistent_data getInstance(){
        if(istanza == null){
            synchronized (persistent_data.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){
                    istanza = new persistent_data();
                }
            }
        }
        return istanza;
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

    public boolean save_user(user utente){
        if(! new File(user_folder).exists()) return false;
        JSONObject usr = new JSONObject();

        try {
            usr.put("username", utente.getUsername());
            usr.put("password", utente.getPassword());
            usr.put("stato","offline");
            JSONArray project_array = new JSONArray();
            for (String s:utente.getLista_progetti()){
                project_array.add(s);
            }
            usr.put("project_list",project_array);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        try (FileWriter file = new FileWriter(user_folder+"Utenti.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(usr.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
