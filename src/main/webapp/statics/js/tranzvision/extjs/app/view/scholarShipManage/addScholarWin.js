Ext.define('KitchenSink.view.scholarShipManage.addScholarWin', {
    extend: 'Ext.window.Window',
    xtype: 'addScholarWin',
    reference: 'addScholarWin',
    controller: 'scholarController',
    title: '新建奖学金',
    closable: true,
    modal: true,
    autoScroll: true,
    bodyStyle: 'padding: 5px;',
    width:700,
    actType: 'add',
    initComponent:function(){
         Ext.apply(this,{
             items: [{
                 xtype: 'form',
                 layout: {
                     type: 'vbox',
                     align: 'stretch'
                 },
                 border: false,
                 style:"margin:8px",
                 bodyStyle:'overflow-y:auto;overflow-x:hidden',
                 fieldDefaults: {
                     msgTarget: 'side',
                     labelWidth: 95,
                     labelStyle: 'font-weight:bold'
                 },
             items:[{
                 xtype:'textfield',
                 fieldLabel: '奖学金名称',
                 value:'',
                 labelStyle: 'font-weight:bold',
                 cls:'bmb_predefine_text',
                 name:'predefine',
                 id:'shcolarName',
                 allowBlank: false
                 },{ 
                   html:'<div class="predefinetpllist" style="overflow-y:auto;height:330px;">'+ this.getWjTpls() +"</div>"
                 }]
             }],
             buttons: [{
                 text: '确定',
                 iconCls: "ensure",
                 handler:'scholarNewEnsure'
             },{
                 text: '关闭',
                 iconCls: "close",
                 handler:'scholarNewClose'
             }]
         }),
         this.callParent();
     },
    getWjTpls:function(){
        var me = this, predefinetpl = '';
        if(!me.isLoaded){
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJGL_STD","OperateType":"QF","comParams":""}';
            Ext.Ajax.request({
                url:Ext.tzGetGeneralURL(),
                async:false,
                params: {
                    tzParams: tzParams
                },
                waitTitle : '请等待' ,
                waitMsg: '正在加载中',
                success: function(response){
                    var resText1 = response.responseText;
                    var responseData1 = Ext.JSON.decode(resText1);
                    var resText = responseData1.comContent;
                    var responseData = resText;
                    for(var i in responseData){ 
                        predefinetpl += '<div class="tplitem" style="padding: 10px;cursor: pointer;border: 1px solid #eee;display: inline-table;margin: 5px;text-align:center;width:150px;" onclick="wjdc_pre(this)" data-id="'+responseData[i].tplid+'"><img src="' + TzUniversityContextPath + '/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png"><br><span class="tplname" title="' + responseData[i].tplname + '">' + Ext.String.ellipsis(responseData[i].tplname,16,true) + '</span></div>';
                    }
                    me.isLoaded = true;
                }
            });
        }
        return predefinetpl;
    } 
});
