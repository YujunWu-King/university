Ext.define('KitchenSink.view.common.externalLinkPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.ux.Ueditor'
    ],
    xtype: 'externallink',
    externalLink:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent: function(){
        var me = this;
        Ext.apply(this,{
            contentEl : Ext.DomHelper.append(document.body, {
                bodyBorder : false,
                tag : 'iframe',
                style : "border:0px none;scrollbar:true",
                src : me.externalLink,
                name : 'tzTargetContent20130513',
                height : "100%",
                width : "100%"
            }),
            buttons: [
                {
                text: '关闭',
                iconCls:"close",
                handler: function(btn){
                    btn.findParentByType("panel").close();
                }
                }]
        });
        this.callParent();
    }
});