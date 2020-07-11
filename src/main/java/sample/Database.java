package sample;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private MongoDatabase database = mongo.getDatabase("javaServer");

    private MongoCollection<Document> collectionUsers = database.getCollection("users");
    private MongoCollection<Document> collectionLogs = database.getCollection("loginHistory");
    private MongoCollection<Document> collectionMessages = database.getCollection("messages");


    public void closeDatabase() {
        this.mongo = null;
        this.database = null;
    }

    public void insertUser(JSONObject jsonObject) throws JSONException {

        Document document = new Document()
                .append("fname", jsonObject.getString("fname"))
                .append("lname", jsonObject.getString("lname"))
                .append("login", jsonObject.getString("login"))
                .append("password", jsonObject.getString("password"))
                .append("token", jsonObject.getString("token"));

        collectionUsers.insertOne(document);

        System.out.println("=================================");
        System.out.println("INSERT into database okey ");
        System.out.println("=================================");
    }

    public void insertMessage(JSONObject jsonObject) throws JSONException {

        Document document = new Document()
                .append("from", jsonObject.getString("from"))
                .append("message", jsonObject.getString("message"))
                .append("to", jsonObject.getString("to"))
                .append("datetime", jsonObject.getString("datetime"));

        collectionMessages.insertOne(document);

        System.out.println("=================================");
        System.out.println("INSERT into database okey ");
        System.out.println("=================================");
    }


    public boolean existLogin(String login) throws JSONException {

        Document found = collectionUsers.find(new Document("login", login)).first();

        System.out.println("found is " + found);


        JSONObject object = new JSONObject(found);

        if (found == null) {
            System.out.println("---------------------------");
            System.out.println(" WE DONT HAVE VALUE");
            System.out.println("---------------------------");
            return false; //  dos not exist record
        } else {
            System.out.println("-----------------------");
            System.out.println("get login from json  === " + object.getString("login") + " ===");
            System.out.println("we HAVE VALUE IN OUR DATABASE ");
            System.out.println("-----------------------");
            return true;
        }
    }


    public JSONObject getUser(String login) throws JSONException {
        Document found = collectionUsers.find(new Document("login", login)).first();
        JSONObject object = new JSONObject(found);

        if (found == null) {
            System.out.println(" WE DONT HAVE VALUE");
            return null; //  dos not exist record
        } else {
            System.out.println("get login from json  === " + object.getString("login") + " ===");
            System.out.println("we HAVE VALUE IN OUR DATABASE ");
            return object;
        }


    }
/*
    public JSONObject getLoginHistory(String login) throws JSONException {
        Document found = collectionLogs.find(new Document("login", login)).first();
        JSONObject loginObject = new JSONObject(found);

        ArrayList<String> listdata = new ArrayList<>();



        if (found == null) {
            System.out.println(" WE DONT HAVE VALUE");

            return null; //  dos not exist record
        } else {

            System.out.println("get login from json  === " + loginObject.getString("login") + " ===");
            System.out.println("we HAVE VALUE IN OUR DATABASE ");
            return loginObject;
        }

    }
    */


    public List<String> getLoginHistory(String login) throws JSONException {

        List<String> loginHistory = new ArrayList<>();

        for (Document document : collectionLogs.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json
            if (object.getString("login").equals(login)) {
                loginHistory.add(object.toString());
            }
        }
        return loginHistory;
    }

    // todo list this function you have to change only my messages no all message this is bad mistake
    public List<String> getAllMessages() throws JSONException {

        List<String> loginHistory = new ArrayList<>();

        for (Document document : collectionMessages.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json


            loginHistory.add(object.toString());

        }
        return loginHistory;
    }


    public List<String> getMessagesFromUser(String fromLogin, String myLogin) throws JSONException {

        List<String> messagesList = new ArrayList<>();

        for (Document document : collectionMessages.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json

            // write conditions where login equals mylogin and from login also equals


            if ((object.getString("from").equals(myLogin) && object.getString("to").equals(fromLogin)) ||
                    (object.getString("from").equals(fromLogin) && object.getString("to").equals(myLogin))) {

                messagesList.add(object.toString());
            }

        }
        return messagesList;
    }


    public List<String> getUsers() throws JSONException {

        JSONObject userListJson = new JSONObject();
        JSONArray usersJsonArray = new JSONArray();

        List<String> usersList = new ArrayList<>();

        for (Document document : collectionUsers.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json
// how to push
            usersList.add(object.getString("login"));

            userListJson.put("login", object.getString("login"));
            usersJsonArray.put(userListJson);
        }
        return usersList;
    }

       /* public List<String> getMessages() throws JSONException {

            List<String> messagesList = new ArrayList<>();

            for (Document document : collectionMessages.find()) {
                JSONObject object = new JSONObject(document.toJson());  // document to json

                messagesList.add(object.toString());
            }
            return messagesList;
        }*/


    public void saveToken(String login, String token) {
        System.out.println("                                            save token into database ");
        Bson updateQuery = new Document("login", login);
        Bson newValue = new Document("token", token);
        Bson update = new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);
    }

    public void saveLoginHistory(JSONObject jsonObject) throws JSONException {

        Document document = new Document()
                .append("type", jsonObject.getString("type"))
                .append("login", jsonObject.getString("login"))
                .append("datetime", jsonObject.getString("datetime"));


        collectionLogs.insertOne(document);

    }


    // my function
    /*public boolean existToken(String token ) throws JSONException {
        Document found = collectionUsers.find(new Document("token", token)).first();

        JSONObject user = new JSONObject(found);

        if (found == null) {
            return false;
        } else {
            System.out.println(user.getString("login"));
            System.out.println(user.getString("token"));

            return true;
        }

    }*/

    public boolean existToken(String token, String login) throws JSONException {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {

            System.out.println("input token " + token);
            System.out.println("input login " + login);

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());

                if (object.getString("login").equals(login) && object.getString("token").equals(token)) {
                    System.out.println("login from object " + object.getString("login"));
                    return true;
                }
            }
        }
        return false;
    }


    public void deleteToken(String login) {

        Bson updateQuery = new Document("login", login);
        Bson newValue = new Document("token", "");
        Bson update = new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);
    }


    public void updatePassword(String login, String hash) {
        // crete new document

        Bson updateQuery = new Document("login", login);
        Bson newValue = new Document("password", hash);
        Bson update = new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);

    }

    public void deleteUser(String login, String token) throws JSONException {


        for (Document document : collectionUsers.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json

            // if equals login and token we can delete
            if (object.getString("login").equals(login) &&
                    object.getString("token").equals(token)) {


                BasicDBObject deleteQuery = new BasicDBObject();
                deleteQuery.put("login", login);
                collectionUsers.deleteOne(deleteQuery);


            } else {
                System.out.println("nesplnena podmienka pre vymazanie ");
            }
        }


    }


    public void deleteUser(String login) throws JSONException {
        BasicDBObject theQuery = new BasicDBObject();
        theQuery.put("login", login);
        collectionUsers.deleteOne(theQuery);

        try (MongoCursor<Document> cursor = collectionMessages.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("from").equals(login)) {
                    theQuery = new BasicDBObject();
                    theQuery.put("from", login);
                    collectionMessages.deleteOne(theQuery);
                }
            }
        }
    }


    public void updateuser(String fname, String lname, String login) {
        // crete new document


        System.out.println("string fname is " + fname + " lname is " + lname);

        Bson filter = new Document("login", login) // na zaklade login
                ;
        Bson newValue = new Document("fname", fname).append("lname", lname);

        Bson updateOperationDocument = new Document("$set", newValue);
        collectionUsers.updateMany(filter, updateOperationDocument);


        /*   Bson updateQuery=new Document("login", login);
        Bson newValue=new Document("password", hash);
        Bson update=new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);*/
    }


}