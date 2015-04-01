package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.HostRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RemoveProviderHostAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3046891321561407250L;
	private int statusCode;
	private long id;
	
	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int providerId = (int) session.get("providerId");
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, providerId);
			
			List<HostRegistry> listOfHost = dataReg.getHostList();
			for(HostRegistry i:listOfHost){
				if(i.getId() == this.id){
					listOfHost.remove(i);
					this.statusCode = 1;
					break;
				}
			}
			
			dataRegDAO.updateDatacenterRegistry(simReg, dataReg);
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

	public void setId(long id) {
		this.id = id;
	}
}
