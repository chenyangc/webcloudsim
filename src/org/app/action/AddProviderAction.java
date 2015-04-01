package org.app.action;

import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddProviderAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2542178277899511586L;
	private String providerName;
	private int statusCode;
	
	public String execute() throws Exception{
		try{
			DatacenterRegistry dataReg = new DatacenterRegistry(this.providerName);
			
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			CustomerRegistryDAO cusRegDAO = new CustomerRegistryDAO();
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			if(dataRegDAO.getDatacenterRegistry(simReg, this.providerName) != null || cusRegDAO.getCustomerRegistry(simReg, providerName) != null){
				this.statusCode = 0;//同名碰撞
			}
			else{
				dataRegDAO.insertNewDatacenterRegistry(simReg, dataReg);
				simRegDAO.updateSimulationRegistry(simReg);
				this.statusCode = 1;
			}
		}
		catch(Exception ex){
			this.statusCode = 2;//系统异常
		}		
		return "success";
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
