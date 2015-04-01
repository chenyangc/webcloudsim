package org.app.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class DatacenterNameAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5228943796933731609L;
	
	private List<Object> result = new ArrayList<Object>();
	
public String execute() throws Exception{
		
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			List<Object> providerName = new ArrayList<Object>();
			
			for(int i = 0; i < simReg.getDatacenterList().size(); ++i){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", simReg.getDatacenterList().get(i).getId());
				map.put("datacenter_name", simReg.getDatacenterList().get(i).getName());
				providerName.add(map);
			}
			
			this.result.clear();
			this.result.addAll(providerName);
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

public List<Object> getResult() {
	return result;
}
}
