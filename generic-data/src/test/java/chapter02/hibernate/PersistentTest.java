package chapter02.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import chapter01.hibernate.Message;
public class PersistentTest {
	SessionFactory factory;
	
	@BeforeSuite
	public void setup() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		factory = new MetadataSources(registry)
				.buildMetadata()
				.buildSessionFactory();
	}
	
	@Test
	public void saveMessage() {
		Message message = new Message("Hello, World!");
		try(Session session = factory.openSession()) {
			Transaction tx = session.beginTransaction();
			session.persist(message);
			tx.commit();
		}
	}
	
	@Test(dependsOnMethods="saveMessage")
	public void readMessage() {
		try(Session session = factory.openSession()) {
			List<Message> list = session.createQuery("from Message", Message.class).list();
			Assert.assertEquals(list.size(), 1);
			list.forEach(l -> System.out.println(l));
		}
	}
}
