Ext.define('KitchenSink.view.enrollmentManagement.tag.tagList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.tag.tagStore',
        'KitchenSink.view.enrollmentManagement.tag.tagController'
    ],
    xtype: 'tagSet',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'tagSet',
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tags","标签定义"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.save","保存"),iconCls:"save",handler:'saveTags'}]
    },{
        xtype:"toolbar",
        items:[
            {text:Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.add","新增"),iconCls:"add",handler:'addTag'},
            {text:Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.delete","删除"),iconCls:"remove",handler:'deleteTags'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.tag.tagStore();
        Ext.apply(this, {
            plugins: {
                ptype: 'cellediting',
                pluginId: 'tagCellEditing'
                //	clicksToEdit: 1
            },
            columns: [{
                    text: Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagName","标签名称"),
                    dataIndex: 'tagName',
                    flex:1,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },{
                    menuDisabled: true,
                    sortable: false,
                    width:40,
                    align:'center',
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.delete","删除"),handler:'deleteTag'}
                    ]
                }],
            store: store,
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
