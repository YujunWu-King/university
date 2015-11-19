Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.classStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.classController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appFormAuditing',
    columnLines: true,
    controller: 'appFormClass',
    name:'appFormClass',
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicationFormAuditing","报名表审核"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
        xtype:"toolbar",
        items:[
            {
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query","查询"),
                iconCls:"query",
                handler:'queryClass'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationForm.classStore();
        Ext.apply(this, {
            columns: [{
                xtype: 'rownumberer',
                width:50
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.className","班级名称"),
                dataIndex: 'className',
                minWidth:180,
                flex:1
            },{
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.projectName","所属项目"),
                dataIndex: 'projectName',
                minWidth:120,
                flex:1
            },{
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.projectType","项目类别"),
                dataIndex: 'projectType',
                minWidth:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicantsNumber","报名人数"),
                dataIndex: 'applicantsNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.noauditNumber","未审核人数"),
                dataIndex: 'noauditNumber',
                width:140,
                renderer: function(val){
                    if(val>0){
                        return '<span  style="color: #ED0048;" >'+val+'</span>';
                    }else{
                        return val;
                    }
                }
            },{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.viewApplicants","查看报考学生"),
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewApplicants"
                }],
                width:130
            },{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.publishResult","报名流程结果公布"),
                width:160,
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "publishResult"
                }]
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

