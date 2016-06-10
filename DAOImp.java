package com.mlxg.BookManager.dao.imp;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.mlxg.BookManager.dao.DAO;


@Component
public abstract class DAOImp<T> implements DAO<T> {

	protected abstract T getModel();//�������ʵ��
	private  T tModel=null;//ʵ��
	private String className="";//����
	private String classNameLowerSub="";//����
	private String primaryKey="";//��������
	private String primaryKeyType="";//��������
	private SessionFactory sessionFactory;
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public DAOImp()
	{
	  tModel=getModel();
      className=tModel.getClass().getSimpleName();//��ȡ����
      String classNameLower=className.toLowerCase();//��ȡʵ����(���������Сд)
      classNameLowerSub=classNameLower.substring(0, 1);    
      getPrimaryKey();
	}

	//��ȡ��������������
	protected void getPrimaryKey()
	{
		Field[] fields=tModel.getClass().getDeclaredFields();
		primaryKey=fields[0].getName();//Ĭ��ģ���е�һ������Ϊ����
		fields[0].setAccessible(true);//�޸ķ���Ȩ��
		primaryKeyType=fields[0].getType().toString();//��������
	}
	@Override
	//��ѯ����
	public List<T> query(T t) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from "+className);
		List<T> ts = (List<T>)q.list();
		return ts;
	}
	@Override
	//��������ѯ
	public T queryById(Object id) {
		Session session = sessionFactory.getCurrentSession();
		String sql="";
		if(primaryKeyType.equals("int"))
		  sql="from "+className+" "+classNameLowerSub+" where "+classNameLowerSub+"."+primaryKey+"=" + id;
	    if(primaryKeyType.equals("class java.lang.String")) 
	      sql="from "+className+" "+classNameLowerSub+" where "+classNameLowerSub+"."+primaryKey+"='" + id+"'";
		//Query q = session.createQuery("from "+className+" "+classNameLowerSub+" where "+classNameLowerSub+"."+primaryKey+"=" + id);
	    Query q = session.createQuery(sql);
		return (T) q.uniqueResult();
	}
	@Override
	//��ӷ���
	public boolean save(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.save(t);
		return true;
	}
	@Override
	//���·���
	public boolean update(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.update(t);
		return true;
	}
	@Override
	//ɾ������
	public boolean delete(Object id) {
		Session session = sessionFactory.getCurrentSession();
		T t=null;
		String Id="";
		Id=id.toString();
		if(primaryKeyType.equals("String"))
		    t = (T)session.get(tModel.getClass(),Id);
		else
		{
			int idInt=Integer.parseInt(Id);
			t = (T)session.get(tModel.getClass(),idInt);
		}
	    session.delete(t);
	    return true;
	}
	@Override
	//��ѯ��Ŀ������
    public int queryPageNum()
    {
    	Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select count("+classNameLowerSub+"."+primaryKey+") from "+className+" "+classNameLowerSub);
		String rowNums = ((Long)query.uniqueResult()).toString();
		int rowNum = Integer.valueOf(rowNums);
		int pageNum = (int) Math.ceil((double) rowNum / 10);
		return pageNum;
    }
    @Override
	//��ҳ��ѯ
	public List<T> query(T t, int page)
	{
		int firstResult = (page - 1) * 10;//��ҳ�ĵ�һ�����
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from "+className);
		q.setMaxResults(10);
		q.setFirstResult(firstResult);
		List<T> ts = (List<T>)q.list();
		return ts;
	}


}
