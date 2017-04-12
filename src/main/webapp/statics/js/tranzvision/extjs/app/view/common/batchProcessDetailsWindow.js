Ext.define('KitchenSink.view.common.batchProcessDetailsWindow', {
    extend: 'Ext.window.Window',
    xtype: 'batchProcessDetailsWindow', 
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.store.batchProcessLogStore'
    ],
    title: '进程运行详细信息', 
    ignoreChangesFlag: true,
    
    width: 680,
    height: 220,
    layout: 'fit',
    resizable: false,
    modal: true,
    closeAction: 'destroy',

    constructor: function (config) {
    	this.processIns = config.processIns;
    	this.tzCallback = config.callBack;
    	
    	var tzParams ='{"ComID":"TZ_COMMON_JCXQ_COM","PageID":"TZ_COMMON_JCXQ_STD","OperateType":"QF","comParams":{"processIns":"'+ config.processIns +'"}}';
		Ext.tzLoadAsync(tzParams,function(respData){
			formData = respData.formData;
		});
		this.formData = formData;
		this.statusCode = formData.statusCode;
    	
        this.callParent();
    },
    initComponent: function(){
    	var formData = this.formData;
    	var processIns = this.processIns;
    	var orgId = formData.orgId;
    	
    	Ext.apply(this,{
    		items: [{
    			xtype: 'form',	
    			border: false,
    			bodyPadding: 10,
    		
    			fieldDefaults: {
    				msgTarget: 'side',
    				labelWidth: 95,
    				labelStyle: 'font-weight:bold'
    			},
    			items:[{
    				layout:'column',
    				items:[{
    					columnWidth: 0.5,
    					xtype: 'fieldcontainer',
    					items:[{
    	    				xtype: 'displayfield',
    	    				fieldLabel: '运行控制ID',
    	    				name: 'runControlID',
    	    				value: formData.runControlID
    	    			},{
    	    				xtype: 'displayfield',
    	    				fieldLabel: '进程实例ID',
    	    				name: 'processInsID',
    	    				value: this.processIns
    	    			},{
    	    				xtype: 'displayfield',
    	    				fieldLabel: '请求创建时间',
    	    				name: 'createDatetime',
    	    				value: formData.createDatetime
    	    			}]
    				},{
    					columnWidth: 0.5,
    					xtype: 'fieldcontainer',
    					items:[{
    	    				xtype: 'displayfield',
    	    				fieldLabel: '运行服务器',
    	    				name: 'runServer',
    	    				value: formData.runServer
    	    			},{
    	    				xtype: 'displayfield',
    	    				fieldLabel: '运行状态',
    	    				name: 'runStatus',
    	    				value: formData.runStatus
    	    			},{
    	    				xtype: 'displayfield',
    	    				fieldLabel: '进程结束时间',
    	    				name: 'endDatetime',
    	    				value: formData.endDatetime
    	    			}]
    				}]
    			},{
					xtype:'button',
					text:'<span style="text-decoration:underline;color:blue;font-weight:normal;">查看进程运行日志</span>',
					textAlign: 'left',
					border:false,
					style:{
						background: 'white',
						boxShadow:'none',
						left:'-6px'
					},
					handler: function(btn){
						//批处理日志store
						var store = new KitchenSink.view.common.store.batchProcessLogStore({
							processIns: processIns,
							orgId: orgId
						});
						
						var logGrid=Ext.create('Ext.grid.Panel', {  
							columnLines: true,
							store: store,
			    			columns: [{
			    				text: "序号",
			    				dataIndex: 'orderNum',
			    				width:60
			    			},{
			    				text: "级别",
			    				dataIndex: 'level',
			    				width:70
			    			},{
			    				text: "日期时间",
			    				dataIndex: 'datetime',
			    				width:140
			    			},{
			    				xtype: 'linkcolumn',
			    				text: "日志详细内容",
			    				dataIndex: 'logContent',
			    				width:200,
			    				flex:1,
			    				renderer:function(v){
			    					this.text=v;
			    					this.tooltip=v;
			    					return ;
			    				},
				    			handler: function(view, rowIndex){
				    				var store = view.findParentByType("grid").store;
				    				var selRec = store.getAt(rowIndex);
				    				var logContent = selRec.get("logContent");
				    				
				    				var logContentWin = Ext.widget('window', {
										 title: '日志详情',
										 width: 600,
										 height: 400,
										 resizable: false,
										 modal: true,
										 closeAction: 'destroy',
										 bodyStyle:'overflow-y:auto;overflow-x:hidden',
										 items:[{
											 xtype:'form',
											 bodyPadding: 10,
											 items:[{
												 xtype: 'label',
											     text: '日志详细内容:',
											     style:'font-weight:bold;',
											 },{
												 xtype:'displayfield',
												 fieldLabel: '日志详细内容',
												 hideLabel: true,
												 value: logContent
											 }]
										 }],
										 dockedItems: [{
											xtype: 'toolbar',
											dock: 'bottom',
											ui: 'footer',
											items:['->',{
													text: '关闭',iconCls:'close',handler: function(closeBtn){
														closeBtn.findParentByType('window').close();
												}
											}]
										 }]
									});
				    				logContentWin.show();
			    				},
			    			}],
			    			bbar: {
			    				xtype: 'pagingtoolbar',
			    				pageSize: 10,
			    				listeners:{
			    					afterrender: function(pbar){
			    						var grid = pbar.findParentByType("grid");
			    						pbar.setStore(grid.store);
			    					}
			    				},
			    				plugins: new Ext.ux.ProgressBarPager()
			    			}	
						});
					
						var logWin = Ext.widget('window', {
							 title: '进程运行日志',
							 width: 800,
							 minWidth: 800,
							 height: 400,
							 resizable: true,
							 modal: true,
							 closeAction: 'destroy',
							 listeners:{
								resize:function(win, width, height, oldWidth, oldHeight, eOpts ){
									var buttonHeight = 75;
									var grid = win.child('grid');
									if(grid) grid.setHeight( height-buttonHeight);
								}
							 },
							 items:logGrid,
							 dockedItems: [{
								xtype: 'toolbar',
								dock: 'bottom',
								ui: 'footer',
								items:['->',{
										text: '关闭',iconCls:'close',handler: function(closeBtn){
											closeBtn.findParentByType('window').close();
									}
								}]
							 }]
						});
						logWin.show();
					}
				}]
    		}]
    	});
    	
    	this.callParent();
    },
    buttons: [{
		text: '刷新',
		iconCls:"refresh",
		handler: function(btn){
			var win = btn.findParentByType("window");
			win.mask("刷新中...");
			var form = win.child('form').getForm();
			var processIns = win.processIns;
			
			var tzParams ='{"ComID":"TZ_COMMON_JCXQ_COM","PageID":"TZ_COMMON_JCXQ_STD","OperateType":"QF","comParams":{"processIns":"'+ processIns +'"}}';
			Ext.tzLoad(tzParams,function(respData){
				formData = respData.formData;
				win.statusCode = formData.statusCode;
				
				form.setValues(formData);
				win.unmask();
			});
		}
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			var statusCode = win.statusCode;
			win.tzCallback(statusCode);
			//关闭窗口
			win.close();
		}
	}]
});
