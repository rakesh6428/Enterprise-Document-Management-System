//SJSU CS 218 Fall 2019 TEAM7


package dmsMaven;

public class BucketList {

	private long size;
	private String key;
	public BucketList()
	{
		
	}
	public BucketList(String key,Long size)
	{
		this.key = key;
		this.size = size;
		
	}
	
	public long getSize()
	{
		return size;	
	}
	public String getKey()
	{
		return key;
	}
	

	
	public void setSiz3(long size) {
		this.size = size;
	}
	public void setKey(String key) 
	{
		this.key = key;
	}

}
