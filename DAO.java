package com.mlxg.BookManager.dao;

import java.util.List;

import org.springframework.stereotype.Component;

public interface DAO<T>{

	    //��ѯ����
		public List<T> query(T t);
		//��������ѯ
		public T queryById(Object id);
		//���ӷ���
		public boolean save(T t);
		//�޸ķ���
		public boolean update(T t);
		//ɾ������
		public boolean delete(Object id);
		//��ѯ��Ŀ������
		public int queryPageNum();
		//��ҳ��ѯ
		public List<T> query(T t, int page);
}
