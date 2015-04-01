package org.app.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CustomerListAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1729057384128667576L;
	private int id;
	private String text;
	private List<Object> children = new ArrayList<Object>();
	private List<Object> result = new ArrayList<Object>();
	
	
	public String execute() throws Exception{
		
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			List<Object> service = new ArrayList<Object>();
			
			for(int i = 0; i < simReg.getCustomerList().size(); ++i){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", simReg.getCustomerList().get(i).getId());
				map.put("text", simReg.getCustomerList().get(i).getName());
				service.add(map);
			}
			
			Map<String, Object> map_new = new HashMap<String, Object>();
			map_new.put("id", 0);
			map_new.put("text", "Service_list");
			map_new.put("children", service);
			
			this.result.clear();
			this.result.add(map_new);
			
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public List<Object> getChildren() {
		return children;
	}

	public List<Object> getResult() {
		return result;
	}

}
