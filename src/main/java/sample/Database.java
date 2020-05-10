package sample;


import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    // get Connection

    //disconnect

    //add user into database

    //mongodb://127.0.0.1:27017/?readPreference=primary&appname=MongoDB%20Compass%20Community&ssl=false

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        try (MongoClient mongoClient =  MongoClients.create("mongodb://localhost:27017")) {

            //Using already made DB company, create new collection employees
            //database.createCollection("employees");

            List<String> databases = mongoClient.listDatabaseNames().into(new ArrayList<>());
            System.out.println("database" + databases);


            System.out.println("=======================================================");
            MongoDatabase database = mongoClient.getDatabase("javaServer");

            System.out.println("database name: " + database.getName());
            //print all collections
            for (String name: database.listCollectionNames()){
                System.out.println(name);
            }

            //MongoCursor<String> cursor = strings.cursor();
            System.out.println("///////////////////////////////////////////////////////////////");
            System.out.println("///////////////////////////////////////////////////////////////");

            MongoCollection<Document> collection=database.getCollection("users");


            Document doc= new Document("name", "Peter Sagan ")
                    .append("age", 30 )
                    .append("possition", "Sport")
                    .append("startInCompany", "10.2.2019");

            //insertOne
            collection.insertOne(doc);



            System.out.println("--                                          ---                 --");
            System.out.println("--                                          ---                 --");
            System.out.println("--                                          ---                 --");
            //print all employees
            System.out.println("_________All users_________");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                System.out.println(document.toJson());
            }



            //update one employee
            Bson updateQuery=new Document("possition", "Programmer");
            Bson newValue=new Document("possition", "Better Programmer like yesterday");
            Bson update=new Document("$set", newValue);

            collection.updateOne(updateQuery, update);


            System.out.println("--                                          ---                 --");
            System.out.println("--                                          ---                 --");
            System.out.println("--                                          ---                 --");
            System.out.println("_________Update user________");
            MongoCursor<Document> cursor1 = collection.find().iterator();
            while (cursor1.hasNext()) {
                Document document = cursor1.next();
                System.out.println(document.toJson());
            }


            //delete one employee
            BasicDBObject deleteQuery=new BasicDBObject();
            deleteQuery.put("name","Peter Toth");
            collection.deleteOne(deleteQuery);

            System.out.println("_________users after delete________");
            MongoCursor<Document> cursor2 = collection.find().iterator();
            while (cursor2.hasNext()) {
                Document document = cursor2.next();
                System.out.println(document.toJson());
            }
        }
    }
}
