package utils;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import app.Appliction;

public class BeanPool {
	private static Context context;
	private static BeanPool instance = null;
	
	public static BeanPool getInstance() {
		if(instance==null){
			instance = new BeanPool();
		}
		return instance;
	}
	public BeanPool() {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, Appliction.getInstance().getSettings().get("server_url"));
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");

		try {
			context = new InitialContext(env);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean (Class<T> remoteClass){
		try {
			String beanName = remoteClass.getSimpleName();
			beanName = beanName.substring(0, beanName.length()-6);
			return (T)context.lookup(beanName + "/remote");
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
