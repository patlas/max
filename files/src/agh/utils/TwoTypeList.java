package agh.utils;

import java.util.ArrayList;


public class TwoTypeList<AType, BType> {

	private ArrayList<AType> A = new ArrayList<AType>();
	private ArrayList<BType> B = new ArrayList<BType>();
	
	public int size;
	
	public TwoTypeList()
	{
		size = 0;
	}
	
	public Boolean addItem(AType i1, BType i2)
	{		
		A.add(i1);
		B.add(i2);
		size++;
		return true;
	}
	
	public AType getAitem(int index)
	{
		return A.get(index);
	}
	
	public BType getBitem(int index)
	{
		return B.get(index);
	}
	
	
	
}
