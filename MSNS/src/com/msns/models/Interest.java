package com.msns.models;

public class Interest {
	public int Id;
	
	public String Key;
	
	@Override
	public boolean equals(Object object) {
		boolean areEqual = false;

        if (object != null && object instanceof Interest) {
            Interest other = (Interest) object;
            areEqual = this.Key.equals(other.Key);
        }

        return areEqual;
	}
}
