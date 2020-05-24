//SJSU CS 218 Fall 2019 TEAM7
public class BucketList {

	private long size;
	private String key;
	
	public BucketList(String key,Long size)
	{
		this.key = key;
		this.size = size;
		
	}
	
	public BucketList() {
		// TODO Auto-generated constructor stub
	}

	public long getSize()
	{
		return size;	
	}
	public String getKey()
	{
		return key;
	}
	

	
	public void setSize(long size) {
		this.size = size;
	}
	public void setKey(String key) 
	{
		this.key = key;
	}

}
