package org.app.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.HostRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchHostIdAction extends ActionSupport{
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
			
			List<Object> hostId = new ArrayList<Object>();
			
			for(DatacenterRegistry dr:simReg.getDatacenterList()){
				for(HostRegistry host:dr.getHostList()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", host.getId());
					map.put("hostid", host.getId());
					hostId.add(map);
				}				
			}
			
			this.result.clear();
			this.result.addAll(hostId);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return "success";
	}

public List<Object> getResult() {
	return result;
}
}
