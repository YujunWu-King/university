Ext.define('KitchenSink.view.enrollmentManagement.color.colorList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.colorpick.Field',
        'KitchenSink.view.enrollmentManagement.color.colorStore',
        'KitchenSink.view.enrollmentManagement.color.colorController'
    ],
    xtype: 'colorSet',
    name:'colorSetGrid',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'colorSet',
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colors","颜色类别定义"),
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
            {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.save","保存"),iconCls:"save",handler:'saveColors'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.ensure","确认"),iconCls:"ensure",handler: 'ensureColors'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.close","关闭"),iconCls:"close",handler: 'closeColors'}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.add","新增"),iconCls:"add",handler:'addColor'},"-",
            {text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.edit","编辑"),iconCls:"edit",handler:"editColor"},"-",
            {text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.delete","删除"),iconCls:"remove",handler:'deleteColors'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.color.colorStore();
        Ext.apply(this, {
            plugins: {
                ptype: 'cellediting',
                pluginId: 'colorSortCellEditing'
                //	clicksToEdit: 1
            },
            columns: [
                {
                    text: Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorSortID","颜色ID"),
                    dataIndex: 'colorSortID',
                    hidden:true
                },
                {
                    text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.color","颜色"),
                    width:160,
                    bind: '{color}',
                    editable:false,
                    dataIndex: 'colorCode' ,
                    editor: {
                        xtype: 'colorfield',
                        allowBlank: false
                    },
                    renderer:function(value){
                        return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
                    }
                },{
                    xtype: 'colorselector',
                    hidden: true,
                    flex: 1,
                    bind: {
                        value: '{color}',
                        visible: '{full}'
                    }
                },{
                    text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorName","颜色类别名称") ,
                    dataIndex: 'colorName',
                    minWidth: 180,
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
                        {iconCls:"edit",tooltip:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.edit","编辑"),handler:"editCurrentColor"},
                        {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.delete","删除"),handler:'deleteColor'}
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
