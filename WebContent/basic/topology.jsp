<%@ page contentType="text/html; charset=utf-8" language="java" import="java.lang.*,java.util.*,org.app.utils.*,org.app.dao.*,org.app.models.*" pageEncoding="utf-8" %>
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
    
  	
 	<script type="text/javascript" src="js/echarts.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>  
	<script type="text/javascript" src="js/jquery.min.js"></script>
	
	
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/Begin_tree.js"></script>	
	<script type="text/javascript" src="js/Begin_table.js"></script>
	<script type="text/javascript" src="js/Begin_form.js"></script>
	<script type="text/javascript" src="js/Customer_add_delete_customer_provider.js"></script>	
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript">
<% 	

	SimulationRegistryDAO srDAO=new SimulationRegistryDAO();	
	CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
	SimulationRegistry sr=srDAO.getSimulationRegistry((long)session.getAttribute("currentSimId"));
	List<CustomerRegistry> crlist=sr.getCustomerList();
	Map<String,String> map=new HashMap<String,String>();
	map.put("bj","北京");
	map.put("sh","上海");
	map.put("cq","重庆");
	map.put("ls","拉萨");
	map.put("gz","广州");
	map.put("nn","南宁");
	map.put("nj","南京");
	map.put("cc","长春");



%>

require.config({
    paths: {
        echarts: 'js'
    },
    packages: [
        {
            name: 'BMap',
            location: 'js/chart',
            main: 'main'
        }
    ]
});
require(
	    [
	        'echarts',	
	        'BMap',
	        'echarts/chart/map'
	    ],
function (echarts, BMapExtension) {
	   
        // 基于准备好的dom，初始化echarts图表
        var BMapExt = new BMapExtension($('#topology')[0], BMap, echarts);
        var map = BMapExt.getMap();
        var container = BMapExt.getEchartsContainer();

        var startPoint = {
            x: 104.114129,
            y: 37.550339
        };
        var point = new BMap.Point(startPoint.x, startPoint.y);
        map.centerAndZoom(point, 5);
        map.enableScrollWheelZoom(true);
        option = {
                color: ['gold','aqua','lime'],
                title : {
                    text: '',
                    subtext:'',
                    x:'center',
                    textStyle : {
                        color: '#fff'
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function (v) {
                        return v[1].replace(':', ' > ');
                    }
                },
                legend: {
                    orient: 'vertical',
                    x:'left',
                    data:['北京', <%
                          for(CustomerRegistry cr:crlist){
                               out.print("'"+map.get(cr.getLocation())+"',");
                          }                    		
                          %> '广州'],
                    selectedMode: 'single',
                    selected:{ 
                    	'北京' :false,
                        '广州' : false
                    },
                    textStyle : {
                        color: '#fff'
                    }
                },
                toolbox: {
                    show : true,
                    orient : 'vertical',
                    x: 'right',
                    y: 'center',
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataRange: {
                    min : 0,
                    max : 100,
                    x: 'right',
                    calculable : true,
                    color: ['#ff3333', 'orange', 'yellow','lime','aqua'],
                    textStyle:{
                        color:'#fff'
                    }
                },
                series : [
                    {
                        name:'北京',
                        type:'map',
                        mapType: 'none',
                        data:[],
                        geoCoord: {
                            '上海': [121.4648,31.2891],
                            '东莞': [113.8953,22.901],
                            '东营': [118.7073,37.5513],
                            '中山': [113.4229,22.478],
                            '临汾': [111.4783,36.1615],
                            '临沂': [118.3118,35.2936],
                            '丹东': [124.541,40.4242],
                            '丽水': [119.5642,28.1854],
                            '乌鲁木齐': [87.9236,43.5883],
                            '佛山': [112.8955,23.1097],
                            '保定': [115.0488,39.0948],
                            '兰州': [103.5901,36.3043],
                            '包头': [110.3467,41.4899],
                            '北京': [116.4551,40.2539],
                            '北海': [109.314,21.6211],
                            '南京': [118.8062,31.9208],
                            '南宁': [108.479,23.1152],
                            '南昌': [116.0046,28.6633],
                            '南通': [121.1023,32.1625],
                            '厦门': [118.1689,24.6478],
                            '台州': [121.1353,28.6688],
                            '合肥': [117.29,32.0581],
                            '呼和浩特': [111.4124,40.4901],
                            '咸阳': [108.4131,34.8706],
                            '哈尔滨': [127.9688,45.368],
                            '唐山': [118.4766,39.6826],
                            '嘉兴': [120.9155,30.6354],
                            '大同': [113.7854,39.8035],
                            '大连': [122.2229,39.4409],
                            '天津': [117.4219,39.4189],
                            '太原': [112.3352,37.9413],
                            '威海': [121.9482,37.1393],
                            '宁波': [121.5967,29.6466],
                            '宝鸡': [107.1826,34.3433],
                            '宿迁': [118.5535,33.7775],
                            '常州': [119.4543,31.5582],
                            '广州': [113.5107,23.2196],
                            '廊坊': [116.521,39.0509],
                            '延安': [109.1052,36.4252],
                            '张家口': [115.1477,40.8527],
                            '徐州': [117.5208,34.3268],
                            '德州': [116.6858,37.2107],
                            '惠州': [114.6204,23.1647],
                            '成都': [103.9526,30.7617],
                            '扬州': [119.4653,32.8162],
                            '承德': [117.5757,41.4075],
                            '拉萨': [91.1865,30.1465],
                            '无锡': [120.3442,31.5527],
                            '日照': [119.2786,35.5023],
                            '昆明': [102.9199,25.4663],
                            '杭州': [119.5313,29.8773],
                            '枣庄': [117.323,34.8926],
                            '柳州': [109.3799,24.9774],
                            '株洲': [113.5327,27.0319],
                            '武汉': [114.3896,30.6628],
                            '汕头': [117.1692,23.3405],
                            '江门': [112.6318,22.1484],
                            '沈阳': [123.1238,42.1216],
                            '沧州': [116.8286,38.2104],
                            '河源': [114.917,23.9722],
                            '泉州': [118.3228,25.1147],
                            '泰安': [117.0264,36.0516],
                            '泰州': [120.0586,32.5525],
                            '济南': [117.1582,36.8701],
                             '济宁': [116.8286,35.3375],
                            '海口': [110.3893,19.8516],
                            '淄博': [118.0371,36.6064],
                            '淮安': [118.927,33.4039],
                            '深圳': [114.5435,22.5439],
                            '清远': [112.9175,24.3292],
                            '温州': [120.498,27.8119],
                            '渭南': [109.7864,35.0299],
                            '湖州': [119.8608,30.7782],
                            '湘潭': [112.5439,27.7075],
                            '滨州': [117.8174,37.4963],
                            '潍坊': [119.0918,36.524],
                            '烟台': [120.7397,37.5128],
                            '玉溪': [101.9312,23.8898],
                            '珠海': [113.7305,22.1155],
                            '盐城': [120.2234,33.5577],
                            '盘锦': [121.9482,41.0449],
                            '石家庄': [114.4995,38.1006],
                            '福州': [119.4543,25.9222],
                            '秦皇岛': [119.2126,40.0232],
                            '绍兴': [120.564,29.7565],
                            '聊城': [115.9167,36.4032],
                            '肇庆': [112.1265,23.5822],
                            '舟山': [122.2559,30.2234],
                            '苏州': [120.6519,31.3989],
                            '莱芜': [117.6526,36.2714],
                            '菏泽': [115.6201,35.2057],
                            '营口': [122.4316,40.4297],
                            '葫芦岛': [120.1575,40.578],
                            '衡水': [115.8838,37.7161],
                            '衢州': [118.6853,28.8666],
                            '西宁': [101.4038,36.8207],
                            '西安': [109.1162,34.2004],
                            '贵阳': [106.6992,26.7682],
                            '连云港': [119.1248,34.552],
                            '邢台': [114.8071,37.2821],
                            '邯郸': [114.4775,36.535],
                            '郑州': [113.4668,34.6234],
                            '鄂尔多斯': [108.9734,39.2487],
                            '重庆': [107.7539,30.1904],
                            '金华': [120.0037,29.1028],
                            '铜川': [109.0393,35.1947],
                            '银川': [106.3586,38.1775],
                            '镇江': [119.4763,31.9702],
                            '长春': [125.8154,44.2584],
                            '长沙': [113.0823,28.2568],
                            '长治': [112.8625,36.4746],
                            '阳泉': [113.4778,38.0951],
                            '青岛': [120.4651,36.3373],
                            '韶关': [113.7964,24.7028] 
                        },

                        markLine : {
                            smooth:true,
                            effect : {
                                show: true,
                                scaleSize: 1,
                                period: 30,
                                color: '#fff',
                                shadowBlur: 10
                            },
                            itemStyle : {
                                normal: {
                                    borderWidth:1,
                                    lineStyle: {
                                        type: 'solid',
                                        shadowBlur: 10
                                    }
                                }
                            },
                            data : [
                                [{name:'北京'}, {name:'上海',value:95}],
                                [{name:'北京'}, {name:'广州',value:90}],
                                [{name:'北京'}, {name:'大连',value:80}],
                                [{name:'北京'}, {name:'南宁',value:70}],
                                [{name:'北京'}, {name:'南昌',value:60}],
                                [{name:'北京'}, {name:'拉萨',value:50}],
                                [{name:'北京'}, {name:'长春',value:40}],
                                [{name:'北京'}, {name:'包头',value:30}],
                                [{name:'北京'}, {name:'重庆',value:20}],
                                [{name:'北京'}, {name:'常州',value:10}]
                            ]
                        },
                        markPoint : {
                            symbol:'emptyCircle',
                            symbolSize : function (v){
                                return 10 + v/10
                            },
                            effect : {
                                show: true,
                                shadowBlur : 0
                            },
                            itemStyle:{
                                normal:{
                                    label:{show:false}
                                }
                            },
                            data : [
                                {name:'上海',value:95},
                                {name:'广州',value:90},
                                {name:'大连',value:80},
                                {name:'南宁',value:70},
                                {name:'南昌',value:60},
                                {name:'拉萨',value:50},
                                {name:'长春',value:40},
                                {name:'包头',value:30},
                                {name:'重庆',value:20},
                                {name:'常州',value:10}
                            ]
                        }
                    },  
                    
                    <%
                     for(CustomerRegistry cr:crlist){
                    	StringBuilder databuffer=new StringBuilder(" "); 
                    	StringBuilder databuffer1=new StringBuilder(" ");
              	     	for(NetworkMapEntry ne:sr.getNetworkMapEntrys()){
                    		if(ne.getSource().equals(cr.getName())&&(!ne.getDestination().equals(sr.getDatacenterList().get(0).getName()))){
                    			String descName=map.get(crDAO.getCustomerRegistry(sr, ne.getDestination()).getLocation());
                    			databuffer.append("[{name:'"+map.get(cr.getLocation())+"'},{name:'"+descName+"',value:"+ne.getBandwidth()+"}],");
                    			databuffer1.append("{name:'"+descName+"',value:"+ne.getBandwidth()+"},");
                    		}
                    	}              	     
              	     	   
                    	 out.println(" {  name:'"+map.get(cr.getLocation())+"', type:'map', mapType: 'none', data:[], markLine : { smooth:true, effect :   "+
                    					" { show: true,  scaleSize: 1,  period: 30,color: '#fff', shadowBlur: 10 },  itemStyle : { "+
                    	 			"   normal: {  borderWidth:1,   lineStyle: {    type: 'solid',   shadowBlur: 10    }} },  "+
                    					"data : [ "+databuffer+" ]  },  markPoint : {  symbol:'emptyCircle', "+
                    	 			"  symbolSize : function (v){  return 10 + v/10  },  effect : { show: true,   shadowBlur : 0  "+
                    					"  }, itemStyle:{ normal:{  label:{show:false} }    },   data : ["+databuffer1+"]  } },");
                    
                    }                                      
                       
                    %>                    
                    
                    {
                        name:'广州',
                        type:'map',
                        mapType: 'none',
                        data:[],
                        markLine : {
                            smooth:true,
                            effect : {
                                show: true,
                                scaleSize: 1,
                                period: 30,
                                color: '#fff',
                                shadowBlur: 10
                            },
                            itemStyle : {
                                normal: {
                                    borderWidth:1,
                                    lineStyle: {
                                        type: 'solid',
                                        shadowBlur: 10
                                    }
                                }
                            },
                            data : [
                                [{name:'广州'},{name:'福州',value:95}],
                                [{name:'广州'},{name:'太原',value:90}],
                                [{name:'广州'},{name:'长春',value:80}],
                                [{name:'广州'},{name:'重庆',value:70}],
                                [{name:'广州'},{name:'西安',value:60}],
                                [{name:'广州'},{name:'成都',value:50}],
                                [{name:'广州'},{name:'常州',value:40}],
                                [{name:'广州'},{name:'北京',value:30}],
                                [{name:'广州'},{name:'北海',value:20}],
                                [{name:'广州'},{name:'海口',value:10}]
                            ]
                        },
                        markPoint : {
                            symbol:'emptyCircle',
                            symbolSize : function (v){
                                return 10 + v/10
                            },
                            effect : {
                                show: true,
                                shadowBlur : 0
                            },
                            itemStyle:{
                                normal:{
                                    label:{show:false}
                                }
                            },
                            data : [
                                {name:'福州',value:95},
                                {name:'太原',value:90},
                                {name:'长春',value:80},
                                {name:'重庆',value:70},
                                {name:'西安',value:60},
                                {name:'成都',value:50},
                                {name:'常州',value:40},
                                {name:'北京',value:30},
                                {name:'北海',value:20},
                                {name:'海口',value:10}
                            ]
                        }
                    }
                   
                                      
                ]
            };
                       

    	 var myChart = BMapExt.initECharts(container);
    	BMapExt.setOption(option); 	      
		  
    }
);


			  	
	
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
	<div id="topology" style="width:800px ;height:600px;    position:absolute;
    top:0px;
    left:50px;">
	

	</div>
</div>
</body>
</html>