/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coen280_hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shrut
 */
public class Populate {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws SQLException, ParseException, FileNotFoundException, IOException {
        // TODO code application logic here
        
        Populate pop = new Populate();
        Connection connection = pop.ConnectToDB();
        pop.CreateStatements(connection);
//       pop.ClearTableData(connection);     
        
        
            
//        pop.InsertBusiness(connection);
//        pop.InsertFriends(connection);
//        pop.InsertMainCategories(connection);
pop.InsertSubCategories(connection);
//         pop.InsertonlyMainCategories(connection);
//          pop.InsertOnlySubCategories(connection);
//            
//        pop.InsertUsers(connection);
//        pop.InsertReviews(connection);
      
//         pop.InsertAttributes(connection);
         

//        pop.closeDBConnection(connection);
    }
    

        
        public Connection ConnectToDB(){
            String host = "localhost";
        String dbName = "orcl";
        int port = 1521;
        String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        String username = "Scott";
        String password = "tiger";
        Connection connection = null;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            }
            catch (Exception E) {
                System.err.println("Unable to load driver.");
                E.printStackTrace();
            }
            try {
                connection = DriverManager.getConnection(oracleURL,username,password); //?user=root&password=xyz");

            }
            catch (SQLException E) {
                System.out.println("SQLException:" + E.getMessage());
                System.out.println("SQLState:" + E.getSQLState());
                System.out.println("VendorError:" + E.getErrorCode());
            }
            return connection;
        }
        
   
    
    public void closeDBConnection(Connection connection) throws SQLException{
        try{
            connection.close();
        }
        catch(Exception E){
            E.printStackTrace();
        }
        
    }
    
    public void CreateStatements(Connection connection)throws SQLException{
        try{
            
            Statement s = connection.createStatement();
//            System.out.println("==>"+connection);
            int age = 20;
            
            String query = "SELECT * FROM MainCategories";
//            String dropquery = "DROP TABLE TEST";
            s.executeQuery(query);
//            s.execute(dropquery);
            try{
//                String update = "INSERT INTO TEST VALUES(\'"+pname+"\',"+age+")";
//                s.executeUpdate(update);
           }
           catch (Exception e){
               e.printStackTrace();
           }
            ResultSet res=s.getResultSet();
            if (res!=null) {
                while(res.next()){
//                    System.out.println("\n"+res.getString(1));
                }
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void SelectStatements(Connection connection) throws SQLException{
        Statement s = connection.createStatement();
        try{
        
        }
        catch(Exception E){
            E.printStackTrace();
        }
        
    }
    
    public void InsertBusiness(Connection connection) throws ParseException, SQLException, IOException{
        String data = "";
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM Business");
//        prevstmt.executeUpdate();
        JSONParser parser = new JSONParser();
        try{
            BufferedReader myReader = new BufferedReader(new FileReader("yelp_business.json")); 
//            BufferedReader myReader = new BufferedReader(new FileReader("BusinessTestJSON.json"));
            String line = myReader.readLine();
            while (line != null) {
                data = line;
                Object obj=null;
                try{
//                System.out.println(data);
                obj = parser.parse(data);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                JSONObject jsonObject = (JSONObject) obj;



                //reading json line by line
                String business_id = (String) jsonObject.get("business_id");
                String full_address =(String) jsonObject.get("full_address");
                boolean open = (boolean)jsonObject.get("open");
    //            String main_category = ();
                String city = (String) jsonObject.get("city");
                long review_count = (long)jsonObject.get("review_count");
                String name = (String)jsonObject.get("name");
                double longitude = (double)jsonObject.get("longitude");
                String state = (String) jsonObject.get("state");
                double stars = (double)jsonObject.get("stars");
                double latitude = (double)jsonObject.get("latitude");
                String type = (String)jsonObject.get("type");
                JSONObject attributes = (JSONObject)jsonObject.get("attributes");
                               
                    
                    

//                    System.out.println(attributes.entrySet());
                    
                 
               

                PreparedStatement stmt = connection.prepareStatement("INSERT INTO Business VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                try{
                     stmt.setString(1, business_id); 
                    stmt.setString(2, full_address); 
                    stmt.setBoolean(3, open); 
                    stmt.setString(4, city); 
                    stmt.setLong(5, review_count); 
                    stmt.setString(6, name); 
                    stmt.setDouble(7, longitude); 
                    stmt.setString(8, state); 
                    stmt.setDouble(9, stars); 
                    stmt.setDouble(10, latitude);  
                    stmt.setString(11, type);
                    stmt.setString(12, attributes.toString());
                    stmt.executeUpdate();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                stmt.close();
                line = myReader.readLine();
           } 
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void InsertAttributes(Connection connection) throws IOException, SQLException{
        //begin
        String data = "";
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM Business");
//        prevstmt.executeUpdate();
        JSONParser parser = new JSONParser();
        try{
            BufferedReader myReader = new BufferedReader(new FileReader("yelp_business.json")); 
//            BufferedReader myReader = new BufferedReader(new FileReader("BusinessTestJSON.json"));
            String line = myReader.readLine();
            while (line != null) {
                data = line;
                Object obj=null;
                try{
//                System.out.println(data);
                obj = parser.parse(data);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                JSONObject jsonObject = (JSONObject) obj;
                
                //reading json line by line
               String business_id = (String) jsonObject.get("business_id");
                JSONObject attributes = (JSONObject)jsonObject.get("attributes");
                   
    //           JSONObject extends HashMap class
                    PreparedStatement stmt = connection.prepareStatement("INSERT INTO Attributes VALUES(?,?)");
                Iterator<Map.Entry<Object, Object>> iterator = attributes.entrySet().iterator();
                 while(iterator.hasNext()) {
                         Map.Entry<Object, Object> attrEntries = iterator.next();
                         if(attrEntries.getValue() instanceof JSONObject)
                           { 
                             JSONObject jsonObj = (JSONObject) attrEntries.getValue();
                             Iterator<Map.Entry<Object, Object>> iterator1 = jsonObj.entrySet().iterator();
                             while (iterator1.hasNext()) {
                                     Map.Entry<Object, Object> entry1 = iterator1.next();
                                     String attribute_string=attrEntries.getKey()+""+entry1.getKey() + "" + entry1.getValue();
                                    stmt.setString(1, jsonObject.get("business_id").toString());
                                     stmt.setString(2,attribute_string);
                                     stmt.executeUpdate();
                                     }
                           }//end of iterator1 while loop
                             else{
                                    String attribute_string=attrEntries.getKey()+"_"+attrEntries.getValue();
                                     stmt.setString(1, jsonObject.get("business_id").toString());
                                     stmt.setString(2,attribute_string);
                                     stmt.executeUpdate();
                                     }
                            }
                                     stmt.close();
    
     
                
                line = myReader.readLine();
           } 
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        
        
        
        //end
    }
    public void InsertMainCategories(Connection connection) throws FileNotFoundException, ParseException, SQLException{
        // inserting the main category list statically on application run.
        String[] mainCatSet = {"Active Life" ,"Arts & Entertainment", "Automotive", "Car Rental", "Cafes",
                                "Beauty & Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores", "Department Stores", "Education", 
                                "Event Planning & Services", "Flowers & Gifts", "Food", "Health & Medical", "Home Services", "Home & Garden", 
                                "Hospitals", "Hotels & Travel", "Hardware Stores", "Grocery", "Medical Centers", "Nurseries & Gardening", 
                                "Nightlife", "Restaurants","Shopping","Transportation"};
        ArrayList<String> mainCatArr = new ArrayList<>();
               
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM MainCategories");
//                prevstmt.executeUpdate();
        // inserting maincategories into the table
        
        for(String s : mainCatSet){
                        PreparedStatement stmt = connection.prepareStatement("INSERT INTO MainCategories VALUES(?)");
//                        System.out.println(s);
                        stmt.setString(1,s);
                        stmt.executeUpdate();
        }    
        
        PreparedStatement stmt2 =  connection.prepareStatement("SELECT S.business_id FROM SubCategories S, MainCategories M WHERE M.category_name = S.sub_category_name"); 
        stmt2.executeQuery();
        ResultSet res =stmt2.getResultSet();
        if (res!=null) {
              while(res.next()){
//              System.out.println("\n"+res.getString(1));
              }
          }
        JSONParser parser = new JSONParser();
       
//          File myfile = new File("yelp_business.json");
            File myfile = new File("BusinessTestJSON.json");
            Scanner myReader = new Scanner(myfile);
            
            while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;
                
                //reading json line by line
//                PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO MainCategories SELECT ?,? "
//                + "FROM SubCategories S, MainCategories M "
//                + "WHERE M.category_name = S.sub_category_name ");
                String business_id = (String) jsonObject.get("business_id");
                    
                 
                
//                stmt2.setString(1, business_id);
//                stmt2.setString(2,"");
//                stmt2.executeUpdate();
            }
        
        
        
        
//        System.out.println(mainCatArr);
        
       
    }
    
    
    
    
    
    public void InsertSubCategories(Connection connection) throws SQLException, FileNotFoundException, ParseException, IOException{
        //Inserting data into subCategories Table
        String data = "";
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM SubCategories");
//        prevstmt.executeUpdate();
        JSONParser parser = new JSONParser();
        PreparedStatement stmt = null;
       
          BufferedReader myReader = new BufferedReader(new FileReader("yelp_business.json")); 
//            BufferedReader myReader = new BufferedReader(new FileReader("BusinessTestJSON.json"));
            String line = myReader.readLine();
            while (line != null) {
                data = line;
                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;
                
                //reading json line by line
                String business_id = (String) jsonObject.get("business_id");
                  //categories as Java ArrayList<>                
//                
              //categories as JSONArray
                JSONArray subCategories = (JSONArray) jsonObject.get("categories");
//                subCategories.remove(categoriesArr.size()-1);
                Iterator<String> iterator = subCategories.iterator();
                
            
                while(iterator.hasNext()){
                    stmt = connection.prepareStatement("INSERT INTO SubCategories VALUES(?,?)");
                    stmt.setString(1, business_id); //business_id column
                    stmt.setString(2, iterator.next().toString()); //category_name column
                    stmt.executeUpdate(); //check data?
                    stmt.close();
                }
                
//                System.out.println();
                stmt.close();
                line = myReader.readLine();
            }
            
    }
    
    

     public void InsertUsers(Connection connection) throws ParseException, SQLException, IOException{
        String data = "";
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM YelpUser");
//        prevstmt.executeUpdate();
        JSONParser parser = new JSONParser();
        try{
            BufferedReader myReader = new BufferedReader(new FileReader("yelp_user.json")); 
//            BufferedReader myReader = new BufferedReader(new FileReader("YelpUserTestJSON.json"));
            String line = myReader.readLine();
            while (line != null) {
                data = line;
                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;
                //reading json line by line
                String yelping_since = (String) jsonObject.get("yelping_since");
                long review_count = (long)jsonObject.get("review_count");
                String name = (String)jsonObject.get("name");
                String user_id = (String) jsonObject.get("user_id");
                long fans = (long) jsonObject.get("fans");
                double average_stars = (double)jsonObject.get("average_stars");
                String type = (String)jsonObject.get("type");
                JSONObject compliment = (JSONObject) jsonObject.get("compliments");
                JSONArray elite = (JSONArray)jsonObject.get("elite");
                
                
    //           Comment: JSONObject extends HashMap class
                jsonObject.keySet().forEach(keyStr ->{
                    Object keyvalue = jsonObject.get(keyStr);

                });

                PreparedStatement stmt = connection.prepareStatement("INSERT INTO YelpUser VALUES(To_date(? ,'YYYY-MM'),?,?,?,?,?,?,?,?)");
                try{
                    stmt.setString(1, yelping_since); 
                    stmt.setLong(2, review_count); 
                    stmt.setString(3, name); 
                    stmt.setString(4, user_id); 
                    stmt.setLong(5, fans); 
                    stmt.setDouble(6, average_stars); 
                    stmt.setString(7, type); 
                    stmt.setString(8, compliment.toString()); 
                    stmt.setString(9, elite.toString()); 
                    stmt.executeUpdate();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                stmt.close();
                line = myReader.readLine();
           } 
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
     
    public void InsertReviews(Connection connection) throws SQLException, IOException, ParseException{
        String data = "";
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM Reviews");
//        prevstmt.executeUpdate();

        JSONParser parser = new JSONParser();
        try{
//            BufferedReader myReader = new BufferedReader(new FileReader("ReviewTestJSON.json")); 
          BufferedReader myReader = new BufferedReader(new FileReader("yelp_review.json"));
            String line = myReader.readLine();
            while (line != null) {
                data = line;
                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;
                //reading json line by line
                String user_id = (String) jsonObject.get("user_id");
                String review_id = (String)jsonObject.get("review_id");
                long stars = (long) jsonObject.get("stars");
                String review_date = (String) jsonObject.get("date");
                String text = (String)jsonObject.get("text");
                String type = (String) jsonObject.get("type");
                String business_id = (String)jsonObject.get("business_id");
                JSONObject votes = (JSONObject) jsonObject.get("votes");
                
               PreparedStatement stmt = connection.prepareStatement("INSERT INTO Reviews VALUES(?,?,?,To_date(? ,'YYYY-MM-DD'),?,?,?)");
                try{
                    stmt.setString(1, user_id); 
                    stmt.setString(2, review_id); 
                    stmt.setLong(3, stars); 
                    stmt.setString(4, review_date); 
                    stmt.setString(5, text); 
                    stmt.setString(6, type); 
                    stmt.setString(7, business_id); 
                    stmt.executeUpdate();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                stmt.close();
                
                PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO Votes VALUES(?,?,?,?)");
    //           Comment: JSONObject extends HashMap class
                jsonObject.keySet().forEach(keyStr ->{
                 });
                
                stmt2.setString(1, review_id);
                        stmt2.setLong(2, (long)votes.get("cool"));
                        stmt2.setLong(3, (long)votes.get("useful"));
                        stmt2.setLong(4,(long)votes.get("funny"));
                        stmt2.executeUpdate();
                stmt2.close();
                
                line = myReader.readLine();
           } 
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    
    public void InsertonlyMainCategories(Connection connection) throws SQLException{
        Statement s = connection.createStatement();
        String query = "select distinct s.business_id,s.SUB_CATEGORY_NAME as only_main_categories from subcategories s, maincategories m  where s.SUB_CATEGORY_NAME IN (select m2.CATEGORY_NAME from maincategories m2)";
        s.executeQuery(query);
        ResultSet res = s.getResultSet();
        PreparedStatement stmt = null;
        if(res != null){
            while(res.next()){
                stmt = connection.prepareStatement("INSERT INTO OnlyMainCategories VALUES(?,?)");
                stmt.setString(1, res.getString(1));
                stmt.setString(2, res.getString(2));
                stmt.executeUpdate();
                stmt.close();
                System.out.println(res.getString(1)+" "+res.getString(2));
                
            }
        }
    }
    
    public void InsertOnlySubCategories(Connection connection) throws SQLException{
        Statement s = connection.createStatement();
        String query ="select s.business_id,s.SUB_CATEGORY_NAME as only_sub_categories from subcategories s, maincategories m where s.SUB_CATEGORY_NAME NOT IN (select m2.CATEGORY_NAME from maincategories m2)";
        s.executeQuery(query);
        ResultSet res = s.getResultSet();
        PreparedStatement stmt = null;
        if(res != null){
            while(res.next()){
                stmt = connection.prepareStatement("INSERT INTO OnlySubCategories VALUES(?,?)");
                stmt.setString(1, res.getString(1));
                stmt.setString(2, res.getString(2));
                stmt.executeUpdate();
                stmt.close();
                System.out.println(res.getString(1)+" "+res.getString(2));
                
            }
        }
    }
    
    public void InsertFriends(Connection connection) throws IOException, ParseException, SQLException{
         String data = "";
//        PreparedStatement prevstmt = connection.prepareStatement("DELETE FROM YelpUser");
//        prevstmt.executeUpdate();
        JSONParser parser = new JSONParser();
        try{
            BufferedReader myReader = new BufferedReader(new FileReader("yelp_user.json")); 
//            BufferedReader myReader = new BufferedReader(new FileReader("YelpUserTestJSON.json"));
            String line = myReader.readLine();
            while (line != null) {
                data = line;
                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;
                
                String user_id = (String) jsonObject.get("user_id");
                
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO Friends VALUES(?,?)");
                try{
                 JSONArray friend = (JSONArray) jsonObject.get("friends");
                    Iterator<String> iterator = friend.iterator();
                    
                    while(iterator.hasNext()){
                        stmt.setString(1, iterator.next().toString()); 
                        stmt.setString(2, user_id.toString()); 
                        stmt.executeUpdate();
                        
                    }
                   
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                stmt.close();
                line = myReader.readLine();
           } 
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void ClearTableData(Connection connection) throws SQLException {
        PreparedStatement attrstmt = connection.prepareStatement("DELETE FROM Attributes");
        attrstmt.executeUpdate(); 
        
        PreparedStatement friendsstmt = connection.prepareStatement("DELETE FROM Friends");
        friendsstmt.executeUpdate(); 
        
        PreparedStatement prevstmt6 = connection.prepareStatement("DELETE FROM Votes");
        prevstmt6.executeUpdate();
        
        PreparedStatement prevstmt5 = connection.prepareStatement("DELETE FROM Reviews");
        prevstmt5.executeUpdate();
        
         PreparedStatement prevstmtsub = connection.prepareStatement("DELETE FROM onlySubCategories");
        prevstmtsub.executeUpdate();
         // only subcategoriess etc
        PreparedStatement prevstmtonlymain = connection.prepareStatement("DELETE FROM onlyMainCategories");
        prevstmtonlymain.executeUpdate();
        
        PreparedStatement prevstmt4 = connection.prepareStatement("DELETE FROM SubCategories");
        prevstmt4.executeUpdate();
        
       
        PreparedStatement prevstmt3 = connection.prepareStatement("DELETE FROM MainCategories");
        prevstmt3.executeUpdate();
        PreparedStatement prevstmt2 = connection.prepareStatement("DELETE FROM YelpUser");
        prevstmt2.executeUpdate();
        PreparedStatement prevstmt1 = connection.prepareStatement("DELETE FROM Business");
        prevstmt1.executeUpdate();
        prevstmt1.close();
        prevstmt2.close();
        prevstmt3.close();
        prevstmt4.close();
        prevstmt5.close();
        prevstmt6.close();
        prevstmtonlymain.close();
        
    }
    
}

