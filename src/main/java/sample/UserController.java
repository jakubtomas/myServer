package sample;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
public class UserController {
    List<User> list = new ArrayList<User>();
    List<String> log = new ArrayList<>();
    List<String> messages = new ArrayList<>();

    public UserController() {
        list.add(new User("peter", "sagan", "sagan", "$2a$08$pwyuZ84u7Qp2P.vFHS5vZ.ei2brZdqWY3NiUycbhY0tJgYCnpFaEG"));
        list.add(new User("veronika", "veronika", "veronika", "$2a$08$pwyuZ84u7Qp2P.vFHS5vZ.ei2brZdqWY3NiUycbhY0tJgYCnpFaEG"));
/*
        list.add(new User("peter", "juliana", "juliana", "$2a$08$pwyuZ84u7Qp2P.vFHS5vZ.ei2brZdqWY3NiUycbhY0tJgYCnpFaEG"));
        list.add(new User("peter", "dog", "Dog", "$2a$08$pwyuZ84u7Qp2P.vFHS5vZ.ei2brZdqWY3NiUycbhY0tJgYCnpFaEG"));
*/

    }




    // check all users than someone has equals token
    public boolean validToken(String token) {
        for (User useri : list)
            if (useri.getToken() != null && useri.getToken().equals(token))
                return true;

        return false;
    }




    ///////////////////////////////////////////
    // SIGN UP


    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<String> signup(@RequestBody String data) throws JSONException { // input data from body html page

        System.out.println("input data are under");
        System.out.println(data);

        JSONObject objj = new JSONObject(data);

        // CHECK WE HAVE ALL DATA
        if (objj.has("fname") && objj.has("lname") && objj.has("login") && objj.has("password")) {

            System.out.println("I have alll name "); // control  show data in console
            System.out.println(" I have all ");


            // CHECK  EXIST LOGIN
            if (existLogin(objj.getString("login"))) {  // take login into function exist Login   return true or false
                JSONObject result = new JSONObject();          // create json object
                result.put("error", "User already exists");         // put error message
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result.toString()); // to string okey
                // note what means contentType media type check the video
            }


            //CHECK  PASSWORD IS EMPTY
            String password = objj.getString("password");
            if (password.isEmpty()) {
                JSONObject result = new JSONObject();

                result.put("error", "Password is a mandatory field");
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result.toString());

            }

            String hashPass = hash(objj.getString("password")); // create hash

            User user = new User(objj.getString("fname"), objj.getString("lname"), objj.getString("login"), hashPass);
            list.add(user);
            JSONObject res = new JSONObject(); // create json
            res.put("fname", objj.getString("fname"));  // put message
            res.put("lname", objj.getString("lname"));
            res.put("login", objj.getString("login"));

            System.out.println("Succesf");

            return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(res.toString()); // to string json data


            // return "I have all name okey ";
        } else {
            JSONObject res = new JSONObject();
            res.put("error", "Invalid body request");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(res.toString());
            // return "I dont have";
        }


        // todo create the hash bcrypt
        // todo uniq login  you have to check  ,, use array list and compare data
        //return null;
    }

    private boolean existLogin(String login) {
        for (User user : list) {
            if (user.getLogin().equalsIgnoreCase(login))
                return true;
        }
        return false;
    }

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(8));
    }





    ///////////////////////////////////////////
    /////   LOGIN


    @RequestMapping(method=RequestMethod.POST, value="/login")

    public ResponseEntity<String> login(@RequestBody String credential) throws JSONException {

        JSONObject obj = new JSONObject(credential);/*don't copy*/   // why

      // time actually
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyy HH:mm:ss");
        LocalDateTime localTime = LocalDateTime.now();
        String time = dtf.format(localTime);



        //CHECK WE HAVE LOGIN AND PASSWORD
        if(obj.has("login") && obj.has("password")){

            JSONObject result = new JSONObject(); // CREATE NEW JSON OBJECT
            JSONObject logHistory = new JSONObject(); // CREATE NEW JSON OBJECT

            //check password and login that are empty
            if(obj.getString("password").isEmpty() || obj.getString("login").isEmpty()){
                result.put("error","Password and login are mandatory fields");
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(result.toString());
            }

            String hashInputpassword = hash(obj.getString("password")); // create hash

            System.out.println("your hash input password   " +  hashInputpassword) ;


            //check the existing the login and check password than is correct
            if(existLogin(obj.getString("login")) && checkPassword(obj.getString("login"), obj.getString("password")))
            {

                User loggedUser=getUser(obj.getString("login"));

                if(loggedUser==null){
                    // tento riadok by sa nemal nikdy vykonat, osetrene kvoli jave
                    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("{}");
                }

                result.put("fname",loggedUser.getFname());
                result.put("lname",loggedUser.getLname());
                result.put("login",loggedUser.getLogin());


                // put data into json object
                logHistory.put("type","login");
                logHistory.put("login",loggedUser.getLogin());
                logHistory.put("datetime",loggedUser.getLogin());

                System.out.println("history" + logHistory);
                // Generate new token
                log.add(logHistory.toString()); //add into the list time



                String token = generateNewToken();

                System.out.println( "generate token is  "  + token);
                System.out.println( "time  "  + time);



                result.put("token",token);
                loggedUser.setToken(token);

                //   Strinng JSondatatime  = {"type":"logout","login":"martin5","datetime":"04052020 13:58:04"}
                // better tocreate JSON object


                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(result.toString());
            }
            else{
                result.put("error","Invalid login or password");
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(result.toString());
            }

        }else {
            JSONObject res = new JSONObject();
            res.put("error","Invalid body request");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }
    }


    /////////////////////////////////////////////////////////// END LOGIN



    //////////////   LOGOUT


    @RequestMapping(method=RequestMethod.POST, value="/logout")
    public ResponseEntity<String> logout(@RequestBody String data, @RequestHeader(name = "Authorization") String token) throws JSONException {

        JSONObject obj = new JSONObject(data);

        String login = obj.getString("login");


        User user = getUser(login);

        if(user!=null && validToken(token)){
            user.setToken(null);
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("{}");
        }


        JSONObject res = new JSONObject();
        res.put("error","Incorrect login or token");
        return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
    }


////////////////////////////////////////////////// END LOGOUT



    //////////////CHANGE PASSWORD


    /// create post method  url change password
 // input parameter login, old password , and new password  ,, token
//  validation old password and token

    @RequestMapping(method = RequestMethod.POST, value = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody String data, @RequestHeader(name = "Authorization") String token) throws JSONException {

        JSONObject obj = new JSONObject(data); // json from input data body
        JSONObject res = new JSONObject();// result JSON

        User temp = getUser(obj.getString("login")); // return object user according to login name



        if (temp == null) {
            res.put("error", "Incorrect login");
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }

        // check we have data like login old password and new password

        if (obj.has("login") && obj.has("oldpassword") && obj.has("newpassword")) {

            System.out.println(obj.getString("oldpassword"));

            if (temp.getLogin().equals(obj.getString("login")) && BCrypt.checkpw(obj.getString("oldpassword"), temp.getPassword())
                    && temp.getToken().equals(token)) {

                System.out.println("change  passwrod to "   + obj.getString("newpassword") );

                // better to add return 200 with body message success
                temp.setPassword(obj.getString("newpassword"));


            } else {
                res.put("error", "Wrong password or token");
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
            }

        } else
            res.put("error", "Wrong input");
        return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());

    }


///////////////////END



////////////////////// LOG history my login
    // input json login : peter and into the header put the token

    @RequestMapping(value = "/log")
    public ResponseEntity<String> log(@RequestBody String data, @RequestHeader(name = "Authorization") String token) throws JSONException {

        JSONObject obj = new JSONObject(data);
        JSONObject res = new JSONObject();

        User temp = getUser(obj.getString("login"));


        if (temp == null  || !validToken(token) ) { // check we have user and check the token
            res.put("error", "Incorrect login or invalid TOKEN ");
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }


        if (obj.has("login")  ) {
            //existLogin(obj.getString("login")) && existLogin(obj.getString("acceptor")

            if (existLogin(obj.getString("login"))) {
               // res.put("message", "everythink is okey ");

                for(int i = 0; i < log.size(); i++) {

                    res.put("list" + i , log.get(i));
                    System.out.print(log.get(i));
                }

                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(res.toString());
            } else {
                res.put("error", "login dos not exist in our database or array list   ");
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
            }


        } else {
            res.put("error", "empty login name");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }




        //return null;

    }






    @RequestMapping("/users") // get users according to token ??
    public ResponseEntity<String> getUsers(@RequestParam(value="token") String token) throws JSONException {

        if(token==null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("{\"error\",\"Bad request\"}");
        }
        if(validToken(token)){
            JSONArray array = new JSONArray();
            for(User user : list){
                JSONObject obj = new JSONObject();
                obj.put("fname",user.getFname());
                obj.put("lname",user.getLname());
                obj.put("login",user.getLogin());
                array.put(obj);
            }

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(array.toString());
        }
        else return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body("{\"error\":\"Invalid token\"}");
    }

///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////  POST  NEW MESSAGE

    @RequestMapping(method = RequestMethod.POST, value = "/message/new")
    public ResponseEntity<String> sendMessage(@RequestBody String data, @RequestHeader(name = "Authorization") String token) throws JSONException {


        JSONObject obj = new JSONObject(data);
        JSONObject res = new JSONObject();

        User temp = getUser(obj.getString("from"));

        if (temp == null  || !validToken(token) ) { // check we have user and check the token
            res.put("error", "Incorrect login or invalid TOKEN ");
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }

        if (obj.has("from") &&  obj.has("message") && obj.has("to")) {

            if (existLogin(obj.getString("from")) && existLogin(obj.getString("to"))) {
                // json add into array list
                res.put("from", obj.getString("from"));   // sender
                res.put("message", obj.getString("message"));   // message
                res.put("to", obj.getString("to"));   // acceptor


                System.out.println("message" + res.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(res);

                System.out.println("jsonarray is  " + jsonArray);
                messages.add(res.toString());


                return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(res.toString());


            } else {
                res.put("error", "Acceptor or sender  dosnt exist in our database / list  ");
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
            }
        } else {
            res.put("mistake", "empty message or acceptor or login sender ");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }


    }

/////////////////////////////////////// GET MESSAGES

    @RequestMapping(method = RequestMethod.GET, value = "/messages")
    public ResponseEntity<String> getMessages(@RequestBody String data, @RequestHeader(name = "Authorization") String token) throws JSONException {


        JSONObject obj = new JSONObject(data);
        JSONObject res = new JSONObject();

        User temp = getUser(obj.getString("login"));

        if (temp == null  || !validToken(token) ) { // check we have user and check the token
            res.put("error", "Incorrect login or invalid TOKEN ");
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }

        if (obj.has("login")   ) {

            if (existLogin(obj.getString("login")) ) {
                // json add into array list
                res.put("from", obj.getString("login"));



                //  put messages into json
                for(int i = 0; i < messages.size(); i++) {

                    res.put("message " + i, messages.get(i));
                    System.out.print(messages.get(i));
                }

                return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(res.toString());


            } else {
                res.put("error", "Acceptor or sender  dosnt exist in our database / list  ");
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(res.toString());
            }
        } else {
            res.put("mistake", "empty LOGIN ");
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(res.toString());
        }


    }






    private boolean checkPassword(String login, String password) {
        User user = getUser(login);
        if (user != null) {

            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }


    public static String generateNewToken() {
        Random rand=new Random();
        long longToken = Math.abs(rand.nextLong());
        String random = Long.toString(longToken, 16);

        return random;
    }

  /*  public static char[] generatePswd(int len){
            System.out.println("Your Password ");
            String charsCaps="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String Chars="abcdefghijklmnopqrstuvwxyz";
            String nums="0123456789";
            String symbols="!@#$%^&*()_+-=.,-+";
            String passSymbols=charsCaps + Chars + nums +symbols;
            Random rnd=new Random();
            char[] password=new char[len];

            for(int i=0; i<len;i++){
                password[i]=passSymbols.charAt(rnd.nextInt(passSymbols.length()));
            }
            return password;

        }*/

    private User getUser(String login){
        for(User user:list){
            if(user.getLogin().equals(login))
                return user;
        }
        return null;
    }


}
