Ext.define('KitchenSink.view.template.proTemplate.bmlcmbMgInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.proTemplate.bmlcmbMgController',
        'KitchenSink.view.template.proTemplate.bmlcmbModel',
        'KitchenSink.view.template.proTemplate.bmlcmbListStore'

    ],
    xtype: 'proTemplate',
    controller: 'proTemplate',
    reference:'protmpDtlist',
    store: {
        //type: 'orgListStore'
        type: 'bmlcmbListStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.bmlcmbgl","报名流程模板管理"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.save","保存"),iconCls:"save",handler:'onPanelSave'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.ensure","确定"),iconCls:"ensure",handler:'onPanelEnsure'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.close","关闭"),iconCls:"close",handler:'onPanelClose'}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.query","查询"),iconCls:"query",handler:'queryProTmp'},'-',
            {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.add","新增"),iconCls:"add",handler:'addDataModelPro'},'-',
            {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.edit","编辑"),iconCls:"edit",handler:'editProTmp'},'-',
            {text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.delete","删除"),iconCls:'remove',handler:'deleteProTmp'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.template.proTemplate.bmlcmbListStore();
        Ext.apply(this, {
            columns: [
               // new Ext.grid.RowNumberer() ,
                {
                    text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMBGL_STD.TZ_APPPRO_TMP_NAME","模板名称"),
                    sortable: true,
                    dataIndex: 'TZ_APPPRO_TMP_NAME',
                    width: 320
                },{
                    text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMBGL_STD.TZ_APPPRO_STATUS","启用"),
                    dataIndex: 'TZ_APPPRO_STATUS',
                    width: 300,
                    renderer:function(v){
                        if(v=='Y'){
                            return "已启用";
                        }else{
                            return "未启用";
                        }
                    },
                    flex:1
                },{
                    menuDisabled: true,
                    sortable: false,
                    width:100,
                    align: 'center',
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'edit',text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.edit","编辑"),handler:'editSelSmtDtTmp'},
                        {iconCls: 'remove',text:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.delete","删除"),handler:'deleteSelSmtDtTmp'}
                    ]
                }],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});
