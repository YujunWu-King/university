Ext.define('KitchenSink.view.tzLianxi.ldd.zxdc.wjmbInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'wjmbInfoPanel',
    reference: 'wjmbInfoWindow',
    controller: 'zxdcMbController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.tzLianxi.ldd.zxdc.zxdcMbListStore'
       // 'KitchenSink.view.proTemplate.orgMemListStore'
    ],
    title: '报名流程模板详情',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'bmlcInforForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        //bodyPadding: 10,
        style:"margin:8px",
        //heigth: 300,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            //fieldLabel: '机构编号',
            fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_TMP_ID","报名流程模板编号"),
            name: 'TZ_APPPRO_TMP_ID',
            hidden:true

        }, {
            xtype: 'textfield',
            //fieldLabel: '机构名称',
            fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_TMP_NAME","报名流程模板名称"),
            name: 'TZ_APPPRO_TMP_NAME',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'combobox',
            //fieldLabel: '有效状态',
            fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_STATUS","启用"),
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_APPPRO_STATUS"),
            typeAhead: true,
            queryMode: 'local',
            name: 'TZ_APPPRO_STATUS',
            value:'Y'
        }]
    },{
        xtype: 'grid',
        height: 330,
        title: '报名流程配置',
        reference:'protmpDetSetGrid',
        frame: true,
        columnLines: true,
        style:"margin:10px",
        selModel: {
            type: 'checkboxmodel'
        },
        plugins: {
            ptype: 'cellediting',
            pluginId: 'dataCellediting',
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
                        items[i].set('TZ_SORT_NUM',i+1);
                    }
                    data.view.store.endUpdate();
                }
            }
        },
        columns: [{
            text: '模板编号',
            dataIndex: 'TZ_APPPRO_TMP_ID',
            hidden: true
        },{
            text: '流程编号',
            dataIndex: 'TZ_APPPRO_ID',
            hidden: true
        },{
            text: '显示顺序',
            dataIndex: 'TZ_SORT_NUM',
            hidden: true,
            width: 130
        },{

            text: '流程名称',
            dataIndex: 'TZ_APPPRO_NAME',
            width: 430,
            flex:1,
            editor: {
                xtype:'textfield'
            }
        },{
            xtype:'linkcolumn',
            sortable: false,
            width: 80,
            text: '设置',
            hidden:true,

            items:[{
                text: '设置',
                handler:'editDataInfo'
            }]
        },{
            xtype:'linkcolumn',
            sortable: false,
            flex:1,
            width: 380,
            text: '回复语模板设置',

            items:[{
                text: '回复语模板设置',
               // handler:'editDataInfo2'
               // handler:'editSelbackmsg'
                handler:'bmlcbackmsgedit'
            }]
        },{
            menuDisabled: true,
            sortable: false,
            align: 'center',
            text: '操作',
            width:60,
            xtype: 'actioncolumn',
            items:[
               // {iconCls: 'edit',tooltip: '编辑',handler:'editDataInfo'},
                //{iconCls: 'add',tooltip: '添加', handler: 'addData'},
                {iconCls: 'remove',tooltip: '删除',handler:'deleteDataInfo'}
            ]
        }],
       store: {
            type: 'bmlcmbMemListStore'
            //store:new KitchenSink.view.template.proTemplate.bmlcmbdetailModel()
        },
        dockedItems:[{
            xtype:"toolbar",
            items:[
                //{text:"查询",tooltip:"查询数据",iconCls: "query"},"-",
                 {text:"新增",tooltip:"添加报名流程",iconCls:"add",handler:'addProDataInfo'},
                //{text:"新增",tooltip:"添加报名流程",iconCls:"add",handler:'addDataInfo3'},'-',
                {text:"删除",tooltip:"删除",iconCls:"remove",handler:'deleteDataInfo3'},'-',
                {text:"设置",tooltip:"设置",iconCls:"edit",handler:'editDataInfo4'},'->'
            ]
        }],
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 5,
           // reference: 'adminUserToolBar',
            //store:new KitchenSink.view.template.proTemplate.bmlcmbdetailModel(),
            listeners:{
                afterrender: function(pbar){
                    var grid = pbar.findParentByType("grid");
                    pbar.setStore(grid.store);
                }
            },
            displayInfo: true,
            displayMsg: '显示{0}-{1}条，共{2}条',
            beforePageText: '第',
            afterPageText: '页/共{0}页',
            emptyMsg: '没有数据显示',
            plugins: new Ext.ux.ProgressBarPager()
        }
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onProFormSave'
    },{
        text: '确定',
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onFormClose'
    }]
});
