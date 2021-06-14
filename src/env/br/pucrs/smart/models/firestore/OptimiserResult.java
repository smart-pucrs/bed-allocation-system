package br.pucrs.smart.models.firestore;

import java.util.ArrayList;
import java.util.List;

public class OptimiserResult {
	
	private boolean allAllocated;
	private List<Allocation> sugestedAllocation; // use only idPaciente and leito
	private List<String> notAllocated; // List with idPaciente
	public boolean isAllAllocated() {
		return allAllocated;
	}
	public void setAllAllocated(boolean allAllocated) {
		this.allAllocated = allAllocated;
	}
	public List<Allocation> getSugestedAllocation() {
		return sugestedAllocation;
	}
	public void setSugestedAllocation(List<Allocation> sugestedAllocation) {
		this.sugestedAllocation = sugestedAllocation;
	}
	public void addSugestedAllocation(Allocation allocation) {
		if (this.sugestedAllocation != null) {
			this.sugestedAllocation.add(allocation);
		} else {
			this.sugestedAllocation = new ArrayList<Allocation>();
			this.sugestedAllocation.add(allocation);
		}
	}
	public List<String> getNotAllocated() {
		return notAllocated;
	}
	public void setNotAllocated(List<String> notAllocated) {
		this.notAllocated = notAllocated;
	}
	public void addNotAllocated(String idPaciente) {
		if (this.notAllocated != null) {
			this.notAllocated.add(idPaciente);
		} else {
			this.notAllocated = new ArrayList<String>();
			this.notAllocated.add(idPaciente);
		}
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
	    sb.append(" allAllocated : ");
        sb.append(allAllocated);
        if (sugestedAllocation != null) {
            sb.append(", ");
	        sb.append(" sugestedAllocation : [");
	        for (Allocation a : sugestedAllocation) {
		        sb.append(a.toString());
	        }
	        sb.append("] ");
	    }
        if (notAllocated != null) {
            sb.append(", ");
	        sb.append(" notAllocated : [");
	        for (String s : notAllocated) {
		        sb.append(s);
		        sb.append(", ");
	        }
	        sb.append("] ");
	    }
	    sb.append("} ");
		return sb.toString();
	}

}
