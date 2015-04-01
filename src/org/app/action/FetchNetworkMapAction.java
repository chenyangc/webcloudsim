package org.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.NetworkMapEntry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchNetworkMapAction extends ActionSupport{
	/**
	 * 从数据库获得当前会话的数据，用于已经建立过模拟环境的情况，注意添加了ID字段
	 */
	private static final long serialVersionUID = -6334618798735103556L;
	private int total;
	private List<Object> rows = new ArrayList<Object>();
	
	public String execute() throws Exception{
		try{
			SimulationRegistryDAO ssrDAO=new SimulationRegistryDAO();
			
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			long id = (long) session.get("currentSimId");//需要在创建仿真环境时
			
			SimulationRegistry sr = ssrDAO.getSimulationRegistry(id);
			
			ArrayList<NetworkMapEntry> listOfNetwork = (ArrayList<NetworkMapEntry>) sr.getNetworkMapEntrys();
			
			this.total = listOfNetwork.size();
			
			this.rows.addAll(listOfNetwork);
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
