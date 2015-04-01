package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SanStorageRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RemoveProviderSANAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2660769444823927124L;
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
			
			List<SanStorageRegistry> listOfSAN = dataReg.getSanList();
			
			System.out.println("被删除的id是：" + id);
			
			for(SanStorageRegistry i:listOfSAN){
				if(i.getId() == this.id){
					listOfSAN.remove(i);
					this.statusCode = 1;
					break;
				}
			}
			
			System.out.println("删除成功啦--------------");
			
			dataRegDAO.updateDatacenterRegistry(simReg, dataReg);
			simRegDAO.updateSimulationRegistry(simReg);
		}
    	catch(Exception ex){
    		this.statusCode = 0;
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
