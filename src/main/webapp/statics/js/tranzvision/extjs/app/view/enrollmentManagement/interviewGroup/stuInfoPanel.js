Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.stuInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'auditClassInfo',
    controller: 'appFormInterview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.enrollmentManagement.interviewGroup.stuStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn'
    ],

    
    initComponent:function(){
        var me = this;
        var appFormStuStore = new KitchenSink.view.enrollmentManagement.interviewGroup.stuStore();

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
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyDirectionName","报考方向名称"),
                    name: 'className',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyBatchName","申请批次"),
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
                        }
                       ,'->',
                    ]
                },
                plugins: [
                    {
                        ptype: 'gridfilters',
                        controller: 'appFormClass'
                    },
                    {
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    },
                    
                ],
               selModel: {
                    type: 'checkboxmodel',
                    pruneRemoved: false
                },
                reference: 'stuGrid',
                multiSelect: true,
                store: appFormStuStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 1000,
                    store: appFormStuStore,
                    plugins: new Ext.ux.ProgressBarPager()
                },
                columns: [
	                	/*{
	                        xtype:'checkboxmodel',
	                        minWidth:20,
	                        maxWidth:80
	                    },*/
                    {
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.OPRID","OPRID"),
                        dataIndex: 'oprID',
                        hidden:true
                    },{    
                    	text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.name","姓名"),
                        dataIndex: 'stuName',
                        width:100,
                        lockable   : false,
                        filter: {
                            type: 'string'
                        }
                    },{
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewApplicationID","面试申请号"),
                        dataIndex: 'interviewApplicationID',
                        width:110,
                        lockable   : false,
                        filter: {
                            type: 'string'
                        }
                    },{
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.nationalIDs","报名表编号"),
                        dataIndex: 'appInsID',
                        width:100,
                        lockable   : false,
                        filter: {
                            type: 'string'
                        }
                    },{
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.natisalIDs","性别"),
                        dataIndex: 'grxxTZ_grxx_5',
                        width:100,
                        lockable   : false,
                        filter: {
                            type: 'string'
                        }
                    },{
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submitStates","面试组"),
                        dataIndex: 'group_name',
                        lockable   : false,
                        width: 95,
                        filter: {
                            type: 'list',
                            options: submitStateFilterOptions
                            }
                        },
                        {
                            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submtatesd","面试序号"),
                            lockable   : false,
                            value:'1-1',
                            width: 125,
                            filter: {
                                type: 'list',
                                options: submitStateFilterOptions
                                }
                            },
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.sssd","安排时间"),
                                dataIndex: 'group_date',
                                lockable   : false,
                                width: 200,
                                filter: {
                                    type: 'list',
                                    options: submitStateFilterOptions
                                    }
                                },
                        {
                            menuDisabled: true,
                            sortable: false,
                            width:150,
                            text: "操作",
                            xtype: 'actioncolumn',
                            align:'center',
                            items:[
                                {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSets","面试分组"),handler:'openInterviewGroupWindow'},"-",
                                {iconCls: 'delete',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.delete","删除"),handler:''}
                            ]
                        }
                       ]
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
