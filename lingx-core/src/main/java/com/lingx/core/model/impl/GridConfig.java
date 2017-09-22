package com.lingx.core.model.impl;

import com.lingx.core.model.IConfig;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 上午11:31:35 
 * 类说明 
 */
public class GridConfig  extends AbstractModel implements IConfig {

	private static final long serialVersionUID = 8500058144499092483L;


	@FieldModelConfig(sort="3",name="主键字段")
	private String idField="id";
	@FieldModelConfig(sort="4",name="序号")
	private Boolean rownumbers=true;
	@FieldModelConfig(sort="5",name="checkOnSelect")
	private Boolean checkOnSelect=true;
	@FieldModelConfig(sort="6",name="selectOnCheck")
	private Boolean selectOnCheck=true;
	@FieldModelConfig(sort="8",name="页码")
	private Integer pageNumber=1;
	@FieldModelConfig(sort="9",name="行数")
	private Integer pageSize=20;
	@FieldModelConfig(sort="A",name="排序字段")
	private String sortName="id";
	@FieldModelConfig(sort="B",name="排序方式")
	private String sortOrder="desc";
	@FieldModelConfig(sort="C",name="远程排序")
	private Boolean remoteSort=true;
	@FieldModelConfig(sort="E",name="自动加载")
	private Boolean autoLoad=true;
	@FieldModelConfig(sort="F",name="边框")
	private Boolean broder=false;
	@FieldModelConfig(sort="G",name="displayInfo")
	private Boolean displayInfo=true;
	@FieldModelConfig(sort="I",name="查询字段",inputType="string")
	private String queryField;
	@FieldModelConfig(sort="J",name="双击操作",inputType="string")
	private String dblclickMethod;
	@FieldModelConfig(sort="K",name="双击窗体样式",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=CTYS',reader:{type:'json'}},autoLoad:true})})")
	private String winStyle;
	public GridConfig(){
		super();
		this.setModelType("GridConfig");
		this.setIconCls("icon-8");
		this.setQueryField("");
		this.setName("列表展示");
		this.setLeaf(true);
		this.setDblclickMethod("view");
	}

	public String getDblclickMethod() {
		return dblclickMethod;
	}

	public void setDblclickMethod(String dblclickMethod) {
		this.dblclickMethod = dblclickMethod;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public Boolean getRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(Boolean rownumbers) {
		this.rownumbers = rownumbers;
	}

	public Boolean getCheckOnSelect() {
		return checkOnSelect;
	}

	public void setCheckOnSelect(Boolean checkOnSelect) {
		this.checkOnSelect = checkOnSelect;
	}

	public Boolean getSelectOnCheck() {
		return selectOnCheck;
	}

	public void setSelectOnCheck(Boolean selectOnCheck) {
		this.selectOnCheck = selectOnCheck;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean getRemoteSort() {
		return remoteSort;
	}

	public void setRemoteSort(Boolean remoteSort) {
		this.remoteSort = remoteSort;
	}

	public Boolean getAutoLoad() {
		return autoLoad;
	}

	public void setAutoLoad(Boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	public Boolean getBroder() {
		return broder;
	}

	public void setBroder(Boolean broder) {
		this.broder = broder;
	}

	public Boolean getDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(Boolean displayInfo) {
		this.displayInfo = displayInfo;
	}

	public String getQueryField() {
		return queryField;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}
	public String getWinStyle() {
		if(Utils.isNull(this.winStyle))return "default";
		else
		return winStyle;
	}
	public void setWinStyle(String winStyle) {
		this.winStyle = winStyle;
	}
	
}
