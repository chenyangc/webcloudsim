package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteDatacenterAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2322455858398825257L;
	private String providerName;
	private int statusCode;
	
	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			
			if(dataRegDAO.removeDatacenterRegistry(simReg, this.providerName)){
				System.out.println("-----------删除成功-----------");
				
				simRegDAO.updateSimulationRegistry(simReg);
				
				System.out.println("--------------更新成功-------------");
				
				this.statusCode = 1;//删除成功
			}
			else{
				System.out.println("删除失败");
			}
		}
		catch(Exception ex){
			this.statusCode = 2;//系统异常
		}
		return "success";
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
}
