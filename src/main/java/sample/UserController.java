package sample;


import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {
    List<User> list =  new ArrayList<User>();

    public UserController() {
        list.add(new User("Roman","Simko","roman","heslo"));
    }

    public boolean isTokenValid(String token) {
        for(User useri : list)
            if(useri.getToken()!=null && useri.getToken().equals(token))
                return true;

        return false;
    }


}
