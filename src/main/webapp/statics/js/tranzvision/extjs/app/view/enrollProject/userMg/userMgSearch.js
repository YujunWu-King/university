Ext.define('KitchenSink.view.enrollProject.userMg.userMgSearch', {
	extend: 'Ext.window.Window',
    xtype: 'cfgSearchWindow',
    title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00082"),/*查询*/
    width: 500,
    minWidth: 650,
    minHeight: 250,
    maxHeight:480,
    resizable: true,
    modal: true,
    closeAction: 'destroy',
    ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    requires:[
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.common.store.promptStore',
        'KitchenSink.view.common.store.comboxStore'
    ],
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },

    initComponent: function(){
        var me = this;     
        var labelWidth = 200;
        var comboboxOprColumnWidth = 0.5;
        var inputColumnWidth = 0.5;
        var dataStore;
        var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"GETVALUE","comParams":{}}';
        /*Ext.Ajax.request({
		    url: Ext.tzGetGeneralURL(),
		    params: {
		        tzParams: tzParams
		    },
		    success: function(response){
		    	var result = response.responseText;
		        var json = $.parseJSON(result);
		        var strDataStore = json.comContent.dataStore;
		        dataStore = $.parseJSON(strDataStore);
		        console.log(dataStore);
		    }
		});*/
                
        var textSelect = Ext.create('Ext.data.Store', {
   	 		fields: ['TValue', 'TLDesc'],
   	 		data : [
   	 			{"TValue":"1", "TLDesc":"包含"},
   	 			{"TValue":"2", "TLDesc":"等于"},
   	 			{"TValue":"3", "TLDesc":"开始于"}
   	 		]
   	 	});
        var comboboxSelect = Ext.create('Ext.data.Store', {
   	 		fields: ['TValue', 'TLDesc'],
   	 		data : [   	 			
   	 			{"TValue":"2", "TLDesc":"等于"}
   	 		]
   	 	});
        
        var ruxueYearStore = Ext.create('Ext.data.Store', {
   	 		fields: ['TValue', 'TLDesc'],
   	 		data : [   	 			
   	 			{"TValue":"2017", "TLDesc":"2017"},
   	 			{"TValue":"2018", "TLDesc":"2018"},
   	 			{"TValue":"2019", "TLDesc":"2019"},
   	 			{"TValue":"2020", "TLDesc":"2020"},
   	 			{"TValue":"2021", "TLDesc":"2021"},
   	 			{"TValue":"2022", "TLDesc":"2022"},
   	 			{"TValue":"2023", "TLDesc":"2023"},
   	 			{"TValue":"2024", "TLDesc":"2024"}
   	 		]
   	 	});
       
        
        var applyMajorStore,batchStore,zhiyStore,appStatusStore,luStore,lu2Store,getResultStore,interviewBatchStore,zcJxjStore,lkqTjlqStore,lkqTjlqXmStore,ylqZgXmStore,zslqZgStore,zslqZgXmStore,zzJxjStore,ruXueQkStore,ruXueXmStore,workProvinceStore,industryTypeStore,workZhNStore,PositionTypeStore,isZzCyStore,ZhZhMMStore,isValStore,isYAppStore,ylqZgStore;
        
        Ext.tzLoadAsync(tzParams,function(responseData){
	        var strDataStore = responseData.dataStore;
	        dataStore = $.parseJSON(strDataStore);
	        console.log(dataStore);
	        
	        applyMajorStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.applyMajorStore
	   	 	});
	        
	        batchStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.batchStore
	   	 	});
	        
	        zhiyStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.zhiyStore
	   	 	});
	        
	        appStatusStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.appStatusStore
	   	 	});    
	        
	        luStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.luStore
	   	 	});
	        
	        lu2Store =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.lu2Store
	   	 	});  
	        
	        /*getResultStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.getResultStore
	   	 	});  */
	        
	        interviewBatchStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.interviewBatchStore
	   	 	});
	        
	        zcJxjStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.zcJxjStore
	   	 	});  
	        
	        lkqTjlqStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.lkqTjlqStore
	   	 	});
	        
	        lkqTjlqXmStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.lkqTjlqXmStore
	   	 	});    
	        
	        ylqZgXmStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.ylqZgXmStore
	   	 	});
	        
	        zslqZgStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.zslqZgStore
	   	 	});
	        
	        zslqZgXmStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.zslqZgXmStore
	   	 	});
	        
	        zzJxjStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.zzJxjStore
	   	 	});
	        
	        ruXueQkStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.ruXueQkStore
	   	 	}); 
	        
	        ruXueXmStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.ruXueXmStore
	   	 	});
	        
	        workProvinceStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.workProvinceStore
	   	 	});
	        
	        industryTypeStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.industryTypeStore
	   	 	});
	        
	        workZhNStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.workZhNStore
	   	 	});
	        
	        PositionTypeStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.PositionTypeStore
	   	 	}); 
	        
	        isZzCyStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.isZzCyStore
	   	 	}); 
	        
	        ZhZhMMStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.ZhZhMMStore
	   	 	});
	        
	        isValStore = Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.isValStore
	   	 	}); 
	        
	        isYAppStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.isYN
	   	 	});
	        
	        ylqZgStore =  Ext.create('Ext.data.Store', {
	   	 		fields: ['TValue', 'TLDesc'],
	   	 		data : dataStore.ylqZgStore
	   	 	});
        });
        Ext.apply(this,{
            items: [{
                xtype: 'form',
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
                    labelStyle: 'font-weight:bold'
                },
                /*dockedItems:[{
                    xtype:"toolbar",
                    items:["->",
                        {
                            xtype:'splitbutton',
                            text:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00083")更多操作,
                            iconCls:  'list',
                            glyph: 61,
                            menu:[{
                                text:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00084")基本搜索,
                                listeners:{
                                    click: function (bt, e, eOpts) {
                                        me.changeSearchModel(bt,true );
                                    }
                                }
                            },{
                                text:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00085")高级搜索,
                                listeners:{
                                    click: function (bt, e, eOpts) {
                                        me.changeSearchModel(bt,false);
                                    }
                                }
                            }]
                        }
                    ]
                }],*/
                items: [
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '姓名',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,                           	 	
                           	 	store: textSelect,
                           	 	queryMode: 'local',                           	 	
                       			name: 'userNameOper'          			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'userName'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '证件号码',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: textSelect,
                           	 	queryMode: 'local',
                       			name: 'nationIdOper'          			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'nationId'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '面试申请号',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: textSelect,
                           	 	queryMode: 'local',
                       			name: 'mshIdOper'          			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'mshId'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '准考证号',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: textSelect,
                           	 	queryMode: 'local',
                       			name: 'zkzIdOper'          			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'zkzId'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '申请的专业',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'applyMajorOper'  			
                        	},                        	
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: applyMajorStore,
                           	 	queryMode: 'local',
                       			name: 'applyMajor'  			
                        	}
                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '报名表编号',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'appInsIdOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'appInsId'
                            }
                        ]
                	} ,
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '志愿入学年份',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'ruXueYearOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: ruxueYearStore,
                           	 	queryMode: 'local',
                       			name: 'ruXueYear'
                        	}                        	
                        ]
                	} ,
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '批次',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'batchOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: batchStore,
                           	 	queryMode: 'local',
                       			name: 'batch'
                        	}                        	
                        ]
                	} ,
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '志愿',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'zhiyOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: zhiyStore,
                           	 	queryMode: 'local',
                       			name: 'zhiy'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '报名表状态',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'appStatusOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: appStatusStore,
                           	 	queryMode: 'local',
                       			name: 'appStatus'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '条件录取资格（面试结果）',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'appStatusOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: luStore,
                           	 	queryMode: 'local',
                       			name: 'luqu'
                        	}                        	
                        ]
                	} ,
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '条件录取资格项目（面试结果）',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'luquAppOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: lu2Store,
                           	 	queryMode: 'local',
                       			name: 'luquApp'
                        	}                        	
                        ]
                	} ,
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '获得结果批次',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'getResultOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: batchStore,
                           	 	queryMode: 'local',
                       			name: 'getResult'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '面试批次',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'interviewBatchOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: batchStore,
                           	 	queryMode: 'local',
                       			name: 'interviewBatch'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '最初奖学金授予情况',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'zcJxjOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: zcJxjStore,
                           	 	queryMode: 'local',
                       			name: 'zcJxj'
                        	}                        	
                        ]
                	} ,
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '联考前条件录取资格',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'lkqTjlqOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: luStore,
                           	 	queryMode: 'local',
                       			name: 'lkqTjlq'
                        	}                        	
                        ]
                	},                	
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '联考前条件录取项目',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'lkqTjlqXmOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: lu2Store,
                           	 	queryMode: 'local',
                       			name: 'lkqTjlqXm'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '预录取资格',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'ylqZgOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: luStore,
                           	 	queryMode: 'local',
                       			name: 'ylqZg'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '预录取资格项目',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'ylqZgXmOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: lu2Store,
                           	 	queryMode: 'local',
                       			name: 'ylqZgXm'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '正式录取资格资格',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'zslqZgOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: zslqZgStore,
                           	 	queryMode: 'local',
                       			name: 'zslqZg'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '正式录取资格项目',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'zslqZgXmOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: lu2Store,
                           	 	queryMode: 'local',
                       			name: 'zslqZgXm'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '最终奖学金授予情况',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'zzJxjOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: zcJxjStore,
                           	 	queryMode: 'local',
                       			name: 'zzJxj'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '入学情况',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'ruXueQkOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: ruXueQkStore,
                           	 	queryMode: 'local',
                       			name: 'ruXueQk'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '入学项目',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'ruXueXmOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: lu2Store,
                           	 	queryMode: 'local',
                       			name: 'ruXueXm'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '移动电话',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'mobilePhoneOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'mobilePhone'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '注册邮箱',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'regEmailOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'regEmail'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '工作所在省市',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'workProvinceOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: workProvinceStore,
                           	 	queryMode: 'local',
                       			name: 'workProvince'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '公司名称',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'companyNameOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'companyName'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '行业类别',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'industryTypeOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: industryTypeStore,
                           	 	queryMode: 'local',
                       			name: 'industryType'
                        	}                        	
                        ]
                	}, 
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '工作职能',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'workZhNOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: workZhNStore,
                           	 	queryMode: 'local',
                       			name: 'workZhN'
                        	}                        	
                        ]
                	}, 
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '职位类型',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'PositionTypeOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: PositionTypeStore,
                           	 	queryMode: 'local',
                       			name: 'PositionType'
                        	}                        	
                        ]
                	}, 
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '是否自主创业',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'isZzCyOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: isYAppStore,
                           	 	queryMode: 'local',
                       			name: 'isZzCy'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '本科院校',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: textSelect,
                           	 	queryMode: 'local',
                       			name: 'benKeSchoolOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'benKeSchool'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '民族',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: textSelect,
                           	 	queryMode: 'local',
                       			name: 'minZuOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'minZu'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '政治面貌',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'ZhZhMMOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: ZhZhMMStore,
                           	 	queryMode: 'local',
                       			name: 'ZhZhMM'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '是否有效',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'isValOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: isValStore,
                           	 	queryMode: 'local',
                       			name: 'isVal'
                        	}                        	
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '已提交的面试申请数量',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'1',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: textSelect,
                           	 	queryMode: 'local',
                       			name: 'ySubMShNumOper'  			
                        	},
                        	{
                                xtype: 'textfield',
                                columnWidth: inputColumnWidth,
                                name: 'ySubMShNum'
                            }
                        ]
                	},
                	{
                		layout: {type: 'column'},
                        items:[
                        	{
                        		xtype: 'combobox',
                           	 	fieldLabel: '是否预报名',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	value:'2',
                           	 	columnWidth: comboboxOprColumnWidth,
                           	 	editable:false,
                           	 	store: comboboxSelect,
                           	 	queryMode: 'local',
                       			name: 'isYAppOper'  			
                        	},
                        	{
                        		xtype: 'combobox',
                           	 	valueField: 'TValue',
                           	 	displayField: 'TLDesc',
                           	 	labelWidth:labelWidth,
                           	 	columnWidth: inputColumnWidth,
                           	 	editable:false,
                           	 	store: isYAppStore,
                           	 	queryMode: 'local',
                       			name: 'isYApp'
                        	}                        	
                        ]
                	}
                ],
                buttonAlign: 'left'
            }],
            buttons: [
                {
                    text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00086")/*搜索*/,
                    iconCls:"search",
                    handler: function(btn){
                        me.searchCfg();
                    }
                },{
                    text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00087")/*清除*/,
                    iconCls:"clean",
                    handler: function(btn){
                        //搜索信息表单
                        var form = btn.findParentByType("window").child("form").getForm();
                        //重置表单
                        form.reset();
                    }
                },
                {
                    text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00047")/*关闭*/,
                    iconCls:"close",
                    handler: function(btn){
                        //获取窗口
                        var win = btn.findParentByType("window");
                        //修改密码信息表单
                        var form = win.child("form").getForm();
                        //关闭窗口
                        win.close();
                    }
                }]

        });
        this.callParent();
    }    
	
});
