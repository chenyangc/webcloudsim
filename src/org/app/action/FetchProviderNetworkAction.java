package org.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.NetworkMapEntry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchProviderNetworkAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7182239666418688101L;
	private int total;
	private List<Object> rows = new ArrayList<Object>();
	
	public String execute() throws Exception{
		try {
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			int id = (int)session.get("providerId");
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			String source = dataRegDAO.getDatacenterRegistry(simReg, id).getName();
			System.out.println("!!!!!@@"+source);
			List<NetworkMapEntry> listOfNetwork = simReg.getNetworkMapEntrys();
			
			this.rows.clear();
			for(NetworkMapEntry i:listOfNetwork){
				if(i.getSource().equals(source)){
					System.out.println("!!!!find!@@");
					this.rows.add(i);
				}
			}
			System.out.println(rows);
			
			this.total = this.rows.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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