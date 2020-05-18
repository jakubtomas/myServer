package sample;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
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
                .append("password", jsonObject.getString("password"));

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


    public List<String> getLoginHistory(String login ) throws JSONException {

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
    public List<String> getAllMessages( ) throws JSONException {

        List<String> loginHistory = new ArrayList<>();

        for (Document document : collectionLogs.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json


            loginHistory.add(object.toString());

        }
        return loginHistory;
    }

    public List<String> getUsers() throws JSONException {

        List<String> usersList = new ArrayList<>();

        for (Document document : collectionUsers.find()) {
            JSONObject object = new JSONObject(document.toJson());  // document to json


            usersList.add(object.getString("login"));
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
        Bson updateQuery=new Document("login",login );
        Bson newValue=new Document("token", token);
        Bson update=new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);
    }

    public void saveLoginHistory(JSONObject jsonObject) throws JSONException {

        Document document = new Document()
                .append("type", jsonObject.getString("type"))
                .append("login", jsonObject.getString("login"))
                .append("datetime", jsonObject.getString("datetime"));


        collectionLogs.insertOne(document);

    }


    public boolean existToken(String token ) throws JSONException {
        Document found = collectionUsers.find(new Document("token", token)).first();

        JSONObject user = new JSONObject(found);

        if (found == null) {
            return false;
        } else {
            System.out.println(user.getString("login"));
            System.out.println(user.getString("token"));

            return true;
        }

    }

    public void deleteToken(String login) {

        Bson updateQuery=new Document("login", login);
        Bson newValue=new Document("token", "");
        Bson update=new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);
    }


    public void  updatePassword(String login, String hash) {
        // crete new document

        Bson updateQuery=new Document("login", login);
        Bson newValue=new Document("password", hash);
        Bson update=new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);

    }

    public void  updateuser() {
        // crete new document

        System.out.println("                                            generate token");
        Bson updateQuery=new Document("login", "asus1");
        Bson newValue=new Document("token", "generateToken");
        Bson update=new Document("$set", newValue);
        collectionUsers.updateOne(updateQuery, update);

    }

/*
    public void logLogout(JSONObject jsonObject) throws JSONException {
        System.out.println("here log logout");
        Document document = new Document();
        document.append("type", "logout");
        document.append("login", jsonObject.getString("login"));
        document.append("datetime", jsonObject.getString("datetime"));
        collectionLogs.insertOne(document);
    }

    public void logLogin(JSONObject jsonObject) throws JSONException {
        System.out.println("here log login");
        Document document = new Document();
        document.append("type", "login");
        document.append("login", jsonObject.getString("login"));
        document.append("datetime", jsonObject.getString("datetime"));
        collectionLogs.insertOne(document);
    }

    public void insertMessage(JSONObject jsonObject) throws JSONException {
        Document document = new Document();
        document.append("from", jsonObject.getString("from"));
        document.append("message", jsonObject.getString("message"));
        document.append("to", jsonObject.getString("to"));
        collectionMessages.insertOne(document);
    }

    public void deleteUser(String login) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateFName(String name, String firstName) {
        Bson filter = new Document("firstName", name);
        Bson newValue = new Document("firstName", firstName);
        Bson updateOperationDocument = new Document("$set", newValue);
        collectionUsers.updateOne(filter, updateOperationDocument);
    }

    public void updateLName(String name, String lastName) {
        Bson filter = new Document("lastName", name);
        Bson newValue = new Document("lastName", lastName);
        Bson updateOperationDocument = new Document("$set", newValue);
        collectionUsers.updateOne(filter, updateOperationDocument);
    }

    public boolean matchLogin(String login, String password) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("login").equals(login) && BCrypt.checkpw(password, object.getString("password"))) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void login(String login, String token) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (login.equals(object.getString("login"))) {
                    Document filterDoc = new Document().append("login", login);
                    Document updateDoc = new Document().append("$set", new Document().append("token", token));
                    collectionUsers.updateOne(filterDoc, updateDoc);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout(String login, String token) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (login.equals(object.getString("login"))) {
                    Document filterDoc = new Document().append("login", login);
                    Document updateDoc = new Document().append("$unset", new Document().append("token", token));
                    collectionUsers.updateOne(filterDoc, updateDoc);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getUser(String login) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("login").equals(login)) {
                    return object;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean findToken(String token) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("token").equals(token)) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<JSONObject> getLoggedUsers() {
        List<JSONObject> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.has("token")) {
                    list.add(object);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public JSONObject getLoggedUser(String userLogin) {
        JSONObject user = new JSONObject();
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("login").equals(userLogin)) {
                    user = object;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean matchToken(String login, String token) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("login").equals(login) && object.getString("token").equals(token)) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changePassword(String login, String passHash) {
        Bson filter = new Document("login", login);
        Bson newValue = new Document("password", passHash);
        Bson updateOperationDocument = new Document("$set", newValue);
        collectionUsers.updateOne(filter, updateOperationDocument);
    }

    public String getLogin(String token) {
        try (MongoCursor<Document> cursor = collectionUsers.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("token").equals(token)) {
                    return object.getString("login");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getLogs(String login, String logType) {
        List<String> userLog = new ArrayList<>();
        try (MongoCursor<Document> cursor = collectionLogs.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject object = new JSONObject(doc.toJson());
                if (object.getString("type").equals(logType) && object.getString("login").equals(login)) {
                    userLog.add(object.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userLog;
    }
*/



    public void  skuska1() throws JSONException {

        System.out.println("function skuska1 ");
        System.out.println("");
       Document found = collectionUsers.find(new Document("login", "janko")).first();


        System.out.println("found is " + found);
        System.out.println("found is " + found.toString());


        JSONObject object = new JSONObject(found);
        System.out.println("get login from json " + object.getString("login"));

        if (found != null) {
          //return;
            System.out.println("yes we have data ");
        }
    }
}