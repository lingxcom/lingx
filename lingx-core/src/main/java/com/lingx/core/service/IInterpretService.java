package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月26日 下午3:12:06 
 * 类说明 
 */
public interface IInterpretService {
	public Object inputFormat(Object value,IField field, IContext context,
			IPerformer performer);
	public Map<String, String> inputFormat(Map<String, String> map,List<IField> fields, IEntity entity, IContext context,
			IPerformer performer);
	
	public void outputFormat(List<Map<String,Object>> list,List<IField> fields,IEntity entity, IContext context,IPerformer performer);
	public void outputFormat(Map<String, Object> m,List<IField> fields, IEntity entity, IContext context,IPerformer performer);
	public Object outputFormat(IField field, IContext context,IPerformer performer);
	
	public List<Map<String,Object>> getComboData();
}
