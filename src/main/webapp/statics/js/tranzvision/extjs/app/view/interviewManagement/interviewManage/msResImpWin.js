Ext.define('KitchenSink.view.interviewManagement.interviewManage.msResImpWin', {
    extend: 'Ext.window.Window',
    reference: 'msResImpWin',
    xtype: 'msResImpWin',
    controller:'msResImpController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.interviewManagement.interviewManage.msResImpModel',
        'KitchenSink.view.interviewManagement.interviewManage.msResImpStore',
        'KitchenSink.view.interviewManagement.interviewManage.msResImpController'
    ],
    width: 825,
    height:500,
    minHeight: 300,
    columnLines: true,
    ignoreChangesFlag: true,
    style:"margin:8px",
    title:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.winTitle","面试结果导入") ,
    //layout: 'fit',
    resizable: false,
    modal: true,
    closeAction: 'hide',
    initComponent: function () {
        var msResImpStoreIns = new KitchenSink.view.interviewManagement.interviewManage.msResImpStore();
        var msResImpJoinStateTVStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_JOIN_STATE");
        var msResImpItwResultTVStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_RESULT");

        Ext.apply(this, {
            items:[{
                xtype: 'grid',
                store: msResImpStoreIns,
                //frame: true,
                height: 370,
                name: 'msResImpGrid',
                reference: 'msResImpGrid',
                columnLines: true,
                //style: "margin:10px",
                resize:false,
                plugins:[
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1,   //单击进行编辑
                        pluginId: 'msResImpGridCellEditing'
                    })
                ],

                //改变行背景色
                /*
                viewConfig: {
                    getRowClass: function(record) {
                        Ext.util.CSS.createStyleSheet(".refuserow .x-grid-cell {background-color: #FF0000;}","msResImpCkRowColor");
                        return "refuserow";
                    }
                },
                */
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.appId","报名表编号") ,
                    dataIndex: 'appId',
                    width:125,
                    //flex:1,
                    editor:{
                        xtype:'textfield',
                        allowBlank:false
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.stuName","姓名"),
                    dataIndex: 'stuName',
                    width:100,
                    //flex:1,
                    editor:{
                        xtype:'textfield',
                        allowBlank:false
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.msJoinState","是否参加"),
                    dataIndex: 'msJoinState',
                    width:100,
                    editor:{
                        xtype: 'combobox',
                        //emptyText:"请选择",
                        name: 'msJoinStaCb',
                        queryMode: 'remote',
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        editable: false,
                        store:msResImpJoinStateTVStore
                        //allowBlank:false
                    },
                    renderer : function(value, metadata, record) {
                        var index = msResImpJoinStateTVStore.find('TValue',value);
                        if(index!=-1){
                            return msResImpJoinStateTVStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msJoinState');
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.msResult","面试结果"),
                    dataIndex: 'msResult',
                    width:100,
                    editor:{
                        xtype: 'combobox',
                        //emptyText:"请选择...",
                        name: 'msResultCb',
                        queryMode: 'remote',
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        editable: false,
                        store:msResImpItwResultTVStore
                        //allowBlank:false
                    },
                    renderer : function(value, metadata, record) {
                        var index = msResImpItwResultTVStore.find('TValue',value);
                        if(index!=-1){
                            return msResImpItwResultTVStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msResult');
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.msArrDemo","备注") ,
                    dataIndex: 'msArrDemo',
                    width:200,
                    editor:{
                        xtype:'textfield'
                        //allowBlank:false
                    }
                },
                    /*
                     {
                     text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.checkRes","校验") ,
                     dataIndex: 'checkRes',
                     width:50,
                     hidden:true
                     },
                     */

                    {
                        xtype: 'actioncolumn',
                        header:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.checkRes","校验") ,
                        menuDisabled: true,
                        sortable: false,
                        width:30,
                        items:[
                            {
                                iconCls: '',
                                tooltip: '',
                                handler:'dataCKTip',
                                getClass: function(v, meta, record){
                                    if(record.data.checkRes==undefined||record.data.checkRes=="-1"){
                                        return '';
                                    }else if(""==record.data.checkRes||" "==record.data.checkRes){
                                        meta['tdAttr'] = 'data-qtip=""';//动态设置tooltip
                                        return 'pass';
                                    }else{
                                        var str = record.data.checkRes.replace(/\s+/g,"");
                                        meta['tdAttr'] = "data-qtip="+str+"";//动态设置tooltip
                                        return 'refuse';
                                    }
                                }
                            }
                        ]
                    },{
                        xtype: 'actioncolumn',
                        width:30,
                        header:'操作',
                        menuDisabled: true,
                        sortable: false,
                        //flex:1,
                        items:[
                            {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrMsResImpRec'}
                        ]
                    }]
            },{
                xtype: 'form',
                fieldDefaults:{
                    labelWidth:620,
                    labelAlign:"left"
                },
                items:[{
                    xtype:"displayfield",
                    fieldLabel:"",
                    name:"classID",
                    hidden:true
                },{
                    xtype:"displayfield",
                    fieldLabel:"",
                    name:"batchID",
                    hidden:true
                },{
                    xtype:"displayfield",
                    fieldLabel:" ",
                    labelSeparator:"",
                    name:"ImpRecsCount"
                }]
            }]

        });

        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.butSave","保存") ,
        iconCls:"save",
        handler: 'onWinSave'
    }, {
        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.butEnsure","确定") ,
        iconCls:"ensure",
        handler: 'onWinConfirm'
    }, {
        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MSRESIMP_STD.butClose","关闭") ,
        iconCls:"close",
        handler: 'onWinClose'
    }]
});


