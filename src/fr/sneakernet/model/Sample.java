package fr.sneakernet.model;

public class Sample
{
	private String mName;
	private int mCount;
	private boolean isFlagged;
	
	public Sample (String name, int count, boolean isFlagged)
	{
		this.mName = name;
		this.mCount = count;
		this.isFlagged = isFlagged;
	}

	public String getName() {
		return mName;
	}

	public int getCount() {
		return mCount;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}
}
