<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" pageEncoding="utf-8" %>
  <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta charset="utf-8" http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Simulation  - Overview</title>

<link rel="stylesheet" href="css/style.css" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="css/easyui.css">
<link rel="stylesheet" type="text/css" href="css/icon.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
<link rel="stylesheet" type="text/css" href="css/Customer_customer.css">
<link rel="stylesheet" type="text/css" href="css/Customer_provider_main.css">

<script src="js/jquery-2.1.1.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>

<script type="text/javascript" src="js/Begin_tree.js"></script>
<script type="text/javascript" src="js/Begin_table.js"></script>
<script type="text/javascript" src="js/Begin_form.js"></script>
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

       <div id="Customer_add_customer_part">
       <a href="javascript:void(0)" class="easyui-linkbutton" onclick="Customer_add_customer()" style="height: 40px; width: 200px;font-size:18px;">Add Service</a>
       </div>
       <div id="Customer_delete_customer_part">
       <a href="javascript:void(0)" class="easyui-linkbutton" onclick="Customer_delete_customer()" style="height: 40px; width: 200px;font-size:18px;" border="0">Remove Service</a>
       </div>
       <div id="Customer_add_customer_dialog"></div>
	   <div id="Customer_delete_customer_dialog"></div>
	   <div id="Wait_Html" ></div>

       <div id="Customer_users_part">
       <form id="Customer_users_form" method="get">
       <div class="easyui-panel" title="Users" style="height:180px;width:700px">
          <p></p> 
          <table id="Customer_users_table" width="600">
               <tr>
                   <td width="300">Number of services:</td>
                   <td><input name="Number_customers" id="Number_customers" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Number of cloudlet:</td>
                   <td><input name="Cloudlets_minute" id="Cloudlets_minute" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Average length of cloudlets:</td>
                   <td><input name="Average_cloudlets" id="Average_cloudlets" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Average cloudlet's file size:</td>
                   <td><input name="Average_file_size" id="Average_file_size" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Average cloudlet's output size:</td>
                   <td><input name="Average_output_size" id="Average_output_size" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
           </table>
           </div>
        </form>
        </div>
		
        <div id="Customer_virtual_machines_part">
		<form id="Customer_virtual_machines_form" method="post">
           <div class="easyui-panel" title="Virtual Machines" style="height:180px;width:700px">
              <p></p><p></p>
              <table id="Customer_virtual_machines_table" width="600">
              <tr>
                   <td width="300">Number of virtual machine:</td>
                   <td><input name="Number_virtual_machine" id="Number_virtual_machine" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Average image size:</td>
                   <td><input name="Average_image_size" id="Average_image_size" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Average RAM:</td>
                   <td><input name="Average_RAM" id="Average_RAM" class="easyui-textbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               <tr>
                   <td>Average bandwidth:</td>
                   <td><input name="Average_bandwidth" id="Average_bandwidth" class="easyui-numberbox" style="background:transparent;border:0" readonly></input>
                   </td>
               </tr>
               </table>
           </div>
		</form>
        </div>

   </div>
   
   
   <div title="Network" style="padding:10px">
   
        <div>Description of each link whose source is a customer.</div>
        <div id="Customer_network_part">
        <table id="Customer_network_table" style="width:602px;height:350px"></table>
        </div>
   
   </div>
</div>

</body>
</html>
