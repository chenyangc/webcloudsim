package org.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.NetworkMapEntry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchSpecialCustomerNetworkAction extends ActionSupport{

	/**
	 * 从数据库获得当前会话的数据，用于已经建立过模拟环境的情况，注意添加了ID字段
	 */
	private static final long serialVersionUID = -6334618798735103556L;
	private int total;
	private List<Object> rows=new ArrayList<Object>();
	
	public String execute() throws Exception{
		SimulationRegistryDAO ssrDAO=new SimulationRegistryDAO();
		CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		long id = (long) session.get("currentSimId");//需要在创建仿真环境时
		int customerId = (int)session.get("customerId");
		SimulationRegistry sr = ssrDAO.getSimulationRegistry(id);
		rows.clear();
		List<NetworkMapEntry> listOfNetwork = sr.getNetworkMapEntrys();
		for(NetworkMapEntry nm:listOfNetwork){
			if(nm.getSource().equals(crDAO.getCustomerRegistry(sr, customerId).getName())){
				rows.add(nm);
			}
		}
		
		total=rows.size();
		
		return "success";
	}


	public int getTotal() {
		return total;
	}


	public List<Object> getRows() {
		return rows;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public void setRows(List<Object> rows) {
		this.rows = rows;
	}
	
	
}
