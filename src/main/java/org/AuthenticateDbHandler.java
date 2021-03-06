package org;

import java.security.Key;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import oracle.jdbc.OraclePreparedStatement;

public class AuthenticateDbHandler {

	private DatabaseConnectionPool dbpool;
	public AuthenticateDbHandler(){
		dbpool = DatabaseConnectionPool.getInstance();
	}
	
	/*
	 * WARNING.
	 * This method is used to create a secret key for authentication
	 * purposes. It ONLY returns the secret key as a String.
	 * This secret key has to then be stored in the database table
	 * 
	 */
	/*public String generateSecretKey(){
		// create new key
    	SecretKey secretKey = null;
    	try {
    	secretKey = KeyGenerator.getInstance("AES").generateKey();
    	} catch (NoSuchAlgorithmException e) {
    	e.printStackTrace();
    	}
    	
    	// get base64 encoded version of the key
    	String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    	return encodedKey;
	}*/
    
    public Employee login(String username, String password){
    	OraclePreparedStatement stmt = null;
    	Connection con = null;
    	try {
			con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"select "
            		+ "ID,"
            		+ "NAME,"
            		+ "COMPANIES_EMPLOYEE_ID,"
            		+ "COMPANIES_ID,"
            		+ "MANAGER,"
            		+ "SUPER_ADMIN, "
					+ "STATE, "
					+ "web_password, "
					+ "salt "
            		+ "from employees where COMPANIES_EMPLOYEE_ID = ?");

            stmt.setString(1, username);
            ResultSet i = stmt.executeQuery();
            
            if(i.next()){
	        	//parse result
	        	int id = Integer.parseInt(i.getString("ID"));
	        	String name = i.getString("name");
	        	String comp_emp_id = i.getString("COMPANIES_EMPLOYEE_ID");
	        	int comp_id = Integer.parseInt(i.getString("COMPANIES_ID"));
	        	boolean mang = ( Integer.parseInt(i.getString("MANAGER")) == 1 ) ? true : false;
	        	boolean super_ad = ( Integer.parseInt(i.getString("SUPER_ADMIN")) == 1 ) ? true : false;
				int state = Integer.parseInt(i.getString("STATE"));
				byte[] hashed_password = i.getBytes("web_password");
				byte[] salt = i.getBytes("salt");

	        	System.out.println("AuthenticateDbHandler -> login -> employee name from sql query: " + name);

	        	Employee emp = 
	        			new Employee(
				        			id, 
				            		name, 
				            		comp_emp_id, 
				            		comp_id, 
				            		mang,
				            		super_ad,
									state);

				if(!emp.validPassword(hashed_password, salt, password)){
					return  null;
				}

	        	return emp;
            }
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    	return null;
    }
    
    public WebTokens createJWT(Employee emp){
    	
    	try {
    		//generate random CSRF token
    		String xsrfToken = UUID.randomUUID().toString();
    		
    		String encodedKey = getSecretKey();
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
        	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
        	
        	// Default: 10 hours
        	int milliSecondOffset = (int) TimeUnit.MILLISECONDS.convert(10, TimeUnit.HOURS);
        	
	    	String compactJws = Jwts.builder()
	    			.claim(JsonVar.EMPLOYEE_ID, emp.getId())
	    			.claim(JsonVar.EMPLOYEE_NAME, emp.getName())
	    			.claim(JsonVar.EMPLOYEE_COMPANIES_ID, emp.getCompany_id())
	    			.claim(JsonVar.MANAGER, emp.isManager())
	    			.claim(JsonVar.SUPER_ADMIN, emp.isSuper_admin())
	    			.claim(JsonVar.XSRF_TOKEN, xsrfToken)
	    			.setIssuer("localhost")
	    			.setExpiration( new Date(System.currentTimeMillis() + milliSecondOffset))
	    			.setSubject("Employee")
	    			.signWith(SignatureAlgorithm.HS512, signingKey)
	    			.compact();
    	    //created
			System.out.println("LOG: Login->createJWT->jwt=\n"+compactJws);
	    	return new WebTokens(compactJws, xsrfToken);
    	} catch (Exception e) {

    	    //don't trust the JWT!
    		System.out.println("ERROR. Building JWS -> " + e.getMessage()); 
    	}
    	return null;
    }
    
    /*public boolean isAuth(WebTokens webTokens){
    	
    	try{
    		String compactJws = webTokens.getJsonWebToken();
	    	String encodedKey = getSecretKey();
	        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
	    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
	    	Jwts.parser().setSigningKey(signingKey).parseClaimsJws(compactJws);
	
		    System.out.println("Log: trusted:  " + compactJws);
		    //This line will throw an exception if it is not a signed JWS (as expected)
		    Claims claims = Jwts.parser()         
		       .setSigningKey(signingKey)
		       .parseClaimsJws(compactJws).getBody();
		   
		    String xsrfToken = (String) claims.get(JsonVar.XSRF_TOKEN);
		    if( !webTokens.getXsrfToken().equals(xsrfToken) ){
		    	throw new SignatureException("Invalid XSRF Token: the JWT and the HTTP request tokens do NOT match");
		    }
		    
		    return true;
    	} catch (SignatureException se) {
    	    //don't trust the JWT!
    		System.out.println("ERROR. Invalid signature on token -> " + se.getMessage()); 
    	} catch (ExpiredJwtException eje){
    		//don't trust the JWT!
    		System.out.println("ERROR. Expired token -> " + eje.getMessage());
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }*/
    

	private String getSecretKey() {
		OraclePreparedStatement stmt = null;
		Connection con = null;
		try {
			con = dbpool.getConnection();
            stmt = (OraclePreparedStatement) con.prepareStatement(
            		"select * from system");
            ResultSet i = stmt.executeQuery();
        	//parse result
            if(!i.next()){
            	return null;
            }
        	String state = i.getString("secret_key");
        	return state;
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{stmt.close();}catch(Exception ignore){}
			try{con.close();}catch(Exception ignore){}
        }
    	return null;
	}

	public Claims getClaims(WebTokens compactJws) {
		String jsonToken = compactJws.getJsonWebToken();
    	String encodedKey = getSecretKey();
        
    	try{
	    	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
	    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
	    	Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jsonToken);
	
		    System.out.println("Log: trusted:  " + jsonToken);
		    //This line will throw an exception if it is not a signed JWS (as expected)
		    Claims claims = Jwts.parser()         
		       .setSigningKey(signingKey)
		       .parseClaimsJws(jsonToken).getBody();
	
			String xsrfToken = (String) claims.get(JsonVar.XSRF_TOKEN);
		
			if( compactJws.getXsrfToken() == null || !compactJws.getXsrfToken().equals(xsrfToken) ){
				throw new SignatureException("Invalid XSRF Token: the JWT and the HTTP request tokens do NOT match");
			}

			return claims; //valid key
	    }catch (SignatureException se) {
    	    //don't trust the JWT!
    		System.out.println("ERROR. Invalid signature on token -> " + se.getMessage());
    	}catch (ExpiredJwtException eje){
    		//don't trust the JWT!
    		System.out.println("ERROR. Expired token -> " + eje.getMessage());
    	}catch(IllegalArgumentException iae){
    		//no JWT?
    		System.out.println("ERROR. Expired token -> " + iae.getMessage());
    	}catch(Exception e){
	    	e.printStackTrace();
	    }
	   
	    return null;
	}
	
	public Employee employeeFromJWT(WebTokens tokens){
		Claims claims = getClaims(tokens);
		if(claims == null){
			return null;
		}
		int emp_id = (Integer) claims.get(JsonVar.EMPLOYEE_ID);
		Employee emp = Employee.getEmployeeById(emp_id);
		
		return emp;

	}
	
	public int getInt(String key, WebTokens compactJws) {
		String jsonToken = compactJws.getJsonWebToken();
    	String encodedKey = getSecretKey();
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
    	Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jsonToken);

	    System.out.println("Log: trusted:  " + jsonToken);
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(signingKey)
	       .parseClaimsJws(jsonToken).getBody();

		String xsrfToken = (String) claims.get(JsonVar.XSRF_TOKEN);
	    
	    int result = -1;
	    try{
	    	if( !compactJws.getXsrfToken().equals(xsrfToken) ){
				throw new SignatureException("Invalid XSRF Token: the JWT and the HTTP request tokens do NOT match");
			}
	    	result = (Integer) claims.get(key);
	    }catch (SignatureException se) {
    	    //don't trust the JWT!
    		System.out.println("ERROR. Invalid signature on token -> " + se.getMessage());
    	} catch (ExpiredJwtException eje){
    		//don't trust the JWT!
    		System.out.println("ERROR. Expired token -> " + eje.getMessage());
    	} catch(Exception e){
	    	e.printStackTrace();
	    }
	   
	    return result;
	}

//	public boolean isSuperAdmin(WebTokens webTokens) {
//		try{
//    		String compactJws = webTokens.getJsonWebToken();
//	    	String encodedKey = getSecretKey();
//	        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
//	    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
//	    	Jwts.parser().setSigningKey(signingKey).parseClaimsJws(compactJws);
//
//		    System.out.println("Log: trusted:  " + compactJws);
//		    //This line will throw an exception if it is not a signed JWS (as expected)
//		    Claims claims = Jwts.parser()
//		       .setSigningKey(signingKey)
//		       .parseClaimsJws(compactJws).getBody();
//
//		    String xsrfToken = (String) claims.get(JsonVar.XSRF_TOKEN);
//		    if( !webTokens.getXsrfToken().equals(xsrfToken) ){
//		    	throw new SignatureException("Invalid XSRF Token: the JWT and the HTTP request tokens do NOT match");
//		    }
//
//		    //CHECK IF SUPER ADMIN HERE
//		    boolean isSuperAdmin = (Boolean) claims.get(JsonVar.SUPER_ADMIN);
//		    if( !isSuperAdmin ){
//		    	throw new SignatureException("Not super admin");
//		    }
//
//		    return true;
//    	} catch (SignatureException se) {
//    	    //don't trust the JWT!
//    		System.out.println("ERROR. Invalid signature on token -> " + se.getMessage());
//    	} catch (ExpiredJwtException eje){
//    		//don't trust the JWT!
//    		System.out.println("ERROR. Expired token -> " + eje.getMessage());
//    	} catch (Exception e){
//    		e.printStackTrace();
//    	}
//    	return false;
//	}

}