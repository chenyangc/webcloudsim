package org.app.action;

import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddCustomerAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5888940230895013745L;
	private String customerName;
	private int statusCode;
	
	public String execute() throws Exception{
		CustomerRegistry cusReg = new CustomerRegistry(this.customerName);
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			
			CustomerRegistryDAO cusRegDAO = new CustomerRegistryDAO();
			if(cusRegDAO.getCustomerRegistry(simReg, this.customerName) != null || dataRegDAO.getDatacenterRegistry(simReg, customerName) != null){
				this.statusCode = 0;//同名碰撞
				return "success";

			}
			else{//存入数据库
				cusRegDAO.insertNewCustomerRegistry(simReg, cusReg);
				
				simRegDAO.updateSimulationRegistry(simReg);
				this.statusCode = 1;
				return "success";
			}
		}
		catch(Exception ex){
			this.statusCode = 2;//系统异常			
		}
		return "success";
	}

	public int getStatuCode() {
		return statusCode;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
