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
        database.getUser("janko");

      //  database.updateuser("again new asôldfkjasfôlkashdfnew fname " , "update new lname " , "user16");
       // database.updateuser();

        System.out.println("Json object" + database.getUser("janko"));
        JSONObject res = new JSONObject();
        //System.out.println("Find login  janko " + database.findLogin("peter"));;
        //database.skuska1();


        System.out.println("---                                                                  ----");
        System.out.println("DO YOU HAVE VALUE LIENKA IN DATABASE "+  database.existLogin("medulienka"));;
        System.out.println("DO YOU HAVE VALUE LIENKA IN DATABASE "+  database.existLogin("medulienka"));;
        System.out.println("DO YOU HAVE VALUE sagan IN DATABASE "+  database.existLogin("sagan"));;
        System.out.println("---                                                                  ----");

        UserController userController = new UserController();

       // userController.checkPassword()
        /*

        res.put("fname", "fname pridavam");  // put message
        res.put("lname", "lname try to send");
        res.put("login", "login try to send ");


        database.insertUser(res);


*/



    }
}
