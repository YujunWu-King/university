Ext.define('KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUp', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpMode',
        'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpStore',
		'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpController'
    ],
    xtype: 'webSiteSetUp',
	controller: 'webSiteSetUpController',
	/*store: {
        type: 'comStore'
    },*/
	listeners: {
		afterrender: function(p){
						
		}
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '招生网站设置',
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
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeWebSiteSetUp'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchWebSiteUp"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addWebSiteInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editWebSiteInfo"}
		]
	}],
    initComponent: function () { 
	    //招生网站设置
	    var store = new KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpStore();
	    store.load({
            callback: function(records, options, success) {
            	var item = "siteid";
    			var svalue = location.search.match(new RegExp("[\?\&]" + item + "=([^\&]*)(\&?)","i"));
    			if(svalue != null && svalue != ""){
    				var siteId = svalue ? svalue[1] : svalue;
    				
    				Ext.tzSetCompResourses("TZ_USER_REG_COM");
    				//是否有访问权限
    				var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_USER_REG_COM"]["TZ_REGGL_STD"];
    				if( pageResSet == "" || pageResSet == undefined){
    					Ext.MessageBox.alert('提示', '您没有修改数据的权限');
    					return;
    				}
    				//该功能对应的JS类
    				var className = pageResSet["jsClassName"];
    				if(className == "" || className == undefined){
    					Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_REGGL_STD，请检查配置。');
    					return;
    				}
    				
    				var contentPanel,cmp, className, ViewClass, clsProto;
    				var themeName = Ext.themeName;
    				
    				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
    				contentPanel.body.addCls('kitchensink-example');

    				//className = 'KitchenSink.view.security.com.comInfoPanel';
    				if(!Ext.ClassManager.isCreated(className)){
    					Ext.syncRequire(className);
    				}	
    				ViewClass = Ext.ClassManager.get(className);

    				clsProto = ViewClass.prototype;

    				if (clsProto.themes) {
    					clsProto.themeInfo = clsProto.themes[themeName];

    					if (themeName === 'gray') {
    						clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
    					} else if (themeName !== 'neptune' && themeName !== 'classic') {
    						if (themeName === 'crisp-touch') {
    							clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
    						}
    						clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
    					}
    					// <debug warn>
    					// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
    					if (!clsProto.themeInfo) {
    						Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
    							themeName + '\'. Is this intentional?');
    					}
    					// </debug>
    				}
    				
    				var config = {
    						"siteId": siteId
    				}
    				cmp = new ViewClass(config);
    				
    				//cmp.on('afterrender',function(panel){

    				//});
    				
    				tab = contentPanel.add(cmp);     
    				
    				contentPanel.setActiveTab(tab);

    				Ext.resumeLayouts(true);
    				if (cmp.floating) {
    					cmp.show();
    				}
    			}
            }
        });
	    Ext.apply(this, {
            columns: [{ 
                text: '站点ID',
                dataIndex: 'siteId',
				width: 240
            },{
                text: '站点名称',
                sortable: true,
                dataIndex: 'siteName',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '站点基本信息',handler: 'editSelWebSiteInfo'},
				  {iconCls: 'view',tooltip: '用户显示信息项配置',handler: 'editWebSiteReg'},
			   	  {iconCls: 'preview',tooltip: '站点风格选择',handler: 'editSelWebSiteStyle'},
			   	  {iconCls: 'publish',tooltip: '站点页面设置',handler: 'editWebSitePage'}
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
