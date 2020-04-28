/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coen280_hw3;
import coen280_hw3.Populate;
import com.toedter.calendar.JDateChooser;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import static java.sql.JDBCType.ARRAY;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import oracle.sql.ArrayDescriptor;
import oracle.sql.ARRAY;
/**
 *
 * @author shrut
 */
public class HW3 extends javax.swing.JFrame {

    /**
     * Creates new form HW3
     */
//    String test="test change text";
    Populate pop = new Populate();
    Connection connection = pop.ConnectToDB();
     Statement stmt = connection.createStatement();
//    HW3 h = new HW3();
//    String cat_selected="";
//     String categorychecklist = "activeLifeCheck, artsAndECheck, automotiveCheck, carRentalCheck, cafesCheck, convStoresCheck, beautyNSpaCheck, dentistsCheck, doctorsCheck, drugstoresCheck, deptStoresCheck, educationCheck, foodCheck, healthNMedicalCheck, homeServicesCheck, eventPlanningNSCheck, flowersNGiftsCheck, homeNGardenCheck, hospitalsCheck, hotelsNTravelCheck, hardwareStoresCheck, groceriesCheck, medicalCentersCheck, nursNgardeningCheck, nightlifeCheck, restaurantsCheck, shoppingCheck, transportationCheck";
//       String[] categoryCheckArr = categorychecklist.split(",");
       ArrayList<String> subcategories = new ArrayList<>();
         ArrayList<String> checkedcategories = new ArrayList<>();
         ArrayList<String> checkedsubcategories = new ArrayList<>();
         ArrayList<String> checkedattributes = new ArrayList();
         ArrayList<String> attributes = new ArrayList();
         ArrayList<String> facetedBid = new ArrayList();
         ResultSet subCatListRes =null;
         ResultSet mainCatListRes =null;

         
         int count=0;
         

    public HW3() throws SQLException {
        initComponents();
//        temp();
    
        selectMainTableRow(); // must

         mainCatListRes = loadCategories();
         mainCategorylistJPanel.removeAll();
        mainCategorylistJPanel.setLayout(new BoxLayout(mainCategorylistJPanel,BoxLayout.Y_AXIS));
        String bizAndOrSel = bizAndOrComboBox.getSelectedItem().toString();
        if(mainCatListRes != null){
         while(mainCatListRes.next()){
                       
                       String mcName = mainCatListRes.getString(1);
                       JCheckBox mcCheckBox = new JCheckBox(mcName);
//                       
                       mainCategorylistJPanel.add(mcCheckBox);
                       populateSubcategories(mcCheckBox);
                       // starts
//                       
                        // ends
                      
              }
            
       
       } //end if
        
        
        
       
    }
   
    public void populateSubcategories(JCheckBox mcCheckBox){
         mcCheckBox.addActionListener(new ActionListener(){
                           @Override
                           public void actionPerformed(ActionEvent e){
                                
                                  JCheckBox mcCheckBox = (JCheckBox) e.getSource();
                                    checkedcategories.clear();
                                    checkedsubcategories.clear();
                                    attributes.clear();
//                                   String query = "";
//                                   String catSet = "";
////                                   ArrayDescriptor ad;
//                                  StringBuilder sbString = new StringBuilder();
                                  subCategoryListJPanel.removeAll();
                                 subCategoryListJPanel.revalidate();
                                    subCategoryListJPanel.repaint();
                                    
                                     attributeListJPanel.removeAll();
                                     attributeListJPanel.revalidate();
                                    attributeListJPanel.repaint();
                                    
//                                   ARRAY catSqlArr =null;
                                    for(Component c: mainCategorylistJPanel.getComponents()){
                                        if(c.getClass().equals(javax.swing.JCheckBox.class)){
                                            JCheckBox jcb  = (JCheckBox) c;
                                            if(jcb.isSelected()){
                                                checkedcategories.add(jcb.getText());
                                                
                                                
                                            }
                                        }
                                    }
                                    System.out.println("root :checkedcategories"+checkedcategories);
                                   showSubCatList();
//                                   showTableEntries();
                                
                           }
                           
                       });
    }
    
    public void showSubCatList(){
        String query = "";
        StringBuilder sbString = new StringBuilder();
        if(checkedcategories.size()>0){
            for(String items : checkedcategories){
                    sbString.append("'"+items).append("',");
                    if(bizAndOrComboBox.getSelectedItem() == "AND"){
                        query ="select distinct s.SUB_CATEGORY_NAME from OnlysubCategories s,onlymaincategories m "
                                + "where m.BUSINESS_ID = s.BUSINESS_ID and m.main_category_name in ("+sbString.toString().replaceAll(",$","")+")" +
                        "GROUP BY s.SUB_CATEGORY_NAME " +
                        "HAVING COUNT(Distinct m.main_category_name) = "+checkedcategories.size(); 
                    }
                    else if(bizAndOrComboBox.getSelectedItem() == "OR"){
                         query ="select distinct s.SUB_CATEGORY_NAME from OnlysubCategories s,onlymaincategories m " +
                        "where m.BUSINESS_ID = s.BUSINESS_ID and m.main_category_name in ("+sbString.toString().replaceAll(",$","")+")";  
                    }
            }
        
        
        
            

        try {
                                   PreparedStatement catToSubcatStmt = connection.prepareStatement(query);
                                   ResultSet subCatListRes = catToSubcatStmt.executeQuery();
                                   
                                    subCategoryListJPanel.removeAll();
                                    subCategoryListJPanel.setLayout(new BoxLayout(subCategoryListJPanel,BoxLayout.Y_AXIS));
                                    subCategoryListJPanel.revalidate();
                                    subCategoryListJPanel.repaint();
                                   
                                   while(subCatListRes.next()){
                                       
                                       String scName = subCatListRes.getString(1);
                                       subcategories.add(scName);
                                      
                                       JCheckBox  scCheckBox = new JCheckBox(scName);
                                       subCategoryListJPanel.add(scCheckBox);
                                      subCategoryListJPanel.setVisible(true);
                                      scCheckBox.setVisible(true);
                                      subCategoryListJPanel.revalidate();
                                      //noewmethod
                                       scCheckBox.addActionListener(new ActionListener(){
                                        @Override
                                        public void actionPerformed(ActionEvent e){

                                            try {
                                                JCheckBox scCheckBox = (JCheckBox) e.getSource();
                                                checkedsubcategories.clear();
                                                checkedattributes.clear();
                                               attributeListJPanel.removeAll();
                                                attributeListJPanel.revalidate();
                                                attributeListJPanel.repaint();
//                                               


                                                for(Component c: subCategoryListJPanel.getComponents()){
                                                    if(c.getClass().equals(javax.swing.JCheckBox.class)){
                                                        JCheckBox jcb  = (JCheckBox) c;
                                                        if(jcb.isSelected()){
                                                            checkedsubcategories.add(jcb.getText());
                                                            

                                                        }
                                                    }
                                                }
                                                System.out.println("root: checkedsubcategories"+checkedsubcategories);
                                                showAttrList();
                                            } catch (SQLException ex) {
                                                Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                       });
                                      
                                   }
                                   
                                      
                                   
            } catch (SQLException ex) {
                Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
            }
//                                  
//            displayQueryJTextArea.setText(query);
            }
    }
    
    
    public void populateAttributes(JCheckBox scCheckBox){
        
    }
    
 
    public void showAttrList() throws SQLException{
       
//        getcheckedsubcategories();
        String query= "";
        StringBuilder subString = new StringBuilder();
        StringBuilder catString = new StringBuilder();
//        System.out.println(checkedcategories);
//        System.out.println(checkedsubcategories); // mostupdated values
        String andcatquery="";
         String orcatquery ="";
  String andsubquery="";
          String orsubquery = "";
          if(checkedsubcategories.size() > 0){
              for(String items : checkedsubcategories){
           
                    subString.append("'"+items).append("',");
                    
                        andsubquery =" select distinct a.ATTRIBUTES from OnlysubCategories s,Attributes a "
                                + "where a.BUSINESS_ID = s.BUSINESS_ID and s.SUB_CATEGORY_NAME in ("+subString.toString().replaceAll(",$","")+") "
                                + " GROUP BY a.ATTRIBUTES HAVING COUNT(Distinct s.SUB_CATEGORY_NAME) = "+checkedsubcategories.size();
                     
                         orsubquery ="  select distinct a.ATTRIBUTES from OnlysubCategories s,Attributes a "
                                + "where a.BUSINESS_ID = s.BUSINESS_ID and s.SUB_CATEGORY_NAME in ("+subString.toString().replaceAll(",$","")+") ";
                    
                 
                }
              if(bizAndOrComboBox.getSelectedItem() == "AND"){
                    query = andcatquery+ " Intersect "+andsubquery;
                }
               else if(bizAndOrComboBox.getSelectedItem() == "OR"){
                    query = orcatquery+ " Intersect "+orsubquery;
                }
         
        
        for(String items: checkedcategories){
            catString.append("'"+items).append("',");
            
            andcatquery = " select distinct a.ATTRIBUTES from attributes a,onlymaincategories m  "
                                + "where m.BUSINESS_ID = a.BUSINESS_ID and m.main_category_name in ("+catString.toString().replaceAll(",$","")+") "
                                + " GROUP BY  a.ATTRIBUTES HAVING COUNT(Distinct m.main_category_name) = "+checkedcategories.size();
            orcatquery =" select distinct  a.ATTRIBUTES from attributes a,onlymaincategories m  "
                                + "where m.BUSINESS_ID = a.BUSINESS_ID and m.main_category_name in ("+catString.toString().replaceAll(",$","")+") ";
            
            if(bizAndOrComboBox.getSelectedItem() == "AND"){
            query = andcatquery+ " Intersect "+andsubquery;
            }
           else if(bizAndOrComboBox.getSelectedItem() == "OR"){
                query = orcatquery+ " Intersect "+orsubquery;
            }
        
        }
        
        
        
//        System.out.println(query);
        
        String showAttrQuery = query;
//        showAttrQuery = "SELECT * FROM MainCategories";
//             PreparedStatement showarrstmt = connection.prepareStatement(showAttrQuery);
//            ResultSet showAttrRes = showarrstmt.executeQuery();
             Statement showarrstmt = connection.createStatement();
            ResultSet showAttrRes = showarrstmt.executeQuery(showAttrQuery);
            attributeListJPanel.removeAll();
            attributeListJPanel.setLayout(new BoxLayout(attributeListJPanel,BoxLayout.Y_AXIS));
            attributeListJPanel.revalidate();
            attributeListJPanel.repaint();
            
            while(showAttrRes.next()){
                String atcName = showAttrRes.getString(1);
                attributes.add(atcName);
                JCheckBox atcCheckBox = new JCheckBox(atcName);
                attributeListJPanel.add(atcCheckBox);
                attributeListJPanel.setVisible(true);
                atcCheckBox.setVisible(true);
                
                attributeListJPanel.revalidate();
                attributeListJPanel.repaint();
           
             
             atcCheckBox.addActionListener(new ActionListener(){
                 @Override
                 public void actionPerformed(ActionEvent e){
                     checkedattributes.clear();
                     JCheckBox atcCheckBox = (JCheckBox) e.getSource();
                      for(Component c: attributeListJPanel.getComponents()){
                        if(c.getClass().equals(javax.swing.JCheckBox.class)){
                            JCheckBox jcb  = (JCheckBox) c;
                            if(jcb.isSelected()){
                                checkedattributes.add(jcb.getText());
                                

                            }
                        }
                    }
                      System.out.println("root: checkedattributes:"+checkedattributes);
                      
                 }
            });
             
        }
            // checkedAttrlist arraylist gets filled
//            paint ui
                
        }     
//             displayQueryJTextArea.setText(showAttrQuery);       
     }
    //to get cmmon biz id list for any check box -- too expensive
    public void facetedBusinessIds(String checkedbox) throws SQLException{
        Statement fst = connection.createStatement();
        ResultSet res= fst.executeQuery("select business_id from onlymaincategories where main_category_name ='"+checkedbox+"'");
        while(res.next()){
            if(!facetedBid.contains(checkedbox)){
                facetedBid.add(res.getString(1));
            }
        
        }
        
//        System.out.println(facetedBid+checkedbox);
    } 
    
    
    
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        yelpSearchJTabbedPane = new javax.swing.JTabbedPane();
        userSearchJPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        userAndOrComboBox = new javax.swing.JComboBox<>();
        memberSinceJLabel = new javax.swing.JLabel();
        memberSinceJDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        reviewCountComboBox = new javax.swing.JComboBox<>();
        reviewCountVal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        noOfFriendsComboBox = new javax.swing.JComboBox<>();
        noOfFriendsVal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        avgStarsComboBox = new javax.swing.JComboBox<>();
        avgStarsVal = new javax.swing.JTextField();
        bizSearchJPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        bizAndOrComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        subCategoryListJPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        attributeListJPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mainCategorylistJPanel = new javax.swing.JPanel();
        activeLifeCheck = new javax.swing.JCheckBox();
        artsAndECheck = new javax.swing.JCheckBox();
        automotiveCheck = new javax.swing.JCheckBox();
        carRentalCheck = new javax.swing.JCheckBox();
        cafesCheck = new javax.swing.JCheckBox();
        convStoresCheck = new javax.swing.JCheckBox();
        beautyNSpaCheck = new javax.swing.JCheckBox();
        dentistsCheck = new javax.swing.JCheckBox();
        doctorsCheck = new javax.swing.JCheckBox();
        drugstoresCheck = new javax.swing.JCheckBox();
        deptStoresCheck = new javax.swing.JCheckBox();
        educationCheck = new javax.swing.JCheckBox();
        foodCheck = new javax.swing.JCheckBox();
        healthNMedicalCheck = new javax.swing.JCheckBox();
        homeServicesCheck = new javax.swing.JCheckBox();
        eventPlanningNSCheck = new javax.swing.JCheckBox();
        flowersNGiftsCheck = new javax.swing.JCheckBox();
        homeNGardenCheck = new javax.swing.JCheckBox();
        hospitalsCheck = new javax.swing.JCheckBox();
        hotelsNTravelCheck = new javax.swing.JCheckBox();
        hardwareStoresCheck = new javax.swing.JCheckBox();
        groceriesCheck = new javax.swing.JCheckBox();
        medicalCentersCheck = new javax.swing.JCheckBox();
        nursNgardeningCheck = new javax.swing.JCheckBox();
        nightlifeCheck = new javax.swing.JCheckBox();
        restaurantsCheck = new javax.swing.JCheckBox();
        shoppingCheck = new javax.swing.JCheckBox();
        transportationCheck = new javax.swing.JCheckBox();
        reviewfilterJPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        reviewFromJDate = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        reviewToJDate = new com.toedter.calendar.JDateChooser();
        reviewStarComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        reviewVotesComboBox = new javax.swing.JComboBox<>();
        reviewStarVal = new javax.swing.JTextField();
        reviewVotesVal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        displayQueryJTextArea = new javax.swing.JTextArea();
        executeQueryJButton = new javax.swing.JButton();
        yelptableJPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        yelpJTable = new javax.swing.JTable();
        reviewtableJPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        reviewJTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        yelpSearchJTabbedPane.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel5.setText("Select for AND/OR between attributes: ");

        userAndOrComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND", "OR" }));
        userAndOrComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userAndOrComboBoxActionPerformed(evt);
            }
        });

        memberSinceJLabel.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        memberSinceJLabel.setForeground(new java.awt.Color(0, 102, 153));
        memberSinceJLabel.setText("Member Since ");

        memberSinceJDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                memberSinceJDatePropertyChange(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Review Count");
        jLabel1.setAlignmentX(0.5F);

        reviewCountComboBox.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        reviewCountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<" }));
        reviewCountComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewCountComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("Number of Friends");

        noOfFriendsComboBox.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        noOfFriendsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<" }));
        noOfFriendsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noOfFriendsComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 153));
        jLabel3.setText("Average Stars");

        avgStarsComboBox.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        avgStarsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<" }));
        avgStarsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avgStarsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout userSearchJPanelLayout = new javax.swing.GroupLayout(userSearchJPanel);
        userSearchJPanel.setLayout(userSearchJPanelLayout);
        userSearchJPanelLayout.setHorizontalGroup(
            userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSearchJPanelLayout.createSequentialGroup()
                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userSearchJPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userAndOrComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(userSearchJPanelLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(userSearchJPanelLayout.createSequentialGroup()
                                .addComponent(memberSinceJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userSearchJPanelLayout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(23, 23, 23))
                                .addGroup(userSearchJPanelLayout.createSequentialGroup()
                                    .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(userSearchJPanelLayout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addComponent(jLabel3)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                        .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(memberSinceJDate, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(userSearchJPanelLayout.createSequentialGroup()
                                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(noOfFriendsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(avgStarsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(reviewCountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(avgStarsVal, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noOfFriendsVal, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(reviewCountVal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 248, Short.MAX_VALUE))
        );
        userSearchJPanelLayout.setVerticalGroup(
            userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userSearchJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userAndOrComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(memberSinceJDate, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(memberSinceJLabel))
                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userSearchJPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(reviewCountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noOfFriendsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(avgStarsVal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(avgStarsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(userSearchJPanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(userSearchJPanelLayout.createSequentialGroup()
                                .addComponent(reviewCountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(userSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(noOfFriendsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))))))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        yelpSearchJTabbedPane.addTab("User Search", userSearchJPanel);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel6.setText("Search for AND/OR between attributes: ");

        bizAndOrComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND", "OR" }));

        jLabel7.setText("Category");

        subCategoryListJPanel.setMaximumSize(new java.awt.Dimension(32767, 500));

        javax.swing.GroupLayout subCategoryListJPanelLayout = new javax.swing.GroupLayout(subCategoryListJPanel);
        subCategoryListJPanel.setLayout(subCategoryListJPanelLayout);
        subCategoryListJPanelLayout.setHorizontalGroup(
            subCategoryListJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 154, Short.MAX_VALUE)
        );
        subCategoryListJPanelLayout.setVerticalGroup(
            subCategoryListJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(subCategoryListJPanel);

        jLabel8.setText("Sub-category");

        attributeListJPanel.setMaximumSize(new java.awt.Dimension(32767, 500));

        javax.swing.GroupLayout attributeListJPanelLayout = new javax.swing.GroupLayout(attributeListJPanel);
        attributeListJPanel.setLayout(attributeListJPanelLayout);
        attributeListJPanelLayout.setHorizontalGroup(
            attributeListJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );
        attributeListJPanelLayout.setVerticalGroup(
            attributeListJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(attributeListJPanel);

        jLabel9.setText("Attributes");

        mainCategorylistJPanel.setMaximumSize(new java.awt.Dimension(32767, 500));
        mainCategorylistJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mainCategorylistJPanelMousePressed(evt);
            }
        });

        activeLifeCheck.setText("Active Life");
        activeLifeCheck.setToolTipText("");
        activeLifeCheck.setAlignmentY(0.0F);
        activeLifeCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeLifeCheckActionPerformed(evt);
            }
        });

        artsAndECheck.setText("Arts & Entertainment");
        artsAndECheck.setAlignmentY(0.0F);
        artsAndECheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                artsAndECheckActionPerformed(evt);
            }
        });

        automotiveCheck.setText("Automotive");
        automotiveCheck.setAlignmentY(0.0F);

        carRentalCheck.setText("Car Rental");
        carRentalCheck.setAlignmentY(0.0F);
        carRentalCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carRentalCheckActionPerformed(evt);
            }
        });

        cafesCheck.setText("Cafes");
        cafesCheck.setAlignmentY(0.0F);

        convStoresCheck.setText("Convenience Stores");
        convStoresCheck.setAlignmentY(0.0F);

        beautyNSpaCheck.setText("Beauty & Spas");
        beautyNSpaCheck.setAlignmentY(0.0F);

        dentistsCheck.setText("Dentists");
        dentistsCheck.setAlignmentY(0.0F);

        doctorsCheck.setText("Doctors");
        doctorsCheck.setAlignmentY(0.0F);

        drugstoresCheck.setText("Drugstores");
        drugstoresCheck.setAlignmentY(0.0F);

        deptStoresCheck.setText("Department Stores");
        deptStoresCheck.setAlignmentY(0.0F);
        deptStoresCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptStoresCheckActionPerformed(evt);
            }
        });

        educationCheck.setText("Education");
        educationCheck.setAlignmentY(0.0F);

        foodCheck.setText("Food");
        foodCheck.setAlignmentY(0.0F);
        foodCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodCheckActionPerformed(evt);
            }
        });

        healthNMedicalCheck.setText("Health & Medical");
        healthNMedicalCheck.setAlignmentY(0.0F);

        homeServicesCheck.setText("Home Services");
        homeServicesCheck.setAlignmentY(0.0F);

        eventPlanningNSCheck.setText("Event Planning & Services");
        eventPlanningNSCheck.setAlignmentY(0.0F);

        flowersNGiftsCheck.setText("Flowers & Gifts");
        flowersNGiftsCheck.setAlignmentY(0.0F);

        homeNGardenCheck.setText("Home & Garden");
        homeNGardenCheck.setAlignmentY(0.0F);
        homeNGardenCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeNGardenCheckActionPerformed(evt);
            }
        });

        hospitalsCheck.setText("Hospitals");
        hospitalsCheck.setAlignmentY(0.0F);

        hotelsNTravelCheck.setText("Hotels & Travel");
        hotelsNTravelCheck.setAlignmentY(0.0F);

        hardwareStoresCheck.setText("Hardware Stores");
        hardwareStoresCheck.setAlignmentY(0.0F);

        groceriesCheck.setText("Grocery");
        groceriesCheck.setAlignmentY(0.0F);

        medicalCentersCheck.setText(" Medical Centers");
        medicalCentersCheck.setAlignmentY(0.0F);

        nursNgardeningCheck.setText(" Nurseries & Gardening");
        nursNgardeningCheck.setAlignmentY(0.0F);

        nightlifeCheck.setText(" Nightlife");
        nightlifeCheck.setAlignmentY(0.0F);

        restaurantsCheck.setText(" Restaurants");
        restaurantsCheck.setAlignmentY(0.0F);

        shoppingCheck.setText("Shopping");
        shoppingCheck.setAlignmentY(0.0F);

        transportationCheck.setText(" Transportation");
        transportationCheck.setAlignmentY(0.0F);

        javax.swing.GroupLayout mainCategorylistJPanelLayout = new javax.swing.GroupLayout(mainCategorylistJPanel);
        mainCategorylistJPanel.setLayout(mainCategorylistJPanelLayout);
        mainCategorylistJPanelLayout.setHorizontalGroup(
            mainCategorylistJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCategorylistJPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(mainCategorylistJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transportationCheck)
                    .addComponent(shoppingCheck)
                    .addComponent(restaurantsCheck)
                    .addComponent(nightlifeCheck)
                    .addComponent(nursNgardeningCheck)
                    .addComponent(medicalCentersCheck)
                    .addComponent(groceriesCheck)
                    .addComponent(hardwareStoresCheck)
                    .addComponent(hotelsNTravelCheck)
                    .addComponent(healthNMedicalCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(foodCheck)
                    .addComponent(educationCheck)
                    .addComponent(flowersNGiftsCheck)
                    .addComponent(eventPlanningNSCheck)
                    .addComponent(drugstoresCheck)
                    .addComponent(doctorsCheck)
                    .addComponent(dentistsCheck)
                    .addComponent(convStoresCheck)
                    .addComponent(beautyNSpaCheck)
                    .addComponent(cafesCheck)
                    .addComponent(carRentalCheck)
                    .addComponent(automotiveCheck)
                    .addComponent(artsAndECheck)
                    .addComponent(activeLifeCheck)
                    .addComponent(deptStoresCheck)
                    .addComponent(homeServicesCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainCategorylistJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(homeNGardenCheck, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hospitalsCheck, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        mainCategorylistJPanelLayout.setVerticalGroup(
            mainCategorylistJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainCategorylistJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(activeLifeCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(artsAndECheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(automotiveCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(carRentalCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cafesCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(beautyNSpaCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(convStoresCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dentistsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doctorsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drugstoresCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deptStoresCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(educationCheck)
                .addGap(6, 6, 6)
                .addComponent(eventPlanningNSCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(flowersNGiftsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(healthNMedicalCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(homeServicesCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(homeNGardenCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hospitalsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hotelsNTravelCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hardwareStoresCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groceriesCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(medicalCentersCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nursNgardeningCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nightlifeCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(restaurantsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shoppingCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(transportationCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(mainCategorylistJPanel);

        jLabel10.setText("Review");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 153));
        jLabel11.setText("From");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 153));
        jLabel12.setText("To");

        reviewStarComboBox.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        reviewStarComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<" }));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 153));
        jLabel13.setText("Star");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 153));
        jLabel14.setText("Value");

        reviewVotesComboBox.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        reviewVotesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<" }));
        reviewVotesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewVotesComboBoxActionPerformed(evt);
            }
        });

        reviewStarVal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewStarValActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 153));
        jLabel15.setText("Votes");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 153));
        jLabel16.setText("Value");

        javax.swing.GroupLayout reviewfilterJPanelLayout = new javax.swing.GroupLayout(reviewfilterJPanel);
        reviewfilterJPanel.setLayout(reviewfilterJPanelLayout);
        reviewfilterJPanelLayout.setHorizontalGroup(
            reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reviewfilterJPanelLayout.createSequentialGroup()
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reviewfilterJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(reviewfilterJPanelLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(reviewFromJDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(reviewStarComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reviewToJDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reviewStarVal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reviewVotesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reviewVotesVal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(reviewfilterJPanelLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reviewfilterJPanelLayout.setVerticalGroup(
            reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reviewfilterJPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reviewFromJDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reviewfilterJPanelLayout.createSequentialGroup()
                        .addComponent(reviewToJDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reviewStarComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(16, 16, 16)
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reviewStarVal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reviewVotesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(reviewfilterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reviewVotesVal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)))
        );

        javax.swing.GroupLayout bizSearchJPanelLayout = new javax.swing.GroupLayout(bizSearchJPanel);
        bizSearchJPanel.setLayout(bizSearchJPanelLayout);
        bizSearchJPanelLayout.setHorizontalGroup(
            bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bizSearchJPanelLayout.createSequentialGroup()
                .addGroup(bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bizSearchJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bizSearchJPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel7)
                        .addGap(117, 117, 117)
                        .addComponent(jLabel8)
                        .addGap(120, 120, 120)
                        .addComponent(jLabel9))
                    .addGroup(bizSearchJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(bizAndOrComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reviewfilterJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        bizSearchJPanelLayout.setVerticalGroup(
            bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bizSearchJPanelLayout.createSequentialGroup()
                .addGroup(bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bizSearchJPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(reviewfilterJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bizSearchJPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bizAndOrComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bizSearchJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(25, 25, 25))
        );

        yelpSearchJTabbedPane.addTab("Business Search", bizSearchJPanel);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel4.setText("SQL Query for the search shows below: ");

        displayQueryJTextArea.setColumns(20);
        displayQueryJTextArea.setFont(new java.awt.Font("Monospaced", 0, 15)); // NOI18N
        displayQueryJTextArea.setLineWrap(true);
        displayQueryJTextArea.setRows(5);
        displayQueryJTextArea.setText("<SQL Query appears here>");
        displayQueryJTextArea.setWrapStyleWord(true);
        jScrollPane2.setViewportView(displayQueryJTextArea);

        executeQueryJButton.setText("Execute Query ");
        executeQueryJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeQueryJButtonActionPerformed(evt);
            }
        });

        jScrollPane5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        yelpJTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        yelpJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(yelpJTable);

        javax.swing.GroupLayout yelptableJPanelLayout = new javax.swing.GroupLayout(yelptableJPanel);
        yelptableJPanel.setLayout(yelptableJPanelLayout);
        yelptableJPanelLayout.setHorizontalGroup(
            yelptableJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yelptableJPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 21, Short.MAX_VALUE))
        );
        yelptableJPanelLayout.setVerticalGroup(
            yelptableJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yelptableJPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        reviewJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(reviewJTable);

        javax.swing.GroupLayout reviewtableJPanelLayout = new javax.swing.GroupLayout(reviewtableJPanel);
        reviewtableJPanel.setLayout(reviewtableJPanelLayout);
        reviewtableJPanelLayout.setHorizontalGroup(
            reviewtableJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reviewtableJPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );
        reviewtableJPanelLayout.setVerticalGroup(
            reviewtableJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reviewtableJPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(executeQueryJButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(209, 209, 209)
                                .addComponent(jLabel4))))
                    .addComponent(yelpSearchJTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reviewtableJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(yelptableJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(yelptableJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(yelpSearchJTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(executeQueryJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(reviewtableJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    
    private void reviewCountComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewCountComboBoxActionPerformed
        // TODO add your handling code here:
        System.out.println(reviewCountComboBox.getSelectedItem());
    }//GEN-LAST:event_reviewCountComboBoxActionPerformed
    public void stateChanged(ChangeEvent e) {
        yelpSearchJTabbedPane = (JTabbedPane) e.getSource();
        int selectedIndex = yelpSearchJTabbedPane.getSelectedIndex();
        System.out.println(selectedIndex);
//        JOptionPane.showMessageDialog(null, "Selected Index: " + selectedIndex);
    }
    
    public void CreateTable(ResultSet r) throws SQLException{ //generic
        DefaultTableModel dtm=new DefaultTableModel();
        yelptableJPanel.revalidate();
        yelptableJPanel.repaint();
        
       dtm.setRowCount(0);
         while(r.next()){
             
             System.out.println("rsmetadata: "+r.getString(1));
                ResultSetMetaData rsmetadata = r.getMetaData();
                
                dtm.setColumnCount(rsmetadata.getColumnCount());
                Vector<String> columnData = new Vector();
                for(int i=1;i<=rsmetadata.getColumnCount();i++){
                    columnData.add(rsmetadata.getColumnName(i));
                    
                }
                dtm.setColumnIdentifiers(columnData);
                while(r.next()){
                    columnData = new Vector();
                    for(int i=1;i<=rsmetadata.getColumnCount();i++){
                        columnData.add(r.getString(rsmetadata.getColumnName(i)));
                        
                    }
                    dtm.addRow(columnData);
                    
                }
                yelpJTable.setModel(dtm);
                yelpJTable.setFillsViewportHeight(true);
                System.out.println("row count:"+yelpJTable.getRowCount());
            }
//         yelpJTable.setCellSelectionEnabled(true);
         yelpJTable.setRowSelectionAllowed(true);
        ListSelectionModel select= yelpJTable.getSelectionModel();  
         select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         yelptableJPanel.revalidate();
        yelptableJPanel.repaint();
        
         
    }
    
    public void selectMainTableRow() throws NullPointerException{
         String  reviewQuery = "";
         StringBuilder reviewFilterQuery = new StringBuilder();
        DefaultTableModel dtm=new DefaultTableModel();
        dtm.setRowCount(0);
        reviewtableJPanel.revalidate();
        reviewtableJPanel.repaint();
        String bizAndOrSel = bizAndOrComboBox.getSelectedItem().toString();
        yelpJTable.addMouseListener(new java.awt.event.MouseAdapter(){
             @Override
             public void  mouseClicked(java.awt.event.MouseEvent evt){
                 if (yelpSearchJTabbedPane.getSelectedIndex() == 0){
                     //user
                     int r = yelpJTable.getSelectedRow();
                       int c = yelpJTable.getSelectedColumn();
                       int row = yelpJTable.getSelectedRow();
                            System.out.println(yelpJTable.getValueAt(r,0));
                 
                            String biz_id_sel = (String)yelpJTable.getValueAt(r,0);
                            String reviewQuery ="select u.USER_NAME, r.TEXT from yelpuser u,reviews r "
                        + "where u.user_id = r.user_id and r.BUSINESS_ID = '"+yelpJTable.getValueAt(r,0)+"'";
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                 
                 }
                 else if(yelpSearchJTabbedPane.getSelectedIndex() ==1){
                     //business
                     
                     int r = yelpJTable.getSelectedRow();
                       int c = yelpJTable.getSelectedColumn();
                       int row = yelpJTable.getSelectedRow();
                            System.out.println(yelpJTable.getValueAt(r,0));
                       String biz_id_sel = (String)yelpJTable.getValueAt(r,0);
                           String  reviewQuery ="select u.USER_NAME, r.TEXT from yelpuser u,reviews r "
                        + "where u.user_id = r.user_id and r.BUSINESS_ID = '"+yelpJTable.getValueAt(r,0)+"'";
                     //here
                      String bizAndOrSel = bizAndOrComboBox.getSelectedItem().toString();
         
        
        
        try{
       
        String reviewFromJDateF ="";
               
                DateFormat reviewFromFormat = new SimpleDateFormat("YYYY-MM");
                 reviewFromJDateF = reviewFromFormat.format(reviewFromJDate.getDate());
              
              
                String reviewToJDateF ="";
                
               
                    DateFormat reviewToFormat = new SimpleDateFormat("YYYY-MM");
                     reviewToJDateF = reviewToFormat.format(reviewToJDate.getDate());
                
                
                
                String reviewStarSel = reviewStarComboBox.getSelectedItem().toString();
                String reviewStarInp = reviewStarVal.getText();
                
                String reviewVotesSel = reviewVotesComboBox.getSelectedItem().toString();
                String reviewVotesInp = reviewVotesVal.getText().toString();
              
                
                
                reviewFilterQuery.append("select u.user_name, r.text from reviews r, votes v, yelpuser u where r.user_id=u.user_id and r.business_id = "+"'"+yelpJTable.getValueAt(r,0)+"' " );
              if(!reviewFromJDateF.isEmpty()){
                  reviewFilterQuery.append(bizAndOrSel);
              reviewFilterQuery.append(" review_date >= To_date('"+reviewFromJDateF+"' ,'YYYY-MM')  ");
               
              }
              if(!reviewToJDateF.isEmpty()){
                  reviewFilterQuery.append(bizAndOrSel);
                   reviewFilterQuery.append(" review_date <= To_date('"+reviewToJDateF+"' ,'YYYY-MM') ");
                   
              }
               
               if(!reviewStarInp.isEmpty()){
                   reviewFilterQuery.append(bizAndOrSel);
                   reviewFilterQuery.append(" stars "+reviewStarSel+" "+reviewStarInp+" ");
                   
               }         
//               if(reviewVotesSel.matches("funny|cool|useful")){
                 if(!reviewVotesInp.isEmpty()){
                   reviewFilterQuery.append(bizAndOrSel);
                   reviewFilterQuery.append("  r.review_id=v.review_id group by  u.user_name,r.text having  sum(v.funny+v.useful+v.cool)"+reviewVotesSel+" "+reviewVotesInp);
                   
               }
                reviewQuery = reviewFilterQuery.toString();
               System.out.println(reviewQuery);

                     }
                     catch(NullPointerException e){
                         System.out.println("Null values due to optional fields");
                     }
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                //here
                            System.out.println(reviewFilterQuery.toString());
                     try {
                         PreparedStatement reviewps = connection.prepareStatement(reviewQuery);
                         ResultSet reviewrs =reviewps.executeQuery();
                         while(reviewrs.next()){
                             ResultSetMetaData rsmetadata = reviewrs.getMetaData();
                             
                            dtm.setColumnCount(rsmetadata.getColumnCount());
                            Vector<String> columnData = new Vector();
                            for(int i=1;i<=rsmetadata.getColumnCount();i++){
                                columnData.add(rsmetadata.getColumnName(i));
                            }
                            dtm.setColumnIdentifiers(columnData);
                
               
                            while(reviewrs.next()){
                                columnData = new Vector();
                                for(int i=1;i<=rsmetadata.getColumnCount();i++){
                                    columnData.add(reviewrs.getString(rsmetadata.getColumnName(i))); //changed

                                }
                                dtm.addRow(columnData);

                            }  
                             
                            reviewJTable.setModel(dtm);
                            reviewJTable.setFillsViewportHeight(true);
                             
                             
                         }
                         reviewJTable.setCellSelectionEnabled(true);
                        reviewJTable.setRowSelectionAllowed(true);
                       ListSelectionModel select= reviewJTable.getSelectionModel();  
                        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        reviewJTable.revalidate();
                       reviewJTable.repaint();
                     } catch (SQLException ex) {
                         Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
                     }
                            
                 }
                  
                            
                            
             }
         });
    }
    
    public void Reviewfilter(){
        
//        String bizAndOrSel = bizAndOrComboBox.getSelectedItem().toString();
//         
//        
//        
//        
//        
//        String reviewFromJDateF ="";
//               try{ 
//                DateFormat reviewFromFormat = new SimpleDateFormat("YYYY-MM");
//                 reviewFromJDateF = reviewFromFormat.format(reviewFromJDate.getDate());
//               }
//               catch(Exception e){
//                   e.printStackTrace();
//               }
//              
//                String reviewToJDateF ="";
//                try{
//               
//                    DateFormat reviewToFormat = new SimpleDateFormat("YYYY-MM");
//                     reviewToJDateF = reviewToFormat.format(reviewToJDate.getDate());
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//                
//                
//                String reviewStarSel = reviewStarComboBox.getSelectedItem().toString();
//                String reviewStarInp = reviewStarVal.getText();
//                
//                String reviewVotesSel = reviewVotesComboBox.getSelectedItem().toString();
//                String reviewVotesInp = reviewVotesVal.getText().toString();
//              StringBuilder reviewFilterQuery = new StringBuilder();
//                
//                
//                reviewFilterQuery.append("select r.user_name, r.text from reviews r, votes v where r.business_id = "+business_id );
//              if(!reviewFromJDateF.isEmpty()){
//                  reviewFilterQuery.append(bizAndOrSel);
//              reviewFilterQuery.append(" review_date >= To_date('"+reviewFromJDateF+"' ,'YYYY-MM')  ");
//               
//              }
//              if(!reviewToJDateF.isEmpty()){
//                  reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append(" review_date <= To_date('"+reviewToJDateF+"' ,'YYYY-MM') ");
//                   
//              }
//               
//               if(!reviewStarInp.isEmpty()){
//                   reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append(" star "+reviewStarSel+" "+reviewStarInp+" ");
//                   
//               }         
////               if(reviewVotesSel.matches("funny|cool|useful")){
//                 if(!reviewVotesInp.isEmpty()){
//                   reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append("  r.review_id=v.review_id group by  r.user_name,r.text having  sum(v.funny+v.useful+v.cool)"+reviewVotesSel+" "+reviewVotesInp);
//                   
//               }
//               String query = reviewFilterQuery.toString();
//               System.out.println(query);

               
               
               //old
               
               
//        DateFormat reviewFromFormat = new SimpleDateFormat("YYYY-MM");
//                String reviewFromJDateF = reviewFromFormat.format(reviewFromJDate.getDate());
//                
//                DateFormat reviewToFormat = new SimpleDateFormat("YYYY-MM");
//                String reviewToJDateF = reviewToFormat.format(reviewToJDate.getDate());
//                
//                String reviewStarSel = reviewStarComboBox.getSelectedItem().toString();
//                String reviewStarInp = reviewStarVal.getText();
//                
//                String reviewVotesSel = reviewVotesComboBox.getSelectedItem().toString();
//                String reviewVotesInp = reviewVotesVal.getText().toString();
//              StringBuilder reviewFilterQuery = new StringBuilder();
//              reviewFilterQuery.append("select r.user_name, r.text from reviews r, votes v where r.business_id = "+business_id );
//              if(!reviewFromJDateF.isEmpty()){
//                  reviewFilterQuery.append(bizAndOrSel);
//              reviewFilterQuery.append(" review_date >= To_date('"+reviewFromJDateF+"' ,'YYYY-MM')  ");
//               
//              }
//              if(!reviewToJDateF.isEmpty()){
//                  reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append(" review_date <= To_date('"+reviewToJDateF+"' ,'YYYY-MM') ");
//                   
//              }
//               
//               if(!reviewStarInp.isEmpty()){
//                   reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append(" star "+reviewStarSel+" "+reviewStarInp+" ");
//                   
//               }         
////               if(reviewVotesSel.matches("funny|cool|useful")){
//                 if(!reviewVotesInp.isEmpty()){
//                   reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append("  r.review_id=v.review_id and  votes('cool','funny','useful')"+reviewVotesSel+" "+reviewVotesInp);
//                   
//               }

//            reviewFilterQuery.append("select r.user_name, r.text from reviews r, votes v where r.business_id = "+business_id );
//              if(!reviewFromJDateF.isEmpty()){
//                  reviewFilterQuery.append(bizAndOrSel);
//              reviewFilterQuery.append(" review_date >= To_date('"+reviewFromJDateF+"' ,'YYYY-MM')  ");
//               
//              }
//              if(!reviewToJDateF.isEmpty()){
//                  reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append(" review_date <= To_date('"+reviewToJDateF+"' ,'YYYY-MM') ");
//                   
//              }
//               
//               if(!reviewStarInp.isEmpty()){
//                   reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append(" star "+reviewStarSel+" "+reviewStarInp+" ");
//                   
//               }         
////               if(reviewVotesSel.matches("funny|cool|useful")){
//                 if(!reviewVotesInp.isEmpty()){
//                   reviewFilterQuery.append(bizAndOrSel);
//                   reviewFilterQuery.append("  r.review_id=v.review_id group by  r.user_name,r.text having  sum(v.funny+v.useful+v.cool)"+reviewVotesSel+" "+reviewVotesInp);
//                   
//               }
//               String query = reviewFilterQuery.toString();
//               System.out.println(query);
    }
             
    
    private void executeQueryJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeQueryJButtonActionPerformed
        try {
           
            

            if(yelpSearchJTabbedPane.getSelectedIndex()==0){//user
                String reviewCountSel = reviewCountComboBox.getSelectedItem().toString();
                String reviewCountInp = reviewCountVal.getText();          
                String noOfFriendsSel = noOfFriendsComboBox.getSelectedItem().toString();
                String noOfFriendsInp = noOfFriendsVal.getText();
                String avgStarsSel =  avgStarsComboBox.getSelectedItem().toString();
                String avgStarsInp = avgStarsVal.getText();
                System.out.println("tab userr");
                String userAndOrSel = userAndOrComboBox.getSelectedItem().toString();
                
                DateFormat memberSinceFormat = new SimpleDateFormat("YYYY-MM");
                String memberSinceDateF = memberSinceFormat.format(memberSinceJDate.getDate());
                
                
                 String userQuery = "select user_id, user_name, to_char(yelping_since,'YYYY-MM')as yelping_since,average_stars from yelpuser "
                        + " where YELPING_SINCE >= To_date('"+memberSinceDateF+"' ,'YYYY-MM')  "+userAndOrSel
                        + " review_count "+reviewCountSel+" "+reviewCountInp
                        +" "+userAndOrSel+" average_stars "+avgStarsSel+" "+avgStarsInp+" "+userAndOrSel
                        +" user_id in (select u.user_id from friends f,yelpuser u where u.user_id = f.user_id "
                        + "group by u.user_id having count(f.friend_id) "+noOfFriendsSel+" "+noOfFriendsInp+" )";
//                        System.out.println(userQuery);

                displayQueryJTextArea.setText("");
                displayQueryJTextArea.setText(userQuery);
                PreparedStatement userstmt = connection.prepareStatement(userQuery);
                ResultSet userRes= userstmt.executeQuery();
                CreateTable(userRes);
                
                
                

//eventually final query goes here to display on the textarea

            }
            
            else if(yelpSearchJTabbedPane.getSelectedIndex()==1){
                
                

                //business
                
                String bizAndOrSel = bizAndOrComboBox.getSelectedItem().toString();
                
                
                
                System.out.println("tab bizz");
                
                
                
                
                
                
                //eventually somequery runs here, goes into the display text area
                 String bizQuery ="";
            Statement s = connection.createStatement();
//            String query = " ";
//            ResultSet r = s.executeQuery("select business_id,city,state,stars "
//                    + "from business "
//                    + "where rownum <10" );
            if(checkedcategories.size() >0){
                StringBuilder sb = new StringBuilder();
                String attrQuery ="";
//                String attrQuery = "";
                String catQuery = "";
//                String catQuery = "";
//                String andsubQuery = "";
                String subQuery = "";
                 for(String items : checkedcategories){     
                           sb.append("'"+items).append("',");
                                if(bizAndOrSel == "AND"){
                                    catQuery= " select distinct m.business_id from onlymaincategories m "
                                                + "where m.main_category_name in ("+sb.toString().replaceAll(",$","")+") "
                                                + "GROUP BY m.business_id HAVING COUNT(Distinct m.main_category_name) = "+checkedcategories.size();
                                }else if(bizAndOrSel == "OR"){
                                    catQuery= " select distinct m.business_id from onlymaincategories m "
                                                + "where m.main_category_name in ("+sb.toString().replaceAll(",$","")+") ";
                                }
                                 
                            }
                
                if(checkedsubcategories.size()>0){
                    sb = new StringBuilder();
                    for(String items : checkedsubcategories){
                        
                           sb.append("'"+items).append("',");
                           if(bizAndOrSel == "AND"){
                                subQuery= " select distinct m.business_id from onlysubcategories m "
                                            + "where m.sub_category_name in ("+sb.toString().replaceAll(",$","")+") "
                                            + "GROUP BY m.business_id HAVING COUNT(Distinct m.sub_category_name) = "+checkedsubcategories.size();
                               }if(bizAndOrSel == "OR"){
                                   subQuery= "select distinct m.business_id from onlysubcategories m "
                                            + "where m.sub_category_name in ("+sb.toString().replaceAll(",$","")+") ";
                               }
                               
                            
                            }
                            subQuery = " Intersect "+subQuery;
                    if(checkedattributes.size()>0){
                        sb = new StringBuilder();
                       for(String items : checkedattributes){     
                           sb.append("'"+items).append("',");
                                if(bizAndOrSel == "AND"){
                                    attrQuery = " select distinct m.business_id from attributes m "
                                            + "where m.attributes in ("+sb.toString().replaceAll(",$","")+") "
                                            + "GROUP BY m.business_id HAVING COUNT(Distinct m.attributes) = "+checkedattributes.size();
                                }else if(bizAndOrSel == "OR"){
                                   attrQuery = " select distinct m.business_id from attributes m "
                                            + "where m.attributes in ("+sb.toString().replaceAll(",$","")+") ";
                                }
                                
                            }
                       attrQuery = " Intersect "+attrQuery;
                          
                     }
                   
                }
                
               
//                bizQuery = catQuery + subQuery+ attrQuery ; 
                bizQuery = "select distinct  business_id,city,state,stars from business where business_id in ("+  catQuery + subQuery+ attrQuery+")"; 
//                System.out.println(attrQuery);
                Reviewfilter();
//                System.out.println("biz"+bizQuery);
                
            }
            
            
          displayQueryJTextArea.setText("");
                    displayQueryJTextArea.setText(bizQuery);
                PreparedStatement userstmt = connection.prepareStatement(bizQuery);
                ResultSet bizRes= userstmt.executeQuery();
                CreateTable(bizRes);
                
                                
                
                
//           Reviewfilter();
                
  }
            // UserSearch begins here
            //memberSinceJDate
//            showTableEntries();

//        Connection connection = pop.ConnectToDB();
//        String query ="";
//        ArrayList<String> checkedcategories = new ArrayList<>();
//        for(Component c: mainCategorylistJPanel.getComponents()){
//            if(c.getClass().equals(javax.swing.JCheckBox.class)){
//                JCheckBox jcb  = (JCheckBox) c;
//                if(jcb.isSelected()){
//                    checkedcategories.add(jcb.getText());
//                }
//            }
//        }



//       subCategoryListJPanel.setLayout(new BoxLayout(subCategoryListJPanel,BoxLayout.Y_AXIS));
//       String selectedCatChecks[] = {}; 
//       for(String a :categoryCheckArr ){
//           JCheckBox chk = new JCheckBox(a,true);
//            subCategoryListJPanel.add(chk);
//             chk.setVisible(true);
//            subCategoryListJPanel.revalidate();
//            subCategoryListJPanel.repaint();
//           System.out.println(a);
//       }

//        System.out.println(subCategoryListJPanel.getComponentCount());


//         displayQueryJTextArea.setText(checkedcategories.toString());


//                memberSinceJDate.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e){
//                memberSinceJDatePropertyChange(e);
//            }
//         });
        } catch (SQLException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        

    }//GEN-LAST:event_executeQueryJButtonActionPerformed

    private void foodCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodCheckActionPerformed
        // TODO add your handling code here:
//        if(foodCheck.isSelected()){
//            cat_selected = cat_selected + foodCheck.getText()+",";
//        }
    }//GEN-LAST:event_foodCheckActionPerformed

    private void artsAndECheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_artsAndECheckActionPerformed
        // TODO add your handling code here:
//        if(artsAndECheck.isSelected()){
//            cat_selected = cat_selected + artsAndECheck.getText()+",";
//        }
    }//GEN-LAST:event_artsAndECheckActionPerformed

    private void carRentalCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carRentalCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_carRentalCheckActionPerformed

    private void deptStoresCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptStoresCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deptStoresCheckActionPerformed

    private void activeLifeCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeLifeCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_activeLifeCheckActionPerformed

    private void homeNGardenCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeNGardenCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeNGardenCheckActionPerformed

    private void mainCategorylistJPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainCategorylistJPanelMousePressed
        // TODO add your handling code here:
//        System.out.print(this.mainCategorylistJPanel.getComponents());
    }//GEN-LAST:event_mainCategorylistJPanelMousePressed

    private void memberSinceJDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_memberSinceJDatePropertyChange
        // TODO add your handling code here:
        //
        //working code:
//        DateFormat memberSinceFormat = new SimpleDateFormat("YYYY-MM");
//          String memberSinceDateF=""; 
//          memberSinceDateF = memberSinceFormat.format(memberSinceJDate.getDate());
//        System.out.println(memberSinceDateF);
        //
        
        
//        Date memberSinceDate = new Date();
//        memberSinceJDate = (JDateChooser) evt.getSource();
//         memberSinceDate = memberSinceJDate.getDate();
//         memberSinceFormat.format(memberSinceDate);
//    memberSinceJDate.setDateFormatString("YYYY-MM");
//         System.out.println(memberSinceDate);
//         jTextField2.setText(memberSinceJDate.getDate().toString());
         
//        memberSinceDateF = memberSinceJDate.toString();
//        memberSinceJDate.setDateFormatString("YYYY-MM");
        
//        jTextField2.setText(memberSinceFormat.format(memberSinceJDate.getDate()));
       
       
    }//GEN-LAST:event_memberSinceJDatePropertyChange

    private void userAndOrComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userAndOrComboBoxActionPerformed
        // TODO add your handling code here:
//         System.out.println(userAndOrComboBox.getSelectedItem());
    }//GEN-LAST:event_userAndOrComboBoxActionPerformed

    private void noOfFriendsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noOfFriendsComboBoxActionPerformed
        // TODO add your handling code here:
//        System.out.println(noOfFriendsComboBox.getSelectedItem());
    }//GEN-LAST:event_noOfFriendsComboBoxActionPerformed

    private void avgStarsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avgStarsComboBoxActionPerformed
        // TODO add your handling code here:
//        System.out.println(avgStarsComboBox.getSelectedItem());
    }//GEN-LAST:event_avgStarsComboBoxActionPerformed

    private void reviewStarValActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewStarValActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reviewStarValActionPerformed

    private void reviewVotesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewVotesComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reviewVotesComboBoxActionPerformed

    public static void getSubCategoriesList(ActionEvent e){
//        System.out.println("Worked");
    }
    /**
     * @param args the command line arguments
     */
    
    
    
    private ResultSet loadCategories() throws SQLException{
//        Connection connection = pop.ConnectToDB();
//        Statement stmt = connection.createStatement();
//        String mainCatQuery = "SELECT * FROM MainCategories";
//        ResultSet res = stmt.executeQuery(mainCatQuery); 
//       if(res != null){
//        while(res.next()){
//             System.out.println("load(): "+res.getString(1));
//         }
//       }
        String mainCatQuery = "SELECT * FROM MainCategories";
        ResultSet res = stmt.executeQuery(mainCatQuery); 
//       if(rs != null){
//        while(rs.next()){
//             System.out.println("load(): "+rs.getString(1));
//         }
//        System.out.print("ended");
       
       
//       }
       



        return res;
    }
    
    
//    private void temp() throws SQLException{
////        Statement stmt = connection.createStatement();
////        String mainCatQuery = "SELECT * FROM MainCategories";
////        ResultSet rs = stmt.executeQuery(mainCatQuery); 
////       if(rs != null){
////        while(rs.next()){
////             System.out.println("load(): "+rs.getString(1));
////         }
////       
////       
////       }
//    }
    
    
    public static void main(String args[]) throws SQLException {
        
//        HW3 h = new HW3();
//        h.temp();
//        ArrayList<String> checkedcategories = new ArrayList<>();
//        
//            
//        
//        for(Component c: h.mainCategorylistJPanel.getComponents()){
//            if(c.getClass().equals(javax.swing.JCheckBox.class)){
//                JCheckBox jcb  = (JCheckBox) c;
//                if(jcb.isSelected()){
//                    checkedcategories.add(jcb.getText());
//                }
//            }
//        }
        

        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new HW3().setVisible(true);
                    
//                hw3.myGUITest();
                } catch (SQLException ex) {
                    Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox activeLifeCheck;
    private javax.swing.JCheckBox artsAndECheck;
    private javax.swing.JPanel attributeListJPanel;
    private javax.swing.JCheckBox automotiveCheck;
    private javax.swing.JComboBox<String> avgStarsComboBox;
    private javax.swing.JTextField avgStarsVal;
    private javax.swing.JCheckBox beautyNSpaCheck;
    private javax.swing.JComboBox<String> bizAndOrComboBox;
    private javax.swing.JPanel bizSearchJPanel;
    private javax.swing.JCheckBox cafesCheck;
    private javax.swing.JCheckBox carRentalCheck;
    private javax.swing.JCheckBox convStoresCheck;
    private javax.swing.JCheckBox dentistsCheck;
    private javax.swing.JCheckBox deptStoresCheck;
    private javax.swing.JTextArea displayQueryJTextArea;
    private javax.swing.JCheckBox doctorsCheck;
    private javax.swing.JCheckBox drugstoresCheck;
    private javax.swing.JCheckBox educationCheck;
    private javax.swing.JCheckBox eventPlanningNSCheck;
    private javax.swing.JButton executeQueryJButton;
    private javax.swing.JCheckBox flowersNGiftsCheck;
    private javax.swing.JCheckBox foodCheck;
    private javax.swing.JCheckBox groceriesCheck;
    private javax.swing.JCheckBox hardwareStoresCheck;
    private javax.swing.JCheckBox healthNMedicalCheck;
    private javax.swing.JCheckBox homeNGardenCheck;
    private javax.swing.JCheckBox homeServicesCheck;
    private javax.swing.JCheckBox hospitalsCheck;
    private javax.swing.JCheckBox hotelsNTravelCheck;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPanel mainCategorylistJPanel;
    private javax.swing.JCheckBox medicalCentersCheck;
    private com.toedter.calendar.JDateChooser memberSinceJDate;
    private javax.swing.JLabel memberSinceJLabel;
    private javax.swing.JCheckBox nightlifeCheck;
    private javax.swing.JComboBox<String> noOfFriendsComboBox;
    private javax.swing.JTextField noOfFriendsVal;
    private javax.swing.JCheckBox nursNgardeningCheck;
    private javax.swing.JCheckBox restaurantsCheck;
    private javax.swing.JComboBox<String> reviewCountComboBox;
    private javax.swing.JTextField reviewCountVal;
    private com.toedter.calendar.JDateChooser reviewFromJDate;
    private javax.swing.JTable reviewJTable;
    private javax.swing.JComboBox<String> reviewStarComboBox;
    private javax.swing.JTextField reviewStarVal;
    private com.toedter.calendar.JDateChooser reviewToJDate;
    private javax.swing.JComboBox<String> reviewVotesComboBox;
    private javax.swing.JTextField reviewVotesVal;
    private javax.swing.JPanel reviewfilterJPanel;
    private javax.swing.JPanel reviewtableJPanel;
    private javax.swing.JCheckBox shoppingCheck;
    private javax.swing.JPanel subCategoryListJPanel;
    private javax.swing.JCheckBox transportationCheck;
    private javax.swing.JComboBox<String> userAndOrComboBox;
    private javax.swing.JPanel userSearchJPanel;
    private javax.swing.JTable yelpJTable;
    private javax.swing.JTabbedPane yelpSearchJTabbedPane;
    private javax.swing.JPanel yelptableJPanel;
    // End of variables declaration//GEN-END:variables

   
}
