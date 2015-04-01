<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" pageEncoding="utf-8" %>
  <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Provider</title>
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	<link rel="stylesheet" type="text/css" href="css/Provider_special_provider.css">
	<link rel="stylesheet" type="text/css" href="css/Customer_provider_main.css">
	<script src="js/jquery-2.1.1.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	
    <script type="text/javascript" src="js/buttonjs.js"></script>
    <script type="text/javascript" src="js/Begin_tree.js"></script>
    <script type="text/javascript" src="js/Begin_form.js"></script>

	<script type="text/javascript" src="js/Begin_table.js"></script>
	<script type="text/javascript" src="js/Customer_add_delete_customer_provider.js"></script>
	<script type="text/javascript" src="js/environmentName.js"></script>
</head>
<body>

<div id="logo">
	<img src="images/bupt_logo.png" alt="logo" />
</div>
<div id="Customer_provider_begin_link">
	<div class="easyui-accordion" style="width:250px;">
		<div title="WebCloudSim" data-options="collapsed:false,collapsible:false" style="width:70px;padding:10px;">
		    <div>Simulation environments:</div>
            <p></p>
            <p></p>
			<div>
			
			<table>
			<tr>		
			<td>
                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="Environment_add_environment()" style="height: 25px; width: 80px;font-size:12px;">Add</a>				
            </td>          
			</tr>
			<tr><td>Now Simulation Name</td></tr>
			<tr><td><input type="text"  id="simulationName" readonly/></td></tr>
			</table>
			
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




<div class="easyui-tabs" id="choice_general_hosts_costs_san_network" style="height:550px;width:800px">

   <div title="General" style="padding:10px">
        <div id="Speial_Provider_settings_part">
        <div class="easyui-panel" title="Settings" style="height:250px;width:700px">
		<s:form id="Speial_Provider_settings_form">
		    <table  style="height:50px;width:500px">
			    <tr>
				   <td style="text-align:right;width:150px">Allocation Policy:</td>
				   <td style="text-align:left">
				       <select id="Allocation_Policy" name="Allocation_Policy" class="easyui-combobox" name="state" editable="false" required="true"  panelHeight="50px" style="width:200px; data-options=panelHeight:'50'">

		               <option value="Single threshold">Single threshold</option></select></td>
		           <td id="Provider_add_datacenter_part">

       <a class="easyui-linkbutton" onclick="saveSettings()" style="height: 20px; width: 120px;font-size:16px;margin:10px;">save Settings</a>

       </td>
				</tr>
				
			</table>
			
		    <table  style="width:600px">
			    <tr>
				   <td style="text-align:right;width:150px;height:35px">Architecture:</td>
				   <td style="text-align:left;width:150px"><select id="Architecture" name="Architecture" class="easyui-combobox" style="width:120px;" editable="false" required="true" data-options="panelHeight:'50'">
		               <option value="x86">x86</option></select></td>
				   <td style="text-align:right;width:150px">Upper Threshold:</td>
				   <td style="text-align:left"><input id="Upper_Threshold" name="Upper_Threshold" class="easyui-numberbox" style="width:120px;" value="" required="true" precision="2" min="0"></input></td>
				</tr>
			    <tr>
				   <td style="text-align:right;height:35px">Operating System:</td>
				   <td style="text-align:left"><select id="Operating_System" name="Operating_System" class="easyui-combobox" style="width:120px;" editable="false" required="true" data-options="panelHeight:'50'">
		               <option value="Linux">Linux</option><option value="Unix">Unix</option></select></td>
				   <td style="text-align:right">Lower Threshold:</td>
				   <td style="text-align:left"><input id="Lower_Threshold" name="Lower_Threshold" class="easyui-numberbox" style="width:120px;" value="" required="true" precision="2" min="0"></input></td>
				</tr>
			    <tr>
				   <td style="text-align:right;height:35px">Hypervisor:</td>
				   <td style="text-align:left"><select id="Hypervisor" name="Hypervisor" class="easyui-combobox" style="width:120px;" editable="false" required="true" data-options="panelHeight:'50'">
		               <option value="Xen">Xen</option><option value="KVM">KVM</option></select></td>
				   <td style="text-align:right">VM Migrations:</td>
				   <td style="text-align:left"><select id="VM_Migrations" name="VM_Migrations" class="easyui-combobox" style="width:120px;" editable="false" required="true" data-options="panelHeight:'50'">
		               <option value="true">true</option><option value="false">false</option></select></td>
				</tr>
			    <tr>
				   <td style="text-align:right;height:35px">Scheduling interval:</td>
				   <td style="text-align:left"><input id="Scheduling_interval" name="Scheduling_interval" class="easyui-numberbox" style="width:120px;" value="" required="true" min="0"></input></td>
				   <td style="text-align:right">Monitoring interval:</td>
				   <td style="text-align:left"><input id="Monitoring_interval" name="Monitoring_interval" class="easyui-numberbox" style="width:120px;" value="" required="true" min="0"></input></td>
				</tr>
			</table>
	    </s:form>
		</div>
		</div>
		<div id="Wait_Html"></div>
		<div id="Special_provider_information_part">
		<div class="easyui-panel" title="Information" style="height:210px;width:700px">
		<s:form id="Provider_general_information_form">
            <table width="600">
               <tr>
                   <td width="300">Number of datacenters:</td>
                   <td><input name="Number_datacenters" id="Number_datacenters" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Number of hosts:</td>
                   <td><input name="Number_hosts" id="Number_hosts"  class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Number of processing units:</td>
                   <td><input name="Number_processing_units" id="Number_processing_units"  class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Processing capacity(MIPS):</td>
                   <td><input name="Processing_capacity" id="Processing_capacity"  class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Storage capacity:</td>
                   <td><input name="Storage_capacity" id="Storage_capacity"  class="easyui-textbox" style="background:transparent;border:0" readonly></input></td>
               </tr>
               <tr>
                   <td>Total amount of RAM:</td>
                   <td><input name="Total_amount_RAM" id="Total_amount_RAM" class="easyui-textbox" style="background:transparent;border:0" readonly></input></td>
               </tr>
            </table>
		    </s:form>
		</div>
		</div>
   </div>

   <div title="Hosts" style="padding:10px">
    <div id="Special_provider_hosts_part">
    <table id="Special_provider_hosts_table" style="width:700px;height:400px"></table>
    </div>
   </div>

   <div title="Costs" style="padding:10px">
        <div id="Special_provider_costs_part">
		<div class="easyui-panel" title="Utilization Costs" style="height:210px;width:700px">
		<s:form id="Special_provider_costs_form">
		<table  style="width:600px">
			    <tr>
				<td style="text-align:left;width:200px;height:40px">Processing cost(per sec):</td>
				<td style="text-align:left"><input id="Processing_cost" name="Processing_cost" class="easyui-numberbox" value="" style="width:200px;" required="true" precision="3" min="0"></input></td>
				</tr>
			    <tr>
				<td style="text-align:left;height:40px">Memory cost(per MB):</td>
				<td style="text-align:left"><input id="Memory_cost" name="Memory_cost" class="easyui-numberbox" value="" style="width:200px;" required="true" precision="3" min="0"></input></td>
				</tr>
			    <tr>
				<td style="text-align:left;height:40px">Storage(per MB):</td>
				<td style="text-align:left"><input id="Storage" name="Storage" class="easyui-numberbox" value="" style="width:200px;" required="true" precision="3" min="0"></input></td>
				</tr>
			    <tr>
				<td style="text-align:left;height:40px">Bandwidth(per MB):</td>
				<td style="text-align:left"><input id="Bandwidth" name="Bandwidth" class="easyui-numberbox" value="" style="width:200px;" required="true" precision="3" min="0"></input></td>
				<td id="Provider_add_datacenter_part">
       <a class="easyui-linkbutton" onclick="saveCosts()" style="height: 20px; width: 120px;font-size:16px;">save Costs</a>
       </td>
				</tr>
		</table>
		</s:form>
		</div>
		</div>
   </div>
   
   <div title="SAN" style="padding:10px">
       <div id="Special_provider_san_part">
       <table id="Special_provider_san_table" style="width:602px;height:400px"></table>
	   </div>
   </div>
   
   <div title="Network" style="padding:10px">
       <div>Destination of links whose source is this datacenter.</div>
       <div id="Special_provider_network_part">
       <table id="Special_provider_network_table" style="width:602px;height:400px"></table>
	   </div>
   </div>

</body>
</html>
