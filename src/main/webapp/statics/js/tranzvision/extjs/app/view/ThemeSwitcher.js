Ext.define('KitchenSink.view.ThemeSwitcher', function() {
    var theme = location.href.match(/theme=([\w-]+)/),
        language = location.href.match(/language=([\w-]+)/);

    //theme = (theme && theme[1]) || (Ext.microloaderTags.desktop ? 'neptune' : 'neptune-touch');
    theme =  TranzvisionMeikecityAdvanced.Boot.getTheme();
    language = TranzvisionMeikecityAdvanced.Boot.getLanguage();

    if (!Ext.themeName && !!theme) {
        var m = theme.match(/^([\w-]+)-(?:he)$/);
        Ext.themeName = m ? m[1] : theme;
    }

    return {
        extend: 'Ext.Container',
        xtype: 'themeSwitcher',
        id: 'tranzvision-framework-theme-switcher-btn',
        margin: '0 10 0 0',
        layout: 'hbox',

        initComponent: function() {
        	var me = this;

            function setQueryParam(name, value) {
                var query = Ext.Object.fromQueryString(location.search);
                query[name] = value;
                location.search = Ext.Object.toQueryString(query);
            }

            function makeItem(value, text, paramName) {
                paramName = paramName || "theme";

                var checked = value === (paramName == "theme" ? theme : language);

                return {
                    text: text,
                    group: (paramName == 'theme' ? 'themegroup' : 'localegroup'),
                    checked: checked,
                    handler: function () {
                        if (!checked) {
                            if(paramName == 'theme') {
                                setQueryParam('theme', value);
                            } else {
                                setQueryParam('language', value);
                            }
                        }
                    }
                };
            }

			//20115-04-21---ZDQ--添加---获取数据方法
			function getMenuItems(){
				var items = [];
				var themeitems=[];
				var selfhelpitem=[];
				
				/*加载主题、语言等menu*/
				var boot = TranzvisionMeikecityAdvanced.Boot;
			    try
			      {
			        Ext.Ajax.request(
			        {
					  url: boot.mainapp_interaction_url,
					  params: {
						  tzParams: '{"OperateType":"ZTYY"}'
					  },
			          //url: '/tranzvision/main/main.json',
			          async: false,
			          success: function(response)
			          {
			            var jsonText = response.responseText;

			            try
			            {
			              var jsonObject = Ext.util.JSON.decode(jsonText);

			              /*判断服务器是否返回了正确的信息*/
			              if(jsonObject.state.errcode === 0)
			              {/*服务器正确返回数据*/
			                var comContent = jsonObject.comContent;
							//语言
			                var languages = comContent.languages;
							
			                for(var i=0;i<languages.length;i++){
			                	//语言ID
			                	var langid = languages[i].languageid;
			                	//语言名称
			                	var langname = languages[i].languageName;
			                	//2016-04-19 暂时不允许切换语言，需要时放开即可。
								//items.push(makeItem(langid,langname,'language'));
			                }
							if(items.length > 0){
								items.push("-");
							}
							
			                //主题
			                var themes = comContent.themes;
							for(var i=0;i<themes.length;i++){								
								//主题编号
								var themeid = themes[i].themeid;
								//主题名称
								var themeName = themes[i].themeName;
								themeitems.push(makeItem(themeid,themeName));
							}
							
							
							var thememenu = new Ext.menu.Menu({
								items:themeitems
							});
							items.push( {text:'皮肤设置',menu:thememenu} );//嵌套子菜单
							
							if(items.length > 0){
								items.push("-");
							}
							//自助信息修改
							selfhelpitem.push({
								text: '绑定手机',
								handler: function(){

									var className = 'KitchenSink.view.common.bindPhoneWindow';
									Ext.syncRequire(className);
									var ViewClass = Ext.ClassManager.get(className);
									var win;
									if(Ext.getCmp("tzBindPhoneWindow201505131528") == undefined){
										//新建类
										win = new ViewClass();
									}else{
										win = Ext.getCmp("tzBindPhoneWindow201505131528");
									}
									
									//参数
									var tzParams = '{"ComID":"TZ_GD_BPHONE_COM","PageID":"TZ_GD_BPHONE_STD","OperateType":"QF","comParams":{}}';
									//加载数据
									var form = win.child('form').getForm();
									Ext.tzLoad(tzParams,function(responseData){
										var telphone = responseData.telPhone;
										form.setValues({telPhone:telphone});
									});										
									win.show();
								}
							}); 
							selfhelpitem.push({
								text: '绑定邮箱',
								handler: function(){
									var className = 'KitchenSink.view.common.bindEmailWindow';
									Ext.syncRequire(className);
									var ViewClass = Ext.ClassManager.get(className);
									var win;
									if(Ext.getCmp("tzBindEmailWindow201505140917") == undefined){
										//新建类
										win = new ViewClass();
									}else{
										win = Ext.getCmp("tzBindEmailWindow201505140917");
									}
									
									//参数
									var tzParams = '{"ComID":"TZ_GD_BEMAIL_COM","PageID":"TZ_GD_BEMAIL_STD","OperateType":"QF","comParams":{}}';
									//加载数据
									var form = win.child('form').getForm();
									Ext.tzLoad(tzParams,function(responseData){
										var Email = responseData.Email;
										form.setValues({Email:Email});
									});	
									win.show();
								}
							}); 
							
							var selfhelpmenu = new Ext.menu.Menu({
								items:selfhelpitem
							});
							//items.push( {text:'自助信息',menu:selfhelpmenu} );//嵌套子菜单
							items.push( {
								text: '自助信息',
								handler: function(){
									var className = 'KitchenSink.view.common.selfInfo.selfInfoWindow';
									Ext.syncRequire(className);
									var ViewClass = Ext.ClassManager.get(className);
									var win;
									if(Ext.getCmp("selfInfoWindow201506011708") == undefined){
										//新建类
										win = new ViewClass();
									}else{
										win = Ext.getCmp("selfInfoWindow201506011708");
									}
									
									//参数
									var tzParams = '{"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_SELF_INFO_STD","OperateType":"QF","comParams":{}}';
									//加载数据
									var form = win.child('form').getForm();
									Ext.tzLoad(tzParams,function(responseData){
										var Email = responseData.Email;
										var Phone= responseData.Phone;
										var accountID=responseData.accountID;
										var accountName=responseData.accountName;
										var bindPhoneFlg=responseData.bindPhoneFlg;
										var bindEmailFlg=responseData.bindEmailFlg;
										var contactEmail=responseData.contactEmail;
										var contactPhone=responseData.contactPhone;
										form.setValues({Email:Email,accountID:accountID,accountName:accountName,Phone:Phone,contactPhone:contactPhone,contactEmail:contactEmail,bindPhoneFlg:bindPhoneFlg,bindEmailFlg:bindEmailFlg});
										if (bindPhoneFlg=="Y"){
											 Ext.getCmp('buttonBindPhone201506070235').setText('解除绑定');
										}else{
											 Ext.getCmp('buttonBindPhone201506070235').setText('绑定登录手机');
										}
										if (bindEmailFlg=="Y"){
											 Ext.getCmp('buttonBindEmail201506070235').setText('解除绑定');
										}else{
											 Ext.getCmp('buttonBindEmail201506070235').setText('绑定登录邮箱');
										}
										
									});	
									win.show();
								}
							} );
			
			              }
			              else
			              {
			                boot.errorMessage(jsonObject.state.errdesc);
			              }
			            }
			            catch(e)
			            {
			              boot.errorMessage(boot.getMessage('TZGD_FWINIT_00013'));
			            }
			            Ext.getBody().unmask();
			          },
			          failure: function(response, opts)
			          {
			            boot.errorMessage("获取数据失败");
			            Ext.getBody().unmask();
			          }
			        });
			    }
			    catch(e)
			    {
			        boot.errorMessage(e.toString());
			        Ext.getBody().unmask();
			    }
			    /*
				items =  [
					
                    makeItem('neptune',       'Neptune'),
                    makeItem('neptune-touch', 'Neptune Touch'),
                    makeItem('crisp',         'Crisp'),
                    makeItem('crisp-touch',   'Crisp Touch'),
                    makeItem('classic',       'Classic'),
                    makeItem('gray',          'Gray'),
                    '-',
                    makeItem('ENG',            'English',    'language'),
                    makeItem('ZHS',            '中文版',     'language'),
					'-',
					
					{
						text: '密码修改',
						handler: function(){
							var className = 'KitchenSink.view.common.modifyPwdWindow';
							Ext.syncRequire(className);
							var ViewClass = Ext.ClassManager.get(className);
							var win;
							if(Ext.getCmp("tzModifyPwdWindow201504201420") == undefined){
							    //新建类
					            win = new ViewClass();
				            }else{
				            	win = Ext.getCmp("tzModifyPwdWindow201504201420");
				            }
				            win.show();
						}
					}
                ] 
				*/
				
                if(items.length > 0){
					items.push("-");
				}   
				items.push({
					text: '密码修改',
					handler: function(){
						var className = 'KitchenSink.view.common.modifyPwdWindow';
						Ext.syncRequire(className);
						var ViewClass = Ext.ClassManager.get(className);
						var win;
						if(Ext.getCmp("tzModifyPwdWindow201504201420") == undefined){
						    //新建类
				            win = new ViewClass();
			            }else{
			            	win = Ext.getCmp("tzModifyPwdWindow201504201420");
			            }
			            win.show();
					}
				});  
				
				//退出系统
				if(items.length > 0){
					items.push("-");
				}   
				items.push({
					text: '退出系统',
					handler: function(){

						Ext.MessageBox.confirm('确认', '您确定要退出系统吗?', function(btnId){
							if(btnId == 'yes'){

								window.location = TzUniversityContextPath + "/login/logout";
								
								/*
								//提交参数
								var tzParams = '{"OperateType":"LOGOUT"}';
								var org=Ext.tzOrgID.toLowerCase();
								Ext.Ajax.request({
									url: Ext.tzGetGeneralURL(),
									params: {
										tzParams: tzParams
									},
									timeout: 60000,
									success: function(response, opts)
									{   
										//返回值内容
										var url=Ext.JSON.decode(response.responseText).comContent;
										//console.log(url.comContent);
										//window.location.href=url+org+"/login.html";
										window.location.href="/"+org+"/login.html";
									},
									failure: function(response, opts){
										//错误信息响应报文
									}
								});
								*/
							}												  
						},this);   	   	
					}
					
				});  


				
				return items;
			}
			
			var menu = new Ext.menu.Menu({
                items:getMenuItems()
            });
						 
            this.items = [{
                    xtype: 'component',
                    id: 'tranzvision-framework-theme-switcher',
                    cls: 'ks-theme-switcher',
                    margin: '0 5 0 0',
                    listeners: {
                        scope: this,
                        click: function (e) {
                            menu.showBy(this,null,[0,5]);
                        },
						mouseout:function(e){
							 if(!e.getRelatedTarget().contains(e.getTarget())&& !menu.getEl().contains(e.getRelatedTarget())){ 						
									menu.hide();
							} 
								
						},
                        element: 'el'
                    }
                }];

            this.callParent();
        }
    };
});
