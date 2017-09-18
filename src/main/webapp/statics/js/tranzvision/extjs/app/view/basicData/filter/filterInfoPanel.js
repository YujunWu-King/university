    Ext.define('KitchenSink.view.basicData.filter.filterInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'filterInfoPanel',
	controller: 'filterInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.filter.filterInfoStore',
		'KitchenSink.view.basicData.filter.FldDataSetStore',
        'KitchenSink.view.basicData.filter.filterInfoController',
        'tranzvision.extension.grid.column.Link'
	],
    title: '可配置搜索配置',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'filterForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: '组件编号',
            readOnly:true,
			cls:"lanage_1",
            name: 'ComID'
        }, {
            xtype: 'textfield',
            fieldLabel: '组件名称',
            readOnly:true,
			cls:"lanage_1",
            name: 'comMc'
        }, {
            xtype: 'textfield',
            fieldLabel: '页面编号',
            readOnly:true,
			cls:"lanage_1",
            name: 'PageID'
        }, {
            xtype: 'textfield',
            fieldLabel: '页面名称',
            readOnly:true,
			cls:"lanage_1",
            name: 'pageMc'
        }, {
            xtype: 'textfield',
            fieldLabel: '视图名称',
            readOnly:true,
			cls:"lanage_1",
            name: 'ViewMc'
        },{
            xtype: 'textfield',
            fieldLabel: '返回搜索结果最大行数',
            name: 'maxNum'
        },{
            xtype: 'checkboxfield',
            //fieldLabel: '默认进入高级搜索模式',
            boxLabel: '<span style="font-weight:bold;">默认进入高级搜索模式</span>',
            margin: '0 0 5 170',
            hideLabel: true,
            inputvalue:'Y',
			name: 'advModel'
        },{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">基本搜索模式下搜索条件操作运算符不可编辑</span>',
            margin: '0 0 5 175',
            hideLabel: true,
            inputvalue:'Y',
			name: 'baseSchEdit',
			hidden:true
        }/*,{
        	 xtype: 'component',
        	 html:'<a href="javascript:void(0)" style="text-decoration:none">' + '<span style="font-weight:bold;">高级设置</span>' + '</a>',
             margin: '0 0 0 175',
	         listeners:{
	            	click:{
	            		 element: 'el', //bind to the underlying el property on the panel
	            		 fn: ''
	            	}
	            }
        }*/]
    },
	{
	    xtype: 'tabpanel',
		frame: true,
		style:"margin:10px",
		items:[{
			xtype: 'grid',
			height: 360,
			title: '搜索条件字段列表',
			name:"filterGrid",
			columnLines: true,
			dockedItems:{
					xtype:"toolbar",
					items:[
						{text:"新增",tooltip:"新增",iconCls:"add",handler:'addFld'},"-",
						{text:"新增DeepQuery字段",tooltip:"新增DeepQuery字段",iconCls:"add",handler:'addDqFld'},"-",
						{text:"编辑",tooltip:"编辑",iconCls:"edit",handler:'editSelFld'},"-",
						{text:"删除",tooltip:"删除",iconCls:"remove",handler:'deleteFlds'}
					]
			},
			selModel: {
					type: 'checkboxmodel'
			},
				reference: 'filterGrid',
				multiSelect: true,
				//style:"margin:10px",
				store: {
					type: 'filterInfoStore'
				},
				viewConfig: {
					plugins: {
						ptype: 'gridviewdragdrop',
						containerScroll: true,
						dragGroup: this,
						dropGroup: this
					},
					listeners: {
						drop: function(node, data, dropRec, dropPosition) {
							data.view.store.beginUpdate();
							var items = data.view.store.data.items;
							for(var i = 0;i< items.length;i++){
								items[i].set('orderNum',i+1);
							}
							data.view.store.endUpdate();
						}
					}
				},
				columns: [{
					text: '组件编号',
					dataIndex: 'ComID',
					hidden: true
				},
				{
					text: '页面编号',
					dataIndex: 'PageID',
					hidden: true
				},
				{
					text: '视图',
					dataIndex: 'ViewMc',
					hidden: true
				},{
					text: '序号',
					dataIndex: 'orderNum',
					width: 55
				},{
					text:'字段名称',               
					sortable: true,
					dataIndex: 'FieldMc',
								width: 160
								/*
								renderer: function(val, mataData, record){
									//return '<a href="#" onclick="editFilterFldInfo(\''+ record.data.projectId +'\')">'+ val +'</a>';
									return '<a href="#" onclick="editFilterFldInfo(\''+ record.data.ComID +'\',\''+ record.data.PageID +'\',\''+ record.data.ViewMc +'\',\''+ record.data.FieldMc +'\')">'+ val +'</a>';	
								}
								*/
				},{
					text: '字段描述',
					dataIndex: 'fieldDesc',
					width: 130
				},{
					xtype: 'checkcolumn',
					text: '只读',
					dataIndex: 'fldReadonly',
					width: 80,
					disabled:true
					/*listeners: {
						checkchange: function(item, rowIndex, checked, eOpts ){
							var grid = item.findParentByType("grid");
							//页面注册信息数据
							var store = grid.getStore();
							var _r = store.getAt(rowIndex); 
							var index = store.findBy(function (record) {
								return record.get('fldReadonly') == true && record.id != _r.id;	
							});
							if(checked){
								if (index >= 0) {
									store.getAt(index).set('fldReadonly', false);
								}
							}else{
								if(index < 0){
									store.getAt(0).set('fldReadonly', true);
								}
							}
						}	
					}*/
				},{
					xtype: 'checkcolumn',
					text: '隐藏',
					dataIndex: 'fldHide',
					width: 80,
					disabled:true
					/*listeners: {
						checkchange: function(item, rowIndex, checked, eOpts ){
							var grid = item.findParentByType("grid");
							//页面注册信息数据
							var store = grid.getStore();
							var _r = store.getAt(rowIndex); 
							var index = store.findBy(function (record) {
								return record.get('fldHide') == true && record.id != _r.id;	
							});
							if(checked){
								if (index >= 0) {
									store.getAt(index).set('fldHide', false);
								}
							}else{
								if(index < 0){
									store.getAt(0).set('fldHide', true);
								}
							}
						}	
					}*/
				},{
					text: 'Prompt表名称',
					dataIndex: 'promptTab',
					width: 130
				},{
					text: 'Prompt字段名称',
					dataIndex: 'promptFld',
					Width: 120,
					flex: 1
				},{
					text: '是否DeepQuery字段',
					dataIndex: 'deepQueryFlgDesc',
					Width: 200,
					flex: 1
				},{
					text: '是否DeepQuery字段',
					dataIndex: 'deepQueryFlg',
					hidden: true
				},{
					text: 'DeepQuery视图',
					dataIndex: 'deepQueryView',
					hidden: true
				},{
					text: 'DeepQuery字段',
					dataIndex: 'deepQueryFld',
					hidden: true
				},{
				   menuDisabled: true,
				   sortable: false,
				   width:60,
				   xtype: 'actioncolumn',
				   items:[
					  {iconCls: 'edit',tooltip: '编辑',handler: 'editFld'},
					  /** '-',  **/
					  {iconCls: 'remove',tooltip: '删除',handler: 'deleteFLd'}
				   ]
				}],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 5,
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			},{
	        	xtype: 'grid',
	        	name:"dataSetGrid",
				minHeight: 360,
				maxHeight: 360,
				autoScroll: true,
				title: '数据集',
				columnLines: true,
		        dockedItems:{
		            xtype:"toolbar",
		            items:[
		                {text:"新增",tooltip:"新增",iconCls:"add",handler:'addDataSetFld'},"-",
						{text:"编辑",tooltip:"编辑",iconCls:"edit",handler:'editDataSetSelFld'},"-",
		                {text:"删除",tooltip:"删除",iconCls:"remove",handler:'deleteDataSetFlds'}
		            ]
		        },
		        selModel: {
		            type: 'checkboxmodel'
		        },
		        multiSelect: true,
				store: {
					type: 'FldDataSetStore'
				},
				/*
				plugins: {
	                ptype: 'cellediting',
	                pluginId: 'fldCellEditing',
	                clicksToEdit: 1
	            },
	            viewConfig: {
					plugins: {
						ptype: 'gridviewdragdrop',
						containerScroll: true,
						dragGroup: this,
						dropGroup: this
					},
					listeners: {
						drop: function(node, data, dropRec, dropPosition) {
							data.view.store.beginUpdate();
							var items = data.view.store.data.items;
							for(var i = 0;i< items.length;i++){
								items[i].set('fieldOrder',i+1);
							}
							data.view.store.endUpdate();
						}
					}
				},*/
				columns: [{
		            text: '组件编号',
		            dataIndex: 'ComID',
		            hidden: true
		        },{
		            text: '页面编号',
		            dataIndex: 'PageID',
		            hidden: true
		        },{
		            text: '视图',
		            dataIndex: 'ViewMc',
		            hidden: true
		        },{
		            text: '序号',
		            dataIndex: 'fieldOrder',
		            width: 70
		        },{
		            text: '关联字段',
		            dataIndex: 'dataSetFld',
					width: 150
		        },{
		            text: '搜索记录名称',
		            dataIndex: 'searchRec',
					width: 200
		        },{
		            text: '描述',
		            dataIndex: 'fldDstDesc',
					flex:1
		        },{
					xtype: 'checkcolumn',
					text: '默认',
					dataIndex: 'fldDstDefault',
					width: 80,
					disabled:true
		        },{
		            text: '有效状态',
		            dataIndex: 'fldDstStatus',
		            width: 100
		        },{
				   menuDisabled: true,
				   sortable: false,
				   width:60,
				   xtype: 'actioncolumn',
				   items:[
					  {iconCls: 'edit',tooltip: '编辑',handler: 'editDataSetFld'},
					  /** '-',  **/
					  {iconCls: 'remove',tooltip: '删除',handler: 'deleteDataSetFLd'}
				   ]
				}],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 5,
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			}
		]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFilterInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFilterInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFilterInfoClose'
	}]
});


/*
function editFilterFldInfo(ComID,PageID,ViewMc,FieldMc){
	
	//是否有访问权限
	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FILTER_FLD_STD"];
	if( pageResSet == "" || pageResSet == undefined){
		Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		return;
	}
	//该功能对应的JS类
	var className = pageResSet["jsClassName"];
	if(className == "" || className == undefined){
		Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FILTER_FLD_STD，请检查配置。');
		return;
	}
	var contentPanel, cmp, ViewClass, clsProto;
		
	contentPanel = Ext.getCmp('tranzvision-framework-content-panel');		
	contentPanel.body.addCls('kitchensink-example');
	
	if(!Ext.ClassManager.isCreated(className)){
		Ext.syncRequire(className);
	}	
	ViewClass = Ext.ClassManager.get(className);
	clsProto = ViewClass.prototype;

	if (clsProto.themes) {
		clsProto.themeInfo = clsProto.themes[themeName];

		if (themeName === 'gray') {
			clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
		} else if (themeName !== 'neptune' && themeName !== 'classic') {
			if (themeName === 'crisp-touch') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
			}
			clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
		}
		// <debug warn>
		// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
		if (!clsProto.themeInfo) {
			Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
				themeName + '\'. Is this intentional?');
		}
		// </debug>
	}

	cmp = new ViewClass();
	cmp.actType="update";
	
	cmp.on('afterrender',function(panel){
		var form = panel.child('form').getForm();
            form.findField("FieldMc").setReadOnly(true);
            //资源信息列表
            var grid = panel.child('form').child('tabpanel').getActiveTab();
            //参数
            var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FILTER_FLD_STD","OperateType":"QF","comParams":{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+FieldMc+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                //资源集合信息列表数据
                var roleList = responseData.listData;
            });
            var queryID;
			if(grid.name == "searchFld"){
				queryID = "1";
			}
			if(grid.name == "promptFld"){
				queryID = "2";
			}
			var tzStoreParams = '{"queryID":"' + queryID + '","ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+FieldMc+'"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
	});
	
	tab = contentPanel.add(cmp);     
	
	contentPanel.setActiveTab(tab);

	Ext.resumeLayouts(true);

	if (cmp.floating) {
		cmp.show();
	}	
}
*/