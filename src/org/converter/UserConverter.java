package org.converter;

import java.util.Map;
import org.domain.User;

import org.apache.struts2.util.StrutsTypeConverter;

public class UserConverter extends StrutsTypeConverter{

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		// TODO Auto-generated method stub
		User user=new User();
		String[] userValues=values[0].split(",");
		user.setName(userValues[0]);
		user.setPass(userValues[1]);
		return user;
	}

	@Override
	public String convertToString(Map context, Object o) {
		// TODO Auto-generated method stub
		User user=(User)o;
		return "<"+user.getName()+","+user.getPass()+">";
	}
	
}
