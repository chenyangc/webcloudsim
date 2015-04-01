<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,org.app.utils.*,org.app.dao.*,org.app.models.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Simulation Report - Customers</title>
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
    <link rel="stylesheet" type="text/css" href="css/log_customer_datacenter.css">
    <link rel="stylesheet" type="text/css" href="css/Customer_provider_main.css">
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" charset="utf-8" />
    <script type="text/javascript" src="js/jquery-2.1.1.js"></script>
    <script type="text/javascript" src="js/echarts-all.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/Begin_tree.js"></script>
	
	<script type="text/javascript" src="js/Begin_table.js"></script>
	<script type="text/javascript" src="js/Begin_form.js"></script>
	<script type="text/javascript" src="js/Customer_add_delete_customer_provider.js"></script>
	<script type="text/javascript" src="js/environmentName.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	
<script type="text/javascript">
$(document).ready(function() {	

		<% 	DrawDataDAO ddDAO=new DrawDataDAO(); 
		SimulationRegistryDAO srDAO=new SimulationRegistryDAO();			
		SimulationRegistry sr=srDAO.getSimulationRegistry((long)session.getAttribute("currentSimId"));
		%>
		var overall=<%=ddDAO.getDrawData(sr,"vm-1").getValue()%>;
		<%	CustomerRegistryDAO  crDAO=new CustomerRegistryDAO();
			
	    List<CustomerRegistry> customerlist=sr.getCustomerList();
	    int vm_number=(int)crDAO.getTotalNumOfVms(sr);
      
        	
       for(int i=0;i<vm_number;i++){
        	int vmid=i;
        	 String tmp=ddDAO.getDrawData(sr,"vm"+i).getValue();
			out.println("var vm"+i+" ="+tmp+";");
       }
        				 
		out.println("var map={ \"overall\" :overall,");
      
		 for(int i=0;i<vm_number;i++){
	        	int vmid=i;
        		if(i==vm_number-1){
        			out.println("\"vm"+i+"\":vm"+i+"};");
        		}
        		else{
        			out.println("\"vm"+i+"\":vm"+i+",");
        		}
        	}
        
							
		%>
		
		var option2_cloudlets_Customer1=<%=ddDAO.getDrawData(sr,"cloudletsNum").getValue()%>;
		var option3_execution_time_Customer1=<%=ddDAO.getDrawData(sr,"cloudletsTime").getValue()%>;
		
		draw('option1', overall);
		draw('option2', option2_cloudlets_Customer1);
		draw('option3', option3_execution_time_Customer1);
		$(".source").change(function() {
			var sel = $(this);
			var value = sel.val();
			var option = map[value];
			draw('option1', option);
		});
		
		function draw(dom, option){
			var myChart = echarts.init(document.getElementById(dom)); 
			myChart.setOption(option);
		}
});
	
</script>
</head>
<body>
<div id="logo">
	<img src="images/bupt_logo.png" alt="logo" />

</div>
<div id="Customer_provider_begin_link" >
	<div class="easyui-accordion" style="width:250px;">
		<div title="WebCloudSim" data-options="collapsed:false,collapsible:false" style="width:70px;padding:10px;">
		    <div>Simulation environments:</div>
            <p></p>
            <p></p>
			<div>
			<form id="simulationName_form" method="post">
			<table>
			<tr>		
			<td>
                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="Environment_add_environment()" style="height: 25px; width: 80px;font-size:12px;">Add</a>				
            </td>          
			</tr>
			<tr><td>Now Simulation Name</td></tr>
			<tr><td><input type="text" name="simulationName" id="simulationName" readonly/></td></tr>
			</table>
			</form>
			</div>
			<div id="Environment_add_environment_dialog"></div>
			<div id="Environment_delete_environment_dialog"></div>
		</div>

		<div title="Datacenter" style="overflow:auto;padding:10px">
		    <div class="easyui-panel" style="padding:5px">
			<ul id="Provider_tree"></ul>
			</div>
		</div>

		<div title="Service" style="padding:10px">
		    <div class="easyui-panel" style="padding:5px">
			<ul id="Customer_tree"></ul>
			</div>
		</div>		
		<div title="Result View"   style="padding:0px">
		  	<div class="easyui-panel" style="padding:0px">
			<ul id="Result_tree">
				<li><a href="topology.jsp">Topology View</a></li>
				<li><a href="Provider_simulation_report.jsp">Datacenter Result View</a></li>
				<li><a href="Customers_simulation_report.jsp">Service Result View</a></li>
				<li><a href="Log_simulation_report.jsp">Log Result View</a></li>
			</ul>
			</div>
		</div> 
	</div>
</div>
<div id="runButton" style="position:absolute;height:52px;width:200px;top:520px;left:70px;">
 	<a href="javascript:void(0)"  onclick="Run_simulation()" ><img src="css/icons/run.png"></img> </a>
</div>



<div id="new_topic">

	    <div id="container">
            <div id="top_menu">	
		    <ul>
			<li class="customer"><a href="#" class="selected">Select a customer:</a>								    
			<select id="select_customer" >
				<option value="customer_Customer1">Customer1</option>
			</select>
			</li>
       		</ul>
	    </div>
      		
        <div id="customer_Customer1">
            <div id="content">
		        <div id="resource_utilization_Customer1" style="width:750px;height:423px">		
			        <p>Select the source:
					    <select class="source" id="select_esource_utilization_Customer1"   >
						    <option value="overall">Overall</option>
						    <%
						    for(int i=0;i<vm_number;i++){
					        	int vmid=i;
								out.println("<option value=\"vm"+i+"\">VM"+i+"</option>");
							}%>
						</select>	
			        </p>		
                    <div id="option1" style="width:750px;height:386px"></div>					
		        </div> 
	        </div> <!-- end of content -->
            <div id="content">
                <div id="option2" style="width:750px;height:386px"></div>	
         	</div> <!-- end of content -->
	        <div id="content">
                <div id="option3" style="width:750px;height:386px"></div>	
         	</div> <!-- end of content -->
		    </div> <!-- end of customer_Customer1 -->
            <div id="footer"></div><!-- end of footer -->
        </div> <!-- end of container -->
    </div> <!-- end of slider -->

</body>