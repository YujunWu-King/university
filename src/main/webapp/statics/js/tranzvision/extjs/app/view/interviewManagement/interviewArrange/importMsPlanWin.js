Ext.define('KitchenSink.view.interviewManagement.interviewArrange.importMsPlanWin', {
    extend: 'Ext.window.Window',
    xtype: 'importMsPlanWin',
    controller: 'interviewArrangeController',
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeController'
    ],
	
    modal:true,//背景遮罩
    resizable:false,
    width: 600,
    height: 145,
    ignoreChangesFlag:true,
    title: '导入面试日程安排',
            
    constructor:function(conf){
        this.classID = conf.classID;
        this.batchID = conf.batchID;
        this.GridReload = conf.callback;
        
        this.callParent();
    },
    initComponent: function(){
        var me =this;

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding:10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                	xtype: 'label',
                	text: '上传Excel文件'
                },{
                	layout:'column',
                    items:[{
                        xtype: 'filefield',
                        name: 'orguploadfile',
                        itemId:'orguploadfile',
                        msgTarget: 'side',
                        allowBlank: false,
                        anchor: '100%',
                        buttonText: '浏览...',
                        columnWidth: 1,
                        validator:function(value){
                            var excelReg = /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/;
                            if(!excelReg.test(value)&&value){
                                Ext.Msg.alert('提示','文件类型错误,请选择 [xls,xlsx] 格式的Excel文件');
                                return '文件类型错误,请选择 [xls,xlsx] 格式的Excel文件';
                            }else{
                                return true
                            }
                        }
                    },{
                        xtype:'toolbar',
                        name:'downloadExcelTpl',
                        padding:0,
                        style:'margin-left:20px',
                        items:[
                            {
                                xtype:'button',
                                text:'<span class="themeColor" style="text-decoration:underline;color:#00F;">下载Excel模板</span>',
                                cls:'themeColor',
                                border:false,
                                style:{
        							background: 'white',
        							boxShadow:'none'
        						},
        						handler: 'downloadMsPlanTmp'
                            }
                        ]
                    }]
                }]
            }]
        });
        this.callParent();
    },
    buttons: [{
		text: '确定导入',
		iconCls:"ensure",
		handler: 'onEnsureImportMsPlan'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType('window').close();
		}
	}]
});
