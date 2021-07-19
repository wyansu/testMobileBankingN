package testMobileBankingN.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

public class ConfigRunner extends ParentRunner<Method>{

	private Class clazz;
	private Object testObject;
	private int nextSequenceDesc=0;
	private int nextRunSequenceDesc =0;
	private int sequenceCount;
	
	private Method testMethod;
	private Method beforeMethod;
	private Method afterMethod;
	
	private Field config;
	
	public ConfigRunner(Class testClass) throws InitializationError  {
		super(testClass);
		clazz = testClass;
		
		try {
			testObject = clazz.getDeclaredConstructor().newInstance();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		config = populateConfig();
		sequenceCount = getParameterCount();
		populateMethod();
	}

	@Override
	protected List<Method> getChildren(){
		List<Method> lMethod = new ArrayList<Method>();

		for(int i=0; i< sequenceCount ; i++) {
			lMethod.add(testMethod);
		}
		return lMethod;
	}

	@Override
	protected Description describeChild(Method child) {
	
		nextSequenceDesc = getNextSequence(nextSequenceDesc);
		return Description.createTestDescription(clazz,  child.getName() + " ["+nextSequenceDesc+"] "+ getTestCaseName(nextSequenceDesc));
	}

	@Override
	protected void runChild(Method child, RunNotifier notifier) {
		try {
			nextRunSequenceDesc = getNextSequence(nextRunSequenceDesc);
			this.runBefore();
			sendInfoNextSequence(nextRunSequenceDesc);
			notifier.fireTestStarted(Description.createTestDescription(clazz, child.getName()+" ["+nextRunSequenceDesc+"] " +getTestCaseName(nextRunSequenceDesc)));
			child.invoke(testObject);
			notifier.fireTestFinished(Description.createTestDescription(clazz, child.getName() +" ["+nextRunSequenceDesc+"] " +getTestCaseName(nextRunSequenceDesc)));
			this.runAfter();
		}catch(Exception e) {
			notifier.fireTestFailure(new Failure(Description.createTestDescription(clazz, child.getName() +" ["+nextRunSequenceDesc+"] " +getTestCaseName(nextRunSequenceDesc)), e));
			//e.printStackTrace();
		}
		
	}
	
	private void sendInfoNextSequence(int sequence) {
		try {
			Object fieldObject = config.get(testObject);
			Method setNextSequence = TestConfiguration.class.getDeclaredMethod("setNextSequence", int.class);
			setNextSequence.invoke(fieldObject, sequence);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * getParameterCount : untuk mendapatkan berapa kali sebuah test case akan diulang
	 * */
	
	private int getParameterCount() throws InitializationError {
		
		int paramCount =0;
		
		try {
			Object fieldObject = config.get(testObject);
			Method getParameterCount = TestConfiguration.class.getDeclaredMethod("getParameterCount");
			paramCount = (int) getParameterCount.invoke(fieldObject);
			
		}catch (Exception e) {
			throw new InitializationError("Error getting test count");
		}
		

		return paramCount;
	}
	
	/*
	 * karena getDescription beberapa kali kita tidak bisa membedakan swquence test case, ini workaround untuk penghitungan sequence
	 * agar diperoleh sequence dimulai dari 1 sampai ke jumlah test case diulang
	 * */
	private int getNextSequence(int currentSequence) {
		if(currentSequence >= sequenceCount) {
			currentSequence = 1;
		}else {
			currentSequence++;
		}
		
		return currentSequence;
	}
	
	/*
	 * Call before method for each of the test sequence
	 * */
	private void runBefore() {
		try {
		
			if(beforeMethod != null)
				beforeMethod.invoke(testObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Call after method for each of the test sequence
	 * */
	
	private void runAfter() {
		try {
			if(this.afterMethod != null) 
				afterMethod.invoke(testObject);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 *By design this runner only support one Test method, zero or one Before, After
	 *this method populate those method 
	 * */
	private void populateMethod() throws InitializationError {
		
		int numMethodTest = 0;
		int numBefore =0;
		int numAfter =0;
		
		for(Method method: clazz.getMethods()) {
			
			if(method.isAnnotationPresent(Test.class)) {
				numMethodTest++;
				if(numMethodTest > 1) {
					throw new InitializationError("Runner only support one Test method");
				}
				
				this.testMethod = method;
			}
			
			if(method.isAnnotationPresent(Before.class)) {
				numBefore++;
				if(numBefore > 1) {
					throw new InitializationError("Runner only support one Before method");
				}
				
				this.beforeMethod = method;
			}
			
			if(method.isAnnotationPresent(After.class)) {
				numAfter++;
				if(numAfter > 1) {
					throw new InitializationError("Runner only support one After method");
				}
				
				this.afterMethod = method;
			}
			
		}
		
		if(numMethodTest == 0) {
			throw new InitializationError("No test method defined");
		}
	}
	
	private Field populateConfig() throws InitializationError {
		
		Field config = null;
		
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Config.class)) {
				config = field;
				continue;
			}
		}
		
		

		if(config==null) {
			throw new InitializationError("No configuration found");
		} else {
			try {
				Object fieldObject = config.get(testObject);
				if(fieldObject instanceof TestConfiguration) {
					
				}else {
					throw new InitializationError("Configuration is not implementation of TestConfiguration");
				}
			}catch(Exception e) {
				throw new InitializationError("Error getting configuration");
			}
			
			
		} 
		
		return config;
		
	}
	
	private String getTestCaseName(int sequence) {
		String name ="";
		try {
			Object fieldObject = config.get(testObject);
			Method getTestCaseName = TestConfiguration.class.getDeclaredMethod("getTestCaseName", int.class);
			name = (String) getTestCaseName.invoke(fieldObject, sequence);
		}catch(Exception e) {
			System.err.println("Error: cannot get test case name.");
			e.printStackTrace();
		}
		
		return name;
	}
}
