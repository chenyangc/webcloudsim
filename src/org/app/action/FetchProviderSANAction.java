package org.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SanStorageRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchProviderSANAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3048508238487530546L;
	private int total;
	private List<Object> rows = new ArrayList<Object>();
	
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
			
			this.total = listOfSAN.size();
			this.rows.clear();
			this.rows.addAll(listOfSAN);
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

	public int getTotal() {
		return total;
	}

	public List<Object> getRows() {
		return rows;
	}
}
