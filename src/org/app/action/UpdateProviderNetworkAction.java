package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.NetworkMapEntry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateProviderNetworkAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300060928785205733L;
	private int statusCode;
	/** The id of the entry. */
    private long id;
    
    /** The entry's source. */
    private String source;
    
    /** The entry's destination. */
    private String destination;
    
    /** The entry's bandwidth. */
    private double bandwidth;
    
    /** The entry's latency. */
    private double latency;
    
	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			List<NetworkMapEntry> listOfNetwork = simReg.getNetworkMapEntrys();
			
			for(NetworkMapEntry i:listOfNetwork){
				if(i.getId() == this.id){
					i.setBandwidth(bandwidth);
					i.setDestination(destination);
					i.setLatency(latency);
					i.setSource(source);
					
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

	public int getStatusCode() {
		return statusCode;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public void setLatency(double latency) {
		this.latency = latency;
	}
}
