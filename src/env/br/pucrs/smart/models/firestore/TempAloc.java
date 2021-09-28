package br.pucrs.smart.models.firestore;

import java.util.ArrayList;
import java.util.List;

public class TempAloc {
	
	private boolean validated;
	private List<Allocation> allocation;
	private long saveAt;
	private String id;
	
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	public List<Allocation> getAllocation() {
		return allocation;
	}
	public void setAllocation(List<Allocation> allocation) {
		this.allocation = allocation;
	}
	public void addAllocation(Allocation allocation) {
		if (this.allocation != null) {
			this.allocation.add(allocation);
		} else {
			this.allocation = new ArrayList<Allocation>();
			this.allocation.add(allocation);
		}
	}
	public long getSaveAt() {
		return saveAt;
	}
	public void setSaveAt(Integer saveAt) {
		this.saveAt = saveAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
	    if (id != null) {
        	sb.append(" id : ");
        	sb.append(id);
        	sb.append(", ");
        }
	    sb.append(" validated : ");
        sb.append(validated);
        sb.append(", ");
        if (allocation != null) {
	        sb.append(" allocation : [");
	        for (Allocation a : allocation) {
	        	sb.append(" { ");
		        if (a.getIdPaciente() != null) {
		        	sb.append(" idPaciente : ");
		        	sb.append(a.getIdPaciente());
		        	sb.append(", ");
		        }
		        if (a.getLeito() != null) {
		        	sb.append(" leito : ");
		        	sb.append(a.getLeito());
		        }
		        sb.append(" }, ");
	        }
	        sb.append("] ");
	    }
        	sb.append(", ");
        	sb.append(" saveAt : ");
        	sb.append(saveAt);
	    sb.append("} ");
		return sb.toString();
	}
	
	
}
