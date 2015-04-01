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
	<link rel="stylesheet" type="text/css" href="css/Provider_provider.css">
	<link rel="stylesheet" type="text/css" href="css/Customer_provider_main.css">
	<script src="js/jquery-2.1.1.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	
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

<div class="easyui-tabs" id="Choice_overview_network" style="height:550px;width:800px">
   <div title="Overview" style="padding:10px">
       <div id="Provider_add_datacenter_part">
       <a href="javascript:void(0)" class="easyui-linkbutton" onclick="Provider_add_datacenter()" style="height: 40px; width: 200px;font-size:18px;">Add Datacenter</a>
       </div>
       
       <div id="Provider_delete_datacenter_part">
       <a href="javascript:void(0)" class="easyui-linkbutton" onclick="Provider_delete_datacenter()"  style="height: 40px; width: 200px;font-size:18px;" border="0">Remove Datacenter</a>
       </div>
	   <div id="Provider_add_datacenter_dialog"></div>
	   <div id="Provider_delete_datacenter_dialog"></div>
	   <div id="Wait_Html"></div>
	   
	   <div id="Provider_general_information_part">
        <div class="easyui-panel" title="General Information" style="height:220px;width:700px">
            <p></p> 
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
                   <td><input name="Storage_capacity" id="Storage_capacity"  class="easyui-textbox" style="background:transparent;border:0" readonly></td>
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
   
   
   <div title="Network" style="padding:10px">
   
       <div>Description of each link whose source is a datacenter.</div>
       <div id="Provider_network_part">
       <table id="Provider_network_table" style="width:602px;height:400px"></table>
       </div>
		
   </div>
   
</div>
</body>
</html>