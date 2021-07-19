package testMobileBankingN.test;

/*
 * Interface for test configuration
 * */

public interface TestConfiguration {
	
	/*get how many times test case will be repeated, each test represented by sequence
	 * */
	public int getParameterCount();
	
	/*Run next next test case sequence
	 * */
	public void setNextSequence(int sequence);
	
	public String getTestCaseName(int sequence);

}
