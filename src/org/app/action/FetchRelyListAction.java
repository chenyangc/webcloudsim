package org.app.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchRelyListAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5228943796933731609L;
	
	private List<Object> result = new ArrayList<Object>();
	
public String execute() throws Exception{
		
		try{
			
			
			Map<String, Object> session;
			
			ActionContext ctx = ActionContext.getContext();
			session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			CustomerRegistry cr=crDAO.getCustomerRegistry(simReg, (int)session.get("customerId"));
			List<Object> customerName = new ArrayList<Object>();
			
			for(CustomerRegistry tmpcr:simReg.getCustomerList()){
				if(tmpcr.getId()==cr.getId())
					continue;
				if(tmpcr.getReliedCustomerName()!=null&&tmpcr.getReliedCustomerName().equals(cr.getName()))
					continue;
			
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tmpcr.getId());
				map.put("customerName",tmpcr.getName());
				customerName.add(map);
			}
			
			this.result.clear();
			this.result.addAll(customerName);
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
