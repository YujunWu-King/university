Ext.define('KitchenSink.view.enrollProject.userApply.AdmissionAndPayInfoWindow', {
    extend: 'Ext.window.Window',
    requires: [
    	'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userApply.viewJFPlanStore',
		//'KitchenSink.view.enrollProject.userApply.viewAdmissionAndPayController'
	],
	reference: 'admissionAndPayInfoWindow',
    xtype: 'admissionAndPayInfoWindow',
	//controller:'viewAdmissionAndPayController',
	width: 800,
	height: 550,
	minWidth: 600,
	minHeight: 400,
    //title: '',
	layout: 'fit',
	resizable: false,
	modal: true,
	closeAction: 'hide',
	
	constructor: function(){
		
		this.callParent();	
	},
	initComponent: function () {
		var store = new KitchenSink.view.enrollProject.userApply.viewJFPlanStore();
		var tagStore1 = new Ext.data.Store({
 		   fields:['tagName','tagId'],
 		   data:[]
 	   });
        Ext.apply(this,{
		    items: [{
		        xtype: 'form',
		        border: false,
		        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				layout: {
			        type: 'vbox',       // Arrange child items vertically
			        align: 'stretch'    // 控件横向拉伸至容器大小
			    },
		        fieldDefaults: {
		            msgTarget: 'side',
		            //labelWidth: 120,
					labelSeparator:'',
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		        	xtype: 'textfield',
		            fieldLabel: '报名号',
		            name: 'appIns',
		            hidden:true
		        },{
		        	xtype: 'textfield',
		            fieldLabel: '姓名',
		            name: 'stuName',
					readOnly:true
		        },{
		        	xtype: 'textfield',
		            fieldLabel: '报考班级',
		            name: 'bkClass',
                    readOnly:true
		        },{
		        	xtype: 'textfield',
		            fieldLabel: '录取状态',
		            name: 'lqStatus',
                    readOnly:true
		        },{
		        	xtype: 'textfield',
		            fieldLabel: '主要负责人',
		            name: 'mainFZR',
                    readOnly:true
		        },{
		        	xtype: 'textfield',
		            fieldLabel: '所属班级',
		            name: 'sSClass',
                    readOnly:true
		        },{
		           xtype: 'combobox',
  	        	   fieldLabel:'学籍状态',
  	        	   //emptyText:'请选择',
  	        	   queryMode: 'remote',
  	        	   name: 'xjStatus',
  	        	   valueField: 'TValue',
  	        	   displayField: 'TSDesc',
  	        	   store: new KitchenSink.view.common.store.appTransStore("TZ_XJ_STATUS"),
                    readOnly:true
		        },{
		        	xtype: 'textfield',
		            fieldLabel: '学费标准',
		            name: 'xfStatus',
                    readOnly:true
		        },{   
 				   xtype:'tagfield',
				   fieldLabel:'获得奖学金',
				   name:'jxjList',
				   anyMatch:true,
				   filterPickList: true,
				   createNewOnEnter: true,
				   createNewOnBlur: false,
				   enableKeyEvents: true,
				   ignoreChangesFlag:true,
				   store: tagStore1,
				   valueField: 'tagId',
				   displayField: 'tagName',
				   readOnly:true,
				   triggers: {
					   search: {
						   cls: 'x-form-search-trigger',
						   handler: "searchJxjList"
					   }
				   }
			   },{
					xtype: 'textfield',
		            fieldLabel: '应缴金额',
		            name: 'yingJJine',
                    readOnly:true
					
				},{
					xtype: 'textfield',
		            fieldLabel: '已缴金额',
		            name: 'yiJJine',
                    readOnly:true
				},{
		        	xtype: 'grid',
		        	frame: true,
		        	multiSelect: true,
		        	height: 160,
		            viewConfig: {
		                enableTextSelection: true
		            },
		        	reference: 'admissionAndPayInfoGrid',
		            /*plugins: [{
                        ptype: 'gridfilters'
		            }],*/
		        	/*dockedItems:[{
						xtype:"toolbar",
						items:[
                           {text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addRecord"},"-",
                           {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelectedRecord'}
						]
					}],*/
					columnLines: true,    //显示纵向表格线
					selModel:{
						type: 'checkboxmodel'
					},
					store: store,
					columns: [{
						text: '缴费计划主键',
	                    dataIndex: 'TZ_JFPL_ID',
	                    width:100,
	                    hidden:true
					},{
	                    text: '截止缴费日期',
	                    dataIndex: 'TZ_JF_DATE',
	                    width:100
	                },{
	                    text:'本期学费标准',
	                    dataIndex: 'TZ_JF_BZ_JE',
	                    minWidth:100,
	                    width:150,
	                    align:'right',
	        			renderer:function(value){
	        				   return getNum(value)
	        			}
	                },{
	                    text:'本期学费调整' ,
	                    dataIndex: 'TZ_JF_TZ_JE',
	                    minWidth: 100,
	                    align:'right',
	        			renderer:function(value){
	        				   return getNum(value)
	        			}
	                },{
	                    text:'占比',
	                    dataIndex: 'zb',
	                    minWidth: 80,
	                    flex:1
	                },{
	                    text:'奖学金减免金额',
	                    dataIndex: 'TZ_JF_JM_JE',
	                    minWidth: 100,
	                    flex:1,
	                    align:'right',
	        			renderer:function(value){
	        				   return getNum(value)
	        			}
	                },{
	                	text:'本期实退',
	                    dataIndex: 'TZ_JF_BQYT',
	                    width: 100,
	                    minWidth: 80,
	                    align:'right',
	        			renderer:function(value){
	        				   return getNum(value)
	        			}
	                },{
	                	text:'本期应收',
	                    dataIndex: 'TZ_JF_BQYS',
	                    width: 100,
	                    minWidth: 80,
	                    align:'right',
	        			renderer:function(value){
	        				   return getNum(value)
	        			}
	                },{
	                	text:'本期实收',
	                    dataIndex: 'TZ_JF_BQSS',
	                    width: 100,
	                    minWidth: 80,
	                    align:'right',
	        			renderer:function(value){
	        				   return getNum(value)
	        			}
	                },{
	                	text:'付款状态',
	                    dataIndex: 'TZ_JF_STAT',
	                    width: 100,
	                    minWidth: 80,
	                    renderer:function(value){
	                    	if(value=='1'){
 							   return '未缴费';
 						   }else if(value=='2'){
 							   return '部分缴费';
 						   }else{
 							   return '已缴费';
 						   }
	        			 }
	                	
	                }/*,{
	        			   xtype: 'actioncolumn',
	        			   text: '操作',	
	        			   menuDisabled: true,
	        			   menuText: '操作',
	        			   sortable: false,
	        			   flex:0.5,
	        			   "with":50,
	        			   align: 'center',
	        			   items:[
	        			          {text: '删除',iconCls: 'remove',tooltip: '删除',handler:'deleteCurrRec'}
	        			          ]
	        		   }*/],
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 50,
						store: store,
						displayInfo: true,
						plugins: new Ext.ux.ProgressBarPager()
					}
		        }]
			}]
        });
        this.callParent();
    },
    buttons: [/*{
		text: '确定',
		iconCls:"ensure",
		handler: 'onWindowEnsure'
	},*/{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});
function getNum(str) {
	if(typeof(str)=='string'){
		console.log("===========",str)
		str = parseFloat(str);

		console.log("type",typeof(str));
	}
	if(JSON.stringify(str)=="null"|| isNaN(str)){
		str =  0;
	}
	str = str.toFixed(2);//保留两位
	str = parseFloat(str);//转成数字
	str = str.toLocaleString();//转成金额显示模式
	//判断是否有小数
	if(str.indexOf(".")==-1){
		str = str+".00";
	}else{
		str = str.split(".")[1].length<2?str+"0":str;
	}
	return str;
}