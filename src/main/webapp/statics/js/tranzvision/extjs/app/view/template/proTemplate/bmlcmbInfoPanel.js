Ext.define('KitchenSink.view.template.proTemplate.bmlcmbInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'bmlcmbInfoPanel',
    reference: 'bmlcmbInfoWindow',
    controller: 'proTemplate',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.template.proTemplate.bmlcmbMemListStore'
       // 'KitchenSink.view.proTemplate.orgMemListStore'
    ],
    title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.bmlcmbxq","报名流程模板详情"),
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
            fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_STATUS","状态"),
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
        title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.bmlcpz","报名流程配置"),
        reference:'protmpDetSetGrid',
        frame: true,
        columnLines: true,
        style:"margin:10px",
        selModel: {
            type: 'checkboxmodel'
        },
       /* plugins: {
            ptype: 'cellediting',
            pluginId: 'dataCellediting',
            clicksToEdit: 1
        },*/
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

            text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_NAME","流程名称"),
            dataIndex: 'TZ_APPPRO_NAME',
            width: 430,
            flex:1
        },{
            xtype:'linkcolumn',
            sortable: false,
            width: 80,
            text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.set","设置"),
            hidden:true,
            items:[{
                text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.set","设置"),
                handler:'editDataInfo'
            }]
        },{
            xtype:'linkcolumn',
            sortable: false,
            flex:1,
            width: 380,
            text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.bmlcbackmsgedit","回复语模板设置"),

            items:[{
                text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.bmlcbackmsgedit","回复语模板设置") ,
                handler:'bmlcbackmsgedit'
            }]
        },{
            menuDisabled: true,
            sortable: false,
            align: 'center',
            width:60,
            xtype: 'actioncolumn',
            items:[
                {iconCls:'edit',tooltip:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.edit","编辑"),handler:'editDataInfo'},
                {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.delete","删除"),handler:'deleteDataInfo'}
            ]
        }],
       store: {
            type: 'bmlcmbMemListStore'
        },
        dockedItems:[{
            xtype:"toolbar",
            items:[
                //{text:"查询",tooltip:"查询数据",iconCls: "query"},"-",
                 {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.add","新增"),iconCls:"add",handler:'addProDataInfo'},
                {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.edit","编辑"),iconCls:"edit",handler:'editDataInfo5'},
                ,'-',
                {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.set","设置"),iconCls:"set",handler:'editDataInfo4'}
                ,'-',
                {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.delete","删除"),iconCls:"remove",handler:'deleteDataInfo3'}
                ,'->'
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
            plugins: new Ext.ux.ProgressBarPager()
        }
    }],
    buttons: [{
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.save","保存"),
        iconCls:"save",
        handler: 'onProFormSave'
    },{
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.colse","关闭"),
        iconCls:"close",
        handler: 'onFormClose'
    }]
});
