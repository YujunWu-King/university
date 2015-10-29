Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuList', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrangeSetStuList',
    controller: 'interviewArrangeSetStuListController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListModel',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListStore',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListController'
    ],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.panelTitle","面试安排考生名单管理"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    reference: 'interviewArrangeSetStuListPanel',
    //style:"margin:8px",
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid)grid.setHeight( height -buttonHeight -138-8);
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
                mszgFlagSortFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
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
                bodyPadding: 10,
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
                height: 420,
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
                    items:[	{
                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarClearFilters","清除筛选条件"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarClearFiltersTip","清除筛选条件"),
                        iconCls:"reset",
                        reference:'msArrSetStuListClearFiltersBtn',
                        handler:'onClearFilters',
                        disabled:true
                    },	{
                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarAdd","新增"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarAddTip","新增"),
                        iconCls:"add",
                        handler:'addIntervieStus'
                    },	{
                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarRemove","删除"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.tbarRemoveTip","删除"),
                        iconCls:"remove",
                        handler:'delSelStus'
                    }]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.appId","报名表编号") ,
                    dataIndex: 'appId',
                    filter: {
                        type: 'number'
                    },
                    minWidth:125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.stuName","姓名") ,
                    dataIndex: 'stuName',
                    filter: {
                        type: 'string'
                    },
                    minWidth: 100,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.className","报考班级") ,
                    dataIndex: 'className',
                    filter: {
                        type: 'string'
                        //value:curClassName
                    },
                    minWidth: 200,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.msZGFlag","初筛结果"),
                    dataIndex: 'msZGFlag',
                    filter: {
                        type: 'list',
                        options: mszgFlagSortFilterOptions
                    },
                    renderer : function(value, metadata, record) {
                        //alert("render"+value);
                        var index = mszgFlagStore.find('TValue',value);
                        if(index!=-1){
                            return mszgFlagStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msZGFlag');
                    },
                    minWidth:120,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.city","所在城市") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'city',
                    minWidth: 105,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.country","所在国家") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'country',
                    minWidth: 105,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_SSTU_STD.label","标签") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'label',
                    minWidth: 200,
                    flex:2
                }]
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
