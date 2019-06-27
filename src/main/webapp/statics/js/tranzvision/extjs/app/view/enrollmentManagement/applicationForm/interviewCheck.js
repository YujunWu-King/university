Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.interviewCheck', {
	extend: 'Ext.panel.Panel',
	requires: [
		'Ext.data.*',
		'Ext.util.*',
		'KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.classController',
        'tranzvision.extension.grid.column.Link'
	],
	xtype: 'appFormClass',
	title: "查看面试情况",
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	controller: 'appFormClass',
    classID:'',
    batchID:'',
	initComponent: function(){
		var interviewCheckStore = new KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckStore();
		interviewCheckStore.tzStoreParams = '{"classID":"'+this.classID+'","batchID":"'+this.batchID+'"}';
		interviewCheckStore.load();
		var store = new KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckStore();
		var column = [{
			text: '',
			width:80
		}]
		
		var me = this;
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'selForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				ignoreLabelWidth: true,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
					
				fieldDefaults: {
				  msgTarget: 'side',
				  labelWidth: 100,
				  labelStyle: 'font-weight:bold'
				},
				
				items: [{
					layout: {
						type: 'column',
					},
					items:[ {
			           	xtype: 'combo',
			           	afterLabelTextTpl: [
			           	                 '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			           	             ],
						name: 'sel',
						emptyText:"请选择...",
			            queryMode: 'remote',
			            editable:false,
			            width:400,
			            allowBlank: false,
						valueField: 'selOne',
			    		displayField: 'selOne',
			    		ignoreChangesFlag: true,
						store:interviewCheckStore

			        },{
						columnWidth:.4,
						xtype: 'textfield',
						name: 'classID',
						value:this.classID,
						hidden:true
					},{
						columnWidth:.4,
						xtype: 'textfield',
						name: 'batchID',
						value:this.batchID,
						hidden:true
					},{
						xtype: 'button',
						text: "查询",
						maxWidth: 120,
						iconCls:"search",
						margin: '0 0 0 20',
						handler: function(btn){
							var panel = btn.findParentByType("appFormClass");
							var grid = panel.child("grid");
							var form = panel.child('form').getForm();
							var search = form.findField('sel').getValue(); 
							var classID = form.findField('classID').getValue(); 
							var batchID = form.findField('batchID').getValue();
							if(search != ""&&search != null){
								store = new KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckStore();
								var tzParams ='{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_INTERVIEW_STD","OperateType":"queryColumns","comParams":{"classID":"'+classID+'","batchID":"'+batchID+
								'","selOne":"'+search+'"}}';
								Ext.tzLoadAsync(tzParams,function(respData){
									//store.proxy.extraParams.tzParams = tzParams;
									var size = respData.size;
									var	xData = respData.x;
									var all  = respData.all;
									var psSize = respData.psSize;
									if(size > 0){
										column = [{
											text: '',
											dataIndex:'psName',
											width:80
										}]
										for(var i=0;i<size;i++){
											column.push({
												text: xData[i].order,
												dataIndex:'realName'+i,
								                minWidth: 150,
								                flex:1.5,
								                renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
								                	var mszt = record.data["mszt"+(columnIndex-1)]
								                    if(mszt=='1'){  
								                    	cellmeta.style='background:#F4A460';  
								                    }
								                	if(mszt=='2'){  
								                		cellmeta.style='background:#DBDBDB';  
								                    }  
								                    return value;  
								                }  
											});
										}
									}
									grid.reconfigure( store,column );
									for(var i=0;i<psSize;i++){
										store.insert(store.getCount(), all[i]);
									}
									store.commitChanges();
								});
							}else{
								Ext.Msg.alert("提示","请选择要查的分组！");
							}
						}
					}]
				}]
		     },{
	        	xtype: 'grid',
				title: '面试情况一览',
				frame: true,
				columnLines: true,
				minHeight:400,
				maxHeight:400,
				name:'smsHistoryGrid',
				reference:'smsHistoryGrid',
				style: "margin:10px",
		        store: store,
	            columns: column
	        }],
	        buttons: [{
			 	text: "关闭",
				iconCls:"close",
				ignoreChangesFlag: true,
				handler: 'onInterviewClose'
			}]	
		});
		this.callParent();
	},
	constructor: function (classID,batchID) {
		this.classID = classID;
		this.batchID = batchID;
		this.callParent();
	},
	
});