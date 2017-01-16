Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStu', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrangeAddStu',
    controller: 'interviewArrangeAddStuController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuModel',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuStore',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuController'
    ],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.panelTitle","考生选择"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    //style:"margin:8px",
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            //var grid = panel.child('grid');
            //if(grid)grid.setHeight( height -buttonHeight-16);
            var grid = panel.child('grid[name=interviewArrangeAddStuGrid]');
            var formHeight = panel.child('form').getHeight();
            grid.setHeight( height -buttonHeight-formHeight-16);
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
                mszgFlagSortFilterOptions.push([records[i].data.TSDesc,records[i].data.TSDesc]);
            };
        });
        //考生类型
        var orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_ORG_COLOR_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });
        var stuGridColorSortFilterOptions=[];/*考生类别的过滤器数据*/
        orgColorSortStore.addListener("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                stuGridColorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
            };
        });
        orgColorSortStore.load();
        //gridStore添加filterchange监听
        var interviewArrangeAddStuGridStore = new KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuStore({
            listeners:{
                filterchange:function( store, filters, eOpts ){
                    var clearFiltersBtn=me.lookupReference('msArrAddStuClearFiltersBtn');
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
                reference: 'interviewArrangeAddStuForm',
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 10,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.classID","报考班级ID") ,
                    name: 'classID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.className","报考班级") ,
                    name: 'className',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.batchID","面试批次ID"),
                    name: 'batchID',
                    hidden:true
                }]
            },{
                xtype: 'grid',
                height: 340,
                frame: true,
                style:"margin:8px",
                name: 'interviewArrangeAddStuGrid',
                reference: 'interviewArrangeAddStuGrid',
                store: interviewArrangeAddStuGridStore,
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
                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.tbarClearFilters","清除筛选条件"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.tbarClearFiltersTip","清除筛选条件"),
                        iconCls:"reset",
                        reference:'msArrAddStuClearFiltersBtn',
                        handler:'onClearFilters',
                        disabled:true
                    }]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.appId","报名表编号") ,
                    dataIndex: 'appId',
                    filter: {
                        type: 'number'
                    },
                    width:105
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.stuName","姓名") ,
                    dataIndex: 'stuName',
                    filter: {
                        type: 'string'
                    },
                    minWidth: 100,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.msCLPSPC","材料评审批次") ,
                    dataIndex: 'msCLPSPC',
                    filter: {
                        type: 'string'
                    },
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.msZGFlag","面试资格"),
                    dataIndex: 'msZGFlag',
                    filter: {
                        type: 'list',
                        options: mszgFlagSortFilterOptions,
                        value:'Y'
                        //active:true
                    },
                    renderer : function(value, metadata, record) {
                        //alert("render"+value);
                        var index = mszgFlagStore.find('TValue',value);
                        if(index!=-1){
                            return mszgFlagStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msZGFlag');
                    },
                    width:106
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.isConfTimezone","是否已确认时区") ,
                    dataIndex: 'isConfTimezone',
                    filter: {
                        type: 'list',
                        options:['是','否']
                    },
                    width: 130
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.sort","考生类型") ,
                    filter: {
                        type: 'list',
                        options: stuGridColorSortFilterOptions
                    },
                    sortable: true,
                    dataIndex: 'sort',
                    minWidth: 140,
                    flex:1,
                    renderer:function(v){
                        if(v){
                            var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,true,true,false);
                            if(rec>-1){
                                return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                            }else{
                                return "";
                            }
                        }
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.label","标签") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'label',
                    minWidth: 200,
                    flex:2
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.lxEmail","邮箱") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'lxEmail',
                    minWidth: 180,
                    flex:1
                }/*,{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.earlyDecision","early decision") ,
                    xtype:'datecolumn',
                    format:'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d'
                    },
                    sortable: true,
                    dataIndex: 'earlyDecision',
                    minWidth: 105,
                    flex:1
                }*/]
            }]
        });
        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.butSave","保存") ,
        iconCls:"save",
        handler:'onPanelSave'
    },{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.butEnsure","确定") ,
        iconCls:"ensure",
        handler:'onPanelEnsure'
    },{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.butClose","关闭") ,
        iconCls:"close",
        handler:'onPanelClose'
    }]
});
