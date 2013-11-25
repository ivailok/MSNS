package com.msns.navdrawer;

public interface NavDrawerItem {
	public int getId();
    public String getLabel();
    public int getType();
    public boolean isEnabled();
    public boolean updateActionBarTitle();
}
