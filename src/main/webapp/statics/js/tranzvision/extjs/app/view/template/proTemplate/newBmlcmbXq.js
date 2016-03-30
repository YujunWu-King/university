Ext.define('KitchenSink.view.template.proTemplate.newBmlcmbXq',
{
    extend : 'Ext.tab.Panel',
	xtype : 'side-navigation-tabs_bmlc',
	config : {
        fields : undefined 
    },
	isLoaded:false,
	predefinetpl:'',
    height : 400,
	width : 800,
	ui : 'navigation',
	tabPosition : 'left',
	tabRotation : 0,
	tabBar : {
        // turn off borders for classic theme.  neptune and crisp don't need this
        // because they are borderless by default
        border : false 
    },
    defaults : {
        textAlign : 'left',
		bodyPadding : 15 
    },
	items:'',
	initComponent: function(){
		Ext.apply(this,{
			items : [
				{
					itemId: 'add',
					title:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.xjkbmb","新建空白模板") ,
					glyph: 72,
					html:(function(){
						var divTmp = document.createElement("div");
						var txt = Ext.create('Ext.form.field.Text', {
							 fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.mbmc","模板名称"),
							 labelStyle: 'font-weight:bold',
							 cls:'bmb_blank_text',
							 allowBlank: false,
								value:''
						}).render(divTmp)
						return divTmp.outerHTML;
					})()+'<div>'+Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.kbmbms","空白模板描述")+'</div>'
				},
				{
					itemId: 'predefine',
					title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.syydymb","使用预定义模板"),
					glyph: 117,
					html: (function(){
						var divTmp = document.createElement("div");
						var txt = Ext.create('Ext.form.field.Text', {
							 fieldLabel:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.mbmc","模板名称"),
							 value:'',
							 labelStyle: 'font-weight:bold',
							 cls:'bmb_predefine_text',
							 allowBlank: false
						}).render(divTmp)
						return divTmp.outerHTML;
					})() + '<div class="predefinetpllist" style="overflow-y:auto;height:350px;">'+ this.getPredefinetpl() +"</div>"
				}
			]
		});
		this.callParent();
	},
    getPredefinetpl:function(){
        var me = this, predefinetpl = '';

        if(!me.isLoaded){

            var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_NEWlIST_STD","OperateType":"QF","comParams":""}';
            Ext.Ajax.request({
                //url: 'http://202.120.24.169:9550/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_REG.TZ_TPL_SET.FieldFormula.IScript_GetPreDefineTpl1',
               //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_ZSGL_D.TZ_ZSGL.FieldFormula.IScript_TZ_ZSGL',
                //url: '/psc/TZTST/EMPLOYEE/CRM/s/WEBLIB_ZSGL_D.TZ_ZSGL.FieldFormula.IScript_TZ_ZSGL',
                //url: '/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_ZSGL_D.TZ_ZSGL.FieldFormula.IScript_TZ_ZSGL',
               url:Ext.tzGetGeneralURL(),
                async:false,
                params: {
                    tzParams: tzParams
                    //usAccNum: 1
                },
                waitTitle : Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.pleaseWait","请等待") ,
                waitMsg: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.loading","加载中"),
                success: function(response){
                    var resText1 = response.responseText;
                    var responseData1 = Ext.JSON.decode(resText1)
                    var resText = responseData1.comContent;
                    //var responseData = Ext.JSON.decode(resText);
                    var responseData = resText;
                    for(var i in responseData){
                        predefinetpl += '<div class="tplitem" style="padding: 10px;cursor: pointer;border: 1px solid #eee;display: inline-table;margin: 5px;text-align:center;width:176px;" onclick="mybmb_cj(this)" data-id="'+responseData[i].tplid+'"><img src="' + TzUniversityContextPath + '/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png"><br><span class="tplname" title="' + responseData[i].tplname + '">' + Ext.String.ellipsis(responseData[i].tplname,16,true) + '</span></div>';
                    }
                    me.isLoaded = true;
                }
            });
        }
        return predefinetpl;
    }
});