Ext.define('KitchenSink.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    requires:[
        'KitchenSink.view.navigation.Tree'
    ],

    alias: 'controller.main',

    applyState: function(state) {
        var refs = this.getReferences();

        if (state.hasTreeNav) {
            this.getView().moveBefore({
                region: 'west',
                reference: 'tree',
                xtype: 'navigation-tree'
            }, refs.contentPanel);

            refs.breadcrumb.hide();
            refs.contentPanel.header.hidden = false;
            this._hasTreeNav = true;
        } else {
            this._hasTreeNav = false;
        }
    },

    getState: function() {
        return {
            hasTreeNav: this._hasTreeNav
        };
    },

    showBreadcrumbNav: function() {
        var refs = this.getReferences(),
            breadcrumbNav = refs.breadcrumb,
            treeNav = refs.tree,
            selection = treeNav.getSelectionModel().getSelection()[0];

        if (breadcrumbNav) {
            breadcrumbNav.show();
        } else {
            refs.contentPanel.addDocked({
                xtype: 'navigation-breadcrumb',
                selection: selection
            });
        }

        refs['breadcrumb.toolbar'].setSelection(selection || 'root');

        treeNav.hide();
        refs.contentPanel.getHeader().hide();

        this._hasTreeNav = false;
        this.getView().saveState();
    },
	//�������
	onNewCloseZlgl: function(btn) {
		var win = btn.findParentByType("window");
		win.close();
	},
	/*���������ģ��ҳ�棬ȷ��*/
	onNewEnsure_1: function(btn) {
		//���ע����Ϣ�б�
		//var grid = btn.findParentByType("submitDataModelMg");
		//���ע����Ϣ���
		//var store = grid.getStore();

		var win = this.lookupReference('myBmbRegWindow');
		var activeTab = win.items.items[0].getActiveTab(),
			id = '';
		var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
			tplId = "";
		if (activeTab.itemId == "predefine") {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					if (this.style.backgroundColor == "rgb(173, 216, 230)") {
						tplId = this.getAttribute("data-id");
						return false;
					}
				});
		} else {
			tplId = "";
		}
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTSET_STD"];
		var className = pageResSet["jsClassName"];
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

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
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}
		win.close();
		if (tplId&&tplName){
			cmp = new ViewClass();
			cmp.actType = "update";
			cmp.on('afterrender',function(panel){
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}}';
			//�������
			Ext.tzLoad(tzParams,function(responseData){
				//���ע����Ϣ���
				var form = cmp.child('form').getForm();
				var formData = responseData.formData;
				form.setValues(formData);
				//ҳ��ע����Ϣ�б����
				var roleList = responseData.listData;	
				var tzStoreParams = '{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}';
				var grid_1 = cmp.child('grid');
				grid_1.store.tzStoreParams = tzStoreParams;
				grid_1.store.load();
			});
			});
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}else if (tplName) {
			cmp = new ViewClass();
			cmp.actType = "add";
			var form = cmp.child('form').getForm();
			form.findField('smtDtName').setValue(tplName);
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}


		
		/*if (tplName) {
			var tzStoreParams = '{"add":[{"id":"' + tplId + '","name":"' + tplName + '"}]}'
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_ONREG_ADD_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
			Ext.tzSubmit(tzParams,
				function(jsonObject) {
					Ext.get(activeTab.id).select('input').elements[0].value = "";
					store.reload();
					var href = "http://202.120.24.169:9550/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_REG.TZ_TPL_SET.FieldFormula.Iscript_TplDesign?TZ_APP_TPL_ID=" + jsonObject.id;
					window.open(href, '_blank');
				},"",true,this);
		}*/
		//win.close();
	},
	//��ӽ���
    showTreeNav: function() {
        var refs = this.getReferences(),
            treeNav = refs.tree,
            breadcrumbNav = refs.breadcrumb,
            selection = refs['breadcrumb.toolbar'].getSelection();

        if (treeNav) {
            treeNav.show();
        } else {
            treeNav = this.getView().moveBefore({
                region: 'west',
                reference: 'tree',
                xtype: 'navigation-tree'
            }, refs.contentPanel);
        }

        if (selection) {
            treeNav.getSelectionModel().select([
                selection
            ]);

            breadcrumbNav.hide();
            refs.contentPanel.getHeader().show();

            this._hasTreeNav = true;
            this.getView().saveState();
        }
    }
});
