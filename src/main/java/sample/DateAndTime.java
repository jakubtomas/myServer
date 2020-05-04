package sample;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class DateAndTime {
    List<User> list = new ArrayList<User>();

    public DateAndTime() {
        list.add(new User("Roman", "sagan", "roman", "heslo"));
    }


    // todo create the hash bcrypt

    @RequestMapping("/time")
    public String getTime() {

        // ak niesom prihlaseny tak cas my nevypise neautorazovani pristup
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(cal.getTime()));


        return sdf.format(cal.getTime());
    }

    @RequestMapping("/primenumber/{number}")
    public String checkPrimeNumber(@PathVariable int number) {


        boolean flag = false;
        for (int i = 2; i <= number / 2; ++i) {
            // condition for nonprime number
            if (number % i == 0) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            //   System.out.println(num + " is a prime number.");
            return "true"; // todo change return JSON
        } else {
            //System.out.println(num + " is not a prime number.");
            return "false";
        }

    }


    @RequestMapping("/time/hour")
    public String getHour() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        System.out.println(sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    @RequestMapping("/hello")
    public String getHello() {
        return "Hello. How are you? ";
    }

    @RequestMapping("/hello/{name}")
    public String getHelloWithName(@PathVariable String name) {
        return "Hello " + name + ". How are you? ";
    }

    @RequestMapping("/hi") // GET method default
    public String getHi(@RequestParam(value = "fname") String fname, @RequestParam(value = "age") String age) {
        return "Hello. How are you? Your name is " + fname + " and you are " + age;
    }


    // POST METHOD/

    /// postman body send {"login": "igor",
    //                          "password": "tajne heslo"}



    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(@RequestBody String credential) {// premenna string credential prijaty JSON
        System.out.println(credential);  /// request body odkial mam zobrat data z tela ziadosit
        return "{\"Error\":\"Login already exists\"}"; // ak meno heslo je spravne  tak vrati naspet kto je prihlaseny
        // aj he nespravne tak vratim prazdny JSON
    }

    // todo and also Generate token
    // todo compare the password if is ok return object json with all data fName lastn name and so on
    // when you logout  delete token   need token and login name


    @RequestMapping(method = RequestMethod.POST, value = "/signup")

    public String signup(@RequestBody String data) { // input data from body html page

        System.out.println("input data are under");
        System.out.println(data);

        JSONObject objj = null;
        try {
            objj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (objj.has("fname") && objj.has("lname") && objj.has("login") && objj.has("password")) {
            System.out.println("I have alll name ");

            System.out.println(" I have all ");
            return "I have all name okey ";
        } else {
            System.out.println("I dont have all name ");
            return "I dont have";
        }


        //  if(objj.has("fname") && objj.has("lname")&& objj.has("login")&& objj.has("password"))


// todo create the hash bcrypt
        // todo uniq login  you have to check  ,, use array list and compare data
      //  return null;
    }



    // todo every user can see all other user with account without password, only user with account can see
    // user?token and login  return all users

    // users/login novak  return only information about novak j  ,, about one user
}