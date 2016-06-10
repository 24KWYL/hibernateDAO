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

	protected abstract T getModel();//返回类的实例
	private  T tModel=null;//实例
	private String className="";//类名
	private String classNameLowerSub="";//别名
	private String primaryKey="";//主键名称
	private String primaryKeyType="";//主键类型
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
      className=tModel.getClass().getSimpleName();//获取类名
      String classNameLower=className.toLowerCase();//获取实例名(将类名变成小写)
      classNameLowerSub=classNameLower.substring(0, 1);    
      getPrimaryKey();
	}

	//获取主键及主键类型
	protected void getPrimaryKey()
	{
		Field[] fields=tModel.getClass().getDeclaredFields();
		primaryKey=fields[0].getName();//默认模型中第一个属性为主键
		fields[0].setAccessible(true);//修改访问权限
		primaryKeyType=fields[0].getType().toString();//主键类型
	}
	@Override
	//查询方法
	public List<T> query(T t) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from "+className);
		List<T> ts = (List<T>)q.list();
		return ts;
	}
	@Override
	//按主键查询
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
	//添加方法
	public boolean save(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.save(t);
		return true;
	}
	@Override
	//更新方法
	public boolean update(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.update(t);
		return true;
	}
	@Override
	//删除方法
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
	//查询条目总数量
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
	//按页查询
	public List<T> query(T t, int page)
	{
		int firstResult = (page - 1) * 10;//当页的第一条结果
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from "+className);
		q.setMaxResults(10);
		q.setFirstResult(firstResult);
		List<T> ts = (List<T>)q.list();
		return ts;
	}


}
