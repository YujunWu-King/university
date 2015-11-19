    Ext.define('KitchenSink.view.common.drExcel.drExcelInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'drExcelInfoPanel',
	controller: 'drExcel',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.drExcel.drExcelStore',
        'KitchenSink.view.common.drExcel.drExcelController',
        'tranzvision.extension.grid.column.Link'
	],
    title: '导入记录',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'drExcelForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        ignoreLabelWidth: true,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: '任务描述',
            readOnly:true,
            name: 'taskDesc'
        }, {
            xtype: 'textfield',
            fieldLabel: '进程实例号',
            readOnly:true,
            name: 'ProcessID'
        }, /*{
            xtype: 'textfield',
            fieldLabel: '状态',
            readOnly:true,
            name: 'drState'
        },*/{
            xtype: 'combobox',
            fieldLabel: '状态',
            editable:false,
            //emptyText:'请选择',
            queryMode: 'remote',
			name: 'drState',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		readOnly:true,
    		store: new KitchenSink.view.common.store.appTransStore("TZ_JCZT")
        },{
            xtype: 'textfield',
            fieldLabel: '开始时间',
            readOnly:true,
            name: 'startTime'
        }, {
            xtype: 'textfield',
            fieldLabel: '结束时间',
            readOnly:true,
            name: 'endTime'
        },{
            xtype: 'textfield',
            fieldLabel: '总记录数（行）',
            readOnly:true,
            name: 'totalNum'
        }, {
            xtype: 'textfield',
            fieldLabel: '成功记录数（行）',
            readOnly:true,
            name: 'successNum'
        }, {
            xtype: 'textfield',
            fieldLabel: '失败记录数（行）',
            readOnly:true,
            name: 'faildNum'
        },{
           	xtype: 'displayfield', 
           	fieldLabel: '日志信息',
		   	value:'<a href="javascript:void(0)" onclick="openProcess()">详细信息</a>'
        },{
			xtype: 'container',
			layout: {
			    type: 'hbox'
			},
			items:[{
				xtype:'button',
				margin:'0 20 0 5',
				width:100,
				text:'刷新',
				tooltip:'刷新',
				handler:function(btn) {
					var form = btn.findParentByType("container").findParentByType("form");
					var formParams = form.getForm().getValues();
					var ProcessID = formParams['ProcessID'];
					var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_IMPORTEXCEL_STD","OperateType":"QF","comParams":{"ProcessID":"'+ProcessID+'"}}';
			
			        Ext.tzLoad(tzParams,function(responseData){
						//组件注册信息数据
						var formData = responseData.formData;
						form.getForm().setValues(formData);

						var grid = form.nextSibling();
						grid.store.reload();
					});
			    }
			},{
				xtype:'button',
				text:'终止任务',
				width:100,
				tooltip:'终止任务',
				handler: function(btn) {
					var form = btn.findParentByType("form");
					var formParams = form.getForm().getValues();
					var ProcessID = formParams['ProcessID'];
			        var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_IMPORTEXCEL_STD","OperateType":"U","comParams":{"update":[{"ProcessID":"'+ProcessID+'"}]}}';
			        
					Ext.tzLoad(tzParams,function(responseData){
						//组件注册信息数据
						var formData = responseData.formData;
						form.getForm().setValues(formData);
						
						var grid = form.nextSibling();
						grid.store.reload();
					});
			    }
			}]
		}]
    },{
		xtype: 'grid',
		height: 360,
		//title: '搜索条件字段列表',
		margin:10,
		frame: true,
		columnLines: true,
        dockedItems:{
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchExcel'}
            ]
        },
		reference: 'drExcelGrid',
		store: {
			type: 'drExcelStore'
		},
		columns: [{
            text: '任务描述',
            dataIndex: 'taskDesc',
            width: 150
        },
        {
            text: '进程实例号',
            dataIndex: 'ProcessID',
            width: 130
        },
        {
            text: '状态',
            dataIndex: 'drState',
            width: 70
        },{
            text: '开始时间',
            dataIndex: 'startTime',
            width: 170
        },
        {
            text: '结束时间',
            dataIndex: 'endTime',
            width: 170
        },
        {
            text: '总记录数（行）',
            dataIndex: 'totalNum',
            width: 130
        },
        {
            text: '成功记录数（行）',
            dataIndex: 'successNum',
            width: 140
        },
        {
            text: '失败记录数（行）',
            dataIndex: 'faildNum',
            width: 140
        },{
        	xtype:'linkcolumn',
        	text:'日志信息',               
            sortable: false,
			mixwidth: 130,
			items:[{
				text:'详细信息',
				handler:function(grid,rowIndex,colIndex){
					var rec= grid.getStore().getAt(rowIndex);
					var ProcessID = rec.get("ProcessID");
					editExcelInfo(ProcessID);
				}
			}]
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
	}],
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'ondrExcelInfoClose'
	}]
});

function openProcess(){
	var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
	var form = contentPanel.child("drExcelInfoPanel").child("form").getForm();
	var ProcessID = form.getValues()["ProcessID"];
	editExcelInfo(ProcessID);
};

function editExcelInfo(ProcessID){
	//是否有访问权限
	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_EXCEL_LOG_STD"];
	if( pageResSet == "" || pageResSet == undefined){
		Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		return;
	}
	//该功能对应的JS类
	var className = pageResSet["jsClassName"];
	if(className == "" || className == undefined){
		Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EXCEL_LOG_STD，请检查配置。');
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
		var grid = panel;
		grid.store.tzStoreParams = '{"ProcessID":"'+ProcessID+'"}';
	});
	
	tab = contentPanel.add(cmp);     
	
	contentPanel.setActiveTab(tab);

	Ext.resumeLayouts(true);

	if (cmp.floating) {
		cmp.show();
	}	
}