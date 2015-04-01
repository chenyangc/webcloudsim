package org.app.action;

import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEnvironmentAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 642182027907630206L;
	private String environmentName;
	private int statusCode;
	
	public String execute() throws Exception{
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
		
		try{
			if(simRegDAO.getSimulationRegistry(this.environmentName) != null){
				this.statusCode = 0;//数据库已存在同名仿真
				System.out.println("same");
			}
			else{
				SimulationRegistry simReg = new SimulationRegistry(this.environmentName);
				simRegDAO.insertSimulationRegistry(simReg);
				//存入数据之后会产生ID
				SimulationRegistry simRegNew = simRegDAO.getSimulationRegistry(this.environmentName);
				
				session.put("currentSimId", simRegNew.getId());
				this.statusCode = 1;//成功生成内存中的SimulationRegistry实体并存入数据库
				System.out.println("success");
				return "success";
			}
		}
		catch(Exception ex){
			System.out.println(ex);
			this.statusCode = 2;//系统异常
		}
		

		return "success";
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
}
