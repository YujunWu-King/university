Ext.define('KitchenSink.view.enrollProject.userMg.userMgNewAud', {
    extend: 'Ext.window.Window',
    requires: [
               'Ext.data.*',
               'Ext.grid.*',
               'Ext.util.*',
			   'Ext.grid.filters.Filters',
               'Ext.toolbar.Paging',
               'Ext.ux.ProgressBarPager',
       		//   'KitchenSink.view.audienceManagement.newAudWindowStore',
			//   'KitchenSink.view.audienceManagement.myEnrollmentClueController',
               'KitchenSink.view.enrollProject.userMg.userMgAudStore'
           ],
    xtype: 'newAudWindow', 
    title: '听众信息', 
	reference: 'newAudWindow',
    width: 600,
    height: 550,
    minWidth: 300,
    minHeight: 380,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'destroy',
	actType: 'add',
	scrollable: true,  

	initComponent: function () {
		var store = new KitchenSink.view.enrollProject.userMg.userMgAudStore();
		
        Ext.apply(this, {
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 150,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: "机构",
			hidden:true,
			name: 'audJG',
			value:Ext.tzOrgID
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audID","听众编号"),
			name: 'audID',
		//	maxLength: 18,
		/*	afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			*/
			allowBlank: false,
            readOnly:true,
			fieldStyle:'background:#F4F4F4',
            value: 'NEXT'
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audName","听众名称"),
			name: 'audName',
			allowBlank: false,
		}, {
			xtype: 'combobox',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audStat","听众状态"),
			name: 'audStat',
			valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_AUD_STAT"),
            queryMode: 'local',
            value: '1',
            allowBlank: false,
           
		},{
				xtype: 'combobox',
	        	fieldLabel  : Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audType","听众类型"),
	        	name      : 'audType',
	        	valueField: 'TValue',
	            displayField: 'TSDesc',
	            store: new KitchenSink.view.common.store.appTransStore("TZ_AUD_TYPE"),
	            queryMode: 'local',
	         
	            value: '2',
	            allowBlank: false,
	            listeners: {
					"change": function( cbox , newValue, oldValue,lastValue){
						//必填
						
						var audSQLs=cbox.findParentByType("form").getForm().findField("audSQL");
					//	console.log(externalLink);
						
						
						var form = cbox.findParentByType("form").getForm();
						//表单数据
							var formParams = form.getValues();
							var audTypeNow =formParams["audType"];
							
					//		 console.log(audTypeNow);
						
						
						if(audTypeNow=="1"){
						
							audSQLs.allowBlank = false;
						}else{
							audSQLs.allowBlank = true;
						}
					}
				}
		
		}, {
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.tips","备注"),
			name: 'audMS'
		}
	/*	,{
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.sql","SQL"),
			name: 'audSQL',
		
			
		}
		*/
		,{
			
	            xtype:'grid',
	            frame: true,
	            maxheight:300,
	            minHeight: 220,
			//	name: 'audItemGrid',
			/*	dockedItems:[{
	        		xtype:"toolbar",
	        		items:[
	        			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchComList"},"-",
	        			{text:"重新产生听众成员",tooltip:"重新产生听众成员",iconCls:"add",handler:"freshAudMember"},"-",
	        			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteComRegInfos"}
	        		]
	        	}],
	        	*/
				columnLines: true,
			/*	selModel: {
	                type: 'checkboxmodel'
	            },
	            */
	            style:"margin:10px",
				reference: 'audItemGrid',
				
	            scrollable: true,  
				
				store:store,
				
				plugins: {
							ptype: 'cellediting',
					
	    						},
	            viewConfig: {
									plugins: {
										ptype: 'gridviewdragdrop',
										dragText: '拖拽进行信息项的排序'
										},
										listeners: {
										
											drop: function(node, data, dropRec, dropPosition) {
												data.view.store.beginUpdate();
												var items = data.view.store.data.items;
												for(var i = 0;i< items.length;i++){
													items[i].set('applyItemNum',i+1);
												}
												data.view.store.endUpdate();
											}
										}
	    						},
				
	        //    multiSelect: true,
	        
	            name:'tzcy',
	        
	            title: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.tzcy","听众成员"),
	           
	            columns: [
	                      {
	                text: '对象ID',
	                dataIndex: 'dxID',
	                hidden:true,
	                flex:1
	            },{
	                text: '听众ID',
	                dataIndex: 'audID',
	                hidden:true,
	                flex:1
	            },{
	                text: '姓名',
	                dataIndex: 'audName',
	                flex:1
	            },{
	                text: '手机',
	                dataIndex: 'audMobile',
	                flex:1
	            },{
	                text: "邮箱",
	                dataIndex: 'audMail',
	                flex:1
	            },{
					text: '状态',
					dataIndex: 'audDxzt',
					sortable: false,
					flex: 1,
					editor: {
						xtype: 'combobox',
							store:{
								fields: [{name:'applyItemTypeValue'},{name:'applyItemTypeDesc'}],
								data: [{applyItemTypeValue: 'A', applyItemTypeDesc: '正常'},{applyItemTypeValue: 'N', applyItemTypeDesc: '失效'},{applyItemTypeValue: 'L', applyItemTypeDesc: '锁定'}]
									},
						displayField: 'applyItemTypeDesc',
						valueField: 'applyItemTypeValue'
					},
					renderer: function(v){
						if(v=='A'){
							return "正常";
						}else if(v=='N'){
							return "失效";
						}else{
							return "锁定";
						}
					},
					listeners: {
						
			
					} 
					
				},{
	                menuDisabled: true,
	                sortable: false,
	 			   width:60,
	                align:'center',
	                xtype: 'actioncolumn',
	 			   items:[
	 			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteAudInfo'}
	 			   ]
	             }],
	           
	            bbar: {
	                xtype: 'pagingtoolbar',
	                pageSize: 10,
					store: store,
	                plugins: new Ext.ux.ProgressBarPager()
	            }
	        }
		]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onPageRegSave1'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onPageRegEnsure1'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onPageRegClose'
	}],
        });
	 this.callParent();
	}
});
