package com.order.Package.model;

import java.util.Comparator;

public class StatusComparator implements Comparator<Package> {

	@Override
	public int compare(Package o1, Package o2) {
		
		return Integer.compare(getAssignedValue(o1.getStatus()), getAssignedValue(o2.getStatus()));
	}

	
	int getAssignedValue(Status status) {
        switch (status) {
            case IN_STORE:
                return 0;
            case IN_TRANSIT:
                return 1;
            case DELIVERED:
                return 2;
            default:
                return Integer.MAX_VALUE;
        }
    }

	
	

}
