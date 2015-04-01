package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateCostsAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3726228446302403168L;
	
	private int statusCode;
	
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
			
			int providerId = (int) session.get("providerId");
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, providerId);
			
			dataReg.setCostPerBw(costPerBw);
			dataReg.setCostPerMem(costPerMem);
			dataReg.setCostPerSec(costPerSec);
			dataReg.setCostPerStorage(costPerStorage);
			
			dataRegDAO.updateDatacenterRegistry(simReg, dataReg);
			simRegDAO.updateSimulationRegistry(simReg);
			this.statusCode = 1;
		}
    	catch(Exception ex){
    		this.statusCode = 0;
    	}
		
    	return "success";
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setCostPerSec(double costPerSec) {
		this.costPerSec = costPerSec;
	}

	public void setCostPerMem(double costPerMem) {
		this.costPerMem = costPerMem;
	}

	public void setCostPerStorage(double costPerStorage) {
		this.costPerStorage = costPerStorage;
	}

	public void setCostPerBw(double costPerBw) {
		this.costPerBw = costPerBw;
	}
}
