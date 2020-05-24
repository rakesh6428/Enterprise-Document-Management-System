//SJSU CS 218 Fall 2019 TEAM7

package dmsMaven;

public class User {

	
	private String username;
	private String role;
	public User()
	{
		
	}
	public User(String username,String role)
	{
		this.username = username;
		this.role = role;
		
	}
	
	public String getUserName()
	{
		return username;	
	}
	public String getRole()
	{
		return role;
	}
	

	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setKey(String role) 
	{
		this.role = role;
	}

}
