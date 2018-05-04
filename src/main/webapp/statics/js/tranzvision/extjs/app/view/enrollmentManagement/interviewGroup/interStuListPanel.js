Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'auditClassInfo',
    controller: 'appFormClass',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.enrollmentManagement.applicationForm.stuStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn'
    ],
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 42;/*button height plus panel body padding*/
            var formHeight = panel.lookupReference('classInfoForm').getHeight();
            var formPadding = 20;
            var grid = panel.child('grid[name=appFormApplicants]');
            grid.setHeight( height- formHeight -buttonHeight-formPadding);
        }
    },
    bodyPadding:10,
    constructor: function (cfg){
        Ext.apply(this,cfg);
        this.callParent();
    },
    initComponent:function(){
        var me = this;
        var appFormStuStore = new KitchenSink.view.enrollmentManagement.applicationForm.stuStore();

        var submitStateStore = me.initialData.submitStateStore,
	        auditStateStore = me.initialData.auditStateStore,
	        interviewResultStore = me.initialData.interviewResultStore
	        orgColorSortStore = me.initialData.orgColorSortStore;
    
        /*过滤器Options数据*/
        var colorSortFilterOptions=me.initialData.colorSortFilterOptions,
	    	submitStateFilterOptions=me.initialData.submitStateFilterOptions,
	    	auditStateFilterOptions=me.initialData.auditStateFilterOptions,
	    	interviewResultFilterOptions=me.initialData.interviewResultFilterOptions;
        
        /*初始颜色类别数据*/
        var initialColorSortData=me.initialData.initialColorSortData;
        var validColorSortStore =  new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_COLOR_SORT_T',
            condition:{TZ_JG_ID:{
                value:Ext.tzOrgID,
                operator:'01',
                type:'01'
            },TZ_COLOR_STATUS:{
                value:'N',
                operator:'02',
                type:'01'
            }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });

        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOR_SORT_ID","TZ_COLOR_NAME","TZ_COLOR_CODE"
            ],
            data:initialColorSortData
        });
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                bodyPadding:'10px 0 10px 0',
                reference: 'classInfoForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    name: 'modalID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyDirectionNames","报考班级"),
                    name: 'className',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyBatchNames","申请批次"),
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyBatchNames","报考考生数量"),
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyBatchNames","材料评审考生"),
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyBatchNames","参与面试考生"),
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                }
                ]
            },{
                xtype: 'grid',
                height:500,
                header:false,
                name:'appFormApplicants',
                frame: true,
                viewConfig : {
                    enableTextSelection:true
                },
                columnLines: true,
                listeners: {
                    afterrender: {
                    	fn: function(stuGrid){ 
                    		stuGrid.getStore().addListener("refresh",
                    				function(thisStore){
                    			stuGrid.getView().getSelectionModel().deselectAll();
                    		},this);
                    	}
                    }
                },
                dockedItems:{
                    overflowHandler: 'Menu',
                    xtype:"toolbar",
                    items:[
						{
						    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.query","查询"),
						    iconCls:"query",
						    handler:"queryStudents"
						},'-',   
                        
						{
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.add","新增"),
                            iconCls:"add",
                            handler:""
                        },'-',
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.delete","删除"),
                            iconCls:"delete",
                            handler:""
                        },'-',
                        {
                            xtype:'splitbutton',
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.moreOperation","更多操作"),
                            iconCls:  'list',
                            glyph: 61,
                            menu:[]
                        }
                    ]
                },
                columns : [// 列模式  
                    { text:'序号', xtype: 'rownumberer', width:50, align : 'center'},  
                    {  
                        text : "考生姓名",  
                        dataIndex : 'name',  
                        width : 100,  
                        align : 'center'  
                    },{  
                        text : "面试申请号",  
                        dataIndex : 'role',  
                        width : 100,  
                        align : 'center'  
                    }, {  
                        text : "报名表编号",  
                        dataIndex : 'city',  
                        width : 100,  
                        align : 'center'  
                          
                    }, {  
                        text : "性别",  
                        dataIndex : 'validity',  
                        width : 100,  
                        align : 'center'  
                          
                    }, {  
                        text : "面试评委组",  
                        dataIndex : 'validity',  
                        width : 100,  
                        align : 'center'  
                          
                    }, {  
                        text : "面试序号",  
                        dataIndex : 'validity',  
                        width : 100,  
                        align : 'center'  
                          
                    }, {  
                        text : "面试时间",  
                        dataIndex : 'validity',  
                        width : 100,  
                        align : 'center'  
                          
                    }],   
                
                
                
            }]
        })
        this.callParent();
    },
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applicantsList","报考学生列表"),
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
    buttons: [{
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.save","保存"),
        iconCls:"save",
        handler: 'onStuInfoSave'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onStuInfoEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
        iconCls:"close",
        handler: 'onStuInfoClose'
    }]
});
