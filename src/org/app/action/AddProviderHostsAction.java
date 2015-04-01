package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.HostRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddProviderHostsAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7360465479164111209L;
	private int statusCode;
	
	public String execute() throws Exception{		
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int providerId = (int) session.get("providerId");
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			
			if(dataRegDAO.getListOfHosts(simReg, providerId).add(new HostRegistry())){
				simRegDAO.updateSimulationRegistry(simReg);
				
				this.statusCode = 1;
			}
			else{
				this.statusCode = 2;
			}
			
				
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
}
