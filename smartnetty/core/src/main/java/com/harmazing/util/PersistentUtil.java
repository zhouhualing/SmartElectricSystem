package com.harmazing.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PersistentUtil {

	public static void persisUpdateResultData(List lst){
		File myObjectFile=new File("d:\\1.txt");
		ObjectOutputStream oos=null;
		try {
			oos = new ObjectOutputStream(
					new FileOutputStream(myObjectFile));
			oos.writeObject(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{		
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void persisInsertResultData(List lst){
		File myObjectFile=new File("d:\\2.txt");
		ObjectOutputStream oos=null;
		try {
			oos = new ObjectOutputStream(
					new FileOutputStream(myObjectFile));
			oos.writeObject(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{		
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
