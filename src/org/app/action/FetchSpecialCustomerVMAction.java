package org.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FetchSpecialCustomerVMAction extends ActionSupport{

	
	private static final long serialVersionUID = -6334618798735103556L;
	
//	private Map<String, Object> customerVM = new HashMap<String, Object>();
	private int total;
	private List<Object> rows;
//	private VirtualMachineRegistry vm;
//	private String customerName;
	
	public String execute() throws Exception{
		
		SimulationRegistryDAO ssrDAO=new SimulationRegistryDAO();
		CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		int customerId = (int)session.get("customerId");
		long id = (long) session.get("currentSimId");//需要在创建仿真环境时
		
		SimulationRegistry sr = ssrDAO.getSimulationRegistry(id);
		
		CustomerRegistry cr=crDAO.getCustomerRegistry(sr, customerId);
		rows=new ArrayList<Object>();
		total=cr.getVmList().size();
		rows.addAll(cr.getVmList());

//						
//		customerVM.put("total", cr.getVmList().size());
//		customerVM.put("rows", cr.getVmList());
//		System.out.println(customerVM.toString());
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

	



//	public String getCustomerName() {
//		return customerName;
//	}



//	public void setCustomerName(String customerName) {
//		this.customerName = customerName;
//	}
//	
	
	
}
