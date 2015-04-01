<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" pageEncoding="utf-8" %>
  <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Customer</title>
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	<link rel="stylesheet" type="text/css" href="css/Customer_special_customer.css">
	<link rel="stylesheet" type="text/css" href="css/Customer_provider_main.css">
	<script src="js/jquery-2.1.1.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	
    <script type="text/javascript" src="js/Begin_tree.js"></script>
	<script type="text/javascript" src="js/Begin_form.js"></script>
	<script type="text/javascript" src="js/Begin_table.js"></script>
	<script type="text/javascript" src="js/Customer_add_delete_customer_provider.js"></script>

	<script type="text/javascript" src="js/environmentName.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
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




<div class="easyui-tabs" id="Choice_machine_profile_network" style="height:550px;width:800px">
   <div title="virtual Machines" style="padding:10px">
       <div>Virtual machines owned by this customer:</div>
       <div id="Special_customer_virtual_machine_part">
	   <table id="Special_customer_virtual_machine_table" style="width:700px;height:400px"></table>
	   </div>
   </div>
   
   <div title="Utilization Profile" style="padding:10px">
  	 <div style="text-align:center">This customer's utilization profile: 
     <a  class="easyui-linkbutton" onclick="saveCustomerUtilization()" 
    	 style="height: 20px; width: 120px;font-size:16px;margin:10px;">Save Utilization</a>
   </div>
   <div id="Special_customer_datacenter_broker_part">
       <div class="easyui-panel" title="Datacenter Broker" style="height:80px;width:700px">
            <p></p>
            <table width="600">
            <tr>
            <td width="150">Broker policy:</td>
            <td>
            <select id="datacenter_broker" class="easyui-combobox" value="NetApp" 
              data-options="panelHeight:'50'" style="width:100px" editable='false'>
                 <option value="NetApp">Net App</option>
                <option value="Round robin">Round robin</option>               
		       	<option value="Other">Other</option>
		       	
              </select>
            </td>
            <td width="150">Location:</td>
            <td>
            <select id="location" class="easyui-combobox" value="北京" 
              data-options="panelHeight:'150'" style="width:100px" editable='false'>           
		       	<option value="sh">上海</option>
		       	<option value="ls">拉萨</option>
		       	<option value="cq">重庆</option>
		       	<option value="nn">南宁</option>		     
		       	<option value="nj">南京</option>
		       	<option value="cc">长春</option>
              </select>
            </td>
            </tr>
            </table>
       </div>
   </div>
   <div id="Wait_Html"></div>
    <div id="Special_customer_rely_part">
       <div class="easyui-panel" title="Rely" style="height:80px;width:700px">
            <p></p>
            <table width="600">
            <tr>
            <td width="200">Rely:</td>
            <td>
            <input class="easyui-combobox" id="Special_customer_rely_combobox"  editable='false'></input>
            </td>
            </tr>
            </table>
       </div>
   </div>
   

   <div id="Special_customer_cloudlets_part">
   <div class="easyui-panel" title="Cloudlets" style="height:250px;width:700px">
           <p></p><p></p>
		   <form id="Special_customer_cloudlets_form">
           <table width="650">
           <tr>
           <td width="150" height="35">Processing elements:</td>
           <td width="200"><input id ="Processing_elements" class="easyui-numberbox" required='true' value="100" min="0"></input></td>
           <td width="200">CPU utilization model:</td>
           <td><select id="cpu_model" class="easyui-combobox" name="state" style="width:100px;" editable='false' data-options="panelHeight:'50'">
		       <option value="Full">Full</option></select></td>
		       <option value="Stochastic">Stochastic</option>
		       
           </tr>
           <tr>
           <td height="35">Maximum length:</td>
           <td><input id ="Maximum_length" class="easyui-numberbox" required='true' value="10000000" min="0"></input></td>
           <td>RAM utilization model:</td>
           <td><select id="ram_model" class="easyui-combobox" editable='false' style="width:100px;" data-options="panelHeight:'50'">
		        <option value="Full">Full</option></select></td>
		       <option value="Stochastic">Stochastic</option>
		      
           </tr>
           <tr>
           <td height="35">File size:</td>
           <td><input id ="File_size" class="easyui-numberbox" required='true' value="400" min="0"></input></td>
           <td>Bandwidth utilization model:</td>
           <td><select id="bw_model" class="easyui-combobox" editable='false'  name="state" style="width:100px;" data-options="panelHeight:'50'">
		      <option value="Full">Full</option></select></td>
		       <option value="Stochastic">Stochastic</option>
		       
           </tr>
           <tr>
           <td height="35">Output size:</td>
           <td><input id ="Output_size" required='true' class="easyui-numberbox" value="500" min="0"></input></td>
           <td></td>
           <td></td>
           </tr>
           </table>
		   </form>
   </div>
   </div>
   </div>

   <div title="Network" style="padding:10px">
      <div>Destination of links whose source is this costomer.</div>
       <div id="Special_customer_Network_part">
       <table id="Special_customer_network_table" style="width:602px;height:400px"></table>
       </div>
   </div>
   </div>
   
</div>
</body>
</html>