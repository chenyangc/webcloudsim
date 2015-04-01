package org.app.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CurrentCustomerAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1075531937928819078L;
	private int id;
	private int statusCode;
	
	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			session.put("customerId", this.id);
			this.statusCode = 1;
		}
		catch(Exception ex){
			this.statusCode = 0;
		}
		
		return "success";
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setId(int id) {
		this.id = id;
	}
}
