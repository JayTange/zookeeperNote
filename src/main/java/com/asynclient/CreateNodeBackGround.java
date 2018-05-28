package com.asynclient;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateNodeBackGround {

    static String path = "/zk-asyn";
    static String zkIp = "192.168.90.53:2181";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(zkIp)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    static CountDownLatch semphore = new CountDownLatch(2);
    static ExecutorService tp = Executors.newFixedThreadPool(2);
    public static void main(String args[]) throws Exception{
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    public void processResult(CuratorFramework client, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code: "+curatorEvent.getResultCode()+" type="+curatorEvent.getType());
                        System.out.println(Thread.currentThread().getName()+"正在执行任务");
                        semphore.countDown();
                    }
                },tp).forPath(path,"aaaa".getBytes());
        semphore.await();
    }

}
