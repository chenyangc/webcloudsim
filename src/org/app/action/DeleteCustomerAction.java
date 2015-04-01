package org.app.action;

import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCustomerAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6637630575211016939L;
	private String customerName;
	private int statusCode;
	
	public String execute() throws Exception{
		try{
			System.out.println("@!!"+customerName);
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			CustomerRegistryDAO cusRegDAO = new CustomerRegistryDAO();
			CustomerRegistry cusReg = cusRegDAO.getCustomerRegistry(simReg, this.customerName);
			if(cusReg != null){
				System.out.println(customerName+" delete success");
				//存在，可以删除
				cusRegDAO.removeCustomerRegistry(simReg, this.customerName);
				simRegDAO.updateSimulationRegistry(simReg);
				this.statusCode = 1;//删除成功
				return "success";
			}
			else{
				System.out.println("@!!"+customerName+"failed");
				this.statusCode = 0;//不存在该customer名，失败
				return "success";
			}
		}
		catch(Exception ex){
			this.statusCode = 2;//系统异常
			return "success";
		}	
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
