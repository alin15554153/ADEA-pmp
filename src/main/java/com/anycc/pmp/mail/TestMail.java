/**  
 * @Title: TestMail.java 
 * @Package common.method.mail 
 * @Description: TODO 
 * @author 葛钰鹏
 * @date 2014年11月15日 下午10:47:44 
 * @version V1.0  
 */

package com.anycc.pmp.mail;

/**
 * @ClassName: TestMail
 * @Description:
 * @author: 葛钰鹏
 * @date 2014年11月15日 下午10:47:44
 * 
 */

public class TestMail {

	/**
	 * 
	 * @Title: main
	 * 
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @param @param args 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */

	public static void main(String[] args) {
		String [] as = new String[]{"1","2","3"};
		String [] bs = new String[]{"2","4"};
		StringBuffer ab = new StringBuffer();
		StringBuffer bb = new StringBuffer();
		for(String A : as){
			for(String B:bs){
				if(A.equals(B)){
					ab.append(A);
				}else{
					bb.append(A);
				}
			}
		}
		System.out.println(ab);
		System.out.println(bb);
	}

}
