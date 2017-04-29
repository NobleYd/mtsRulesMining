package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class MemoryTest {

	// 5.45->5.82
	@Test
	public void test1() throws InterruptedException {
		long start = System.currentTimeMillis();
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 1000_0000; i++) {
			Integer num = new Integer(i);
			list.add(num);
		}
		Thread.sleep(5000);
		long end = System.currentTimeMillis();
		System.out.println((end-start)+"ms");
	}

	// 5.45->5.61
	@Test
	public void test2() throws InterruptedException {
		long start = System.currentTimeMillis();
		HashMap<Integer, Integer> integers = new HashMap<Integer, Integer>();
		for (int i = 0; i <= 13142; i++) {
			Integer num = new Integer(i);
			integers.put(num, num);
		}
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 1000_0000; i++) {
			list.add(integers.get(i));
		}
		Thread.sleep(5000);
		long end = System.currentTimeMillis();
		System.out.println((end-start)+"ms");
	}
}
