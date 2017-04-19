Ext.define('KitchenSink.view.activity.applicants.applicantsList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.activity.applicants.applicantsStore',	
		'KitchenSink.view.activity.applicants.sendFormWin',
		'KitchenSink.view.activity.applicants.setStatusBatWin',
		'KitchenSink.view.activity.applicants.applicantsController'
    ],
    xtype: 'applicantsMg',
	controller: 'applicantsMg',
	store: {
		type: 'applicantsStore'
    },
	
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '活动报名人管理',
	//活动id
	appColumns: [],
	colStore:[],
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
	columns: [],
    dockedItems:[
		{xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveData"},
			   '-',{minWidth:80,text:"确定",iconCls:"ensure",handler:"onEnsure"},
			   '-',{minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
			   ]
		},
		{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchComList"},"->",
			//{text:"新增",tooltip:"新增数据",iconCls:"add"},"-",
			//{text:"编辑",tooltip:"编辑数据",iconCls: 'edit'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'发送函件',handler:'showSendWindow'
				},{
					text:'导出选中活动报名人信息',handler:'exportApplyInfo'
				},{
					text:'导出搜索结果中报名人信息',handler:'exportSearchApplyInfo'
				},{
					text:'批量更改参与状态',handler:"showSetStatusWindow"
				}]
			}
		]},{
				xtype: 'form',	
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 50,
					labelStyle: 'font-weight:bold'
				},
				items: [{
					xtype: 'hidden',
					fieldLabel: '',
					name: 'activityId'
				}]
			}
	],
	plugins: [
		Ext.create('Ext.grid.plugin.CellEditing',{
			clicksToEdit: 1
		})
	],
	
    initComponent: function () {   
		
		var columns = this.appColumns ;
		//报名状态store
		var bmztStore = new KitchenSink.view.common.store.appTransStore("TZ_BMR_APPLY_STA");
		//var bmztStore=bmztStore;
		//报名渠道store
		var bmqdStore = new KitchenSink.view.common.store.appTransStore("TZ_BMR_APPLY_QD");
		//参与状态store
		//var cyztStore = new KitchenSink.view.common.store.appTransStore("TZ_MBRGL_CYZT");
		var cyztStore = new KitchenSink.view.common.store.comboxStore({
				recname:'TZ_PT_ZHZXX_TBL',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_MBRGL_CYZT',
						operator:'01',
						type:'01'	
					},
					TZ_EFF_STATUS:{
						value:'A',
						operator:'01',
						type:'01'	
					}		
				},
				result: 'TZ_ZHZ_ID,TZ_ZHZ_CMS',
				listeners:{
					load:function(store,records,opt){
						var data = {"TZ_ZHZ_ID":"","TZ_ZHZ_CMS":""};
						var rs = [new Ext.data.Record(data)];
						store.insert(0,rs);	
					}	
				}
			});
		
		//grid的固定列
		var gdColumn = [{
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.applyStatus","报名状态"),
			sortable: true,
			dataIndex: 'applyStatus',
			width: 90,
			renderer: function(value,metadata,record){	
			 var index = bmztStore.find('TValue',value);   
                        if(index!=-1){   
                               return bmztStore.getAt(index).data.TSDesc;   
                        }   
                        return record.get('applyStatusDesc');  
				 
			} 
			
		},{
			text: '报名状态',
			dataIndex: 'applyStatusDesc',
			hidden:true
		},{
			text: '操作',
			width: 80,
			sortable: true,
			menuDisabled: true,
			resizable: false, 
			renderer: function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
				cellmeta.style = "padding:0px";	
				var _hdId,_bmrId,_sta;
				var _json = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"setRegSta","comParams":{';
				
				_hdId = record.get('activityId');
				_bmrId = record.get('applicantsId');
				_sta = record.get('applyStatus');

				_json = _json + '"activityId":"'+_hdId+'","applicantsId":"'+_bmrId+'","applyStatus":"'+_sta+'"}}';
	
				if (_sta=="1" || _sta=="4"){
					return "<input type='button' value='撤销' onclick=applyOrCancel(\'"+ _json +"\',\'"+ _sta +"\'); style='width:100%;height:25px;'/>";
				}else if(_sta=="2" || _sta=="3"){
					return "<input type='button' value='报名' onclick=applyOrCancel(\'"+ _json +"\',\'"+ _sta +"\'); style='width:100%;height:25px;'/>";
				}
			}
		},{
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.channel","报名渠道"),
			dataIndex: 'channel',
			width: 130,
			editor: {
				xtype: 'combo', 
				forceSelection: true,
				valueField: 'TValue',
            	displayField: 'TSDesc',
				typeAhead: true,
				mode:"remote",
				//store: new KitchenSink.view.common.store.appTransStore("TZ_BMR_APPLY_QD"),
				store: bmqdStore,
				//allowBlank: false
			},
			renderer:function(value,metadata,record){
				 //for (var i = 0; i < bmqdStore.data.items.length; i++) {
				//	  if (bmqdStore.data.items[i].data.TValue == val) {
				//		   return bmqdStore.data.items[i].data.TSDesc;
				//	  }
				// }
				var index = bmqdStore.find('TValue',value);   
				if(index!=-1){   
					   return bmqdStore.getAt(index).data.TSDesc;   
				}   
				return record.get('channelDesc');  
			}
		},{
			text: '报名渠道',
			dataIndex: 'channelDesc',
			hidden:true
		},{
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.signStatus","签到状态"),
			dataIndex: 'signStatus',
			width: 130,
			editor:{
				xtype: 'combo',
				forceSelection: true,
				valueField: 'TZ_ZHZ_ID',
            	displayField: 'TZ_ZHZ_CMS',
				mode:"remote",
				store: cyztStore,
				tpl:'<tpl for=".">' +   
					'<div class="x-boundlist-item" style="height:30px;">' +   
					'{TZ_ZHZ_CMS}' +   
					'</div>'+   
					'</tpl>' 
			},
			renderer:function(value,metadata,record){
				if(value == null || value==""){
					return "";	
				}
				var index = cyztStore.find('TZ_ZHZ_ID',value);   
				if(index!=-1){   
					   return cyztStore.getAt(index).data.TZ_ZHZ_CMS;   
				}   
				return record.get('signStatusDesc'); 
				 
			}
		},{
			text: '签到状态',
			dataIndex: 'signStatusDesc',
			hidden:true
		},{
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.remark","备注"),
			dataIndex: 'remark',
			minWidth: 160,
			flex: 1,
			editor:{
				xtype:'textfield'	
			}	
		},{
			xtype: 'checkcolumn',
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.isReg","是否注册"),
			dataIndex: 'isReg',
			disabled: false,
			sortable: true,
			width: 90,
			align: 'center',
			hidden:true,
			listeners: {
				"beforecheckchange": function(){
					return false;
				}
			}
		},{
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.applyTime","报名时间"),
			sortable: true,
			dataIndex: 'applyTime',
			//formatter: 'date("Y-m-d H:i:s")',
			width: 160
		},{
			text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_BMRGL_STD.signCode","活动签到码"),
			sortable: true,
			dataIndex: 'signCode',
			width: 120,
		},{
		   //menuDisabled: true,
		   text:'二维码门票',
		   sortable: false,
		   width:100,
		   xtype: 'actioncolumn',
		   menuText: '二维码门票',
		   align:'center',
		   hidden:true,
		   items:[
			 {text: '查看',iconCls: 'view',tooltip: '查看'}
		   ]
		}];
	
		for (var i=0; i<gdColumn.length; i++){
			columns.push(gdColumn[i]);	
		}

       Ext.apply(this, {
			columns:columns,
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 500,
				listeners:{
					afterrender: function(pbar){
						var grid = pbar.findParentByType("grid");
						pbar.setStore(grid.store);
					}
				},
				plugins: new Ext.ux.ProgressBarPager()
			}	   
		});
	   
	 	this.callParent();
    },
	
	constructor: function(appColumns,colStore){
		this.appColumns = appColumns;
		this.colStore = colStore;
		this.callParent();
	}
});


//报名、撤销报名
function applyOrCancel(jsonParams,sta){
	if (jsonParams != '' || jsonParams != null || jsonParams != undefined){
		var msg = "";
		if (sta == "1" || sta == "4"){
			msg = "撤销成功！";
		} else {
			msg = "报名成功！";
		}
		Ext.tzSubmit(jsonParams,function(respDate){	
			gridPanel = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
			gridPanel.getStore().reload();
		},msg,true,this);
	}
}
