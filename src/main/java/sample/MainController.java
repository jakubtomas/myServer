package sample;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class  MainController {

    public static void main(String[] args) throws JSONException {// doplnujuci paramter , vsetky tie parametre sa ulozia
        SpringApplication.run(MainController.class,args);

        Database1 database1 = new Database1();
      //  database.runMongo();
       // database1.connectDatabase();
        //database1.inputUser();

        Database database = new Database();
        JSONObject res = new JSONObject();
/*

        res.put("fname", "fname pridavam");  // put message
        res.put("lname", "lname try to send");
        res.put("login", "login try to send ");


        database.insertUser(res);
*/

    }
}
