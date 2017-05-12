Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.showMsTBPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'showMsTB',
    controller: 'appFormClass',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.chart.*',
        'Ext.Window', 
        'Ext.layout.container.Fit',
        'Ext.fx.target.Sprite', 
        'Ext.window.MessageBox',
        'Ext.grid.filters.Filters'
    ],
    bodyPadding:10,
    scrollable: true,
    constructor: function (obj){
    			this.coluObj = obj.coluObj;
    			this.lineObj = obj.lineObj;
				//console.log(this.coluObj);
				//console.log(this.lineObj);
				//this.classID=obj.classID;
				this.callParent();
    },
    initComponent:function(){
 
				var coluObj = this.coluObj;
				var lineObj = this.lineObj;
				
				console.log("000000");
				console.log(coluObj);
    			/*
    			// 曲线图参数
				var lineObj = [ 
				                {'pw' : 'zhangsan',	'n1' :10,'n2' : 20,'n3' : 30,'n4' : 40,'n5' : 40,'n6' : 40}, 
				                {'pw' : 'lisi',     'n1' :15,'n2' : 25,'n3' : 35,'n4' : 45,'n5' : 40,'n6' : 40},
				                {'pw' : 'wangwu',   'n1' :40,'n2' : 30,'n3' : 20,'n4' : 10,'n5' : 40,'n6' : 40},
				                {'pw' : '平均',     'n1' :22,'n2' : 25,'n3' : 30,'n4' : 33,'n5' : 40,'n6' : 40}
				              ];
				// 柱状图参数
				var coluObj = [
				               {'pw' : 'zhangsan', 'pj' : 10},
				               {'pw' : 'lisi',     'pj' : 15},
				               {'pw' : 'wangwu',   'pj' : 40}, 
				               {'pw' : '平均',     'pj' : 22} 
				               ];
				*/
    	
				// 评委数组
				// var pwArr = [ 'data1', 'data2', 'data3' ];
				var pwArr = [];
				// 区间数组
				// var qjArr = [ 'n1', 'n2', 'n3', 'n4' ];
				var qjArr = [];
				//生成评委数组、区间数组
				for (var i = 0; i < lineObj.length; i++) {
					// 生成选择的评委数组
					var person = lineObj[i];
					var personCols = Object.keys(person);
					pwArr.push(person['pw']);
					// 生成区间数组
					if (i == 0) {
						for (var j = 0; j < personCols.length; j++) {
							var col = personCols[j];
							if (col != 'pw') {
								qjArr.push(col);
							}
						}
					}

				}

				// 变量定义 - 曲线图部分
				var arrayLines = []; // 创建一个空数组,用于存放曲线的图例;
				var arrayNameLines = []; // 创建一个空数组,用于存放曲线的图例（包括x坐标）;
				arrayNameLines.push("name");
				var arrayDatas = []; // 创建一个空数组,用于存放曲线数据（包括x坐标）;
				var arraySeries = []; // 创建一个空数组,用于存放曲线;
				// 变量定义 - 柱状图部分
				var arrayColumDatas = []; // 柱状图数据;

				// -----------------根据评委个数、区间个数设置页面的高度、宽度-----------------------------
				var pwNum = pwArr.length;// 评委个数
				var qjNum = qjArr.length;// 指标区间个数

				var columnWidth = 1300;// 柱状图宽度;
				if (((pwNum + 1) * 100) > 1300) {
					columnWidth = (pwNum + 1) * 100;
				}
				var columnHeight = 400;// 柱状图高度;

				var lineWidth = 1300; // 曲线图宽度;
				if ((qjNum * 150) > 1300) {
					lineWidth = qjNum * 200;
				}
				var lineHeigth = 400; // 曲线图高度;
				lineHeigth = (pwNum + 1) * 20 + 400;

				// 保持柱状图和线状图宽度一致
				if (columnWidth > lineWidth) {
					//lineWidth = columnWidth;
				} else {
					//columnWidth = lineWidth;
				}
				// 对曲线图增加点宽度;
				lineWidth = lineWidth + (pwNum + 1) * 66; 
				
				
				// ------------------------------------柱状图开始---------------------------
				// 1、生产柱状图数据
				for (var i = 0; i < pwArr.length; i++) {
					// 循环每个评委
					var pw = pwArr[i];
					var datatmp = {};
					datatmp['graphName'] = pw;
					// 从传递过来的数字里面取评委对应的数据;
					var pjs = 0;
					for (var j = 0; j < coluObj.length; j++) {
						if (pw == coluObj[j]['pw']) {
							pjs = coluObj[j]['pj']
						}
					}
					datatmp['graphData'] = pjs;

					arrayColumDatas.push(datatmp);
				}
				// 2、曲线图数据集
				var graphDataStore = Ext.create('Ext.data.Store', {
					fields : [ 'graphName', 'graphData' ],
					data : arrayColumDatas
				});

				// var colors = ['#6E548D','#94AE0A','#FF7348','#3D96AE'];
				// 动态定义曲线图id
				var id1 = "" + Math.ceil(Math.random() * 35);
				// 定义曲线图
				var columnChart = Ext.create('Ext.chart.Chart', {
					xtype : 'chart',
					id : 'columnChart' + id1,
					width : columnWidth,
					//height : columnHeight,
					height : 230,
					animate : true,// 使用动画
					store : graphDataStore,
					shadow : true,// 使用阴影
					axes : [ {// x轴与y轴的声明
						type : 'Numeric',
						position : 'left',
						title : '平均分',
						minimum : 0,
						maximum : 100,
						grid : true
					}, {
						type : 'Category',
						position : 'bottom',
						fields : 'graphName',
						title : '评委列表'
					} ],
					series : [ {
						type : 'column',
						axis : 'bottom',
						style: { width: 50 },
						xField : 'graphName',
						yField : 'graphData',// x与y轴的数据声明
					} ]
				});
				// ------------------------------------柱状图结束---------------------------

				// ------------------------------------曲线图开始---------------------------
				// 处理曲线图的数据-开始
				// 1、获取每个区间的数据-开始
				for (var i = 0; i < qjArr.length; i++) {
					// 循环每个区间
					var qj = qjArr[i];// 区间
					var lineData = {};
					lineData["name"] = qjArr[i];
					for (var j = 0; j < pwArr.length; j++) {
						// 循环每个评委
						var pw = pwArr[j];
						// 从传递过来的数组里面取评委对应的数据;
						var qjdata = 0;
						for (var k = 0; k < lineObj.length; k++) {
							if (pw == lineObj[k]['pw']) {
								qjdata = lineObj[k][qj]
							}
						}
						lineData[pw] = qjdata;
					}
					arrayDatas.push(lineData);

				}
				;
				// 获取每个区间的数据-结束

				// 2、循环生成曲线-开始
				for (var k = 0; k < pwArr.length; k++) {
					// 循环每个评委
					var pwTmp = pwArr[k];
					var SerieTmp = {
						type : 'line',
						highlight : {
							size : 7,
							radius : 7
						},
						tips : {
							trackMouse : true,
							width : 300,
							height : 28,
							renderer : function(storeItem, item) {
								this.setTitle('评委：' + item.series.yField
										+ '，分布区间：' + storeItem.get('name')
										+ '，分布比率 ' + item.value[1]);
							}
						},
						axis : 'left',
						fill : false,
						xField : 'name',
						yField : pwTmp,
						markerConfig : {
							type : 'circle',
							size : 4,
							radius : 4,
							'stroke-width' : 0
						},
						smooth : true
					};
					arraySeries.push(SerieTmp);

					arrayLines.push(pwTmp);
					arrayNameLines.push(pwTmp);

				}
				;
				// 循环生成曲线-结束
				// 处理曲线图的数据-开始

				// 曲线图定义部分-开始
				// 1、曲线图的数据源
				var mystore = Ext.create('Ext.data.JsonStore', {
					fields : arrayLines,
					data : arrayDatas
				});
				// 2、动态定义曲线图的id
				var id2 = "" + Math.ceil(Math.random() * 35);
				// 3、生成曲线图
				var lineChart = Ext.create('Ext.chart.Chart', {
					xtype : 'chart',
					style : 'background:#fff',
					//height : lineHeigth,
					height : 440,
					// id: 'linechart',
					id : 'lineChart' + id2,
					width : lineWidth,
					animate : true,
					// insetPadding: 20,
					store : mystore,
					shadow : true,
					theme : 'Category1',
					layout : 'fit',
					axes : [ {
						type : 'Numeric',
						minimum : 0,
						position : 'left',
						fields : arrayLines,
						title : '分布比率（%）',
						minimum : 0,
						maximum : 100,
						minorTickSteps : 1,
						grid : {
							odd : {
								opacity : 1,
								fill : '#ddd',
								stroke : '#bbb',
								'stroke-width' : 0.5
							}
						}
					}, {
						type : 'Category',
						position : 'bottom',
						fields : [ 'name' ],
						title : '分布区间'
					} ],
					series : arraySeries,
					legend : {
						position : 'top'
					}
				});
				// 曲线图定义部分-结束
				// ------------------------------------曲线图结束---------------------------

				// 整个页面定义部分-开始
				Ext.apply(this, {
					xtype : 'form',
					frame : true,
					name : 'mypanel',
					// autoGenId:false,
					// id:'mypanel',
					activeTab : 0,
					plain : false,
					autoScroll : true,
					bodyStyle : 'overflow-x:scroll; overflow-y:scroll',
					// height: chartHeigth,
					// width: chartWidth,
					style : 'margin:0 10px 0 10px',
					resizeTabs : true,
					items : []
				});
				// 整个页面定义部分-结束

				// 填充页面部分-开始
				// this.removeAll();
				this.items.push(columnChart);
				this.items.push(lineChart);
				this.doLayout();
				// 填充页面部分-结束

				this.callParent();
			},
    title: "查看图表"
});
