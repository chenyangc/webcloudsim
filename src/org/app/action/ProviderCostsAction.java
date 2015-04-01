package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ProviderCostsAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6393376220951619594L;
	
	/** The cost by second of processing. */
    private double costPerSec;
    
    /** The cost by RAM usage. */
    private double costPerMem;
    
    /** The cost by storage usage. */
    private double costPerStorage;
    
    /** The cost by bandwidth usage. */
    private double costPerBw;

	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int id = (int)session.get("providerId");
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, id);
			
			this.costPerBw = dataReg.getCostPerBw();
			this.costPerMem = dataReg.getCostPerMem();
			this.costPerSec = dataReg.getCostPerSec();
			this.costPerStorage = dataReg.getCostPerStorage();
			
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

	public double getCostPerSec() {
		return costPerSec;
	}

	public double getCostPerMem() {
		return costPerMem;
	}

	public double getCostPerStorage() {
		return costPerStorage;
	}

	public double getCostPerBw() {
		return costPerBw;
	}
}
