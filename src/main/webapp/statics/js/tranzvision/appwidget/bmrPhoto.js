/*====================================================================
 + 功能描述：个人照片上传控件，可控制上传照片的格式及大小			++
 + 开发人：孔卫丽							++
 + 开发日期：2015-05-08							++
 =====================================================================*/
SurveyBuild.extend("bmrPhoto", "baseComponent", {
    itemName: "个人照片",
    title: "个人照片",
    "StorageType": "S",
    upFileType: "jpg",    //允许上传格式
    upFileSize: "2",    //允许上传大小
    isClip: "",    //是否允许剪裁
    _getHtml: function(data, previewmode) {
        var c = "";

        if (previewmode) {
            /*if(SurveyBuild.appInsId == "0"){
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"KSPHOTO"}}';
                $.ajax({
                    type: "get",
                    dataType: "JSON",
                    data: {
                        tzParams: params
                    },
                    async: false,
                    url: SurveyBuild.tzGeneralURL,
                    success: function(f) {
                        if (f.state.errcode == "0") {
                            var val = f.comContent.photo;
                            if (val && val.length > 1) {
                                data.value = val;
                                data["sysFileName"] = f.comContent.sysFileName;
                                data["filename"] = f.comContent.filename;
                                data["imaPath"] = f.comContent.imaPath;
                                data["path"] = f.comContent.path;
                            }
                        }
                    }
                });
            }*/
            if(SurveyBuild.accessType == "M"){
            	  if(SurveyBuild._readonly) {
                   	c += '<div class="input-list">';
                  	c += ' 	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                  	c += '  <div class="input-list-text left headshot">';
                  	c += '		<div class="headshot-pic">';
                  	c += '			<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + TzUniversityContextPath + data.value + '" data-id="' + data.instanceId + '">';
                  	c += '			<img src="' + (data.value.length < 1 ? TzUniversityContextPath + "/statics/images/appeditor/bjphoto.jpg": TzUniversityContextPath + data.value) + '" id="photo" />';
                  	c += '		</div>';
                  	c += '	</div>';
                  	c += '	<div class="left headshot-info">' + data.suffix + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
                  	c += '	<div class="clear"></div><br>';
                  	c += '</div>';
                  } else {
                  	c += '<div class="item">';
                  	c += ' 	<p>'+data.title+'<span>*</span></p>';
                  	c += '  <a class="photo" id="photox"><img src="' + (data.value.length < 1 ? TzUniversityContextPath + "/statics/images/appeditor/m/bjphoto.jpg": TzUniversityContextPath + data.value) + '"/></a>';
                  	c +='</div>';
                  	c += '<article class="htmleaf-container">';
                    c += '</article>';
                  	}
                }
            else{
                    if(SurveyBuild._readonly) {
                 	c += '<div class="input-list">';
                	c += ' 	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                	c += '  <div class="input-list-text left headshot">';
                	c += '		<div class="headshot-pic">';
                	c += '			<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + TzUniversityContextPath + data.value + '" data-id="' + data.instanceId + '">';
                	c += '			<img src="' + (data.value.length < 1 ? TzUniversityContextPath + "/statics/images/appeditor/bjphoto.jpg": TzUniversityContextPath + data.value) + '" id="photo" />';
                	c += '		</div>';
                	c += '	</div>';
                	c += '	<div class="left headshot-info">' + data.suffix + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
                	c += '	<div class="clear"></div><br>';
                	c += '</div>';
                } else {
                	if(SurveyBuild.appInsId == "0"){
                        var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"KSPHOTO"}}';
                        $.ajax({
                            type: "get",
                            dataType: "JSON",
                            data: {
                                tzParams: params
                            },
                            async: false,
                            url: SurveyBuild.tzGeneralURL,
                            success: function(f) {
                                if (f.state.errcode == "0") {
                                    var val = f.comContent.photo;
                                    if (val && val.length > 1) {
                                        data.value = val;
                                        data["sysFileName"] = f.comContent.sysFileName;
                                        data["filename"] = f.comContent.filename;
                                        data["imaPath"] = f.comContent.imaPath;
                                        data["path"] = f.comContent.path;
                                    }
                                }
                            }
                        });
                    }
                	c += '<div class="input-list">';
                	c += ' 	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
                	c += '  <div class="input-list-text left headshot">';
                	c += '		<div class="headshot-pic">';
                	c += '			<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + TzUniversityContextPath + data.value + '" data-id="' + data.instanceId + '">';
                	c += '			<a id="photo' + data.itemId + '" style="cursor: pointer;"><img src="' + (data.value.length < 1 ? TzUniversityContextPath + "/statics/images/appeditor/bjphoto.jpg": TzUniversityContextPath + data.value) + '" id="photo" /></a>';
                	c += '		</div>';
                	c += '	</div>';
                	c += '	<div class="left headshot-info">' + data.suffix + '<div id="' + data.itemId + 'Tip" class="onShow"><div class="onShow"></div></div></div>';
                	c += '	<div class="clear"></div><br>';
                	c += '</div>';
                	c += '<input type="hidden" value="" name="mbaSqphoto" id="mbaSqphoto">';
                 }
            } 
        }
         else {
            c += '<div class="question-answer">';
            c += '	<div class="format">';
            c += '		<img src="' + TzUniversityContextPath + '/statics/images/appeditor/bjphoto.jpg" width="120" height="165" />';
            c += '		<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '	</div>';
            c += '</div>'
        }

        return c;
    },
    _edit: function(data) {
        var e = "";
        //后缀
        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">后缀：<a data-animation="fade" data-reveal-id="myModal" class="big-link" onclick="SurveyBuild.showMsg(this,event)" data-for-id="help_suffix" href="#">(?)</a></span>';
        e += '  <input type="text" value="' + data.suffix + '" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" class="medium">';
        e += '</div>';

        //参数设置
        e += '<div class="edit_paramSet mt10">';
        e += '	<span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
        e += '	<div class="groupbox">';
        e += '		<div class="edit_item_warp">';
        e += '          <span class="edit_item_label">文件格式：</span>';
        e += '         <input type="text" class="medium edit_attaType" onkeyup="SurveyBuild.saveAttr(this,\'upFileType\')" data_id="' + data.instanceId + '" value="' + data.upFileType + '"/>';
        e += '		</div>';
        e += '		<span style="margin-left:103px; font-size:12px;color:#FF0000">文件格式以“,”分隔</span>';

        //文件大小
        e += '		<div class="edit_item_warp">';
        e += '			 <span class="edit_item_label">文件大小：</span>';
        e += '         <input type="text" class="medium edit_attSize" style="width:175px;" onkeyup="SurveyBuild.saveAttr(this,\'upFileSize\')" data_id="' + data.instanceId + '" value="' + data.upFileSize + '"/>';
        e += '			 <span>M</span>';
        e += '		</div>';
        e += '		<span style="margin-left:103px; font-size:12px;color:#FF0000">输入允许上传的最大文件大小</span>';

        //是否允许剪裁
        e += '		<div class="edit_item_warp" style="margin-top: 5px">';
        e += '			<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isClip\')" ' + (data.isClip == "Y" ? "checked='checked'": "") + ' id="isClip">';
        e += '			<label for="isClip">允许剪裁</label>';
        e += '		</div>';

        //裁剪标准
        e += '		<div class="edit_item_warp mb10">';
        e += '          <span class="edit_item_label">裁剪标准：</span>';
        e += '		    <select id="clip" data-id="' + data.instanceId + '" onchange="SurveyBuild.saveAttr(this,\'clip\')">';
        e += '			    <option value="1" ' + (data.clip == "1" ? "selected='selected'": "") + '>证件照</option>';
        e += '			    <option value="2" ' + (data.clip == "2" ? "selected='selected'": "") + '>1寸标准照</option>';
        e += '			    <option value="3" ' + (data.clip == "3" ? "selected='selected'": "") + '>2寸标准照</option>';
        e += '			    <option value="4" ' + (data.clip == "4" ? "selected='selected'": "") + '>任意</option>';
        e += '		    </select>';
        e += '		</div>';
        e += '	</div>';
        e += '</div>';

        //规则设置
        e += '<div class="edit_jygz">';
        e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '      <div class="groupbox">';
        e += '          <div class="edit_item_warp" style="margin-top:5px;">';
        e += '              <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '          </div>';
        e += '      </div>';
        //高级设置
        e += '      <div class="edit_item_warp">';
        e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '      </div>';
        e += '</div>';

        return e;
    },
    _eventbind: function(data) {
    	if(SurveyBuild.accessType == "M"){

    		var wid=$(window).width()*0.9
    		// alert(wid);
    		var obUrl = ''
    		//document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
    		$("#clipArea").photoClip({
    			width:wid,
    			height:wid,
    			file: "#file",
    			view: "#view",
    			ok: "#clipBtn",
    			loadStart: function() {
    				console.log("照片读取中");
    			},
    			loadComplete: function() {
    				console.log("照片读取完成");
    			},
    			clipFinish: function(dataURL) {
    				console.log(dataURL);
    			}
    		});


    		$(function(){
    		$("#photox").click(function(){
    		$(".htmleaf-container").show();
    		})
    			$("#clipBtn").click(function(){
    				$("#photox").empty();
    				$('#photox').append('<img src="' + imgsource + '" align="absmiddle" >');
    				$(".htmleaf-container").hide();
    			})
    		});


    		$(function(){
    			jQuery.divselect = function(divselectid,inputselectid) {
    				var inputselect = $(inputselectid);
    				$(divselectid+" small").click(function(){
    					$("#divselect ul").toggle();
    					$(".mask").show();
    				});
    				$(divselectid+" ul li a").click(function(){
    					var txt = $(this).text();
    					$(divselectid+" small").html(txt);
    					var value = $(this).attr("selectid");
    					inputselect.val(value);
    					$(divselectid+" ul").hide();
    					$(".mask").hide();
    					$("#divselect small").css("color","#333")
    				});
    			};
    			$.divselect("#divselect","#inputselect");
    		});


    		$(function(){
    			jQuery.divselectx = function(divselectxid,inputselectxid) {
    				var inputselectx = $(inputselectxid);
    				$(divselectxid+" small").click(function(){
    					$("#divselectx ul").toggle();
    					$(".mask").show();
    				});
    				$(divselectxid+" ul li a").click(function(){
    					var txt = $(this).text();
    					$(divselectxid+" small").html(txt);
    					var value = $(this).attr("selectidx");
    					inputselectx.val(value);
    					$(divselectxid+" ul").hide();
    					$(".mask").hide();
    					$("#divselectx small").css("color","#333")
    				});
    			};
    			$.divselectx("#divselectx","#inputselectx");
    		});


    		$(function(){
    			jQuery.divselecty = function(divselectyid,inputselectyid) {
    				var inputselecty = $(inputselectyid);
    				$(divselectyid+" small").click(function(){
    					$("#divselecty ul").toggle();
    					$(".mask").show();
    				});
    				$(divselectyid+" ul li a").click(function(){
    					var txt = $(this).text();
    					$(divselectyid+" small").html(txt);
    					var value = $(this).attr("selectyid");
    					inputselecty.val(value);
    					$(divselectyid+" ul").hide();
    					$(".mask").hide();
    					$("#divselecty small").css("color","#333")
    				});
    			};
    			$.divselecty("#divselecty","#inputselecty");
    		});


    		$(function(){
    		   $(".mask").click(function(){
    			   $(".mask").hide();
    			   $(".all").hide();
    		   })
    			$(".right input").blur(function () {
    				if ($.trim($(this).val()) == '') {
    					$(this).addClass("place").html();
    				}
    				else {
    					$(this).removeClass("place");
    				}
    			})
    		});


    		$("#file0").change(function(){
    			var objUrl = getObjectURL(this.files[0]) ;
    			 obUrl = objUrl;
    			console.log("objUrl = "+objUrl) ;
    			if (objUrl) {
    				$("#img0").attr("src", objUrl).show();
    			}
    			else{
    				$("#img0").hide();
    			}
    		}) ;
    		function qd(){
    		   var objUrl = getObjectURL(this.files[0]) ;
    		   obUrl = objUrl;
    		   console.log("objUrl = "+objUrl) ;
    		   if (objUrl) {
    			   $("#img0").attr("src", objUrl).show();
    		   }
    		   else{
    			   $("#img0").hide();
    		   }
    		}
    		function getObjectURL(file) {
    			var url = null ;
    			if (window.createObjectURL!=undefined) { // basic
    				url = window.createObjectURL(file) ;
    			} else if (window.URL!=undefined) { // mozilla(firefox)
    				url = window.URL.createObjectURL(file) ;
    			} else if (window.webkitURL!=undefined) { // webkit or chrome
    				url = window.webkitURL.createObjectURL(file) ;
    			}
    			return url ;
    		}


    		var subUrl = "";
    		$(function (){
    			$(".file-3").bind('change',function(){
    				subUrl = $(this).val()
    				$(".yulan").show();
    				$(".file-3").val("");
    			});

    			$(".file-3").each(function(){
    				if($(this).val()==""){
    					$(this).parents(".uploader").find(".filename").val("营业执照");
    				}
    			});
    		$(".btn-3").click(function(){
    		$("#img-1").attr("src", obUrl);
    		$(".yulan").hide();
    		$(".file-3").parents(".uploader").find(".filename").val(subUrl);
    		})
    			$(".btn-2").click(function(){
    				$(".yulan").hide();
    			})

    		});
    		


	           /* $("#clipBtn").click(function(){
	                var imgUrl=$(".photo img").attr('src');
	            	
	            
					if($("#sysfilename").val() != ""){
					    loading();
				        $("#crop_form").ajaxSubmit({
				            dataType:"json",
							data:{
								"imaPath":imgUrl
							},
				            success: function(msg) {
				              	//var data="sysFileName=" + msg.name + "&filename=" + filename + "&imaPath=" + imaPath + "&path=" + path;
			          			var fileStrJson = {"sysFileName": msg.name , "filename": filename, "imaPath": imaPath, "path": path};
					  			var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_UP_PHOTO_STD","OperateType":"PHOTO","comParams":' + JSON.stringify( fileStrJson ) + '}';
			
				                $.ajax({
				                    type:"POST",
				                    url:"%bind(:1)",
									dataType:"json",
				                    data: {"tzParams" : tzParams },	
				                    beforeSend:function() {
				                        $(function(){
				                            $("#loading").fadeIn();
				                        });
				                    },
				                    success:function(html){
				                        if(html.comContent.success == "Y"){
				 							window.parent.$("#photo").attr("src", "%bind(:20)"+html.comContent.url);					
											window.parent.SurveyBuild && window.parent.$("#photo").parent().parent().find('input[type="hidden"]').val(html.comContent.url);
											//做如下修改 以适用分组框 modity by caoy
											//layer.close(layer.index);
											$("#loading").fadeOut();
											closeThis();
				                        }else{
				                        	 //closeThis();
											 alert("%bind(:12)");
											 //layer.close(layer.index);
											 $("#loading").fadeOut();
										}
				                    },
						            error:function(xhr){
						                btn.html(xhr.responseText);
						                $("#loading").fadeOut();
										//layer.close(layer.index);
						            }
				                })
				            },
				            error:function(xhr){
				                btn.html(xhr.responseText);
								layer.close(layer.index);
				            }
				        });
					}else{
						alert("%bind(:13)");
					}
			    });*/

    		function setImagePreview(){
    			var preview, img_txt, localImag, file_head = document.getElementById("file_head"),
    					picture = file_head.value;
    			if (!picture.match(/.jpg|.gif|.png|.bmp/i)) return alert("您上传的图片格式不正确，请重新选择！"),
    					!1;
    			if (preview = document.getElementById("preview"), file_head.files && file_head.files[0]) preview.style.display = "block",
    					preview.style.width = "63px",
    					preview.style.height = "63px",
    					preview.src = window.navigator.userAgent.indexOf("Chrome") >= 1 || window.navigator.userAgent.indexOf("Safari") >= 1 ? window.webkitURL.createObjectURL(file_head.files[0]) : window.URL.createObjectURL(file_head.files[0]);
    			else {
    				file_head.select(),
    						file_head.blur(),
    						img_txt = document.selection.createRange().text,
    						localImag = document.getElementById("localImag"),
    						localImag.style.width = "63px",
    						localImag.style.height = "63px";
    				try {
    					localImag.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
    							localImag.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = img_txt
    				} catch(f) {
    					return alert("您上传的图片格式不正确，请重新选择！"),
    							!1
    				}
    				preview.style.display = "none",
    						document.selection.empty()
    			}
    			return document.getElementById("DivUp").style.display = "block",
    					!0
    		}

    		
    	}else{
        var $photoBox = $("#photo" + data.itemId);
        var up;
        $photoBox.click(function(e) {
            var photoUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
            var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_UP_PHOTO_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '","siteId":"' + $("#siteId").val() + '"}}';
            photoUrl = photoUrl + window.escape(params);
            up = $.layer({
                type: 2,
                title: false,
                fix: false,
                closeBtn: 2,
                shadeClose: false,
                shade: [0.3, '#000', true],
                border: [3, 0.3, '#000', true],
                offset: ['50%', ''],
                area: ['840px', '610px'],
                iframe: {
                    src: photoUrl
                }
            });
          });
    	}
     }
});