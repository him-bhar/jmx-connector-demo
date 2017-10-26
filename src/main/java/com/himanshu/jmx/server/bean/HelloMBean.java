package com.himanshu.jmx.server.bean;

public class HelloMBean implements IHelloMBean {
  public int add(int a, int b) {
    return a+b;
  }
}
