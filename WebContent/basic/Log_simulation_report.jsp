<%@   page   import= "org.app.utils.*,org.app.dao.*,java.text.*,java.util.*,org.app.simulation.* ,org.app.models.*"%>
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
		<div id="content">	
		<%        
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String timeAndDate = sdf.format(Calendar.getInstance().getTime());        
		%>
			<p>Report generated at <em><%=timeAndDate.split(" ")[0]%></em> 
			on <em><%=timeAndDate.split(" ")[1]%></em>.</p>
		</div>
	<div id="content">
		<% 	DrawDataDAO ddDAO=new DrawDataDAO(); 
			
			SimulationRegistryDAO srDAO=new SimulationRegistryDAO();
			SimulationRegistry sr=srDAO.getSimulationRegistry((long)session.getAttribute("currentSimId"));

		%>
        <%=ddDAO.getDrawData(sr,"log").getValue()%>                 
    </div> <!-- end of content -->

	<div id="footer"></div><!-- end of footer -->
    </div> <!-- end of container -->
</div> <!-- end of slider -->


</div>
</body>