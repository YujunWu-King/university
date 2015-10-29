Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.studentsList', {
    extend: 'Ext.panel.Panel',
    xtype: 'studentsList',
    controller: 'studentsList',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.bmb_lc.studentsStore',
        'KitchenSink.view.enrollmentManagement.bmb_lc.studentsController'
    ],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.lcjgfb","流程结果发布"),
    bodyPadding: 8,
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 42;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid)grid.setHeight( height -buttonHeight -16);
        }
    },
    initComponent: function (){
       
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'studentsForm',
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:'班级ID',
                    name: 'classID',
                    hidden:true
                }]
            }]
        });
        this.callParent();
    },
    buttons: [  {
        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});
