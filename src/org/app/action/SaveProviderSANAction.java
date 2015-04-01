package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SanStorageRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SaveProviderSANAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2538984043591867692L;
	
	/** The SAN's id. */
    private long id;
    
    /** The SAN's name. */
    private String name;
    
    /** The SAN's capacity. */
    private double capacity;
    
    /** The SAN's bandwidth. */
    private double bandwidth;
    
    /** The SAN's latency. */
    private double networkLatency;
    
    private int statusCode;

	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int providerId = (int) session.get("providerId");
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, providerId);
			
			List<SanStorageRegistry> listOfSAN = dataReg.getSanList();
			
			for(SanStorageRegistry i:listOfSAN){
				if(i.getId() == this.id){
					i.setBandwidth(bandwidth);
					i.setCapacity(capacity);
					i.setName(name);
					i.setNetworkLatency(networkLatency);
					this.statusCode = 1;
				}
			}
			
			simRegDAO.updateSimulationRegistry(simReg);
		}
    	catch(Exception ex){
    		this.statusCode = 0;
    	}
		
    	return "success";
    }

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public void setNetworkLatency(double networkLatency) {
		this.networkLatency = networkLatency;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
}
