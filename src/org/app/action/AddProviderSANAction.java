package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SanStorageRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddProviderSANAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1501199902816374326L;

	private int statusCode;
    
    /** The SAN's name. */
    private String name;
	
	public String execute() throws Exception{
			try{
				ActionContext ctx = ActionContext.getContext();
				Map<String, Object> session = ctx.getSession();
				
				SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
				SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
				
				DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
				int providerId = (int) session.get("providerId");
				DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, providerId);
				
				dataReg.getSanList().add(new SanStorageRegistry(name));
				
				simRegDAO.updateSimulationRegistry(simReg);
				
				this.statusCode = 1;	
				return "success";
			}
			catch(Exception ex){
				this.statusCode = 2;//系统异常
			}
			return "success";
		}

	public int getStatusCode() {
		return statusCode;
	}

	public void setName(String name) {
		this.name = name;
	}
}
