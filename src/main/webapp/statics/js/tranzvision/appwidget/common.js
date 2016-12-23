//$(function(){
//	$(".popo").popover({trigger:"hover"});//初始化帮助提示
//});
function loading(obj)
{
	obj.css("position","relative");
	$('<div class="loading"></div>').appendTo(obj).css({
		width:obj.innerWidth(),
		height:obj.innerHeight()
	});
}
function loaded(obj)
{
	obj.find(".loading").remove();
}
function noteing(str,t)
{
	var nc='success';
	if(t==2) nc='error';
	else if(t==3) nc='info';
	var n='<div class="noteing"><div class="alert alert-'+nc+'">'+str+'</div></div>';
	$(n).appendTo("body").css({
		"top":$(window).height()/2-30,
		"left":$(window).width()/2-$(".noteing").width()/2
	}).fadeIn("1000",function(){
//		setTimeout(function(){
		if(t==2) $(".noteing").fadeOut(3000,function(){$(this).remove();});
		$(".noteing").fadeOut(1000,function(){$(this).remove();});
//		},2000);
	}).on("click",function(){
		$(this).remove();
	});
}
/**
 * utf-8截取字符
 */
function mb_cutstr(str, maxlen, dot)
{
	var len = 0;
	var ret = '';
	var dot = dot ? dot : '';
	maxlen = maxlen - dot.length;
	for (var i = 0; i < str.length; i++) {
		len += str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255 ? 2 : 1;
		if (len > maxlen) {
			ret += dot;
			break;
		}
		ret += str.substr(i, 1);
	}
	return ret;
}
/**
 * 读取COOKIE
 */
function getcookie(name)
{
	var name=unescape(name);
	var start = document.cookie.indexOf(name);
	var end = document.cookie.indexOf(";", start);
	return start == -1 ? '' : unescape(document.cookie.substring(start + name.length + 1, (end > start ? end : document.cookie.length)));
}
/**
 * 设置COOKIE
 */
function setcookie(name, value, seconds, path, domain)
{
	var expires = new Date(),seconds=seconds==undefined?3600*24*365:seconds;
	expires.setTime(expires.getTime() + seconds);
	document.cookie = escape(name) + '=' + escape(value)
	+ (expires ? '; expires=' + expires.toGMTString() : '')
	+ (path ? '; path=' + path : '/')
	+ (domain ? '; domain=' + domain : '');
}
//打乱数组
function shuffle(v)
{
	for(var j,x,i=v.length;i;j=parseInt(Math.random()*i),x=v[--i],v[i]=v[j],v[j]=x);
	return v;
}

//简单的对象复制
function cloneObj(obj)
{
	var newObj = null;
    var type = Object.prototype.toString.call(obj);
    if(type.indexOf("Object") > -1){
        newObj = {};
        for(var key in obj){
            if(obj.hasOwnProperty(key)){
                newObj[key] = cloneObj(obj[key]);
            }
        }
    }else if(type.indexOf("Array") > 0){
        newObj = [];
        for(var i=0;i<obj.length;i++){
           newObj[newObj.length] =  cloneObj(obj[i]);
        }
    }else{
        newObj = obj;
    }
    return newObj;
}

//实体化HTML
function htmlentities(str)
{
	str=str.toString();
    if(str.length==0) return '';
	str=str.replace(/&/g,  "&gt;");
	str=str.replace(/</g,  "&lt;");
	str=str.replace(/>/g,  "&gt;");
	str=str.replace(/ /g,  "&nbsp;");
	str=str.replace(/\'/g, "&apos;");
	str=str.replace(/\"/g, "&quot;");
    return str;
}
//取得指定位数的随机字符串
function str_rand(len)
{
    var chars = "abcdefhjmnpqrstuvwxyz23456789ABCDEFGHJKLMNPQRSTUVWYXZ";
    var values = '';
    for ( i = 0; i < len; i++ ) {
        values += chars.charAt( Math.floor( Math.random() * chars.length ))
    }
    return values;
}

//显示tip
function showTip(msg)
{
	$('.alert').html(msg).fadeIn(300).delay(2000).fadeOut(400);
}
Date.prototype.format =function(format)
{
    var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(),    //day
		"h+" : this.getHours(),   //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3),  //quarter
		"S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o){
    	if(new RegExp("("+ k +")").test(format)){
    		format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
    	}
    }
    return format;
}
