package com.harmazing.spms.base.initData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.common.collect.Lists;

public class InitDataUtil2 {
	private static final String URL = "jdbc:mysql://123.57.189.111:3306/spms_vir_device2?useUnicode=true&characterEncoding=utf-8";
    private static final String DEVICE_CLASS = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final GenerateSN genSN= new GenerateSN();
    private static final GenerateMac genMAC= new GenerateMac();
    private static List<String> sns = Lists.newArrayList();
    private static List<String> macs = Lists.newArrayList();

    private static final String USER_PASSWORD = "123456";
    private static String PHONE_PREFIX = "1380000";
    private static String EMAIL_SUFFIX = "@163.com";
//    private static volatile int index = 1;
    
    public  static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(){
        public Connection initialValue(){
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return conn;
        }
    };

    static {
        try {
            Class.forName(DEVICE_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**插入仓库**/
    public static void insertstorage(){
    	Connection conn = connectionHolder.get();
    	String[] storages = new String[]{"1","2","3","4"};
    	String sql = "INSERT INTO dict_device_storage(code,value,iorder )" +
                " values(?,?,?)";

    	PreparedStatement pstam = null;
    	try {
    		conn.setAutoCommit(false);
    		pstam = conn.prepareStatement(sql);
			
			    pstam.setString(1,"1");
			    pstam.setString(2,"望京1号仓库");
			    pstam.setString(3,"1");
			    pstam.addBatch();
			    
			    pstam.setString(1,"2");
			    pstam.setString(2,"学院路1号仓库");
			    pstam.setString(3,"2");
			    pstam.addBatch();
			    
			    pstam.setString(1,"3");
			    pstam.setString(2,"亦庄1号仓库");
			    pstam.setString(3,"3");
			    pstam.addBatch();
			    
			    pstam.setString(1,"5");
			    pstam.setString(2,"已出库");
			    pstam.setString(3,"5");
			    pstam.addBatch();
			    
			pstam.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pstam.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    /**
     * 批量插入10000个网关
     * @throws SQLException
     */
    private static void initDeviceByGW() throws SQLException{
    	Connection conn = connectionHolder.get();
    	String[] storages = new String[]{"1","2","3","5"};
    	String sql = "INSERT INTO spms_device(id,version,cursoft,disabel," +
    			"hardware,mac,model,sn,status,storage,type,vender,software)" +
                " values(?,1,1.0,0,'1.0',?,'Q',?,1,?,1,'极联','test')";

    	PreparedStatement pstam = null;
    	try {
    		conn.setAutoCommit(false);
    		pstam = conn.prepareStatement(sql);
			for(int index=1;index<10001;index++) {
				String gwmac=GenerateData.getGwMac();
				String gwsn=GenerateData.getGwSN();
				System.out.println(gwmac+"---"+gwsn);
			    pstam.setString(1,"ZJ"+index+"ZJ");
			    pstam.setString(2,gwmac);
			    pstam.setString(3,gwsn);
			    pstam.setString(4,storages[index<=4000?0:index<=7000?1:2]);
			    pstam.addBatch();
			    if(index%1000==0){
			    	pstam.executeBatch();
			    	//conn.commit();
			    }
			   
			}
			//pstam.executeBatch();
			//conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.commit();
				pstam.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    /**
     *  批量插入10000个空调
     * @throws SQLException
     */
    private static void initDeviceByAC(){
    	Connection conn = connectionHolder.get();
    	String[] storages = new String[]{"1","2","3","5"};
    	String sql = "INSERT INTO spms_device(id,version,cursoft,disabel," +
    			"hardware,mac,model,sn,status,storage,type,vender,software)" +
                " VALUES(?,1,1.0,0,'1.0',?,'Q',?,1,?,2,'极联','test')";
    
    	PreparedStatement pstam =null;
    	try {
    		conn.setAutoCommit(false);
    		pstam = conn.prepareStatement(sql);
			for(int index=10001;index<20001;index++) {
				String acmac=GenerateData.getACMac();
				String acsn=GenerateData.getACSN();
				System.out.println(acmac+"---"+acsn);
			    pstam.setString(1,"ZJ"+index+"ZJ");
			    pstam.setString(2,acmac);
			    pstam.setString(3,acsn);
			    pstam.setString(4,storages[index<=3000?0:index<=7000?1:2]);
			    pstam.addBatch();
			    if(index%1000==0){
			    	pstam.executeBatch();
			    	//conn.commit();
			    }
			}
			pstam.executeBatch();
	    	//conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pstam.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    /**
     * 
     *  批量插入10000个门窗
     * @throws SQLException
    
    private static void initDeviceByWINDOW(){
    	Connection conn = connectionHolder.get();
    	String[] storages = new String[]{"1","2","3","4"};
    	String sql = "INSERT INTO spms_device(id,version,cursoft,disabel," +
    			"hardware,mac,model,sn,status,storage,type,vender,software)" +
                " VALUES(?,1,1.0,0,'1.0',?,'Q',?,1,?,3,'极联','test')";
    	
    	PreparedStatement pstam = null;
    	try {
    		conn.setAutoCommit(false);
			pstam = conn.prepareStatement(sql);
			for(index=20001;index<30001;index++) {
				String wdmac=GenerateData.getWD();
				String wdsn="SN"+wdmac;
			    pstam.setString(1,index+"");
			    pstam.setString(2,wdmac);
			    pstam.setString(3,wdsn);
			    pstam.setString(4,storages[index<=22000?0:index<=25000?1:index<=27500?2:3]);
			    pstam.addBatch();
			    if(index%500==0){
			    	pstam.executeBatch();
			    	conn.commit();
			    }
			}
			pstam.executeBatch();
	    	conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pstam.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
     */
    /**
     * 初始化用户数据
     * @throws SQLException 
     */
    public static void initUser() throws SQLException{
    	List<String> names = Lists.newArrayList();
        List<String> phones = Lists.newArrayList();
        List<String> emails = Lists.newArrayList();
        List<String> ammeters = Lists.newArrayList();
        List<String> idcards = Lists.newArrayList();
        String[] bizs = new String[]{"3","4","5","6","7","8","9","10"};
        String[] eles = new String[]{"12","13","14","15","16","17","18","19"};
        String[] adds = new String[]{"海淀区","西城区","朝阳区","丰台区","东城区","房山区","门头沟区","石景山区"};
        /*批量生成订户手机号，订户名，电表号*/
        for (int i = 1; i < 10001; i++) {
            String username = GenerateUsername.generateName();
            while(names.contains(username)){
                username = GenerateData.getName();
            }
            names.add(i-1,username);
            
            String idCard = GenerateData.getIDCard();
            while(idcards.contains(idCard)){
            	idCard = GenerateData.getIDCard();
            }
            idcards.add(i-1,idCard);

            String am = genSN.getNum("AM-");
            while(sns.contains(am)){
                am = genSN.getNum("AM-");
            }
            ammeters.add(i-1,am);
            String phone=GenerateData.getPhoneNumber()+"";
            phones.add(i-1,phone);
            emails.add(i-1,phone+EMAIL_SUFFIX);
        }
        
        Connection conn = connectionHolder.get();
        conn.setAutoCommit(false);
       
        /*status0表未删除,userType为2表订户*/
        /*****************/
        String sysUserSql = "INSERT INTO tb_user_user(id,version,email,mobile_phone," +
        		"password,phone_Number,status,userCode,userName,userType) " +
        		"VALUES(?,1,?,?,'123456',?,0,?,?,2)";
        //conn.setAutoCommit(false);
        PreparedStatement sysPs = conn.prepareStatement(sysUserSql);
        for(int i =1;i<10001; i++){
        	sysPs.setString(1, "ZJ"+i+"ZJ");
        	sysPs.setString(2, emails.get(i-1));
        	sysPs.setString(3, phones.get(i-1));
        	sysPs.setString(4, phones.get(i-1));
        	sysPs.setString(5, phones.get(i-1));
        	sysPs.setString(6, names.get(i-1));
        	sysPs.addBatch();
        	if(i%1000==0){
        		sysPs.executeBatch();
		    	//conn.commit();
		    }
        }
        conn.commit();
        sysPs.close();
        /************************/
        //订户试用为1
        String spmsUserSql = "INSERT INTO spms_user(id,version,address,ammeter,email,fullname," +
        		"mobile,type,biz_area_id,ele_area_id,gw_id,sys_user_id,idNumber) VALUES(?,1,?,?,?,?," +
        		"?,1,?,?,?,?,?)";
    	PreparedStatement spmsPs = conn.prepareStatement(spmsUserSql);
        for(int i =1;i<10001; i++){
        	spmsPs.setString(1,"ZJ"+i+"ZJ");
        	/*此处的判断条件换成i%8==1,i%8==2....这样的条件更好*/
        	spmsPs.setString(2, "北京市" + (i<=1000?adds[0]:i<=2000?adds[1]:i<=3000?adds[2]:i<=4000?adds[3]:i<=5000?adds[4]:i<=6000?adds[5]:i<=7000?adds[6]:adds[7]));
        	spmsPs.setString(3, ammeters.get(i-1));
        	spmsPs.setString(4, emails.get(i-1));
        	spmsPs.setString(5, names.get(i-1));
        	spmsPs.setString(6, phones.get(i-1));
        	spmsPs.setString(7, i<=3000?bizs[0]:i<=8000?bizs[1]:i<=20000?bizs[2]:i<=30000?bizs[3]:i<=40000?bizs[4]:i<=50000?bizs[5]:i<=60000?bizs[6]:bizs[7]);
        	spmsPs.setString(8, i<=3000?eles[0]:i<=8000?eles[1]:i<=20000?eles[2]:i<=30000?eles[3]:i<=40000?eles[4]:i<=50000?eles[5]:i<=60000?eles[6]:eles[7]);
	    	/*因为网关是按照i=1累加生成的，所以此处可以直接遍历*/
        	spmsPs.setString(9, "ZJ"+i+"ZJ");
        	spmsPs.setString(10,"ZJ"+i+"ZJ");
        	spmsPs.setString(11, idcards.get(i-1));
        	spmsPs.addBatch();
        	if(i%1000==0){
        		spmsPs.executeBatch();
		    	//conn.commit();
		    }
        }
//        sysPs.executeBatch();
        conn.commit();
        spmsPs.close();
       
    }
    /**
     * 生成手机号
     * @param num
     * @return
     */
    private static String genPhone(int num){
        String result = null;
        int val = num/10;
        if(val == 0){
            result = PHONE_PREFIX + "000" + num;
        }else if(val > 0 && val <10){
            result = PHONE_PREFIX + "00" + num;
        }else if(val >= 10 && val <100){
            result = PHONE_PREFIX + "0" + num;
        }else if(val >= 100 && val < 1000){
            result = PHONE_PREFIX + num;
        }
        return result;
    }
    /**
     * 初始化产品
     * @throws SQLException
     */
    private static void initProduct() throws SQLException{
    	java.util.Date date = new java.util.Date();
    	Object[] productType1 = new Object[]{};
    	Object[] productType2 = new Object[]{};
    	/*status代表产品的激活状态，1激活，0未激活*/
    	String sql = "INSERT INTO spms_product(id,version,activateDate,electricityCostRmb," +
    			"initialCostRmb,status,subscribeDate,type_id,user_id) " +
    			"VALUES(?,0,?,0," +
    			"0,1,?,?,?)";
    	Connection conn = connectionHolder.get();
    	conn.setAutoCommit(false);
    	//conn.setAutoCommit(false);
    	PreparedStatement ps  = conn.prepareStatement(sql);
    	for(int i = 1; i <10001;i++){	    	 
	    	ps.setString(1, "ZJ"+i+"ZJ");
	    	ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
	    	ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
	    	ps.setString(4, i<=50000?"1":"2");
	    	ps.setString(5, "ZJ"+i+"ZJ");
	    	ps.addBatch();
	    	if(i%1000==0){
	    		ps.executeBatch();
		    	conn.commit();
		    }
    	}
//    	ps.executeBatch();
    	conn.commit();
    	ps.close();
    	
    }
    /**
     * 初始化设备产品绑定关系
     * @param args
     * @throws SQLException 
     */
    public static void initProductBinding() throws SQLException{
    	Connection conn = connectionHolder.get();
    	conn.setAutoCommit(false);
    	//conn.setAutoCommit(false);
    	String sql = "INSERT INTO spms_user_product_binding(id,version,device_type,displayHomepage," +
    			"gwId,device_id,product_id,producttype_id,user_id,deviceType) VALUES(?,0,2,1," +
    			"?,?,?,?,?,2)";
    	PreparedStatement ps = ps = conn.prepareStatement(sql);
    	
    	for(int i = 1;i<10001;i++){
    		 ps.setString(1, "ZJ"+i+"ZJ");
    		 ps.setString(2, "ZJ"+i+"ZJ");
    		 ps.setString(3, "ZJ"+(10000+i)+"ZJ");
    		 ps.setString(4, "ZJ"+i+"ZJ");
    		 ps.setString(5, i<=5000?"1":"2");
    		 ps.setString(6, "ZJ"+i+"ZJ");
    		 ps.addBatch();
    		 if(i%1000==0){
 	    		ps.executeBatch();
 		    	//conn.commit();
 		    }
    	}
//    	ps.executeBatch();
	    conn.commit();
	    ps.close();
    }
    /**
     * 导出网关
     * @throws SQLException
     * @throws IOException
     */
    public static void exportGW() throws SQLException, IOException {
    	int n=10;
    	 Connection conn = connectionHolder.get();
    	for(int i=0;i<n;i++){
    		File f=new File("D:/testerdata/111/"+(i+1));
    		f.mkdirs();
    		
	        FileOutputStream fos = new FileOutputStream("D:/testerdata/111/"+(i+1)+"/data.txt");	       
	
	        //String sql = "select DISTINCT sd.id,sd.sn,sd.mac from spms_user_rule_binding surb,spms_device sd,spms_user su where surb.gw_id = sd.id and su.gw_id = sd.id and surb.gw_id not in ('2001','5a77a8c629f446ad870f32ac5736f67e','546d584d39714ec998421390a26a3689','42ca79e46801438bbc84b034c36356b5','8146d0612e934232a537242be63e553a','10000') order by cast(sd.id as signed)";
	        String sql = "select distinct d.sn sn,d.mac mac from spms_user_product_binding p,spms_device d where  p.gwId=d.id and d.type=1 limit "+(i*1000)+",1000";
	        //String sql = "select sn,mac from spms_device where type=1 and status=2";
	        System.out.println(sql);
	        Statement statm = conn.createStatement();
	        ResultSet rs = statm.executeQuery(sql);
	
	        while (rs.next()){
	            String sn = rs.getString("sn");
	            String mac = rs.getString("mac");
	            fos.write((sn + "\t" + mac + System.getProperty("line.separator")).getBytes());
	        }
	
	        fos.flush();
	        fos.close();
	        rs.close();
	        statm.close();
	       
    	}
//    	 conn.close();
    }
    /**
     * 导出空调信息
     * @throws SQLException
     * @throws IOException
     */
    public static void exportAC() throws SQLException, IOException {

        FileOutputStream fos = new FileOutputStream("D:/testerdata/111/data_ac.txt");

        Connection conn = connectionHolder.get();
        
        String sql = "select distinct d.mac from spms_user_product_binding p,spms_device d where  p.device_id=d.id and d.type=2";

        Statement statm = conn.createStatement();
        ResultSet rs = statm.executeQuery(sql);

        while (rs.next()){
            String mac = rs.getString("mac");
            fos.write((mac + System.getProperty("line.separator")).getBytes());
        }

        fos.flush();
        fos.close();
        rs.close();
        statm.close();
       // conn.close();
    }
    
    
    
    public static void main(String[] args) throws SQLException, IOException {
    	/***************************/
//    	insertstorage();
    	long t1 = System.currentTimeMillis();
    	initDeviceByGW();
    	System.out.println("initGW--------:");
    	initDeviceByAC();
    	System.out.println("initAC--------");
//    	initDeviceByWINDOW();
//    	long t4 = System.currentTimeMillis();
//    	System.out.println("initWINDOW--------"+(t4-t1)/1000+"s");
    	initUser();
    	System.out.println("initUser-----------");
    	initProduct();
    	System.out.println("initProduct-----------");
    	initProductBinding();
    	System.out.println("initProductBinding-----------");
    	/**************************************/
    	
//    	exportGW();
//    	exportAC();
    	 Connection conn = connectionHolder.get();
    	 conn.close();
    	
    	
	}
    
    
}
