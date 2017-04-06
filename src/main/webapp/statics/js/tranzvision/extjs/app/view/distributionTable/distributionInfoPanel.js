Ext.define('KitchenSink.view.distributionTable.distributionInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'distributionInfo',
    controller: 'distributionMgController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.distributionTable.distributionDetailStore'
    ],
    title: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.distributionSet","分布对照表设置"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',

    constructor: function(config, callback){
        this.actType = config.actType;
        this.reloadGrid = callback;

        this.callParent();
    },

    initComponent: function () {
        var me = this;
        //有效状态Store
        var effeStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_ISVALID");
        //操作符Store
        var operatorSXStore = new KitchenSink.view.common.store.appTransStore("TZ_OPERATOR_SX");
        var operatorXXStore = new KitchenSink.view.common.store.appTransStore("TZ_OPERATOR_XX");

        var detailStore = new KitchenSink.view.distributionTable.distributionDetailStore();

        var hiddenTab = false;
        var distrIdReadOnly = false;
        var modeIdCls = "";
        if(this.actType == "Add"){
            hiddenTab = true;
        }else{
            distrIdReadOnly = true;
            modeIdCls = "lanage_1";
        }


        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'distributionForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 140,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'hiddenfield',
                    name: 'orgId'
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.distrId","编号"),
                    name: 'distrId',
                    allowBlank: false,
                    readOnly: distrIdReadOnly,
                    cls: modeIdCls,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.distrName","分布对照表名称"),
                    name: 'distrName',
                    maxLength: 50,
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'combo',
                    fieldLabel: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.status","状态"),
                    name: 'status',
                    queryMode: 'local',
                    editable:false,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    store: effeStatusStore,
                    allowBlank: false,
                    value:'Y'
                }]
            },{
                xtype: 'grid',
//                    title: "分布对照表明细设置",
                tabType: 'A',
                firstLoad: true,
                height: 350,
                frame: true,
                columnLines: true,
                name: 'detailGrid',
                reference: 'detailGrid',
                style:"margin:10px",
                selModel: {
//                        type: 'checkboxmodel'
                    type: 'rowmodel'
                },
                store: detailStore,
                dockedItems:[{
                 xtype:"toolbar",
                 items:[
                 {
                 text:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.add","新增"),
                 iconCls:"add",
                 handler:"addItem"
                 }/*,"-",
                 {
                 text:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.remove","删除"),
                 iconCls:"remove",
                 handler:'deleteScoreItems'
                 }*/
                 ]
                 }],
                plugins: {
                    ptype: 'cellediting',
                    pluginId: 'detailCellediting',
                    clicksToEdit: 1
                },
                viewConfig: {
                    plugins: {
                        ptype: 'gridviewdragdrop',
                        containerScroll: true,
                        dragGroup: this,
                        dropGroup: this
                    },
                    listeners: {
                        drop: function(node, data, dropRec, dropPosition) {
                            data.view.store.beginUpdate();
                            var items = data.view.store.data.items;
                            for(var i = 0;i< items.length;i++){
                                items[i].set('sortNum',i+1);
                            }
                            data.view.store.endUpdate();
                        }
                    }
                },
                columns: [{
                    text : '',
                    dataIndex : 'id',
                    xtype : 'rownumberer',
                    width : 50,
                    align : 'center'
                },{
                    text:"sortNum",
                    dataIndex: 'sortNum',
                    width:50,
                    hidden: true
                },{
                    text:"分布对照编号",
                    dataIndex: 'distrId',
                    width:50,
                    hidden: true
                },{
                    text:"分布对照明细编号",
                    dataIndex: 'distrMXId',
                    width:50,
                    hidden: true
                },{
                    text: "序号",
                    dataIndex: 'sequence',
                    width:60,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },{
                    text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.name","名称"),
                    dataIndex: 'name',
                    width:120,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },{
                    text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.explain","说明"),
                    dataIndex: 'explain',
                    width:120,
                    flex: 1,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },{
                    text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.lowOpt","下限操作符"),
                    dataIndex: 'lowOpt',
                    width:120,
                    editor: {
                        xtype: 'combo',
                        queryMode: 'local',
                        editable:false,
//                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: operatorXXStore,
                        allowBlank: false
                    }
                },{
                    text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.lowScore","分值下限"),
                    dataIndex: 'lowScore',
                    width:120,
                    editor: {
//                        xtype: 'textfield',
                        xtype: 'numberfield',
                        minValue: 0,
                        allowBlank: false
                    }
                },{
                    text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.upOpt","上限操作符"),
                    dataIndex: 'upOpt',
                    width:120,
                    editor: {
                        xtype: 'combo',
                        queryMode: 'local',
                        editable:false,
//                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: operatorSXStore,
                        allowBlank: false
                    }
                },{
                    text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.upScore","分值上限"),
                    dataIndex: 'upScore',
                    width:120,
                    editor: {
//                        xtype: 'textfield',
                        xtype: 'numberfield',
                        minValue: 0,
                        allowBlank: false
                    }
                },
                    {
                        menuDisabled: true,
                        sortable: false,
                        width:60,
                        xtype: 'actioncolumn',
                        align: 'center',
                        items:[
                            {iconCls: 'add',tooltip:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.add","新增"), handler: 'addItem'},
                            {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.remove","删除"), handler: 'deleteCurrentRow'}
                        ]
                    }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store:detailStore,
                    plugins: new Ext.ux.ProgressBarPager()
                }
                //}]
            }]
        });

        this.callParent();
    },
    buttons: [{
        text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.save","保存"),
        iconCls:"save",
        closePanel: 'N',
        handler: 'onFormSave'
    }, {
        text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.ensure","确定"),
        iconCls:"ensure",
        closePanel: 'Y',
        handler: 'onFormEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.close","关闭"),
        iconCls:"close",
        handler: 'onFormClose'
    }]
});

