package org.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;
import org.app.models.VirtualMachineRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddCustomerVmAction extends ActionSupport{

	private static final long serialVersionUID = 5888940230895013745L;
		
	private int statusCode;
	
	public String execute() throws Exception{
		
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{
					
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			long simuId=(long) session.get("currentSimId");
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry(simuId);
			int currentCustomerId= (int) session.get("customerId");
			CustomerRegistryDAO cusRegDAO = new CustomerRegistryDAO();	
			CustomerRegistry cr=cusRegDAO.getCustomerRegistry(simReg, currentCustomerId);
			VirtualMachineRegistry vm=new VirtualMachineRegistry();
			cr.getVmList().add(vm);
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


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
}
