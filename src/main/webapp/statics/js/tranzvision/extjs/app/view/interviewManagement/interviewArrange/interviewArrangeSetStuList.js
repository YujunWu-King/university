Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuList', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrangeSetStuList',
    controller: 'interviewArrangeSetStuListController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListModel',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListStore',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListController'
    ],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.panelTitle","面试安排考生名单管理"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    reference: 'interviewArrangeSetStuListPanel',
    bodyPadding: 10,
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 36;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            console.log(grid);
            if(grid)grid.setHeight( height -buttonHeight -115);
        }
    },
    initComponent: function (){
        var me = this;
        //材料评审状态（有无面试资格）
        var mszgFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_MSHI_ZGFLG");
        mszgFlagStore.load();
        //为filter添加值
        var mszgFlagSortFilterOptions=[];
        mszgFlagStore.addListener("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                //mszgFlagSortFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
                mszgFlagSortFilterOptions.push([records[i].data.TSDesc,records[i].data.TSDesc]);
            };
        });
        //gridStore添加filterchange监听
        var interviewArrangeSetStuListGridStore = new KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListStore({
            listeners:{
                filterchange:function( store, filters, eOpts ){
                    var clearFiltersBtn=me.lookupReference('msArrSetStuListClearFiltersBtn');
                    if(filters.length>0){
                        clearFiltersBtn.setDisabled( false );
                    }else{
                        clearFiltersBtn.setDisabled( true );
                    }
                }
            }
        });

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'interviewArrangeSetStuListForm',
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: '0px 0px 5px 0px',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 160,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.classID","报考班级ID") ,
                    name: 'classID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.className","报考班级") ,
                    name: 'className',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.batchID","面试批次ID"),
                    name: 'batchID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.batchName","面试批次") ,
                    name: 'batchName',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.stuCount","已选择安排面试考生") ,
                    name: 'stuCount',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                }]
            },{
                xtype: 'grid',
                frame: true,
                name: 'interviewArrangeSetStuListGrid',
                reference: 'interviewArrangeSetStuListGrid',
                store: interviewArrangeSetStuListGridStore,
                columnLines: true,    //显示纵向表格线
                plugins:[
                    {
                        ptype: 'gridfilters'
                    }
                ],
                selModel:{
                    type: 'checkboxmodel'
                },
                dockedItems:[{
                    xtype:"toolbar",
                    items:[/*{
                    	xtype: 'form',
                    	reference: 'audienceForm',
                    	style: 'padding-top:8px;',
                    	items:[{
                    		xtype: 'tagfield',
                    		name:'audTag',
                    		displayField: 'desc',
                            valueField: 'id',
                            createNewOnEnter: false,
                            createNewOnBlur: false,
                            filterPickList: true,
                            queryMode: 'local',
                            //publishes: 'value',
                            //minWidth:400,
                            //maxWidth:600,
                            width: 600,
                            listConfig:{
                                maxHeight:1
                            }
                    	}]
                    },{
                    	text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.adddAud","添加听众"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.adddAud","添加听众"),
                        iconCls:"add",
                        handler:'addAudience'
                    },*/{
                    	text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.addStudent","添加考生"),
                    	tooltip: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.addStudent","添加考生"),
                        iconCls:"add",
                        handler:'addStudents'
                    },{
                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarRemove","删除"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarRemoveTip","删除"),
                        iconCls:"remove",
                        handler:'delSelStus'
                    },'->',{
							xtype:'splitbutton',
							text:'更多操作',
							iconCls:  'list',
							glyph: 61,
							menu:[{
								text:'给选中考生发送邮件',
								iconCls:"email",
								handler:'tzSendEmailSmsToStu',
								sendType: "EML"
							},{
								text:'给选中考生发送短信',
								iconCls:"sms",
								handler:'tzSendEmailSmsToStu',
								sendType: "SMS"
							}]
						}
					]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },/*{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.appId","报名表编号") ,
                    dataIndex: 'appId',
                    filter: {
                        type: 'number'
                    },
                    width: 100,
                    minWidth: 80,
                    hidden: true
                },*/{
                	text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.interviewAppId","面试申请号") ,
                    dataIndex: 'interviewAppId',
                    filter: {
                        type: 'string'
                    },
                    width: 120,
                    minWidth: 100
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.stuName","姓名") ,
                    dataIndex: 'stuName',
                    filter: {
                        type: 'string'
                    },
                    width: 100,
                    minWidth: 80
                },{
                	text: '报考班级',
                	dataIndex: 'className',
                	width: 140,
                	minWidth: 120,
                	flex: 1,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '申请面试批次',
                	dataIndex: 'batchName',
                	width: 130,
                	minWidth: 100,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
					text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.emial", '邮箱'),
					dataIndex: 'email',
					width: 140,
                    minWidth: 120
				},{
					text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.mobile", '手机'),
					dataIndex: 'mobile',
					width: 120,
                    minWidth: 100
				},/*{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.msZGFlag","面试资格"),
                    dataIndex: 'msZGFlag',
                    filter: {
                        type: 'list',
                        options: mszgFlagSortFilterOptions
                    },
                    width: 100,
                    minWidth: 80
                },*/{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.label","标签") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'label',
                    width: 140,
                    minWidth: 120,
                    flex: 1
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 500,
                    store: interviewArrangeSetStuListGridStore,
                    displayInfo: true,
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });
        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.butSave","保存") ,
        iconCls:"save",
        handler:'onPanelSave'
    },{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.butEnsure","确定") ,
        iconCls:"ensure",
        handler:'onPanelEnsure'
    },{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.butClose","关闭") ,
        iconCls:"close",
        handler:'onPanelClose'
    }]
});
