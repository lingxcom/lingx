
function resetExtParams(){
	extParams={};
		for(var t in params){
			extParams[t]=params[t];
		}
		if(extparam){
			extParams['extparam']=extparam;
		}

		//由于lingxSearch引用到该方法，所以查询时会有错误，查两次
		//if(Ext.getCmp("datas")&&Ext.getCmp("datas").getStore())
		//Ext.getCmp("datas").getStore().loadPage(1);
	return extParams;
}
/**
 * 数据提交
 */
function lingxSubmit(){
	var labels=$(".label");
	var val="",txt="";
	for(var i=0;i<labels.length;i++){
		var o=$(labels[i]);
		val+=o.find(".value").html()+",";
		txt+=o.find(".text").html()+",";
	}
	if(labels.length>0){
		val=val.substring(0,val.length-1);
		txt=txt.substring(0,txt.length-1);
	}
	var win=getFromWindow(fromPageId);
	win.lingxSet({cmpId:cmpId,text:txt,value:val});
	closeWindow();
}

function lingxSubmit_bak(){
	var array=Ext.getCmp("datas").getSelectionModel().getSelection();
	if(array&&array.length>0){
		var obj,val="",txt="";
		for(var i=0;i<array.length;i++){
			obj=array[i];
			val=val+obj.data[valueField]+",";
			txt=txt+obj.data[textField]+",";
		}
		val=val.substring(0,val.length-1);
		txt=txt.substring(0,txt.length-1);
		var win=getFromWindow(fromPageId);
		win.lingxSet({cmpId:cmpId,text:txt,value:val});
		closeWindow();
	}else{
		lgxInfo("请选择列表项");
	}
	//win.lingxSet({value:"有志者事竟成"});
}
/**
 * 数据查询
 */
function lingxSearch(array){
	extParams=resetExtParams();
	for(var i=0;i<array.length;i++){
		var temp=array[i];
		for(var t in temp){
			extParams[t]='_'+temp[t];
		}
	}
	//reloadGrid();
	Ext.getCmp("datas").getStore().loadPage(1);
}
/**
 * 刷新列表数据
 */
function reloadGrid(){
	Ext.getCmp("datas").getStore().reload();
}

Ext.onReady(function(){
	extParams=resetExtParams();
	var items=new Array();
	Lingx.post("g",{e:entityCode},function(json){
		for(var i=0;i<json.fields.list.length;i++){
			var f=json.fields.list[i];
			if(f.inputType=='hidden'||params[f.code]){//隐藏的不留标签,级联字段
				continue;
			}
			///start 2015-11-19
			var obj=f;
			var ipt={
					id:f.code,
					fieldLabel:f.name,
					name:f.code};
			if(obj.inputType=='radio'||obj.inputType=='checkbox'){
				ipt.xtype=obj.inputType;
				if(obj.refEntity=='tlingx_optionitem'){
					ipt.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code='+obj.inputOptions,reader:{type:'json'}}),
						autoLoad:false});
				}else{
					ipt.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+obj.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
						autoLoad:false});
				}
				////obj.valueAjaxUrl='e?e=toption&m=items&lgxsn=1&code='+field.inputOptions;
			}else if(obj.refEntity){
				ipt.displayField= 'text';
				ipt.valueField= 'value';
				ipt.xtype='combobox';
				ipt.store=new Ext.data.Store({proxy: ({ model:'TextValueModel',type:'ajax',url:'e?e='+obj.refEntity+'&m=combo&lgxsn=1',reader:{type:'json'}}),
					autoLoad:false});
			}
			items.push(ipt);
			///end 2015-11-19
			
		}
		// 级联处理
		var tempArray=new Array();
		for(var i=0;i<json.columns.length;i++){
			var c=json.columns[i];
			if(!params[c.dataIndex]){
				tempArray.push(c);
			}
		}
		json.columns=tempArray;
		// 级联处理 end
		Ext.each(json.fields.list,function(obj,index,self){
			if(obj.comboType=='ref-display')textField=obj.code;
			if(obj.comboType=='ref-value')valueField=obj.code;
			if(request_params.valueField)valueField=request_params.valueField;
			if(request_params.valueField)valueField=request_params.valueField;
		});
		if(json.GridConfig.rownumbers){
			json.columns.unshift({ xtype: 'rownumberer',width:26});
		}
		json.toolbar.push("->");
		json.toolbar.push({iconCls:'icon-search',text:"查询",handler:function(){
			openSearchWindow(json.GridConfig.queryField,items);
		}});//,xtype:"cycle"
		/*
		* Model
		*/
		Ext.define(entityCode, {
		    extend: 'Ext.data.Model',
		    fields:json.model,
		    idProperty: json.GridConfig.idField
		});
		
		/*
		* Store
		*/
		var store = Ext.create('Ext.data.Store', {
		    pageSize: json.GridConfig.pageSize,
		    model: entityCode,
		    remoteSort: json.GridConfig.remoteSort,
		    autoLoad:json.GridConfig.autoLoad,
		    proxy: {
		    	actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	            },

		    	type: 'ajax',
		        url: "e?e="+entityCode+"&m="+methodCode+"&lgxsn=1",
		        reader: {
		        	type: 'json',
		            root: 'rows',
		            totalProperty: 'total'
		        }//,
		        //simpleSortMode: true
		    },
		    sorters: [{
		        property: json.GridConfig.sortName,
		        direction: json.GridConfig.sortOrder
		    }],
		    listeners:{
		    	beforeload:function(){
		    		Ext.apply(store.proxy.extraParams,extParams);  
		    	},
		    	load:function(){
		    		//Ext.getCmp("datas").getSelectionModel().select(0);
		    	}
		    }
		});
		var sm = Ext.create('Ext.selection.CheckboxModel');   
		Ext.create("Ext.Viewport",{
			layout:'border',
			border:json.GridConfig.broder,
			items:[{
				region: 'west',
				//title: '对象区',
	            split: true,
	            autoScroll:true,
	            border:false,
	            width: 162,
	            minWidth: 75,
	            maxWidth: 200,
	            margins: '0 0 0 0',
	            contentEl:"Value-DIV"
			},{
				id:"datas",
				region:"center",
				border:json.GridConfig.broder,
				loadMask:json.GridConfig.loadMask,
				store: store,
				//forceFit: true,
				split: true,
				xtype:'grid',
				selModel: sm,
				viewConfig : {
					//True表示为自动展开/缩小列的宽度以适应grid的宽度，这样就不会出现水平的滚动条
					//forceFit : true
					},

			    dockedItems: [{
	    	        xtype: 'toolbar',
	    	        items:json.toolbar,
	    	        dock: 'top',
	    	        displayInfo: true
	    	        }],
	    	        listeners:{
	    	        	itemdblclick:function(view,record,item,index,event,obj){
	    	        		openViewWindow(entityCode,json.name,record.data.id);
	    	        	},
	    	        	select:function(el,record, index, eOpts ){
	    	        		addItem(record.data);
	    	        	}
	    	        },
				  columns:json.columns,
			        bbar: Ext.create('Ext.PagingToolbar', {
			            store: store,
			            displayInfo:json.GridConfig.displayInfo
			        })
			}]
		});
		initItem();
	});
});


function initItem(){
	if(!value)return;
	var arrayValue=value.split(",");
	var arrayText=text.split(",");
	for(var i=0;i<arrayValue.length;i++){
		var o={};
		o[textField]=arrayText[i];
		o[valueField]=arrayValue[i];
		addItem(o);
	}
}
function addItem(data){
	var labels=$(".label");
	for(var i=0;i<labels.length;i++){
		var o=$(labels[i]);
		if(o.find(".value").html()==data[valueField]){
			lgxInfo("该项已选择");
			return;
		}
	}
	//console.log(data);
	var div=$("<div class='label'><span class='value' style='display:none;'>"+data[valueField]+"</span><span class='text'>"+data[textField]+"</span><a class='remove' style='float:right;padding:4px;display:none;' href='javascript:;' onclick='delItem(this)'><img src='lingx/js/resources/css/icons/erase.png' width='12'/></a></div>");
	div.bind("mouseover",function(){
		$(this).find("a").show();
	});
	div.bind("mouseout",function(){
		$(this).find("a").hide();
	});
	$("#Value-DIV").prepend(div);
}
function delItem(el){
	var p=$(el).parent();
	p.empty();
	p.remove();
	delete p;
}