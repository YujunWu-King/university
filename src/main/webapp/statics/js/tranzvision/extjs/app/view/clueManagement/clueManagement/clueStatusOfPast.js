Ext.define('KitchenSink.view.clueManagement.clueManagement.clueStatusOfPast', {
    extend: 'Ext.panel.Panel',
    title: "过往状态",
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.timeLine'
    ],
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor:function(obj,transValue){
        var items;
        var desc = obj.leadDesc,
            obj = obj.content,
            statusBefore=[],
            status=[];
            obj.forEach(function(v){
                var x;
                if ((x = transValue.find('TValue', v.status)) >= 0) {
                    status.push(transValue.getAt(x).data.TSDesc);
                }
				if ((x = transValue.find('TValue', v.statusBefore)) >= 0) {
                    statusBefore.push(transValue.getAt(x).data.TSDesc);
                }
            });
            var points=[{
                xtype:'form',
                style:'padding:"0 15px 15px 15px"',
                fieldDefaults: {
                    labelStyle: 'font-weight:bold;width:68px',
                    labelWidth:68,
                    fieldStyle: 'font-weight:bold;'
                },
                items:[{
                    xtype:'displayfield',
                    fieldLabel:'线索摘要',
                    value:desc,
                    style:'margin:auto;'
                }]
            }];
            for(var x=0;x<obj.length;x++){
                points.push({
                    xtype: 'timePoint',
                    autoLine: true,
                    sinceText:obj[x].time,
                    imgSrc:obj[x].headerImgSrc,
                    content:{
                        xtype: 'form',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        fieldDefaults: {
                            msgTarget: 'side',
                            labelWidth: 110,
                            labelStyle: 'font-weight:bold;color:#35baf6'
                        },
                        items:[{
                            xtype:'displayfield',
                            fieldLabel: "操作人",
                            value:obj[x].name
                        },{
                            xtype:'displayfield',
                            fieldLabel: "操作前状态",
                            value:statusBefore[x]
                        },{
                            xtype:'displayfield',
                            fieldLabel: "操作后状态",
                            value:status[x]
                        },{
                            xtype:'displayfield',
                            fieldLabel: "说明",
                            value:obj[x].descript
                        }]
                    }
                });
            }
            items = {
                xtype:'timeLine',
                margin: 8,
                items: points
            };
        Ext.apply(this,{
            items:[items],
            buttons:[{
                text:'关闭',
                iconCls:'close',
                handler:function() {
                    this.findParentByType("panel").close();
                }
            }]
        });
        this.callParent();
    }
    
});