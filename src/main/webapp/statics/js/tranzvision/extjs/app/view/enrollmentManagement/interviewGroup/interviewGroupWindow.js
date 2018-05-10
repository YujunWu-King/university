Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupWindow', {
    extend: 'Ext.window.Window',
    reference: 'interviewGroupWindow',
    controller: 'appFormInterview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupModel',
        'KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupStore'
    ],
    title: '面试分组',
    bodyStyle:'overflow-y:hidden;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    width: 650,
    y:10,
    minWidth: 400,
    minHeight: 350,
   // maxHeight: 460,
    resizable: true,
    modal:true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    viewConfig: {
        enableTextSelection: true
    },
    items: [{
        xtype: 'form',
        reference: 'classForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            hidden:true,
            name: 'appInsId'
        }, {
            xtype: 'textfield',
            hidden:true,
            name: 'classID'
        }, {
            xtype: 'textfield',
            hidden:true,
            name: 'batchID'
           
        }, {
        	xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.auditStates","评委组"),
            forceSelection: true,
			editable: false,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_MSPS_GR_TBL',
				condition:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
			}),
			valueField: 'TZ_CLPS_GR_ID',
			displayField: 'TZ_CLPS_GR_NAME',
			queryMode: 'local',
			name: 'jugGroupId',
            listeners: {	// select监听函数  
            	select: 'changeResTmpl'
            }
            
        }]
    },{
        xtype:'tabpanel',
        items:[{
            xtype: 'grid',
            height: 'auto',
            title: '面试组',
            autoHeight:true,
            minHeight:120,
            id:'pageGrid',
            columnLines: true,
            reference: 'pageGrid',
            //style:"margin:10px",
            store: {
                type: 'interviewGroupStore'
            },
            plugins : [{
				ptype : 'cellediting',
				clicksToEdit : 1
			}],
            columns: [{
                xtype: 'checkcolumn',
                text: "选择",
                dataIndex: 'check',
                sortable:false,
                width: 70
            },{
                text: '面试组ID',
                dataIndex: 'groupID',
                width: 150,
                sortable:false
            },{
                text: '面试组名称',
                dataIndex: 'groupName',
                minWidth: 150,
                sortable:false,
                editor:{xtype:'textfield',allowBlank:false},
                flex:1
            },{
                text: '已经安排人数',
                dataIndex: 'suNum',
                minWidth: 250,
                sortable:false,
                flex:1
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                //pageSize: 10,
                reference: 'rolePlstToolBar',
                listeners:{
                    afterrender: function(pbar){
                        var grid = pbar.findParentByType("grid");
                        pbar.bindStore(grid.store);
                        pbar.pageSize=10;
                        //pbar.setStore(grid.store);
                    }
                },
                plugins: new Ext.ux.ProgressBarPager()
            }
        }]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onGroupSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onGroupEnsure'
    },{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            //关闭窗口
            win.close();
        }
    }]
});
