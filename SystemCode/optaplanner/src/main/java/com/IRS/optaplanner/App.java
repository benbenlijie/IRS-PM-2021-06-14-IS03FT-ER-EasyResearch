package com.IRS.optaplanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.hibernate.internal.build.AllowSysOut;
import org.ini4j.OptionMap;
import org.ini4j.Wini;

public class App 
{
	
	public static void disableWarning() {
	    System.err.close();
	    System.setErr(System.out);
	}
	
    public static void main(String[] args)
    {
    	disableWarning();
        if(args.length > 0) {
        	try {
        		String abPath = new File("").getAbsolutePath();
        		String fullFilePath = args[0];
        		
        		String userinfo = readTxt(fullFilePath);

        		User user = createUser(userinfo);
        		
        		//Optimization solution
        		Opta opta = new Opta();
        		
        		opta.recommend(user, args[0]);
        		
				
				
        	}catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    public static User createUser(String userinfo) {
    	String[] tokens = userinfo.split("\n");
		User user = new User();
		int ii = 0;
		while(ii < tokens.length) {
    		if(tokens[ii].equalsIgnoreCase("author")) {
    			user.setRequiredAuthors(tokens[ii+1]);
    		}
    		else if(tokens[ii].equalsIgnoreCase("source_preference")) {
    			user.setRequiredSource(tokens[ii+1]);
    		}
    		else if(tokens[ii].equalsIgnoreCase("category_preference")) {
    			user.setRequiredCategory(tokens[ii+1]);
    		}
    		else if(tokens[ii].equalsIgnoreCase("date_preference")) {
    			user.setRequiredDate(tokens[ii+1]);
    		}
    		else if(tokens[ii].equalsIgnoreCase("keywords")) {
    			user.setRequiredTitle(tokens[ii+1]);
    		}
    		ii++;
		}
		return user;
    	
    }
    
    public static String readTxt(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 
                StringBuffer sb = new StringBuffer();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    sb.append(text+"\n");
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
}
