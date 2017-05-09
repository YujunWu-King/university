Ext.define('KitchenSink.view.automaticScreen.autoScreenDetailsPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.automaticScreen.autoScreenController',
        'KitchenSink.view.automaticScreen.autoTagStore',
        'KitchenSink.view.automaticScreen.fmqdListStore',
        'KitchenSink.view.automaticScreen.sdbqListStore',
        'KitchenSink.view.automaticScreen.autoTagOrFmListModel'
    ],
    xtype: 'autoScreenDetails',
	controller: 'autoScreenController',
	
	title: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_ZDCS_INFO_STD.autoScreenDetails","自动初筛详细信息"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	
	constructor: function(config){
		
		this.tzConfig = config;
		this.storeReload = config.storeReload;

		this.callParent();	
	},

    initComponent: function () {   
        Ext.util.CSS.createStyleSheet(" .readOnly-tagfield-cls div {background:#eee;}","readOnly-tagfield-cls");
        
    	var config = this.tzConfig;

    	//考生自动标签store
		var ksbqStore = new KitchenSink.view.automaticScreen.autoTagStore(config);

		//负面清单store
		var fmqdStore = new KitchenSink.view.automaticScreen.fmqdListStore(config);
		
		//手工标签store
		var labelTagStore = new KitchenSink.view.automaticScreen.sdbqListStore(config);
		
    	//初筛结果Store
		/*
		var csStatusStore = Ext.create('Ext.data.Store', {
			 fields: [{
				 	name:'value'
			 	},{
			 		name:'descr'
			 	}],
             data: [{
            	 	value: 'Y', 
            	 	descr: '初筛通过'
             	},{
             		value: 'N', 
             		descr: '淘汰'
             	}]
		 });
    	*/
		var csStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_ZDCS_JG");
		
    	Ext.apply(this, {
    	   items: [{        
		        xtype: 'form',
		        reference: 'autoScreenDetailsForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
		        },
		        border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
		        
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 100,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		        	xtype:'hiddenfield',
		        	name:'classId',
		        	value: config.classId
		        },{
		        	xtype:'hiddenfield',
		        	name:'batchId',
		        	value: config.batchId
		        },{
		            xtype: 'textfield',
					name: 'name',
					fieldLabel: '考生姓名',
					readOnly: true,
					cls: 'lanage_1',
					value: config.name
		        },{
		        	xtype: 'textfield',
					name: 'applyNum',
					fieldLabel: '面试申请号',
					readOnly: true,
					cls: 'lanage_1',
					value: config.msApplyId
		        },{
		        	xtype: 'textfield',
					name: 'appId',
					fieldLabel: '报名表编号',
					readOnly: true,
					cls: 'lanage_1',
					//value: config.appId,
					beforeBodyEl: [
                        '<li id="{id}-viewApplyForm" data-qtip="查看报名表" data-ref="viewApplyForm" class="x-tagfield-item x-tagfield-item-selected" style="cursor:pointer;right:5px;bottom:1px;position:absolute;">' +
                        	'<span class="x-tagfield-item-text" style="padding-right:4px;">查看报名表</span>' +
                        '</li>'
		            	],
					childEls: [
		                    'viewApplyForm'
		                ],
					listeners: {
						change: function (field) {
							var appId = field.getValue();
							var viewApplyForm = this.viewApplyForm;
							var len = appId.length;
							if(len > 0){
								viewApplyForm.applyStyles("display:block");
								if(viewApplyForm){
									viewApplyForm.addListener("click", "tzViewApplyForm");	
								}
							}else{
								viewApplyForm.applyStyles("display:none");	
							}
						}
					}
		        },{
		        	xtype:'combo',
		        	name: 'status',
		        	fieldLabel: '初筛结果',
		        	store: csStatusStore,
		        	queryMode: 'local',
		            editable:false,
		            //valueField: 'value',
		    		//displayField: 'descr'
		            valueField: 'TValue',
	            	displayField: 'TSDesc'
		        },{
		        	xtype: 'displayfield',
		        	name: 'ranking',
		        	fieldLabel: '考生排名'
		        },{
		        	xtype: 'tagfield',
		        	name: 'negativeList',
		        	fieldLabel: '负面清单',
		        	displayField: 'desc',
                    valueField: 'id',
                    queryMode: 'local',
                    store: fmqdStore,
                    readOnly: true
		        },{
		        	xtype: 'tagfield',
		        	name: 'autoLabel',
		        	fieldLabel: '自动标签',
		        	displayField: 'desc',
                    valueField: 'id',
                    queryMode: 'local',
                    store: ksbqStore,
                    readOnly: true
		        },{
		        	xtype: 'tagfield',
		        	name: 'manualLabel',
		        	fieldLabel: '手工标签',
		        	store:labelTagStore,
//                    valueField: 'TZ_LABEL_ID',
//                    displayField: 'TZ_LABEL_NAME',
		        	displayField: 'desc',
                    valueField: 'id',
                    filterPickList:true,
                    createNewOnEnter: true,
                    createNewOnBlur: true,
                    queryMode: 'local'
		        },{
		        	xtype: 'displayfield',
		        	name: 'updateOpr',
		        	fieldLabel: '修改人'
		        },{
		        	xtype: 'displayfield',
		        	name: 'updateDttm',
		        	fieldLabel: '修改时间'
		        }]
    	   }]
		});
	   
	 	this.callParent();
    },
	buttons: [{
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_ZDCS_INFO_STD.save","保存"),
		iconCls:"save",
		handler: 'onAutoScreenDetailsSave'
	}, {
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_ZDCS_INFO_STD.ensure","确定"),
		iconCls:"ensure",
		closePanel:'Y',
		handler: 'onAutoScreenDetailsEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_ZDCS_INFO_STD.close","关闭"),
		iconCls:"close",
		handler: 'onAutoScreenDetailsClose'
	}]
})
