package com.mlxg.BookManager.dao;

import java.util.List;

import org.springframework.stereotype.Component;

public interface DAO<T>{

	    //查询方法
		public List<T> query(T t);
		//按主键查询
		public T queryById(Object id);
		//增加方法
		public boolean save(T t);
		//修改方法
		public boolean update(T t);
		//删除方法
		public boolean delete(Object id);
		//查询条目总数量
		public int queryPageNum();
		//按页查询
		public List<T> query(T t, int page);
}
